package com.example.userservice.jpa;


import javax.persistence.*;

@Entity
@Table(name = "product")
public class Test {

	@Id
	@Column(name = "productId")
	private String id;
}
