package com.insight.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
	Notice findByNoticeTitle(String noticeTitle);
	Notice findByNoticeTitleAndNoticeText(String noticeTitle, String noticeText); // 제목 및 내용 호출
	List<Notice> findByNoticeTitleLike(String noticeTitle);
	Page<Notice> findAll(Pageable pageable); // main 리스트 호출
	Page<Notice> findByNoticeCategory(String noticeCategory, Pageable pageable); // 카테고리 설정
	Page<Notice> findAll(Specification<Notice> spec, Pageable pageable);
	Page<Notice> findByNoticeCategory(Specification<Notice> spec, String noticeCategory, Pageable pageable); // 카테고리 설정
	
	
}