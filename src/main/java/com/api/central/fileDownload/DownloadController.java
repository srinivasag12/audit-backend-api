package com.api.central.fileDownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.central.audit.entity.FileDownloadHistory;
import com.api.central.util.AppConstant;
import com.api.central.util.AppUtil;

@Controller
@RequestMapping("public")
public class DownloadController {

	@Autowired
	FileOperationValues values;

	@Autowired
	private FileDownloadRepository repo;

	@Autowired
	ServletContext servletContext;

	@Autowired
	AppUtil appUtil;

	@RequestMapping(value = "/verify/{linkId}", method = RequestMethod.GET)
	public String showLoginPage(ModelMap model, @PathVariable Integer linkId) {
		return "downloadAuth";
	}

	@RequestMapping(value = "/verify/{linkId}", method = RequestMethod.POST)
	public Object showWelcomePage(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			@RequestParam String otp, @PathVariable Integer linkId) throws FileNotFoundException {

		//FileDownloadHistory data = repo.getDetailsByOtp(otp);
		
		FileDownloadHistory data = repo.getDetailsByOtpAndLinkId(otp, linkId);

		if (data == null) {
			return new CustomErrorController().showFailedDownLoad();
		}
		if (data.getStatus() == 'A') {
			long diffInMillies = Math.abs(new Date().getTime() - data.getCreated_at().getTime());
			long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			if (diff >= 15) {
				data.setStatus('I');
				repo.save(data);
				return new CustomErrorController().showFileExpMsg();
			}
			String path = servletContext.getContextPath() + "\\" + "output" + "\\" + data.getFilename() + "."
					+ data.getFileType();
			return new ResponseEntity<Resource>(appUtil.getFileByteStream(path),
					appUtil.setHeaderStreamType(path, data.getFilename() + "." + data.getFileType()), HttpStatus.OK);

		} else {
			return new CustomErrorController().showFailedDownLoad();
		}
	}

}
