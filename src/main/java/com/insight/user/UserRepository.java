package com.insight.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
	Optional<UserInfo> findByusername(String username);
	UserInfo findByMajor(String major);
	UserInfo findByGrade(String grade);
	UserInfo findByCondition(String condition);
	UserInfo findByWantedAct(String wantedAct);
}