package com.api.central.audit.delegate;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface ReportService {

	/*
	 * public String generateReport(String format,String module,String criteria,String params);
	 */
	public byte[] generateReport(String reportParamVal,String outputType ,String sourcefilepath, String destinationPath,
			String directoryName, String userName) throws IOException, JRException;

     
	JasperPrint fillReport(Map<String, Object> model) throws Exception;

	byte[] convertToByte(JasperPrint jasperPrint,String fileName);

	HttpHeaders setHeaderType(String string);
	/*public void downloadPdf(HttpServletResponse response, HttpServletRequest request, String destinationPath,
			String directoryName);*/
	
	/*public String downloadReport(String format,String filePath,HttpServletResponse response,HttpServletRequest request);*/

}
