package com.reconciliation.util;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Test;

import com.urlshortener.util.FileUtil;

public class FileUtilTest {

	@Test
	public void uploadingFileCheckIfExist() throws Exception {
		Path path = FileUtil.uploadCsvFile(FileUtilTestFixtures.sampleFile);
		assertTrue(Files.exists(path));
		FileUtilTestFixtures.filesToBeDeleted.add(path);

	}

	@Test
	public void deletingUploadedFileCheckIfNotExist() throws Exception {
		Path path = FileUtil.uploadCsvFile(FileUtilTestFixtures.sampleFile);
		FileUtil.deleteCsvFile(path);
		assertTrue(Files.notExists(path));
		FileUtilTestFixtures.filesToBeDeleted.add(path);

	}


	@After
	public void cleanup() {
		FileUtilTestFixtures.filesToBeDeleted.forEach(path -> {
			try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
