package com.urlshortener.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UrlShortenerUtilTest {

	@Test
	public void generateKeyInMemoryTest() {
		String key = UrlShortenerUtil.generateKeyInMemory(UrlShortenerUtilTestFixtures.urlShortenerInMemory);
		assertTrue(key != null);

	}

	@Test
	public void validateUrlTest() throws Exception {
		String result = UrlShortenerUtil.validateUrl(UrlShortenerUtilTestFixtures.webAddress);
		assertTrue(result.equals(UrlShortenerUtilTestFixtures.webAddress));

	}



}
