package com.api.central.util;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletContext;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.api.central.audit.delegate.AuditDelegateImpl;
import com.api.central.audit.entity.AuditDetail;
import com.api.central.master.entity.VesselBean;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lingala.zip4j.ZipFile;
//import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
/*import net.lingala.zip4j.util.Zip4jConstants;*/

@Component
public final class AppUtil {

	static HashSet<String> filesListInDir = new HashSet<String>();
	@Autowired
	ServletContext servletContext;

	static Logger log = LoggerFactory.getLogger(AppUtil.class);

	public Date convertStringToDate(String date) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return java.sql.Date.valueOf(localDate);
	}

	/**
	 * This method zips the directory
	 * 
	 * @param dir
	 * @param zipDirName
	 * @return
	 * @throws IOException
	 */
	public boolean zipDirectory(File dir, String zipDirName) throws IOException {

		/*
		 * try { //synchronized (dir) { filesListInDir.clear();
		 * populateFilesList(dir); //now zip files one by one //create
		 * ZipOutputStream to write to the zip file FileOutputStream fos = new
		 * FileOutputStream(zipDirName); ZipOutputStream zos = new
		 * ZipOutputStream(fos); for(String filePath : filesListInDir){ //for
		 * ZipEntry we need to keep only relative file path, so we used
		 * substring on absolute path ZipEntry ze = new
		 * ZipEntry(filePath.substring(dir.getAbsolutePath().length()+1,
		 * filePath.length())); zos.putNextEntry(ze); //read the file and write
		 * to ZipOutputStream FileInputStream fis = new
		 * FileInputStream(filePath); byte[] buffer = new byte[1024]; int len;
		 * while ((len = fis.read(buffer)) > 0) { zos.write(buffer, 0, len); }
		 * zos.closeEntry(); fis.close(); } zos.close(); fos.close(); // }
		 * return true; } catch (IOException e) { e.printStackTrace(); }
		 */
		File file = new File(zipDirName);
		if (file.exists()) {
			file.delete();
		}

		Path zipPath = Files.createFile(Paths.get(zipDirName));
		try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
			Path sourcePath = Paths.get(dir.toURI());
			Files.walk(sourcePath).filter(path -> !Files.isDirectory(path)).forEach(path -> {
				ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
				try {
					zos.putNextEntry(zipEntry);
					Files.copy(path, zos);
					zos.closeEntry();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean mobileZipDirectory(File dir, String zipDirName) {
		/*File inputFile = dir;
		File compressedFile = new File(zipDirName);*/

		try {
			/*ZipFile zipFile = new ZipFile(compressedFile);
			ZipParameters parameters = new ZipParameters();

			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			zipFile.addFolder(inputFile, parameters);
			// FileUtils.deleteDirectory(transformation.getTransformedApplicationLocation());*/	
		
			
			File directoryToBeZipped = dir;
			  File zipFile = new File(zipDirName);
			  ZipFile zip = new ZipFile(zipFile);

			  // Adding the list of files and directories to be zipped to a list
			  ArrayList<File> fileList = new ArrayList<File>();
			  Arrays.stream(directoryToBeZipped.listFiles()).forEach((File file) -> {fileList.add(file);});

			  ZipParameters parameters = new ZipParameters();
			 /* parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			  parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);*/
			  parameters.setEncryptFiles(false);

			  fileList.stream().forEach((File f) -> {
					try
					{
						if(f.isDirectory())
						{
							zip.addFolder(f, parameters);
						}
						else
						{
							zip.addFile(f, parameters);
						}
					}
					catch(ZipException zipExceptio)
					{
						System.out.println(zipExceptio);
					}
				});
			
			
			return true;
		} catch (Exception e) {
			return false;

		}
	}

	/**
	 * This method populates all the files in a directory to a List
	 * 
	 * @param dir
	 * @throws IOException
	 */
	private void populateFilesList(File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					filesListInDir.add(file.getAbsolutePath());
				} else {
					populateFilesList(file);
				}
			}
		}
	}

	public boolean extractAllFiles(File file, String string) {

		try {
			// Initiate ZipFile object with the path/name of the zip file.
			ZipFile zipFile = new ZipFile(file);
			// Extracts all files to the path specified
			zipFile.extractAll(string);
			return true;

		} catch (ZipException e) {
			e.printStackTrace();

			return false;

		}
	}

	public HttpHeaders setHeaderType(String path, String name) {

		System.out.println(path);
		String mimeType = servletContext.getMimeType(path);
		HttpHeaders headers = new HttpHeaders();
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		headers.setContentType(MediaType.parseMediaType(mimeType));
		headers.setContentDispositionFormData(name, name);
		return headers;
	}

	public HttpHeaders setHeaderStreamType(String path, String name) {
		System.out.println(path);
		String mimeType = servletContext.getMimeType(path);
		HttpHeaders headers = new HttpHeaders();
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.setContentType(MediaType.parseMediaType(mimeType));
		// headers.setContentLength(contentLength);
		return headers;
	}

	public byte[] getFileByte(String path) {
		byte[] fileByte = null;
		if (path != null) {
			Path d = Paths.get(path);
			try {
				fileByte = Files.readAllBytes(d);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileByte;

	}

	public ByteArrayResource getFileByteStream(String path) {
		ByteArrayResource fileByte = null;
		if (path != null) {
			Path filePath = Paths.get(path);
			try {
				fileByte = new ByteArrayResource(Files.readAllBytes(filePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileByte;

	}

	/**
	 * Delete a file or a directory and its children.
	 * 
	 * @param auditSeqNo
	 * @param auditSeqNo
	 * 
	 * @param file
	 *            The directory to delete.
	 * @throws IOException
	 *             Exception when problem occurs during deleting the directory.
	 */
	public void deleteFiles(String directoryName, String auditSeqNo) throws IOException {
		if (directoryName != null) {
			Path directory = Paths.get(directoryName);
			// +AppConstant.SEPARATOR+AppConstant.PREVIOUS_FINDING);
			if (Files.exists(directory)) {
				Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Files.deleteIfExists(file);

						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						Files.deleteIfExists(dir);
						return FileVisitResult.CONTINUE;
					}
				});
				Files.deleteIfExists(Paths.get(directoryName + AppConstant.SEPARATOR + auditSeqNo + AppConstant.JSON));
			}
		}
	}

	public void deleteStampFile(String directoryName) throws IOException {
		if (directoryName != null) {
			Path directory = Paths.get(directoryName);
			if (Files.exists(directory)) {
				Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
							Files.deleteIfExists(file);
						return FileVisitResult.CONTINUE;
					}
				});
			}
		}
	}

	public boolean unZipArchive(File zipFileSrc, String path) {

		try (java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(zipFileSrc)) {
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				// Do we need to create a directory ?
				/*
				 * System.out.printf(
				 * "name: %-20s | size: %6d | compressed size: %6d\n",
				 * zipEntry.getName(), zipEntry.getSize(),
				 * zipEntry.getCompressedSize());
				 */
				File file = new File(path + AppConstant.SEPARATOR + zipEntry.getName());
				if (zipEntry.getName().endsWith("/")) {
					file.mkdirs();
					continue;
				}
				File parent = file.getParentFile();
				if (parent != null) {
					parent.mkdirs();
				}
				// Extract the file
				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				is.close();
				fos.close();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();

			return false;
		}
	}

	public File convertToFile(MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		try {
			convFile.createNewFile();

			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convFile;
	}

	public static String getUserNameFromToken(String authToken) {
		if (null == authToken) {
			return null;
		}
		String[] parts = authToken.split(":");
		return parts[0];
	}

	public String setAuditType(Integer audiTypeId) {
		switch (audiTypeId) {
		case AppConstant.ISM_TYPE_ID:
			return AppConstant.ISM_SRC;
		case AppConstant.ISPS_TYPE_Id:
			return AppConstant.ISPS_SRC;
		case AppConstant.MLC_TYPE_ID:
			return AppConstant.MLC_SRC;
		case AppConstant.DMLC_TYPE_ID:
			return AppConstant.DMLC_SRC;
		}
		return null;
	}

	public String encodeFileToBase64Binary(String fileName) throws IOException {

		File file = new File(fileName);
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}

	@SuppressWarnings("resource")
	public static byte[] loadFile(File file) throws IOException {
		byte[] bytes = null;
		if (file.exists()) {
			InputStream is = new FileInputStream(file);

			long length = file.length();
			if (length > Integer.MAX_VALUE) {
			}

			bytes = new byte[(int) length];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			if (offset < bytes.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
			is.close();
		}
		return bytes;

	}

	public static String filePath(AuditDetail audit) {

		String fileName;
		if (audit.getLeadSign() != null) {
			log.info("lead sign");
		}
		log.info("audit value =" + audit.getLeadSign());
		if (audit.getAuditTypeId() != 1006) {
			if (audit.getReviewerSign() != null || audit.getLockStatus() == AppConstant.OPEN_FOR_CAR_STATUS) {
				fileName = ( audit.getVesselNameAud() !=null ? audit.getVesselNameAud() :  audit.getVesselName() )  + "_" + audit.getVesselImoNo() + "_Final" + audit.getAudTypeDesc()
						+ "_" + new SimpleDateFormat("dd-MMMM-yyyy").format(new java.util.Date()) + ".pdf".toString();
			} else {
				fileName = (audit.getVesselNameAud() !=null ? audit.getVesselNameAud() : audit.getVesselName() )+ "_" + audit.getVesselImoNo() + "_Prelim" + audit.getAudTypeDesc()
						+ "_" + new SimpleDateFormat("dd-MMMM-yyyy").format(new java.util.Date()) + ".pdf".toString();
			}
		} else {
			if (audit.getLeadSign() == null) {
				fileName = (audit.getVesselNameAud() !=null ? audit.getVesselNameAud() : audit.getVesselName())+ "_" + audit.getVesselImoNo() + "_Prelim" + audit.getAudTypeDesc()
						+ "_" + new SimpleDateFormat("dd-MMMM-yyyy").format(new java.util.Date()) + ".pdf".toString();

			} else {
				fileName =( audit.getVesselNameAud() !=null ? audit.getVesselNameAud() : audit.getVesselName()) + "_" + audit.getVesselImoNo() + "_Final" + audit.getAudTypeDesc()
						+ "_" + new SimpleDateFormat("dd-MMMM-yyyy").format(new java.util.Date()) + ".pdf".toString();
			}
		}

		return fileName;
	}

	public static boolean checkNullValue(VesselBean vb) {

		Field[] valueObjFields = vb.getClass().getDeclaredFields();

		for (int i = 0; i < valueObjFields.length; i++) {
			valueObjFields[i].setAccessible(true);
			try {
				Object newObj = valueObjFields[i].get(vb);
				if (newObj == null)
					return false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public HttpHeaders setJsonHeaderType(String path, String name) {

		System.out.println(path);
		String mimeType = servletContext.getMimeType(path);
		HttpHeaders headers = new HttpHeaders();
		if (mimeType == null) {
			mimeType = "application/json";
		}
		headers.setContentType(MediaType.parseMediaType(mimeType));
		headers.setContentDispositionFormData(name, name);
		return headers;
	}

	public byte[] convertJson(int versionId) {

		String Jsonpath = servletContext.getContextPath() + AppConstant.SEPARATOR + "version.json";
		byte[] data = null;
		ObjectMapper mapperObj = new ObjectMapper();
		Map<String, Integer> map = new HashMap<>();
		map.put("versionId", versionId);
		Path path = Paths.get(Jsonpath);
		try {
			mapperObj.writeValue(new File(Jsonpath), map);
			data = Files.readAllBytes(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public static String getCertificateNoFormat(Integer auditType, String certificateNo, Integer auditSubType) {

		String certificatenewPart1 = "";
		String certificatenewPart2 = "";
		DateFormat df = new SimpleDateFormat("yy");

		if (auditType == AppConstant.ISM_TYPE_ID && auditSubType == AppConstant.INTERIM_SUB_TYPE_ID) {
			certificatenewPart1 = "SM";
			certificatenewPart2 = "297E";
		} else if (auditType == AppConstant.ISM_TYPE_ID && auditSubType != AppConstant.INTERIM_SUB_TYPE_ID) {
			certificatenewPart1 = "SM";
			certificatenewPart2 = "297F";
		} else if (auditType == AppConstant.ISPS_TYPE_Id && auditSubType == AppConstant.INTERIM_SUB_TYPE_ID) {
			certificatenewPart1 = "SS";
			certificatenewPart2 = "296G";
		} else if (auditType == AppConstant.ISPS_TYPE_Id && auditSubType != AppConstant.INTERIM_SUB_TYPE_ID) {
			certificatenewPart1 = "SS";
			certificatenewPart2 = "296H";
		} else if (auditType == AppConstant.MLC_TYPE_ID && auditSubType == AppConstant.INTERIM_SUB_TYPE_ID) {
			certificatenewPart1 = "ML";
			certificatenewPart2 = "400I";
		} else if (auditType == AppConstant.MLC_TYPE_ID && auditSubType != AppConstant.INTERIM_SUB_TYPE_ID) {
			certificatenewPart1 = "ML";
			certificatenewPart2 = "400J";
		} else if (auditType == AppConstant.IHM_TYPE_ID) {
			certificatenewPart1 = "IHM";
			certificatenewPart2 = "400J";
		}

		certificateNo = certificateNo + "-" + certificatenewPart1 + "-" + certificatenewPart2 + "-"
				+ df.format(Calendar.getInstance().getTime());

		return certificateNo;

	}

	public static String getCertificateNoFormatForIhm(Integer auditType, String certificateNo, Integer auditSubType, String count) {

		String certificatenewPart1 = "";
		String certificatenewPart2 = "";
		DateFormat df = new SimpleDateFormat("yy");

	
		 if (count.equals("hk") ) {
			certificatenewPart1 = "IHM";
			certificatenewPart2 = "298C";
		}
		 else if (count.equals("eu") ) {
			certificatenewPart1 = "IHM";
			certificatenewPart2 = "298D";
			}
		 else if(count.equals("exe") ) {
			 certificatenewPart1 = "IHM";
			 certificatenewPart2 = "298F";
		 }
		certificateNo = certificateNo + "-" + certificatenewPart1 + "-" + certificatenewPart2 + "-"
				+ df.format(Calendar.getInstance().getTime());

		return certificateNo;

	}

	// generate Random String of 5 chars

	public String getRandomString() {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(5);
		for (int i = 0; i < 5; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}
}
