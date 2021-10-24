package com.urlshortener.model;

import java.util.HashMap;
import java.util.Random;

/*
 * URL Shortener
 */
public class UrlShortener {
	// storage for generated keys
	private HashMap<String, String> shortenedMap; // short url-long url map
	private HashMap<String, String> controlMap;// long url-short url map to control

	public static final int KEY_LENGTH = 8;

	public static final int UPPER_BOUND_FOR_RANDOM = 62;

	public static final String DOMAIN = "https://www.rocketml.net";

	private char characters[]; // This array is used for character to number mapping
	private Random random; // Random object used to generate random integers


	// Default Constructor
	public UrlShortener() {
		shortenedMap = new HashMap<String, String>();
		controlMap = new HashMap<String, String>();
		random = new Random();
		characters = new char[62];
		for (int i = 0; i < 62; i++) {
			int j = 0;
			if (i < 10) {
				j = i + 48;
			} else if (i > 9 && i <= 35) {
				j = i + 55;
			} else {
				j = i + 61;
			}
			characters[i] = (char) j;
		}
	}


	public HashMap<String, String> getShortenedMap() {
		return shortenedMap;
	}

	public void setShortenedMap(HashMap<String, String> shortenedMap) {
		this.shortenedMap = shortenedMap;
	}

	public HashMap<String, String> getControlMap() {
		return controlMap;
	}

	public void setControlMap(HashMap<String, String> controlMap) {
		this.controlMap = controlMap;
	}

	public char[] getCharacters() {
		return characters;
	}

	public void setCharacters(char[] characters) {
		this.characters = characters;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

}
