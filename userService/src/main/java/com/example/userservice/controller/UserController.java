package com.example.userservice.controller;

import com.example.userservice.dto.UserDTO;
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

@RestController
@RequestMapping("/")
public class UserController {

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
}
