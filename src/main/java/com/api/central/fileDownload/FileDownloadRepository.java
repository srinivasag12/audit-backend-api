package com.api.central.fileDownload;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.central.audit.entity.FileDownloadHistory;


@Repository
public interface FileDownloadRepository extends JpaRepository<FileDownloadHistory, String> {

	//FileDownloadHistory getDetailsByOtp(String otp);
	
	FileDownloadHistory getDetailsByOtpAndLinkId(String otp, Integer linkId);
}
