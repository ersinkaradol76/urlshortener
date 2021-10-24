package com.urlshortener.util;

import com.urlshortener.model.UrlShortener;

public class RandomiseUtil {


	private static final String UPLOAD_DIR = "./uploads/";

	private RandomiseUtil() {
		super();
	}

	public static String generateKey(UrlShortener urlShortener) {
		StringBuilder key = new StringBuilder();
		boolean flag = true;
		while (flag) {
			key.append("");
			for (int i = 0; i <= UrlShortener.KEY_LENGTH; i++) {
				key.append(urlShortener.getCharacters()[urlShortener.getRandom()
						.nextInt(UrlShortener.UPPER_BOUND_FOR_RANDOM)]);
			}
			if (!urlShortener.getShortenedMap().containsKey(key.toString())) {
				flag = false;
			}
		}
		return key.toString();
	}

}
