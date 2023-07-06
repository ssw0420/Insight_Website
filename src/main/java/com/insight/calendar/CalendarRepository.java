package com.insight.calendar;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CalendarRepository extends JpaRepository<Calendar, Integer>{
	List<Calendar> findByCalStartDay(LocalDate register);
}