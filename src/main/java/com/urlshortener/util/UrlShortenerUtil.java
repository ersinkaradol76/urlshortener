package com.urlshortener.util;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.validator.routines.UrlValidator;

import com.urlshortener.exceptions.UrlShortenerException;
import com.urlshortener.model.UrlShortenerInFile;
import com.urlshortener.model.UrlShortenerInMemory;

public class UrlShortenerUtil {

	private UrlShortenerUtil() {
		super();
	}


	private static char[] characters = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9' };
	// This array is used for character to number mapping

	private static Random random = new Random(); // Random object used to generate random integers

	public static final int KEY_LENGTH = 8;

	public static final int UPPER_BOUND_FOR_RANDOM = 62;

	public static final String DOMAIN = "https://www.rocketml.net";

	public static String generateKeyInMemory(UrlShortenerInMemory urlShortenerInMemory) {
		StringBuilder key = new StringBuilder();
		boolean flag = true;
		while (flag) {
			key.append("");
			for (int i = 0; i <= KEY_LENGTH; i++) {
				key.append(characters[UrlShortenerUtil.random.nextInt(UPPER_BOUND_FOR_RANDOM)]);
			}
			if (!urlShortenerInMemory.getShortenedMap().containsKey(key.toString())) {
				flag = false;
			}
		}
		return key.toString();
	}

	public static String generateKeyInFile(UrlShortenerInFile urlShortenerInFile) throws UrlShortenerException {
		Map<String, String> shortenedMap = new HashMap<>();
		StringBuilder key = new StringBuilder();
		try (Scanner scanner = new Scanner(urlShortenerInFile.getShortenedFile())) {
			while (scanner.hasNextLine()) {
				String newLine = scanner.nextLine();
				String[] urls = newLine.split("=");
				shortenedMap.put(urls[0], urls[1]);
			}
			boolean flag = true;
			while (flag) {
				key.append("");
				for (int i = 0; i <= KEY_LENGTH; i++) {
					key.append(characters[UrlShortenerUtil.random.nextInt(UPPER_BOUND_FOR_RANDOM)]);
				}
				if (!shortenedMap.containsKey(key.toString())) {
					flag = false;
				}
			}
		} catch (FileNotFoundException e) {
			throw new UrlShortenerException(e.getMessage());
		}

		return key.toString();
	}

	public static String validateUrl(String url) throws UrlShortenerException {

		try {
			url = url.replace("url=", "");
			if (url.isEmpty()) {
				throw new UrlShortenerException("Please enter the url to shorten!");
			} else {
				url = URLDecoder.decode(url, "UTF-8");
				UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });
				if (urlValidator.isValid(url)) {
					return url;
				}else {
					throw new UrlShortenerException("URL Invalid: " + url);
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new UrlShortenerException(e.getMessage());
		}
	}

}
