package com.urlshortener.service;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.urlshortener.exceptions.UrlShortenerException;
import com.urlshortener.model.UrlKey;
import com.urlshortener.model.UrlShortenerInFile;
import com.urlshortener.model.UrlShortenerInMemory;
import com.urlshortener.util.UrlShortenerUtil;

@Service
public class UrlShortenerService {

	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);

	private static final String FILE_KEY_VALUE_SEPARATOR = "=====";

	private UrlShortenerInMemory urlShortenerInMemory;

	private UrlShortenerInFile urlShortenerInFile;

	public UrlShortenerService() {
		this.urlShortenerInMemory = new UrlShortenerInMemory();
		this.urlShortenerInFile = new UrlShortenerInFile();
	}

	public UrlShortenerInMemory getUrlShortenerInMemory() {
		return urlShortenerInMemory;
	}

	public void setUrlShortenerInMemory(UrlShortenerInMemory urlShortenerInMemory) {
		this.urlShortenerInMemory = urlShortenerInMemory;
	}

	public UrlShortenerInFile getUrlShortenerInFile() {
		return urlShortenerInFile;
	}

	public void setUrlShortenerInFile(UrlShortenerInFile urlShortenerInFile) {
		this.urlShortenerInFile = urlShortenerInFile;
	}

	// shortenURL
	// the public method which can be called to shorten a given URL
	public UrlKey shortenURLInMemory(String longURL) throws UrlShortenerException {
		UrlKey urlKey = null;
		try {
			longURL = UrlShortenerUtil.validateUrl(longURL);
			longURL = preventSimilarUrlInsertion(longURL);
			if (urlShortenerInMemory.getControlMap().containsKey(longURL)) {
				String shortURL = UrlShortenerUtil.DOMAIN + "/" + urlShortenerInMemory.getControlMap().get(longURL);
				urlKey = new UrlKey(shortURL, false);
				logger.info("URL Key was generated: {} ", urlKey.getKey());
			} else {
				String key = getKeyInMemory(longURL);
				String shortURL = UrlShortenerUtil.DOMAIN + "/" + key;
				putKeyInMemoryMaps(key, longURL);
				urlKey = new UrlKey(shortURL, true);
				logger.info("URL Key is generated:  {} ", urlKey.getKey());
			}
		} catch (Exception e) {
			throw new UrlShortenerException(e.getMessage());
		}
		return urlKey;
	}

	public UrlKey shortenURLInFile(String longURL) throws UrlShortenerException {

		UrlKey urlKey = null;
		try {
			if (!urlShortenerInFile.getShortenedFile().exists()) {
				urlShortenerInFile.getShortenedFile().createNewFile();
			}
			if (!urlShortenerInFile.getControlFile().exists()) {
				urlShortenerInFile.getControlFile().createNewFile();
			}
			longURL = UrlShortenerUtil.validateUrl(longURL);
			longURL = preventSimilarUrlInsertion(longURL);
			String key = checkUrlInFile(longURL);
			if (key != null) {
				String shortURL = UrlShortenerUtil.DOMAIN + "/" + key;
				urlKey = new UrlKey(shortURL, false);
				logger.info("URL Key was generated: {} ", urlKey.getKey());
			} else {
				key = getKeyInFile(longURL);
				String shortURL = UrlShortenerUtil.DOMAIN + "/" + key;
				putKeyInFiles(key, longURL);
				urlKey = new UrlKey(shortURL, true);
				logger.info("URL Key is generated:  {} ", urlKey.getKey());
			}
		} catch (Exception e) {
			throw new UrlShortenerException(e.getMessage());
		}
		return urlKey;




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


	private String getKeyInMemory(String longURL) {
		return UrlShortenerUtil.generateKeyInMemory(urlShortenerInMemory);
	}

	private void putKeyInMemoryMaps(String key, String longURL) {
		urlShortenerInMemory.getShortenedMap().put(key, longURL);
		urlShortenerInMemory.getControlMap().put(longURL, key);
	}

	private String getKeyInFile(String longURL) throws UrlShortenerException {
		return UrlShortenerUtil.generateKeyInFile(urlShortenerInFile);
	}

	public String checkUrlInFile(String longUrl) throws UrlShortenerException {
		String key = null;
		try (Scanner scanner = new Scanner(urlShortenerInFile.getControlFile())) {

			while (scanner.hasNextLine()) {
				String newLine = scanner.nextLine();
				String[] urls = newLine.split(FILE_KEY_VALUE_SEPARATOR);
				if (urls[0].equals(longUrl)) {
					key = urls[1];
					break;
				}
			}
		} catch (FileNotFoundException e) {

			throw new UrlShortenerException(e.getMessage());
		}

		return key;
	}

	public String checkKeyInFile(String key) throws UrlShortenerException {
		String url = null;
		try (Scanner scanner = new Scanner(urlShortenerInFile.getShortenedFile())) {

			while (scanner.hasNextLine()) {
				String newLine = scanner.nextLine();
				String[] urls = newLine.split(FILE_KEY_VALUE_SEPARATOR);
				if (urls[0].equals(key)) {
					url = urls[1];
					break;
				}
			}
		} catch (FileNotFoundException e) {

			throw new UrlShortenerException(e.getMessage());
		}

		return url;
	}

	public void putKeyInFiles(String key, String longUrl) throws UrlShortenerException {

		try (Writer writerShortened = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(urlShortenerInFile.getShortenedFile(), true), "UTF-8"));
				Writer writerControl = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(urlShortenerInFile.getControlFile(), true), "UTF-8"));) {
			writerShortened.append(key + FILE_KEY_VALUE_SEPARATOR + longUrl + "\n");
			writerControl.append(longUrl + FILE_KEY_VALUE_SEPARATOR + key + "\n");
		} catch (IOException e) {

			throw new UrlShortenerException(e.getMessage());
		}
	}

}
