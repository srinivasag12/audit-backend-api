package com.api.central.audit.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.central.audit.delegate.ReportService;
import com.api.central.audit.delegate.SearchDelegate;
import com.api.central.audit.entity.AuditDetailView;
import com.api.central.audit.entity.CarSearchCriteria;
import com.api.central.audit.entity.IspsBean;
import com.api.central.audit.entity.MlcBean;
import com.api.central.audit.entity.SearchCriteria;
import com.api.central.audit.entity.SubReportBean1;
import com.api.central.audit.entity.VesselCriteria;
import com.api.central.carMaintenance.delegate.CarMaintenanceDelegate;
import com.api.central.certificate.delegate.CertificateDelegate;
import com.api.central.master.dao.MasterDao;
import com.api.central.master.entity.MaPort;
import com.api.central.util.AppConstant;
import com.api.central.util.AppUtil;
import com.google.gson.Gson;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("api/audit/report")

public class ReportController {

	@Autowired
	private CarMaintenanceDelegate carMaintenanceDelegate;

	@Autowired
	private SearchDelegate searchDelegate;

	@Autowired
	private CertificateDelegate certificateDelegate;

	@Autowired
	private ReportService rs;

	@Autowired
	ServletContext servletContext;

	@Autowired
	private MasterDao masterDao;

	private static final Logger log = LoggerFactory.getLogger(ReportController.class);

	@PostMapping(value = "/generatePortReport/{screenId}/{category}/{format}/{companyId}")
	public ResponseEntity<byte[]> doDownloadPortReport(@PathVariable("screenId") String screenName,
			@PathVariable("format") String outputType, @PathVariable("category") String reportCategory,
			@PathVariable("companyId") Long companyId, HttpServletRequest request, HttpServletResponse response)
			throws IOException, JRException {

		String userName = AppUtil.getUserNameFromToken(request.getHeader("X-Auth-Token"));

		String templateName = null;
		if (reportCategory != null) {
			templateName = screenName + "_" + reportCategory.toUpperCase();
		} else if (reportCategory == null) {
			templateName = screenName;
		}

		List<MaPort> port = masterDao.getAllPort(companyId);

		port.stream().forEach((c) -> {
			if (c.getActiveFlag() != null && c.getActiveFlag().equalsIgnoreCase("1")) {
				c.setActiveFlag("Yes");
			} else {
				c.setActiveFlag("No");
			}
		});

		log.info("generatePortReport getAllPort() = > " + port);

		String dataList = new Gson().toJson(port);
		log.info("port dataList => " + dataList.length());

		byte[] outp = rs.generateReport(dataList, outputType, templateName, "CENTRALPORT_WITHCR_EXCEL", templateName,
				userName);
		HttpHeaders headers = new HttpHeaders();

		if (outputType.equals("excel")) {
			headers.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		}
		ResponseEntity<byte[]> responseVal = new ResponseEntity<byte[]>(outp, headers, HttpStatus.OK);

		return responseVal;

	}

	@GetMapping(value = "/certificateData/{companyId}/{utn}")
	public ResponseEntity<byte[]> generateCertificate(@PathVariable Long companyId, @PathVariable String utn,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		JRDataSource JRDataSource1 = new JRBeanCollectionDataSource(
				certificateDelegate.getCertificateData(companyId, utn));
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("url",
				servletContext.getContextPath() + "/src/main/java/com/api/central/audit/jrxml/CERTIFICATE_PDF.jasper");
		model.put("format", "pdf"); // Report Format
		model.put("jrMainDataSource", JRDataSource1);
		model.put("SUBREPORT_DIR", servletContext.getContextPath() + "/src/main/java/com/api/central/audit/jrxml/");

		// model.put ( "SUBREPORT_DIR" ,"/com/api/central/audit/jrxml/");
		JasperPrint jasperPrint = rs.fillReport(model);
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		return new ResponseEntity<byte[]>(rs.convertToByte(jasperPrint, "CERTIFICATE_PDF.pdf"), headers, HttpStatus.OK);

	}

