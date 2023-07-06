package com.insight.user;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.insight.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(String username, String studentName, String email, String password,
    		String phoneNumber, String major, String grade, String doing, String condition,
    		String wantedAct, String introduction) {
    	UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setStudentName(studentName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);
        user.setMajor(major);
        user.setGrade(grade);
        user.setDoing(doing);
        user.setCondition(condition);
        user.setWantedAct(wantedAct);
        user.setIntroduction(introduction);
        user.setJoinDate(LocalDate.now());
        this.userRepository.save(user);
    }
    public UserInfo getUser(String username) {
        Optional<UserInfo> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
    public void modify(UserInfo user, String studentName, String email,
    		String password ,String phoneNumber, String major, 
    		String grade, String doing, String condition,
    		String wantedAct, String introduction) {
        user.setStudentName(studentName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);
        user.setMajor(major);
        user.setGrade(grade);
        user.setDoing(doing);
        user.setCondition(condition);
        user.setWantedAct(wantedAct);
        user.setIntroduction(introduction);
        this.userRepository.save(user);
    }
}