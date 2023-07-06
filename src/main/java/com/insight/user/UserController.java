package com.insight.user;

import jakarta.validation.Valid;

import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.insight.calendar.Calendar;
import com.insight.calendar.CalendarRegisteForm;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/join")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        try {
        userService.create(userCreateForm.getUsername(),userCreateForm.getStudentName(),userCreateForm.getEmail(), 
                 userCreateForm.getPassword1(),userCreateForm.getPhoneNumber(),userCreateForm.getMajor(),
                 userCreateForm.getGrade(),userCreateForm.getDoing(),userCreateForm.getCondition(),
                 userCreateForm.getWantedAct(),userCreateForm.getIntroduction());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
        
    }
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
    
    @GetMapping(value = "/detail/{id}")
    public String detail(UserModifyForm userModifyForm, Model model, @PathVariable("id") String username,
    		Principal principal, BindingResult bindingResult) {
    	UserInfo userInfo = this.userService.getUser(username);
    	model.addAttribute("username",username);
    	if(!userInfo.getUsername().equals(username)) {
    		return "redirect:/";
    	}
//    	if (!userModifyForm.getPassword1().equals(userModifyForm.getPassword2())) {
//            bindingResult.rejectValue("password2", "passwordInCorrect", 
//                    "2개의 패스워드가 일치하지 않습니다.");
//            return "signup_form";
//        }
    	userModifyForm.setUsername(userInfo.getUsername());
    	userModifyForm.setStudentName(userInfo.getStudentName());
    	userModifyForm.setCondition(userInfo.getCondition());
    	userModifyForm.setDoing(userInfo.getDoing());
    	userModifyForm.setEmail(userInfo.getEmail());
    	userModifyForm.setGrade(userInfo.getGrade());
    	userModifyForm.setIntroduction(userInfo.getIntroduction());
    	userModifyForm.setMajor(userInfo.getMajor());
    	userModifyForm.setPassword(userInfo.getPassword());
    	userModifyForm.setPhoneNumber(userInfo.getPhoneNumber());
    	userModifyForm.setWantedAct(userInfo.getWantedAct());
    	return "user_detail";
    }
    
    @PostMapping("/detail/{id}")
    public String detail(@Valid UserModifyForm userModifyForm, BindingResult bindingResult,
    		@PathVariable("id") String username) {
        if (bindingResult.hasErrors()) {
            return "user_detail";
        }
        UserInfo userInfo = this.userService.getUser(username);
        try {
        userService.modify(userInfo, userModifyForm.getStudentName(),userModifyForm.getEmail(), 
        		userModifyForm.getPassword(),userModifyForm.getPhoneNumber(),userModifyForm.getMajor(),
        		userModifyForm.getGrade(),userModifyForm.getDoing(),userModifyForm.getCondition(),
        		userModifyForm.getWantedAct(),userModifyForm.getIntroduction());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "user_detail";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "user_detail";
        }

        return "redirect:/";
        
    }
}
