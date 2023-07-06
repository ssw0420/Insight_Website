package com.insight.board;

import com.insight.DataNotFoundException;
import com.insight.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class NoticeCommentService {

	private final NoticeCommentRepository noticeCommentRepository;
	
	public NoticeComment create(Notice notice, String CmText, UserInfo author) {
		NoticeComment noticeComment = new NoticeComment();
		noticeComment.setCmText(CmText);
		noticeComment.setCmRegister(LocalDateTime.now());
		noticeComment.setNotice(notice);
		noticeComment.setCmAuthor(author);
		this.noticeCommentRepository.save(noticeComment);
		return noticeComment;
	}
	
	public NoticeComment getNoticeComment(Integer cmId) {
        Optional<NoticeComment> noticeComment = this.noticeCommentRepository.findById(cmId);
        if (noticeComment.isPresent()) {
            return noticeComment.get();
        } else {
            throw new DataNotFoundException("comment not found");
        }
    }

    public void modify(NoticeComment noticeComment, String cmText) {
    	noticeComment.setCmText(cmText);
    	noticeComment.setCmModifyRegister(LocalDateTime.now());
        this.noticeCommentRepository.save(noticeComment);
    }
    
    public void delete(NoticeComment noticeComment) {
        this.noticeCommentRepository.delete(noticeComment);
    }
}
