package com.insight.calendar;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//달력 등록, 수정 시에 사용되는 변수
//여기있는 변수 이름으로 등록, 수정 html 입력칸 이름 적기
//스프링 부트 2-15
public class CalendarRegisteForm {
	
	private String calStartDay;
	// html에서 시작 일정의 변수 
	
	@NotEmpty(message = "내용은 필수항목입니다.")
	//빈칸이면 에러메세지 출력
	private String calText;
	// html에서 입력창의 변수
	private String calEndDay;
	private String calStartTime;
	private String calEndTime;
	
}