	@SuppressWarnings("unused")
	@PostMapping(value = "/GenerateSearchReport/{screenId}/{category}/{format}/{companyId}")
	public ResponseEntity<byte[]> doDownload(@PathVariable("screenId") String screenName,
			@PathVariable("format") String outputType, @PathVariable("category") String reportCategory,
			@PathVariable("companyId") String companyId, @RequestBody SearchCriteria searchBean,
			HttpServletRequest request, HttpServletResponse response) throws IOException, JRException {
		// String authToken = request.getHeader("X-Auth-Token");
		String userName = AppUtil.getUserNameFromToken(request.getHeader("X-Auth-Token"));
		// System.out.println("My
		// Username"+TokenUtils.getUserNameFromToken(request.getHeader("X-Auth-Token")));

		String templateName = null;
		if (reportCategory != null) {
			templateName = screenName + "_" + reportCategory.toUpperCase();
		} else if (reportCategory == null) {
			templateName = screenName;
		}

		System.out.println("Template name::" + templateName);
		
		List<AuditDetailView> list = searchDelegate.getSearchResult(searchBean);

		for(AuditDetailView adv : list) {
			if(adv.getAuditTypeId() == AppConstant.SSP_TYPE_ID || adv.getAuditTypeId() == AppConstant.DMLC_TYPE_ID) {
				adv.setCertIssueDate(adv.getAuditDate());
			}
		}
		
		String dataList = "[";
		for (AuditDetailView auditDetailView : list) {
			dataList = dataList + auditDetailView.toJson() + ",";
		}

		if (dataList.lastIndexOf(',') == dataList.length() - 1) {
			dataList = dataList.substring(0, dataList.length() - 1) + "]";
		} else {
			dataList = dataList + "]";
		}

		byte[] outp = rs.generateReport(dataList, outputType, templateName, "CENTRALSEARCH_WITHCR_PDF", templateName,
				userName);
		HttpHeaders headers = new HttpHeaders();

		if (outputType.equals("pdf")) {
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
		} else if (outputType.equals("excel")) {
			headers.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		} else if (outputType.equals("html")) {
			headers.setContentType(MediaType.parseMediaType("application/html"));
		}
		ResponseEntity<byte[]> responseVal = new ResponseEntity<byte[]>(outp, headers, HttpStatus.OK);

		return responseVal;

	}

	@SuppressWarnings("unused")
	@PostMapping(value = "/GenerateStatReport/{screenId}/{category}/{format}/{companyId}")
	public ResponseEntity<byte[]> generateStatReport(@PathVariable("screenId") String screenName,
			@PathVariable("format") String outputType, @PathVariable("category") String reportCategory,
			@PathVariable("companyId") String companyId, @RequestBody List<VesselCriteria> searchBean,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("searchBean" + searchBean);

		/*
		 * VesselCriteria mb = new VesselCriteria(); List<VesselCriteria> mbs =
		 * new ArrayList<VesselCriteria>(); List<SubReportBean1> sbs = new
		 * ArrayList<SubReportBean1>(); SubReportBean1 sb = new
		 * SubReportBean1();
		 * 
		 * mbs.add(searchBean);
		 * 
		 * sb.setVesselCriteria(mbs);
		 * 
		 * sbs.add(sb);
		 */

		// ClassPathResource resource = new
		// ClassPathResource("/com/api/central/audit/jrxml/");
		JRDataSource JRDataSource1 = new JRBeanCollectionDataSource(searchBean);
		Map<String, Object> model = new HashMap<String, Object>();
		// model.put ( "url" ,
		// "/com/api/central/audit/jrxml/VESSELSEARCH_WITHCR_PDF.jasper");
		model.put("url", servletContext.getContextPath()
				+ "/src/main/java/com/api/central/audit/jrxml/VESSELSEARCH_WITHCR_PDF.jasper");
		model.put("format", "pdf"); // Report Format
		model.put("jrMainDataSource", JRDataSource1);
		model.put("SUBREPORT_DIR", servletContext.getContextPath() + "/src/main/java/com/api/central/audit/jrxml/");
		System.out.println("+++" + servletContext.getContextPath());
		// model.put ( "SUBREPORT_DIR" ,"/com/api/central/audit/jrxml/");
		JasperPrint jasperPrint = rs.fillReport(model);
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		return new ResponseEntity<byte[]>(rs.convertToByte(jasperPrint, "VESSELSEARCH_WITHCR_PDF.pdf"), headers,
				HttpStatus.OK);

		/*
		 * String templateName = null; if(reportCategory!=null) {
		 * templateName=screenName+"_"+reportCategory.toUpperCase(); } else
		 * if(reportCategory==null){ templateName=screenName;}
		 * 
		 * 
		 * byte[] outp = rs.generateReport(new Gson().toJson(searchBean) ,
		 * outputType, templateName,
		 * "VESSELSEARCH_WITHCR_PDF",templateName,userName); HttpHeaders headers
		 * = new HttpHeaders();
		 * 
		 * if (outputType.equals("pdf")){
		 * headers.setContentType(MediaType.parseMediaType("application/pdf"));
		 * 
		 * } else if (outputType.equals("excel")) {
		 * headers.setContentType(MediaType.parseMediaType(
		 * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
		 * ; } else if(outputType.equals("html")) {
		 * headers.setContentType(MediaType.parseMediaType("application/html"));
		 * } ResponseEntity<byte[]> responseVal = new
		 * ResponseEntity<byte[]>(outp, headers, HttpStatus.OK);
		 */
		// return responseVal;

	}

