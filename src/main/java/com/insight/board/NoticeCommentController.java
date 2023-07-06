package com.insight.board;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import com.insight.user.UserInfo;
import com.insight.user.UserService;
import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class NoticeCommentController {

	private final NoticeCommentService noticeCommentService;
	private final NoticeService noticeService;
	private final UserService userService;
	
	@PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Integer noticeId, @Valid NoticeCommentForm noticeCommentForm, BindingResult bindingResult, Principal principal) {
        Notice notice = this.noticeService.getNotice(noticeId);
        UserInfo userInfo = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("notice", notice);
            return "question_detail";
        }
        NoticeComment noticeComment = this.noticeCommentService.create(notice, noticeCommentForm.getCmText(), userInfo);
        return String.format("redirect:/notice/detail/%s#comment_%s", noticeComment.getNotice().getNoticeId(), noticeComment.getCmId());
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String noticeCommentModify(Model model, NoticeCommentForm noticeCommentForm, @PathVariable("id") Integer cmId, Principal principal) {
		NoticeComment noticeComment = this.noticeCommentService.getNoticeComment(cmId);
		model.addAttribute("notice", noticeComment.getNotice());
        if (!noticeComment.getCmAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        noticeCommentForm.setCmText(noticeComment.getCmText());
        return "answer_form";
    }
	
	@PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String noticeCommentModify(@Valid NoticeCommentForm noticeCommentForm, BindingResult bindingResult,
            @PathVariable("id") Integer cmId, Principal principal) {
		
		if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        NoticeComment noticeComment = this.noticeCommentService.getNoticeComment(cmId);
        if (!noticeComment.getCmAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.noticeCommentService.modify(noticeComment, noticeCommentForm.getCmText());
        return String.format("redirect:/notice/detail/%s#comment_%s", noticeComment.getNotice().getNoticeId(), noticeComment.getCmId());
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String noticeCommentDelete(Principal principal, @PathVariable("id") Integer cmId) {
		NoticeComment noticeComment = this.noticeCommentService.getNoticeComment(cmId);
        if (!noticeComment.getCmAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.noticeCommentService.delete(noticeComment);
        return String.format("redirect:/notice/detail/%s", noticeComment.getNotice().getNoticeId());
    }
}