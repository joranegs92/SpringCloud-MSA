package com.example.userservice.controller;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user-service")
public class UserController {

	/*
	* vo객체를 사용해서 네트워크 데이터 전송함
	* dto객체로는 db객체에 접근함 -> jpa에 데이터복사
	* */

	private Environment env; //application.yml에 있는 정보들 가지고올수잇음


	private UserService userService;
	@Autowired
	public UserController(Environment env, UserService userService) {
		this.userService=userService;
		this.env = env;
	}


	@GetMapping("/health_check")
	public String status() {
		return String.format("It's Working in User Service"
				+ ", port(local.server.port) = " + env.getProperty("local.server.port")
				+ ", port(server.port) = " + env.getProperty("server.port")
				+ ", token secret  = " + env.getProperty("token.secret")
				+ ", token expiration time= " + env.getProperty("token.expiration_time"));
	}
	@GetMapping("/welcome")
	public String welcome() {
		return env.getProperty("greeting.message");
	}

	/**
	* 유저를 새로 생성한다
	*/
	@PostMapping("/users")
	public ResponseEntity<ResponseUser>/*String */createUser(@RequestBody RequestUser user) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserDTO userDto = mapper.map(user, UserDTO.class);
		userService.createUser(userDto);

		//return  "호출";
		ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
	}

	/**
	* 전체유저를 조회한다
	*/
	@GetMapping("/users")
	public ResponseEntity<List<ResponseUser>> getUsers() {
		Iterable<UserEntity> userList = userService.getUserByAll();
		List<ResponseUser> result = new ArrayList<>();


		userList.forEach(v -> result.add(new ModelMapper().map(v, ResponseUser.class)));

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	/**
	 * 특정유저를 조회한다
	 * @PathVariable
	 */
	@GetMapping("/users/{userId}")
	public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
		UserDTO userDto = userService.getUserById(userId);

		ResponseUser responseUser = new ModelMapper().map(userDto, ResponseUser.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
	}
}