	@SuppressWarnings("unused")
	@PostMapping(value = "/GenerateSubReport/{screenId}/{category}/{format}/{companyId}")
	public ResponseEntity<byte[]> generateSubReport(@PathVariable("screenId") String screenName,
			@PathVariable("format") String outputType, @PathVariable("category") String reportCategory,
			@PathVariable("companyId") String companyId, @RequestBody List<SubReportBean1> searchBean,
			HttpServletRequest request, HttpServletResponse response) throws IOException, JRException {
		System.out.println("ism****" + new Gson().toJson(searchBean));
		// String authToken = request.getHeader("X-Auth-Token");
		String userName = AppUtil.getUserNameFromToken(request.getHeader("X-Auth-Token"));
		// System.out.println("My
		// Username"+TokenUtils.getUserNameFromToken(request.getHeader("X-Auth-Token")));

		String templateName = null;
		if (reportCategory != null) {
			templateName = screenName + "_" + reportCategory.toUpperCase();
		} else if (reportCategory == null) {
			templateName = screenName;
		}

		System.out.println("Template name::" + templateName);
		byte[] outp = rs.generateReport(new Gson().toJson(searchBean), outputType, templateName, "ISMS_WITHCR_PDF",
				templateName, userName);
		HttpHeaders headers = new HttpHeaders();

		if (outputType.equals("pdf")) {
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
		} else if (outputType.equals("excel")) {
			headers.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		} else if (outputType.equals("html")) {
			headers.setContentType(MediaType.parseMediaType("application/html"));
		}
		ResponseEntity<byte[]> responseVal = new ResponseEntity<byte[]>(outp, headers, HttpStatus.OK);

		return responseVal;

	}

	@SuppressWarnings("unused")
	@PostMapping(value = "/GenerateIspsReport/{screenId}/{category}/{format}/{companyId}")
	public ResponseEntity<byte[]> generateIspsReport(@PathVariable("screenId") String screenName,
			@PathVariable("format") String outputType, @PathVariable("category") String reportCategory,
			@PathVariable("companyId") String companyId, @RequestBody List<IspsBean> searchBean,
			HttpServletRequest request, HttpServletResponse response) throws IOException, JRException {
		System.out.println("ism****" + new Gson().toJson(searchBean));
		// String authToken = request.getHeader("X-Auth-Token");
		String userName = AppUtil.getUserNameFromToken(request.getHeader("X-Auth-Token"));
		// System.out.println("My
		// Username"+TokenUtils.getUserNameFromToken(request.getHeader("X-Auth-Token")));

		String templateName = null;
		if (reportCategory != null) {
			templateName = screenName + "_" + reportCategory.toUpperCase();
		} else if (reportCategory == null) {
			templateName = screenName;
		}

		System.out.println("Template name::" + templateName);
		byte[] outp = rs.generateReport(new Gson().toJson(searchBean), outputType, templateName, "ISMS_WITHCR_PDF",
				templateName, userName);
		HttpHeaders headers = new HttpHeaders();

		if (outputType.equals("pdf")) {
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
		} else if (outputType.equals("excel")) {
			headers.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		} else if (outputType.equals("html")) {
			headers.setContentType(MediaType.parseMediaType("application/html"));
		}
		ResponseEntity<byte[]> responseVal = new ResponseEntity<byte[]>(outp, headers, HttpStatus.OK);

		return responseVal;

	}

