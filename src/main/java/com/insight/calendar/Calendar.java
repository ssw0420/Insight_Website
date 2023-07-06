
package com.insight.calendar;

import com.insight.user.UserInfo;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class Calendar {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer calId;
	
	@ManyToOne
	private UserInfo calAuthor;


	private LocalDate calStartDay;
	private LocalDate calEndDay;
	private LocalTime calStartTime;
	private LocalTime calEndTime;
	
	private String calText;
	
	private String calCategory;
}

