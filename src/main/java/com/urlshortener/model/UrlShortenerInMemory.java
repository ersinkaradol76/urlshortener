package com.urlshortener.model;

import java.util.HashMap;
import java.util.Map;


public class UrlShortenerInMemory {

	private Map<String, String> shortenedMap; // short url - long url map to store

	private Map<String, String> controlMap; // long url - short url map to control

	public UrlShortenerInMemory() {
		shortenedMap = new HashMap<>();
		controlMap = new HashMap<>();
	}


	public Map<String, String> getShortenedMap() {
		return shortenedMap;
	}

	public void setShortenedMap(Map<String, String> shortenedMap) {
		this.shortenedMap = shortenedMap;
	}

	public Map<String, String> getControlMap() {
		return controlMap;
	}

	public void setControlMap(Map<String, String> controlMap) {
		this.controlMap = controlMap;
	}

}
