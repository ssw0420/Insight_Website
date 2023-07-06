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
public class Important {
	
	@ManyToOne
	private UserInfo impoAuthor;		
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer impoId;
	
    @Column( length = 200)	// 제목 
    private String impoTitle;
    
    @Column(columnDefinition = "TEXT")   // 내용 
    private String impoText;
    
    private LocalDateTime impoRegister;	// 작성일시
    
    private Boolean impoTf;		// 필독 여부 
    
    private LocalDateTime impoModifyRegister;
    
    
   
}
