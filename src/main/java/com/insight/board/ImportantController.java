package com.insight.board;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import com.insight.user.UserInfo;
import com.insight.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/important")
public class ImportantController {
	
	
	private final ImportantService importantService;
	private final UserService userService;
	
	@GetMapping("/main")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
    		@RequestParam(value = "kw", defaultValue = "") String kw) {
        List<Important> importantList2 = this.importantService.getList();
        Page<Important> importantList = this.importantService.getPage(page, kw);
        model.addAttribute("importantList2", importantList2);
        model.addAttribute("importantList", importantList);
        model.addAttribute("kw", kw);
        
        return "important_list";
    }
	
	@GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer impoId) {
		Important important = this.importantService.getImportant(impoId);
		model.addAttribute("important", important);
        return "important_detail";
    }
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
    public String importantCreate(ImportantForm importantForm) {
        return "important_form";
    }
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
    public String importantCreate(@Valid ImportantForm importantForm, BindingResult bindingResult, Principal principal, MultipartHttpServletRequest multiRequest) throws Exception {
		if (bindingResult.hasErrors()) {
            return "important_form";
        }
		UserInfo userInfo = this.userService.getUser(principal.getName());
		this.importantService.create(importantForm.getImpoTitle(), importantForm.getImpoText(), userInfo, importantForm.getImpoTf());
		
		importantService.uploadFile(multiRequest);
		
		return "redirect:/important/main";
    }

	@PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String importantModify(ImportantForm importantForm, @PathVariable("id") Integer impoId, Principal principal) {
        Important important = this.importantService.getImportant(impoId);
        if(!important.getImpoAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        importantForm.setImpoTf(important.getImpoTf());
        importantForm.setImpoTitle(important.getImpoTitle());
        importantForm.setImpoText(important.getImpoText());
        return "important_form";
    }
	
	@PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String importantModify(@Valid ImportantForm importantForm, BindingResult bindingResult, 
            Principal principal, @PathVariable("id") Integer impoId) {
        if (bindingResult.hasErrors()) {
            return "important_form";
        }
        Important important = this.importantService.getImportant(impoId);
        if (!important.getImpoAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.importantService.modify(important, importantForm.getImpoTitle(), importantForm.getImpoText(), importantForm.getImpoTf());
        return String.format("redirect:/important/detail/%s", impoId);
    }
	
	 @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String importantDelete(Principal principal, @PathVariable("id") Integer impoId) {
	 Important important = this.importantService.getImportant(impoId);
        if (!important.getImpoAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.importantService.delete(important);
        return "redirect:/important/main";
    }
	
	 
}
