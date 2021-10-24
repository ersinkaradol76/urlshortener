package com.urlshortener.service;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.urlshortener.exceptions.UrlShortenerException;
import com.urlshortener.model.UrlKey;

public class UrlShortenerServiceTest {

	@InjectMocks
	UrlShortenerService urlShortenerService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void generateKeyInMemoryTest() {
		UrlKey urlKey = null;
		try {
			urlKey = urlShortenerService.shortenURLInMemory(UrlShortenerServiceTestFixtures.webAddress);
		} catch (UrlShortenerException e) {
			e.printStackTrace();
		}
		assertTrue(urlKey.getKey() != null);

		urlKey = null;
		try {
			urlKey = urlShortenerService.shortenURLInMemory(UrlShortenerServiceTestFixtures.webAddressFalse);
		} catch (UrlShortenerException e) {
			assertTrue(urlKey == null);
		}


	}

}
