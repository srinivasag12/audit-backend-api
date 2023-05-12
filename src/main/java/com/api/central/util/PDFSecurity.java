package com.api.central.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;


@RestController
@RequestMapping("api/audit/pdf")
public class PDFSecurity {
	private static final Logger log = LoggerFactory.getLogger(PDFSecurity.class);
	
	@GetMapping(value = "/pathreturn")
	public ResponseEntity<Object> PathLocation() throws Exception {
		log.info(System.getProperty("user.dir"));
		String paths = System.getProperty("user.dir");
		return new ResponseEntity<Object>(paths, HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/pdfSecurity/{pathVariable}")
	public ResponseEntity<Object> ConvertPDF(@PathVariable String pathVariable) throws Exception {
		
		log.info("pathVariable::"+pathVariable);
		String location = System.getProperty("user.dir").replaceAll("\\\\", "\\\\\\\\");
		/*String bytesEncoded = pathVariable;
		byte[] valueDecoded = Base64.getDecoder().decode(bytesEncoded);
		String location2 = new String(valueDecoded);
		String finalLocation = location+location2;
		log.info(finalLocation);*/
		log.info("Working Directory = "+System.getProperty("user.dir"));
		String tempLocation = "\\tempPdfFile\\";
		String fianlLocation = location+tempLocation;
		String src = fianlLocation+pathVariable+".pdf";
		String dest = fianlLocation+pathVariable+"1.pdf";
		
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		stamper.setEncryption(true, null, null, PdfWriter.ENCRYPTION_AES_128 | PdfWriter.ALLOW_PRINTING);
		reader.close();
		stamper.close();
		
		
		byte[] fileContent = FileUtils.readFileToByteArray(new File(dest));
		String encodedString = Base64.getEncoder().encodeToString(fileContent);
		
		/*JSONObject obj = new JSONObject();
		
		obj.put("dat", encodedString);
*/
		/*BASE64Decoder decoder = new BASE64Decoder();
		byte[] decodedBytes = decoder.decodeBuffer(encodedString);
		
		File file = new File("d:/newfile.pdf");
		FileOutputStream fop = new FileOutputStream(file);

		fop.write(decodedBytes);
		fop.flush();
		fop.close();
		
		log.info("encoded String"+encodedString);
*/		
		return new ResponseEntity<Object>(encodedString, HttpStatus.OK);

	}

}
