package com.insight.user;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//학번
	@Column(unique = true, length = 8)
	private String username;
	
	private String studentName;
	
	private String password;
	
	@Column(unique=true)
	private String email;
	
	@Column(unique=true)
	private String phoneNumber;
	
	private String major;
	private String grade;
	private String doing;
	private String condition;
	private String wantedAct;
	private String introduction;
	private LocalDate joinDate;
	
}
