package com.urlshortener.service;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
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

	/**
	 *
	 * This is the public method which can be called to shorten a given URL This
	 * method stores the url and shortened url in two maps. In the process firstly,
	 * it validates the url. Then checks if the url was stored before. If it is
	 * stored before, returns the shortened url stored before. If it was not stored,
	 * then generates a key and the stores key and url in the maps.
	 *
	 * @param longURL url that will be shortened
	 * @return
	 * @throws UrlShortenerException
	 */
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
				String key = generateKeyInMemory();
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

	/**
	 *
	 * This is the public method which can be called to shorten a given URL This
	 * method stores the url and shortened url in two files. In the process firstly,
	 * it validates the url. Then checks if the url was stored before. If it is
	 * stored before, returns the shortened url stored before. If it was not stored,
	 * then generates a key and the stores key and url in the files.
	 *
	 * @param longURL url that will be shortened
	 * @return
	 * @throws UrlShortenerException
	 */
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
				key = generateKeyInFile();
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

	/**
	 *
	 * This method controls that the url is similar with a previous one such as
	 * www.youtube.com, www.youtube.com/, http://www.youtube.com,
	 * http://www.youtube.com/ all the above URL should point to same shortened URL.
	 *
	 * @param url the url that will be cleared
	 * @return
	 */
	private String preventSimilarUrlInsertion(String url) {
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

	/**
	 *
	 * This method generates a key for memory process of the project
	 *
	 * @return the key generated
	 */
	private String generateKeyInMemory() {
		return UrlShortenerUtil.generateKeyInMemory(urlShortenerInMemory);
	}

	/**
	 *
	 * This method stores the url and the key in the two maps. One is to get the url
	 * by looking to the key and one is to control if the url is stored in the
	 * memory.
	 *
	 * @param key     the generated key for the url
	 * @param longURL the url that will be shortened
	 */
	private void putKeyInMemoryMaps(String key, String longURL) {
		urlShortenerInMemory.getShortenedMap().put(key, longURL);
		urlShortenerInMemory.getControlMap().put(longURL, key);
	}

	/**
	 *
	 * This method generates a key for file process of the project
	 *
	 * @return the key generated
	 */
	private String generateKeyInFile() throws UrlShortenerException {
		return UrlShortenerUtil.generateKeyInFile(urlShortenerInFile);
	}

	/**
	 *
	 * This method checks the url-shorturl file to find url if it is stored before
	 *
	 * @param longUrl the url that will be shortened
	 * @return
	 * @throws UrlShortenerException
	 */
	private String checkUrlInFile(String longUrl) throws UrlShortenerException {
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

	/**
	 *
	 * This method checks the shorturl-url file to get the url corresponds to the
	 * shorturl
	 *
	 * @param longUrl the url that will be shortened
	 * @return
	 * @throws UrlShortenerException
	 */
	private String checkKeyInFile(String key) throws UrlShortenerException {
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

	/**
	 *
	 * This method stores the url and the key in the two files. One is to get the
	 * url by looking to the key and one is to control if the url is stored in the
	 * memory.
	 *
	 * @param key     the generated key for the url
	 * @param longURL the url that will be shortened
	 */
	private void putKeyInFiles(String key, String longUrl) throws UrlShortenerException {

		try (Writer writerShortened = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(urlShortenerInFile.getShortenedFile(), true),
						StandardCharsets.UTF_8));
				Writer writerControl = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(urlShortenerInFile.getControlFile(), true), StandardCharsets.UTF_8));) {
			writerShortened.append(key + FILE_KEY_VALUE_SEPARATOR + longUrl + "\n");
			writerControl.append(longUrl + FILE_KEY_VALUE_SEPARATOR + key + "\n");
		} catch (IOException e) {

			throw new UrlShortenerException(e.getMessage());
		}
	}

}
