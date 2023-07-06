package com.insight.board;

import java.time.LocalDateTime;

import com.insight.user.UserInfo;

import jakarta.persistence.Column;
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
public class NoticeComment {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Integer cmId;
	
	@ManyToOne
	private Notice notice;
	
	@ManyToOne
	private UserInfo cmAuthor;
	
	@Column(columnDefinition = "TEXT")
	private String cmText;
	
	private LocalDateTime cmRegister;
	
	private LocalDateTime cmModifyRegister;
}
