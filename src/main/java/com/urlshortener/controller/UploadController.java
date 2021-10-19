package com.urlshortener.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.urlshortener.model.Url;

@Controller
public class UploadController {

	private static final String MESSAGE_PARAM_NAME = "message";

	private static final String REDIRECT_TO_BASE = "redirect:/";




	/*@GetMapping("/")
	public String homepage() {
		return "index";
	}*/


	@GetMapping("/")
	public String displayLogin(Model model) { 
	    model.addAttribute("url", new Url("test")); 
	    return "index"; 
	}
	
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("url") String url, 
			RedirectAttributes attributes, HttpServletRequest request) throws IOException {

		if (url.isEmpty()) {
			attributes.addFlashAttribute(MESSAGE_PARAM_NAME, "Please select first file to compare!");
			return REDIRECT_TO_BASE;
		}




		attributes.addFlashAttribute("result", "Comparison Results");

		attributes.addFlashAttribute("header1", "General Info");
		
		attributes.addFlashAttribute("message", "Everything ok");
		



		return REDIRECT_TO_BASE;
	}


	@GetMapping("/detail")
	public String unmatched(HttpSession session, Model model) {


		//model.addAttribute("nonmatcheds1", nonmatcheds1);


		model.addAttribute("header2", "Candidate List");
		model.addAttribute("header3", "Nonmatched List");
		model.addAttribute("header4", "Errors and Repeated Lines");

		return "detail :: attributes";
	}

}