package com.urlshortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.urlshortener.exceptions.UrlShortenerException;
import com.urlshortener.model.UrlKey;
import com.urlshortener.service.UrlShortenerService;

@Controller
public class UrlShortenerController {

	private static final String MESSAGE_PARAM_NAME = "message";

	private static final String REDIRECT_TO_BASE = "redirect:/";

	private static final String REDIRECT_TO_FILE_BASE = "redirect:/file";

	@Autowired
	UrlShortenerService urlShortenerService;

	@GetMapping("/")
	public String getInMemoryIndexPage() {
	    return "index";
	}

	@GetMapping("/file")
	public String getInFileIndexPage() {
		return "indexfile";
	}

	@PostMapping("/shortUrlInMemory")
	public String createShortUrlInMemory(@RequestBody String url, RedirectAttributes attributes) {

		try {
			UrlKey urlKey = urlShortenerService.shortenURLInMemory(url);
			if (urlKey.isAddedNow()) {
				attributes.addFlashAttribute(MESSAGE_PARAM_NAME, "URL is added into HashMap as " + urlKey.getKey());
			} else {
				attributes.addFlashAttribute(MESSAGE_PARAM_NAME,
						"URL was added into HashMap before as " + urlKey.getKey());
			}
		} catch (UrlShortenerException e) {
			attributes.addFlashAttribute(MESSAGE_PARAM_NAME, e.getMessage());
		}
		return REDIRECT_TO_BASE;

	}

	@PostMapping("/shortUrlInFile")
	public String createShortUrlInFile(@RequestBody String url, RedirectAttributes attributes) {

		try {
			UrlKey urlKey = urlShortenerService.shortenURLInFile(url);
			if (urlKey.isAddedNow()) {
				attributes.addFlashAttribute(MESSAGE_PARAM_NAME, "URL is added into File as " + urlKey.getKey());
			} else {
				attributes.addFlashAttribute(MESSAGE_PARAM_NAME,
						"URL was added into File before as " + urlKey.getKey());
			}
		} catch (UrlShortenerException e) {
			attributes.addFlashAttribute(MESSAGE_PARAM_NAME, e.getMessage());
		}
		return REDIRECT_TO_FILE_BASE;

	}

}