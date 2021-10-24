package com.urlshortener.model;

import java.io.File;


public class UrlShortenerInFile {

	private File shortenedFile; // short url - long url map to store

	private File controlFile; // long url - short url map to control

	private static final String FILE_DIR = "./files/";

	private static final String SHORTENED_FILE_NAME = "shortened.txt";

	private static final String CONTROL_FILE_NAME = "control.txt";

	public UrlShortenerInFile() {
		this.shortenedFile = new File(FILE_DIR + SHORTENED_FILE_NAME);
		this.shortenedFile.delete();
		this.controlFile = new File(FILE_DIR + CONTROL_FILE_NAME);
		this.controlFile.delete();
	}

	public File getShortenedFile() {
		return shortenedFile;
	}

	public void setShortenedFile(File shortenedFile) {
		this.shortenedFile = shortenedFile;
	}

	public File getControlFile() {
		return controlFile;
	}

	public void setControlFile(File controlFile) {
		this.controlFile = controlFile;
	}



}