	@SuppressWarnings("unused")
	@PostMapping(value = "/GenerateMlcReport/{screenId}/{category}/{format}/{companyId}")
	public ResponseEntity<byte[]> generateMlcReport(@PathVariable("screenId") String screenName,
			@PathVariable("format") String outputType, @PathVariable("category") String reportCategory,
			@PathVariable("companyId") String companyId, @RequestBody List<MlcBean> searchBean,
			HttpServletRequest request, HttpServletResponse response) throws IOException, JRException {
		System.out.println("searchBean****" + new Gson().toJson(searchBean));
		// String authToken = request.getHeader("X-Auth-Token");
		String userName = AppUtil.getUserNameFromToken(request.getHeader("X-Auth-Token"));
		// System.out.println("My
		// Username"+TokenUtils.getUserNameFromToken(request.getHeader("X-Auth-Token")));

		String templateName = null;
		if (reportCategory != null) {
			templateName = screenName + "_" + reportCategory.toUpperCase();
		} else if (reportCategory == null) {
			templateName = screenName;
		}

		System.out.println("Template name::" + templateName);
		byte[] outp = rs.generateReport(new Gson().toJson(searchBean), outputType, templateName, "MLC_WITHCR_PDF",
				templateName, userName);
		HttpHeaders headers = new HttpHeaders();

		if (outputType.equals("pdf")) {
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
		} else if (outputType.equals("excel")) {
			headers.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		} else if (outputType.equals("html")) {
			headers.setContentType(MediaType.parseMediaType("application/html"));
		}
		ResponseEntity<byte[]> responseVal = new ResponseEntity<byte[]>(outp, headers, HttpStatus.OK);

		return responseVal;

	}

	@SuppressWarnings("unused")
	@PostMapping(value = "/GenerateCarSearchReport/{screenId}/{category}/{format}/{companyId}")
	public ResponseEntity<byte[]> GenerateCarSearchReport(@PathVariable("screenId") String screenName,
			@PathVariable("format") String outputType, @PathVariable("category") String reportCategory,
			@PathVariable("companyId") String companyId, @RequestBody CarSearchCriteria searchBean,
			HttpServletRequest request, HttpServletResponse response) throws IOException, JRException {
		// String authToken = request.getHeader("X-Auth-Token");
		String userName = AppUtil.getUserNameFromToken(request.getHeader("X-Auth-Token"));
		// System.out.println("My
		// Username"+TokenUtils.getUserNameFromToken(request.getHeader("X-Auth-Token")));

		String templateName = null;
		if (reportCategory != null) {
			templateName = screenName + "_" + reportCategory.toUpperCase();
		} else if (reportCategory == null) {
			templateName = screenName;
		}

		System.out.println("Template name::" + templateName);
		byte[] outp = rs.generateReport(
				new Gson().toJson(carMaintenanceDelegate.getHistorySearchResultReport(searchBean)), outputType,
				templateName, "CARSEARCH_WITHCR", templateName, userName);
		HttpHeaders headers = new HttpHeaders();

		if (outputType.equals("pdf")) {
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
		} else if (outputType.equals("excel")) {
			headers.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		} else if (outputType.equals("html")) {
			headers.setContentType(MediaType.parseMediaType("application/html"));
		}
		ResponseEntity<byte[]> responseVal = new ResponseEntity<byte[]>(outp, headers, HttpStatus.OK);

		return responseVal;

	}

}
