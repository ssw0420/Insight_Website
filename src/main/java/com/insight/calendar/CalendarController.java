package com.insight.calendar;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.insight.user.UserInfo;
import com.insight.user.UserService;


@RequestMapping("/calendar")
@RequiredArgsConstructor
@Controller

public class CalendarController {
	private final CalendarService calService;
	private final UserService userService;
	
	@GetMapping("")
	public String calendarMain() {
        return "calendar_main";
    }
	
	@GetMapping("/{day}")
	public String loadCalendar(Model model,@PathVariable LocalDate day) {
		List<Calendar> calendar = this.calService.getDayList(day);
		model.addAttribute("calendar",calendar);
		return "calendar_main";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
    public String createAnswer(CalendarRegisteForm calendarRegisteForm) {
		calendarRegisteForm.setCalStartDay((LocalDate.now()).toString());
		calendarRegisteForm.setCalEndDay(null);
        return "calendar_form";
    }
	
	@PreAuthorize("isAuthenticated()")
	//일정 등록 html에서 등록 버튼 눌렀을 때 저장하는 함수
	@PostMapping("/create")
	public String createAnswer(@Valid CalendarRegisteForm calendarRegisteForm, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
            return "calendar_form";
        }
		if(calendarRegisteForm.getCalEndDay() != "")
		{
			if(0<(LocalDate.parse(calendarRegisteForm.getCalStartDay())).compareTo(LocalDate.parse(calendarRegisteForm.getCalEndDay())))
				return "calendar_form";
		}
		UserInfo userInfo = this.userService.getUser(principal.getName());
        this.calService.addData(userInfo, calendarRegisteForm.getCalText(), calendarRegisteForm.getCalStartDay(), calendarRegisteForm.getCalEndDay(), 
        		calendarRegisteForm.getCalStartTime(), calendarRegisteForm.getCalEndTime());
        return String.format("redirect:/calendar/%s", calendarRegisteForm.getCalStartDay());
    }

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String modifyCalendar(CalendarRegisteForm calendarRegisteForm, Model model, @PathVariable("id") Integer calId
			,Principal principal) {
		Calendar calendar = this.calService.getInfo(calId);
		if (!calendar.getCalAuthor().getUsername().equals(principal.getName())) {
			return String.format("redirect:/calendar/%s", calendar.getCalStartDay());
        }
		//매개변수를 CalendarForm, 일정의 id, 사용자의 정보로 한다.
		model.addAttribute(calendar);
		calendarRegisteForm.setCalStartDay((calendar.getCalStartDay()).toString());
		calendarRegisteForm.setCalText(calendar.getCalText());
		if(calendar.getCalEndDay()==null)
			calendarRegisteForm.setCalEndDay(null);
		else
			calendarRegisteForm.setCalEndDay(calendar.getCalEndDay().toString());
		if(calendar.getCalStartTime()==null)
			calendarRegisteForm.setCalStartTime(null);
		else
			calendarRegisteForm.setCalStartTime(calendar.getCalStartTime().toString());
		if(calendar.getCalEndTime()==null)
			calendarRegisteForm.setCalEndTime(null);
		else
			calendarRegisteForm.setCalEndTime(calendar.getCalEndTime().toString());
		return "calendar_modify";
	}
	
	@PreAuthorize("isAuthenticated()")	
	//일정 수정 페이지에서 일정을 수정후 확인 버튼을 누르면 작동하는 함수
	//역할조건 안넣음 @PreAuthorize
    @PostMapping("/modify/{id}")
    public String modifyCalendar(@Valid CalendarRegisteForm calendarRegisteForm, Model model,
    		BindingResult bindingResult, Principal principal, @PathVariable("id") Integer calId) {
        if (bindingResult.hasErrors()) {
            return "calendar_modify";
        }
        Calendar calendar = this.calService.getInfo(calId);
        if (!calendar.getCalAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        if(calendarRegisteForm.getCalEndDay() != "")
		{
			if(0<(LocalDate.parse(calendarRegisteForm.getCalStartDay())).compareTo(LocalDate.parse(calendarRegisteForm.getCalEndDay())))
				return "calendar_modify";
		}
        this.calService.modify(calendar, calendarRegisteForm.getCalText(), calendarRegisteForm.getCalStartDay(), calendarRegisteForm.getCalEndDay(), 
        		calendarRegisteForm.getCalStartTime(), calendarRegisteForm.getCalEndTime());

        return String.format("redirect:/calendar/%s", calendarRegisteForm.getCalStartDay()); //달력 출력 화면으로 전환
    }
	
	@PreAuthorize("isAuthenticated()")
    //일정 삭제 시 작동하는 함수
	@GetMapping("/delete/{id}")
    public String deleteCalendar(@PathVariable("id") Integer calId,
			Principal principal) {
		Calendar calendar = this.calService.getInfo(calId);
		
		if(!calendar.getCalAuthor().getUsername().equals(principal.getName()))/*사용자의 학번과 작성자의 학번 비교)*/ {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
		LocalDate back = calendar.getCalStartDay();
		this.calService.delete(calendar);
		return String.format("redirect:/calendar/%s",back);
    }
}
