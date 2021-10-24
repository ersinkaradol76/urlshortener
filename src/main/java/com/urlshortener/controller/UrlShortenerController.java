package com.urlshortener.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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

	@Autowired
	UrlShortenerService urlShortenerService;

	@GetMapping("/")
	public String displayLogin() {
	    return "index";
	}

	@PostMapping("/shortUrlInMemory")
	private String createShortUrl(@RequestBody String url, RedirectAttributes attributes) {

		try {
			url = URLDecoder.decode(url.replace("url=", ""), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			attributes.addFlashAttribute(MESSAGE_PARAM_NAME, e.getMessage());
			return REDIRECT_TO_BASE;
		}
		if (url.isEmpty()) {
			attributes.addFlashAttribute(MESSAGE_PARAM_NAME, "Please enter the url to shorten!");
			return REDIRECT_TO_BASE;
		} else {

			UrlKey urlKey = null;
			try {
				urlKey = urlShortenerService.shortenURL(url);

				if (urlKey.isAddedNow()) {
					attributes.addFlashAttribute(MESSAGE_PARAM_NAME, "URL is added into HashMap as " + urlKey.getKey());
				} else {
					attributes.addFlashAttribute(MESSAGE_PARAM_NAME,
							"URL was added into HashMap before as " + urlKey.getKey());
				}

			} catch (UrlShortenerException e) {
				attributes.addFlashAttribute(MESSAGE_PARAM_NAME, e.getMessage());
				return REDIRECT_TO_BASE;
			}
			return REDIRECT_TO_BASE;
		}
	}


}