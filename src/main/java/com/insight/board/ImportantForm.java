package com.insight.board;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportantForm {
	@NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=200)
    private String impoTitle;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String impoText;

    private Boolean impoTf;		// 필독 여부 
}
