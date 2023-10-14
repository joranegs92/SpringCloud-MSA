package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;


import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/order-service")
public class OrderController {
    Environment env;
    OrderService orderService;
    //KafkaProducer kafkaProducer;
  //  OrderProducer orderProducer;

    @Autowired
    public OrderController(Environment env, OrderService orderService /*KafkaProducer kafkaProducer,*//* OrderProducer orderProducer*/) {
        this.env = env;
        this.orderService = orderService;
/*        this.kafkaProducer = kafkaProducer;*/
 //       this.orderProducer = orderProducer;
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s", env.getProperty("local.server.port"));
    }

    /**
     * 상품 주문 !
     * @param userId
     * @param orderDetails
     * @return
     */
    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails) {
        log.info("Before add orders data");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = modelMapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);

        // jpa
        OrderDto createdOrder = orderService.createOrder(orderDto);

        /* 반환값은 responseOrder로 바꿔서 보내겠다~~ */
        ResponseOrder responseOrder = modelMapper.map(createdOrder, ResponseOrder.class);

        /* TEST 위한 임시 주석

        // kafka
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());

        // send this order to the kafka
        kafkaProducer.send("example-catalog-topic", orderDto);
        orderProducer.send("orders", orderDto);

        ResponseOrder responseOrder = modelMapper.map(orderDto, ResponseOrder.class);
         */

        log.info("After added orders data");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    /**
     * 사용자가 구매한 목록 조회
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) throws Exception {
        log.info("Before retrieve orders data");
        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);
        List<ResponseOrder> result = new ArrayList<>();//보낼 값

        orderList.forEach(v -> result.add(
                new ModelMapper().map(v, ResponseOrder.class)));
/*
        try {
            Thread.sleep(1000);
            throw new Exception("장애 발생");
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
        }

        log.info("Add retrieved orders data");*/
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
