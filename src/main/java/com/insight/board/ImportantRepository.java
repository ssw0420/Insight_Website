package com.insight.board;


import java.util.List;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportantRepository extends JpaRepository<Important, Integer>{
	Page<Important> findAll(Pageable pageable); // main 리스트 호출
	Page<Important> findByImpoTf(Boolean impoTf, Pageable pageable);		//필독아닌거
	List<Important> findByImpoTf(Boolean impoTf);							//impoTf가 true인것
	Page<Important> findAll(Specification<Important> spec, Pageable pageable);
}
