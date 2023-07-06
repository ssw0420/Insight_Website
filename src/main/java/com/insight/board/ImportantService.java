package com.insight.board;

import java.util.List;
import java.util.Map;

import com.insight.DataNotFoundException;
import com.insight.user.UserInfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.domain.Sort;

import lombok.RequiredArgsConstructor;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
@RequiredArgsConstructor
@Service
public class ImportantService {
	
	public int count=0;
	
	private final ImportantRepository importantRepository;
	
	//검색기능
	private Specification<Important> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Important> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거 
                Join<Important, UserInfo> u1 = q.join("impoAuthor", JoinType.LEFT);
                Predicate predicate= cb.or(cb.like(q.get("impoTitle"), "%" + kw + "%"), // 제목
                        cb.like(u1.get("username"), "%" + kw + "%")    // 내용 
                        );
        		predicate = cb.and(
                        predicate,
                        cb.like(q.get("impoTf"), "%" + "FALSE" + "%")   // 질문 작성자 
                    );
                return predicate;
                 }
        };
    }
	
	/*
	 * public List<Important> getList(){ return this.importantRepository.findAll();
	 * 
	 * }
	 */
	
	public Important getImportant(Integer impoId) {  
        Optional<Important> important = this.importantRepository.findById(impoId);
        if (important.isPresent()) {
            return important.get();
        } else {
            throw new DataNotFoundException("important not found");
        }
    }
	
	public void create(String impoTitle, String impoText, UserInfo user, Boolean impoTf) {
		Important q = new Important();
        q.setImpoTitle(impoTitle);
        q.setImpoText(impoText);
        q.setImpoRegister(LocalDateTime.now());
        q.setImpoAuthor(user);
        q.setImpoTf(impoTf);
        this.importantRepository.save(q);
    }
	
	//필독인거
	/*public List<Important> getList() {
		List<Important> sorts = new ArrayList<>();
		count = sorts.size();
		sorts = importantRepository.findByImpoTf(true);	
		//sorts.sort(null);
		Collections.sort(sorts, Sort.Order.desc("impoRegister"));
		return this.importantRepository.findByImpoTf(true);
	}*/
	
	//필독인거
	public List<Important> getList() {
	    List<Important> sorts = importantRepository.findByImpoTf(true);
	    Collections.sort(sorts, Comparator.comparing(Important::getImpoRegister).reversed());
	    count = sorts.size();
	    return sorts;
	}

	
	//필독아닌거
	public Page<Important> getPage(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("impoRegister"));
        Pageable pageable = PageRequest.of(page, 10-count, Sort.by(sorts));
        Specification<Important> spec = search(kw);
        return this.importantRepository.findAll(spec, pageable);
    }
	
	
	public void modify(Important important, String impoTitle, String impoText, Boolean impoTf) {
		important.setImpoTitle(impoTitle);
		important.setImpoText(impoText);
		important.setImpoTf(impoTf);
		important.setImpoModifyRegister(LocalDateTime.now());
        this.importantRepository.save(important);
    }
	
	public void delete(Important important) {
        this.importantRepository.delete(important);
    }
	
	public void uploadFile(MultipartHttpServletRequest multiRequest) throws Exception{
		Map<String, MultipartFile> files = multiRequest.getFileMap();
		Iterator<Map.Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile mFile=null;
		String filePath = "C:\\Users\\kimbo\\git";
		
		while(itr.hasNext()) {
			Map.Entry<String, MultipartFile> entry = itr.next();
			
			mFile=entry.getValue();
			
			String fileOrgName=mFile.getOriginalFilename();
			
			if(!fileOrgName.isEmpty()) {
				File fileFolder=new File(filePath);
				
				if(!fileFolder.exists()) {
					if(fileFolder.mkdir())
						System.out.println("[file.mkdidrs] : Success!!");
				}
			
				File saveFile = new File(filePath, fileOrgName);
				
				
				
				mFile.transferTo(saveFile);
			}
			
			
		}
	
	}
}
