package com.insight.calendar;



import com.insight.user.UserInfo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Schedule {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Integer calSchId;
	
	@ManyToOne
	//Calendar에서 일정의 calId를 가져온다.
	//calId를 기준으로 참석자의 목록을 만들기 위해서
	private Calendar calGetId;
	
	@ManyToOne
	//UserInfo에서 사용자의 userId 가져온다.
	//userId를 가져와서 UserRrpository에서 findById를 통하여 학번을 가져온다.
	private UserInfo calGetUserId;

}
