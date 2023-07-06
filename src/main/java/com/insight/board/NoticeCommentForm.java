package com.insight.board;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeCommentForm {
	@NotEmpty(message = "내용은 필수항목입니다.")
    private String cmText;
	
}
