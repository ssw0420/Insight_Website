package com.insight.board;

import java.time.LocalDateTime;
import java.util.List;

import com.insight.user.UserInfo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeId;
    
    @Column(length = 200)
    private String noticeTitle;
    
    @Column(columnDefinition = "TEXT")
    private String noticeText;
    
    private String noticeCategory;
    
    private Integer noticeNumber;
    
    @ManyToOne
	private UserInfo noticeAuthor;
    
    @OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE)
	private List<NoticeComment> commentList;
    
    private LocalDateTime noticeRegister;
    
    private LocalDateTime noticeModifyRegister;
    
    @Column(columnDefinition = "integer default 0", nullable = false)	// 조회수의 기본 값을 0으로 지정, null 불가 처리
	private int noticeView;
}
