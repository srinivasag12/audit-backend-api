package com.api.central.laptopSoft;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/download")
public class FileDownloadController {

	@Autowired
	ServletContext servletContext;

	private static final Logger log = LoggerFactory.getLogger(FileDownloadController.class);

	@RequestMapping("/file/{fileName:.+}")
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName) throws IOException {

		log.info(fileName);
		String EXTERNAL_FILE_PATH = servletContext.getContextPath() + "/src/main/java/com/api/central/laptopSoft/";
		log.info("EXTERNAL_FILE_PATH" + EXTERNAL_FILE_PATH);
		File file = new File(EXTERNAL_FILE_PATH + fileName);

		if (file.exists()) {

			// get the mimetype
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				// unknown mimetype so set the mimetype to application/octet-stream
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);

			response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());

		}
	}

	@RequestMapping("/loggers/{fileName:.+}/{view}/{type}")
	public String downloadlogResource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName, @PathVariable("view") String view,
			@PathVariable("type") String type) throws IOException {

		String EXTERNAL_FILE_PATH = "C:/logs/IRI/" + type + "/";
		
		File file = new File(EXTERNAL_FILE_PATH + fileName);

		if (file.exists()) {

			log.info("FILE_PATH =>> " + EXTERNAL_FILE_PATH + "file Name =>> " + file.getName());

			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			
			if (mimeType == null)
				mimeType = response.getContentType();

			response.setContentType(mimeType);

			if (view.equals("VIEW"))
				response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
			else
				response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());

		}

		else {
			return "File Not Found";
		}

		return EXTERNAL_FILE_PATH;

	}

}