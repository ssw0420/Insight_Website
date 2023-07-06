package com.insight.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModifyForm {
	@Size(min = 8, max = 8)
	@NotEmpty(message = "학번은 8자리여야 합니다.")
	private String username;
	
	@NotEmpty(message = "이름은 필수항목입니다.")
	private String studentName;

    private String password;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;
    
    @NotEmpty(message = "번호는 필수항목입니다.")
    private String phoneNumber;
	
    @NotEmpty(message = "전공은 필수항목입니다.")
	private String major;
    
    @NotEmpty(message = "학년은 필수항목입니다.")
	private String grade;
    
	private String doing;
	
	@NotEmpty(message = "재적상태는 필수항목입니다.")
	private String condition;
	
	@NotEmpty(message = "원하는 활동은 필수항목입니다.")
	private String wantedAct;
	
	private String introduction;
	
}