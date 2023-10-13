package com.example.userservice.service;


import com.example.userservice.dto.UserDTO;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{


	UserRepository userRepository;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	/*
	* BCryptPasswordEncoder 얘를 빈으로 주입하기 위해서는 생성자안에 들어있는 인스턴스들도 초기화가 되어야 한다,
	* 얘는 빈으로 주입해준적이 없기때문에 빈으로 등록해줘야 사용이 가능하다
	* 초기 기동할수있는 클래스에 빈으로 등록하는 방식을 사용하면 된다.
	* */


	public UserServiceImpl(UserRepository userRepository,
						   BCryptPasswordEncoder bCryptPasswordEncoder
) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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





}
