package com.urlshortener.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {


	private static final String UPLOAD_DIR = "./uploads/";

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	private FileUtil() {
		super();
	}

	/**
	 *
	 * This method reads csv file, splits the fields into a string array, creates an
	 * object of FileLineInfo by this fields string array and line number and puts
	 * in an ArrayList of FileLineInfo.
	 *
	 * @param csvFilePath the csv file path
	 * @return the list of all the lines of the csv file that
	 * @throws FileUploadException
	 *
	 */


	public static List<String> readCsvFileToStringList(String csvFilePath) throws FileUploadException {


		List<String> fileLineInfoList = new ArrayList<>();
		Path pathToFile = Paths.get(csvFilePath);
		try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
			String line = br.readLine();
			int counter = 0;
			while (line != null) {
				if (counter != 0) { // first line of csv file is header so ignoring that
					String[] fields = line.split(",");

					//fileLineInfoList.add(fields);
				}
				counter++;
				line = br.readLine();

			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			throw new FileUploadException(csvFilePath + " file is not uploaded!");
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new FileUploadException(csvFilePath + " file is not available!");
		}
		return fileLineInfoList;

	}


	/**
	 *
	 * This method uploads the selected file to the specified directory
	 *
	 * @param file		the csv file that will be uploaded
	 * @return the path of the uploaded file
	 * @throws FileUploadException
	 */

	public static Path uploadCsvFile (MultipartFile file) throws FileUploadException {
		if (file == null) {
			throw new FileUploadException("Can not upload file ");
		}
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path path = Paths.get(UPLOAD_DIR + fileName);
		try {
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new FileUploadException("Can not upload file " + fileName);
		}


		return path;

	}

	/**
	 *
	 * This method deletes the specified file
	 *
	 * @param path the path of the file that will be deleted
	 * @throws IOException
	 */

	public static void deleteCsvFile(Path path) throws IOException {

		Files.delete(path);
	}


}
