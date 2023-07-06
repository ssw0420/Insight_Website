package com.insight.board;

import com.insight.DataNotFoundException;
import com.insight.user.UserInfo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoticeService {

	private final NoticeRepository noticeRepository;
	
	private Specification<Notice> search(String kw, String ca){
		return new Specification<>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<Notice> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거 
                Join<Notice, UserInfo> u1 = q.join("noticeAuthor", JoinType.LEFT);
                Join<Notice, NoticeComment> a = q.join("commentList", JoinType.LEFT);
                Join<NoticeComment, UserInfo> u2 = a.join("cmAuthor", JoinType.LEFT);
                Predicate predicate= cb.or(cb.like(q.get("noticeTitle"), "%" + kw + "%"), // 제목 
                        cb.like(q.get("noticeText"), "%" + kw + "%"),      // 내용 
                        cb.like(u1.get("studentName"), "%" + kw + "%"),    // 질문 작성자 
                        cb.like(a.get("cmText"), "%" + kw + "%"),      // 답변 내용 
                        cb.like(u2.get("studentName"), "%" + kw + "%")	// 답변 작성자 
                        );
        		predicate = cb.and(
                        predicate,
                        cb.like(q.get("noticeCategory"), "%" + ca + "%") // 카테고리 필터링
                    );
        		return predicate;

                        
           
            }
        };
	}
	
	public List<Notice> getList(){
		return this.noticeRepository.findAll();
	}
	
	public Notice getNotice(Integer noticeId) {  
        Optional<Notice> notice = this.noticeRepository.findById(noticeId);
        if (notice.isPresent()) {
            return notice.get();
        } else {
            throw new DataNotFoundException("notice not found");
        }
    }
// 쿼리문 사용으로 인해 사용 x
//	public Page<Notice> getCategory(String noticeCategory, int page, String kw){
//		List<Sort.Order> sorts = new ArrayList<>();
//		sorts.add(Sort.Order.desc("noticeRegister"));
//		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
//		//Specification<Notice> spec = search(kw);
//		return this.noticeRepository.findByNoticeCategory( noticeCategory, pageable);
//	}
	
	public void create(String noticeTitle, String noticeText, UserInfo user, String noticeCategory) {
		Notice q = new Notice();
        q.setNoticeTitle(noticeTitle);
        q.setNoticeText(noticeText);
        q.setNoticeRegister(LocalDateTime.now());
        q.setNoticeAuthor(user);
        q.setNoticeCategory(noticeCategory);
        this.noticeRepository.save(q);
    }
	
	/* 테스트 용
	 * public void test(String noticeTitle, String noticeText, String
	 * noticeCategory) { Notice q = new Notice(); q.setNoticeTitle(noticeTitle);
	 * q.setNoticeText(noticeText); q.setNoticeRegister(LocalDateTime.now());
	 * q.setNoticeCategory(noticeCategory); this.noticeRepository.save(q); }
	 */
	
	public Page<Notice> getList(int page, String kw, String ca) {
		List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("noticeRegister"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Notice> spec = search(kw, ca);
        return this.noticeRepository.findAll(spec, pageable);
    }
	
	public void modify(Notice notice, String noticeTitle, String noticeText) {
        notice.setNoticeTitle(noticeTitle);
        notice.setNoticeText(noticeText);
        notice.setNoticeModifyRegister(LocalDateTime.now());
        this.noticeRepository.save(notice);
    }
	
	public void delete(Notice notice) {
        this.noticeRepository.delete(notice);
    }
}
