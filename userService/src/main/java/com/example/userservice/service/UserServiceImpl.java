package com.example.userservice.service;


import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


	UserRepository userRepository;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	Environment env;
	RestTemplate restTemplate;

	/*
	 * BCryptPasswordEncoder 얘를 빈으로 주입하기 위해서는 생성자안에 들어있는 인스턴스들도 초기화가 되어야 한다,
	 * 얘는 빈으로 주입해준적이 없기때문에 빈으로 등록해줘야 사용이 가능하다
	 * 초기 기동할수있는 클래스에 빈으로 등록하는 방식을 사용하면 된다.
	 * */


	public UserServiceImpl(UserRepository userRepository,
						   BCryptPasswordEncoder bCryptPasswordEncoder, Environment env,RestTemplate restTemplate
	) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.env = env;
		this.restTemplate = restTemplate;
	}

	@Override
	public UserDTO createUser(UserDTO userDto) {
		userDto.setUserId(UUID.randomUUID().toString());

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));
		userRepository.save(userEntity);

		UserDTO returnUserDto = modelMapper.map(userEntity, UserDTO.class);
		return returnUserDto;
	}

	/*
	 * userEntity (db)를 아이디로 조회한다
	 * 주문한 것들을 포함시켜서 id를 보낸다.
	 * */
	@Override
	public UserDTO getUserById(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UsernameNotFoundException("User not found");

		UserDTO userDto = new ModelMapper().map(userEntity, UserDTO.class);


		//List<ResponseOrder> order = new ArrayList<>();

		//-----------------------------------------------------------------------------------------------------------------------------------------------------
		/*restTemplate 사용방법~!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
		String orderUrl = "http://127.0.0.1:8000/order-service/4295d039-fea8-4653-9912-a6517d2eeff0/orders";
		/*
		* restTemplate.exchange(전달할 주소~!,어떤방식으로, 요청할때 파라미터, 전달받고자할때 어떤형식으로 전달받을지 )
		* */
		ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<ResponseOrder>>() {
				});
		List<ResponseOrder> orderList = orderListResponse.getBody(); //ResponseEntity -> ResponseOrder타입으로 가져오기

		userDto.setOrders(orderList);
		//-----------------------------------------------------------------------------------------------------------------------------------------------------

		// Using as rest template
//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<ResponseOrder>>() {
//        });
//        List<ResponseOrder> orderList = orderListResponse.getBody();

		// Using as feign client
		// feign exception handling
//        List<ResponseOrder> orderList = null;
//        try {
//            orderList = orderServiceClient.getOrders(userId);
//        } catch (FeignException ex) {
//            log.error(ex.getMessage());
//        }

		// Error Decoder
//        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
		log.info("Before call orders microservice");
		/*CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
		List<ResponseOrder> orderList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
				throwable -> new ArrayList<>()
		);
		log.info("After called orders microservice");
		userDto.setOrders(orderList);
*/
		return userDto;
	}

	@Override
	public Iterable<UserEntity> getUserByAll() {
		return userRepository.findAll();
	}


}
