package com.urlshortener.service;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.urlshortener.exceptions.UrlShortenerException;
import com.urlshortener.model.UrlKey;
import com.urlshortener.model.UrlShortener;
import com.urlshortener.util.RandomiseUtil;

@Service
public class UrlShortenerService {



	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);

	private UrlShortener urlShortener;

	public UrlShortenerService() {
		this.urlShortener = new UrlShortener();
	}

	public UrlShortener getUrlShortener() {
		return urlShortener;
	}

	public void setUrlShortener(UrlShortener urlShortener) {
		this.urlShortener = urlShortener;
	}

	// shortenURL
	// the public method which can be called to shorten a given URL
	public UrlKey shortenURL(String longURL) throws UrlShortenerException {
		UrlKey urlKey = null;
		UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });
		if (urlValidator.isValid(longURL)) {
			longURL = preventSimilarUrlInsertion(longURL);
			if (urlShortener.getControlMap().containsKey(longURL)) {
				String shortURL = UrlShortener.DOMAIN + "/" + urlShortener.getControlMap().get(longURL);
				urlKey = new UrlKey(shortURL, false);
				logger.debug("URL Key was generated: " + urlKey.getKey());
			} else {
				String shortURL = UrlShortener.DOMAIN + "/" + getKey(longURL);
				urlKey = new UrlKey(shortURL, true);
				logger.debug("URL Key generated: " + urlKey.getKey());
			}
		}else {
			throw new UrlShortenerException("URL Invalid: " + longURL);
		}
		return urlKey;
	}

	// expandURL
	// public method which returns back the original URL given the shortened url
	public String getLongURL(String shortURL) {
		String longURL = "";
		String key = shortURL.substring(UrlShortener.DOMAIN.length() + 1);
		longURL = urlShortener.getShortenedMap().get(key);
		return longURL;
	}

	// This method should take care various issues with a valid url
	// e.g. www.google.com,www.google.com/, http://www.google.com,
	// http://www.google.com/
	// all the above URL should point to same shortened URL
	// There could be several other cases like these.
	String preventSimilarUrlInsertion(String url) {
		if (url.substring(0, 7).equals("http://")) {
			url = url.substring(7);
		}

		if (url.substring(0, 8).equals("https://")) {
			url = url.substring(8);
		}

		if (url.charAt(url.length() - 1) == '/') {
			url = url.substring(0, url.length() - 1);
		}
		return url;
	}

	/*
	 * Get Key method
	 */
	private String getKey(String longURL) {
		String shortUrl = RandomiseUtil.generateKey(urlShortener);
		urlShortener.getShortenedMap().put(shortUrl, longURL);
		urlShortener.getControlMap().put(longURL, shortUrl);
		return shortUrl;
	}



}
