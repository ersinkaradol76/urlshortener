package com.urlshortener.model;

public class UrlKey {

	private String key;
	private boolean addedNow;

	public UrlKey(String key, boolean addedNow) {
		super();
		this.key = key;
		this.addedNow = addedNow;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isAddedNow() {
		return addedNow;
	}

	public void setAddedNow(boolean addedNow) {
		this.addedNow = addedNow;
	}

}
