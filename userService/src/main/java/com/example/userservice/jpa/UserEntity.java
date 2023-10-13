package com.example.userservice.jpa;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50, unique = true)
/*unique 같을수 없게 하는 속성*/
	private String email;
	@Column(nullable = false, length = 50)
	private String name;
	@Column(nullable = false, unique = true)
	private String userId;
	@Column(nullable = false, unique = true)
	private String encryptedPwd;
}
