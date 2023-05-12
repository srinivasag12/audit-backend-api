package com.api.central.audit.delegate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@SuppressWarnings("deprecation")
@Service
public class ReportServiceImpl extends AbstractUrlBasedView implements  ReportService{
	
	@Autowired
	ServletContext servletContext;
	
	JRDataSource jrDataSource = null;
	/**
	 * Constant that defines "Content-Disposition" header.
	 */
	protected static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
	/**
	 * The default Content-Disposition header. Used to make IE play nice.
	 */
	protected static final String CONTENT_DISPOSITION_INLINE = "inline";


	/**
	 * A String key used to lookup the <code>JRDataSource</code> in the model.
	 */
	private String reportDataKey;

	/**
	 * Stores the paths to any sub-report files used by this top-level report,
	 * along with the keys they are mapped to in the top-level report file.
	 */
	private Properties subReportUrls;

	/**
	 * Stores the names of any data source objects that need to be converted to
	 * <code>JRDataSource</code> instances and included in the report parameters
	 * to be passed on to a sub-report.
	 */
	private String[] subReportDataKeys;

	/**
	 * Stores the headers to written with each response
	 */
	private Properties headers;

	/**
	 * Stores the exporter parameters passed in by the user as passed in by the user. May be keyed as
	 * <code>String</code>s with the fully qualified name of the exporter parameter field.
	 */
	private Map<?, ?> exporterParameters = new HashMap<Object, Object>();

	/**
	 * Stores the converted exporter parameters - keyed by <code>JRExporterParameter</code>.
	 */
	private Map<JRExporterParameter, Object> convertedExporterParameters;

	/**
	 * Stores the <code>DataSource</code>, if any, used as the report data source.
	 */
	private DataSource jdbcDataSource;

	/**
	 * The <code>JasperReport</code> that is used to render the view.
	 */
	private JasperReport report;

	/**
	 * Holds mappings between sub-report keys and <code>JasperReport</code> objects.
	 */
	private Map<String, JasperReport> subReports;
	

	
	public byte[] generateReport(String dataList, String outputType, String sourcefilepath, String destinationPath,
			String directoryName,String uname) throws IOException, JRException {
		System.out.println(dataList);
		
		if(dataList.length()==2){
			return null;
		}
		
		String result, file = null;
		InputStream input = null;
		OutputStream output = null;
		byte[] byt = null;
		byte[] buffer = new byte[8192];
	
		ClassPathResource resource1 = new ClassPathResource("");
		System.out.println(resource1.getFile().toString());
		System.out.println("***"+sourcefilepath+""+outputType.toUpperCase());
		
			ClassPathResource resource = new ClassPathResource(
					"/com/api/central/audit/jrxml/" + sourcefilepath+"_"+outputType.toUpperCase() + ".jrxml");
			System.out.println("*****path***"+resource);
			//String reportTemplateUrl = resource.getFile().toString();
			String reportTemplateUrl = servletContext.getContextPath()+"/src/main/java/com/api/central/audit/jrxml/"+ sourcefilepath+"_"+outputType.toUpperCase() + ".jrxml";
			                                         // C:\CentralAuditApi\src\main\java\com\api\central\audit\jrxml
			
			System.out.println(reportTemplateUrl);
			
			input = new FileInputStream(new File(reportTemplateUrl));
			JasperDesign jasperDesign = JRXmlLoader.load(input);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			InputStream stream1 = IOUtils.toInputStream(dataList, "UTF-8");
			
			JsonDataSource ds = new JsonDataSource(stream1);
			System.out.println(resource.getPath());
			String reportsDirName="com/api/central/audit/jrxml/";
			
			Map<String, Object> map = new HashMap<String, Object>();
			//map.put ( "url" ,  "/com/api/central/audit/jrxml/" + sourcefilepath+"_"+outputType.toUpperCase() + ".jrxml"); 
			//map.put("USER", uname);
		
		        // jrDataSource =new  JRBeanCollectionDataSource (mbs);
			map.put("SUBREPORT_DIR", reportsDirName);
			map.put ( "jrMainDataSource" , ds);
			
	      
	       
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, ds);
			
		/*	Map<String, Object> model = new HashMap<String, Object>();
	          model.put ( "url" ,  "/com/api/central/audit/jrxml/" + sourcefilepath+"_"+outputType.toUpperCase() + ".jrxml");  
	          model.put ( "format" ,  "pdf" );  // Report Format  
	          model.put ( "jrMainDataSource" , ds);
	          model.put ( "SUBREPORT_DIR" ,  resource.getPath());
			
	          JasperPrint jasperPrint = JasperFillManager.fillReport(model);   
*/
			File fileTemplate = new File("template");
			if (!fileTemplate.exists()) {
				fileTemplate.mkdir();
			}
				
			File filePath = new File("template/" + directoryName);
			if (!filePath.exists()) {
				filePath.mkdir();
			}
			
			if (outputType.equals("pdf")) {
				String serverPath = filePath.getAbsoluteFile() + "/" + destinationPath + ".pdf";
				System.out.println("OUTPUT PDF PATH:" + serverPath);
				FileSystemResource resource2 = new FileSystemResource(serverPath);
				file = resource2.getFile().toString();
				System.out.println("OUTPUT FILE PATH:" + file);
				output = new FileOutputStream(new File(file));
				
				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
				
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				  configuration.setEncrypted(true);
				  configuration.set128BitKey(true);
				  configuration.setPermissions(PdfWriter.ALLOW_PRINTING);
				  exporter.setConfiguration(configuration);
				 
				  exporter.exportReport();
			}

			else if (outputType.equals("excel")) {
				String serverPath = filePath.getAbsoluteFile() + "/" + destinationPath + ".xlsx";
				System.out.println("OUTPUT EXCEL PATH:" + serverPath);
				FileSystemResource resource2 = new FileSystemResource(serverPath);
				file = resource2.getFile().toString();
				System.out.println("OUTPUT FILE PATH:" + file);
				output = new FileOutputStream(new File(file));

				/*JRXlsxExporter xlsxExporter = new JRXlsxExporter();
				xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
				xlsxExporter.exportReport();*/
				long start = System.currentTimeMillis();
				//File sourceFile = new File("build/reports/NoPageBreakReport.jrprint");

				//JasperPrint jasperPrint = (JasperPrint)JRLoader.loadObject(sourceFile);

				//File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xlsx");
				
				JRXlsxExporter exporter = new JRXlsxExporter();
				
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
				SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
				configuration.setOnePagePerSheet(false);
				configuration.setRemoveEmptySpaceBetweenRows(true);
				exporter.setConfiguration(configuration);
				
				exporter.exportReport();

				//System.err.println("XLSX creation time : " + (System.currentTimeMillis() - start));
			}

			else if (outputType.equals("html")) {
				String serverPath = filePath.getAbsoluteFile() + "/" + destinationPath + ".html";
				System.out.println("OUTPUT PDF PATH:" + serverPath);
				FileSystemResource resource2 = new FileSystemResource(serverPath);
				file = resource2.getFile().toString();
				System.out.println("OUTPUT FILE PATH:" + file);
				output = new FileOutputStream(new File(file));

				//JasperExportManager.exportReportToHtmlFile(jasperPrint, file);
				long start = System.currentTimeMillis();
				//File sourceFile = new File("build/reports/NoPageBreakReport.jrprint");

				//JasperPrint jasperPrint1 = (JasperPrint)JRLoader.loadObject(sourceFile);

			//	File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".html");
				
				HtmlExporter exporter = new HtmlExporter();
				
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleHtmlExporterOutput(output));
				
				SimpleHtmlExporterConfiguration exporterConfig = new SimpleHtmlExporterConfiguration();
				exporterConfig.setBetweenPagesHtml("");
				exporter.setConfiguration(exporterConfig);

				SimpleHtmlReportConfiguration reportConfig = new SimpleHtmlReportConfiguration();
				reportConfig.setRemoveEmptySpaceBetweenRows(true);
				exporter.setConfiguration(reportConfig);
				
				exporter.exportReport();

				//System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
				
				
			}
			else if (outputType.equals("print")) {
				long start = System.currentTimeMillis();
				JasperPrintManager.printReport(jasperPrint, true);
				System.err.println("Printing time : " + (System.currentTimeMillis() - start));
			}
			/*long start = System.currentTimeMillis();
			JasperPrintManager.printReport(jasperPrint, true);
			System.err.println("Printing time : " + (System.currentTimeMillis() - start));
			@SuppressWarnings("resource")*/
			FileInputStream stream = new FileInputStream(file);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			int bytesRead;
			while ((bytesRead = stream.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}
			byt = baos.toByteArray();

			result = "success";
			System.out.println( "DataList:::"+dataList);		
		return byt;
	}
	
	public void setReportDataKey(String reportDataKey) {
		this.reportDataKey = reportDataKey;
	}

	/**
	 * Specify resource paths which must be loaded as instances of
	 * <code>JasperReport</code> and passed to the JasperReports engine for
	 * rendering as sub-reports, under the same keys as in this mapping.
	 * @param subReports mapping between model keys and resource paths
	 * (Spring resource locations)
	 * @see #setUrl
	 * @see org.springframework.context.ApplicationContext#getResource
	 */
	public void setSubReportUrls(Properties subReports) {
		this.subReportUrls = subReports;
	}

	/**
	 * Set the list of names corresponding to the model parameters that will contain
	 * data source objects for use in sub-reports. Spring will convert these objects
	 * to instances of <code>JRDataSource</code> where applicable and will then
	 * include the resulting <code>JRDataSource</code> in the parameters passed into
	 * the JasperReports engine.
	 * <p>The name specified in the list should correspond to an attribute in the
	 * model Map, and to a sub-report data source parameter in your report file.
	 * If you pass in <code>JRDataSource</code> objects as model attributes,
	 * specifing this list of keys is not required.
	 * <p>If you specify a list of sub-report data keys, it is required to also
	 * specify a <code>reportDataKey</code> for the main report, to avoid confusion
	 * between the data source objects for the various reports involved.
	 * @param subReportDataKeys list of names for sub-report data source objects
	 * @see #setReportDataKey
	 * @see #convertReportData
	 * @see net.sf.jasperreports.engine.JRDataSource
	 * @see net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
	 * @see net.sf.jasperreports.engine.data.JRBeanArrayDataSource
	 */
	public void setSubReportDataKeys(String[] subReportDataKeys) {
		this.subReportDataKeys = subReportDataKeys;
	}

	/**
	 * Specify the set of headers that are included in each of response.
	 * @param headers the headers to write to each response.
	 */
	public void setHeaders(Properties headers) {
		this.headers = headers;
	}

	/**
	 * Set the exporter parameters that should be used when rendering a view.
	 * @param parameters <code>Map</code> with the fully qualified field name
	 * of the <code>JRExporterParameter</code> instance as key
	 * (e.g. "net.sf.jasperreports.engine.export.JRHtmlExporterParameter.IMAGES_URI")
	 * and the value you wish to assign to the parameter as value
	 */
	public void setExporterParameters(Map<?, ?> parameters) {
		this.exporterParameters = parameters;
	}

	/**
	 * Return the exporter parameters that this view uses, if any.
	 */
	public Map<?, ?> getExporterParameters() {
		return this.exporterParameters;
	}

	/**
	 * Allows subclasses to populate the converted exporter parameters.
	 */
	protected void setConvertedExporterParameters(Map<JRExporterParameter, Object> convertedExporterParameters) {
		this.convertedExporterParameters = convertedExporterParameters;
	}

	/**
	 * Allows subclasses to retrieve the converted exporter parameters.
	 */
	protected Map<JRExporterParameter, Object> getConvertedExporterParameters() {
		return this.convertedExporterParameters;
	}

	/**
	 * Specify the <code>javax.sql.DataSource</code> to use for reports with
	 * embedded SQL statements.
	 */
	public void setJdbcDataSource(DataSource jdbcDataSource) {
		this.jdbcDataSource = jdbcDataSource;
	}

	/**
	 * Return the <code>javax.sql.DataSource</code> that this view uses, if any.
	 */
	protected DataSource getJdbcDataSource() {
		return this.jdbcDataSource;
	}


	/**
	 * JasperReports views do not strictly required a 'url' value.
	 * Alternatively, the {@link #getReport()} template method may be overridden.
	 */
	@Override
	protected boolean isUrlRequired() {
		return false;
	}

	/**
	 * Checks to see that a valid report file URL is supplied in the
	 * configuration. Compiles the report file is necessary.
	 * <p>Subclasses can add custom initialization logic by overriding
	 * the {@link #onInit} method.
	 */
	@Override
	protected final void initApplicationContext() throws ApplicationContextException {
		this.report = loadReport();

		// Load sub reports if required, and check data source parameters.
		if (this.subReportUrls != null) {
			if (this.subReportDataKeys != null && this.subReportDataKeys.length > 0 && this.reportDataKey == null) {
				throw new ApplicationContextException(
						"'reportDataKey' for main report is required when specifying a value for 'subReportDataKeys'");
			}
			this.subReports = new HashMap<String, JasperReport>(this.subReportUrls.size());
			for (Enumeration urls = this.subReportUrls.propertyNames(); urls.hasMoreElements();) {
				String key = (String) urls.nextElement();
				String path = this.subReportUrls.getProperty(key);
				Resource resource = getApplicationContext().getResource(path);
				this.subReports.put(key, loadReport(resource));
			}
		}

		// Convert user-supplied exporterParameters.
		convertExporterParameters();

		if (this.headers == null) {
			this.headers = new Properties();
		}
		if (!this.headers.containsKey(HEADER_CONTENT_DISPOSITION)) {
			this.headers.setProperty(HEADER_CONTENT_DISPOSITION, CONTENT_DISPOSITION_INLINE);
		}

		onInit();
	}

	/**
	 * Subclasses can override this to add some custom initialization logic. Called
	 * by {@link #initApplicationContext()} as soon as all standard initialization logic
	 * has finished executing.
	 * @see #initApplicationContext()
	 */
	protected void onInit() {
	}

	/**
	 * Converts the exporter parameters passed in by the user which may be keyed
	 * by <code>String</code>s corresponding to the fully qualified name of the
	 * <code>JRExporterParameter</code> into parameters which are keyed by
	 * <code>JRExporterParameter</code>.
	 * @see #getExporterParameter(Object)
	 */
	protected final void convertExporterParameters() {
		if (!CollectionUtils.isEmpty(this.exporterParameters)) {
			this.convertedExporterParameters = new HashMap<JRExporterParameter, Object>(this.exporterParameters.size());
			for (Map.Entry<?, ?> entry : this.exporterParameters.entrySet()) {
				JRExporterParameter exporterParameter = getExporterParameter(entry.getKey());
				this.convertedExporterParameters.put(
						exporterParameter, convertParameterValue(exporterParameter, entry.getValue()));
			}
		}
	}

	/**
	 * Convert the supplied parameter value into the actual type required by the
	 * corresponding {@link JRExporterParameter}.
	 * <p>The default implementation simply converts the String values "true" and
	 * "false" into corresponding <code>Boolean</code> objects, and tries to convert
	 * String values that start with a digit into <code>Integer</code> objects
	 * (simply keeping them as String if number conversion fails).
	 * @param parameter the parameter key
	 * @param value the parameter value
	 * @return the converted parameter value
	 */
	protected Object convertParameterValue(JRExporterParameter parameter, Object value) {
		if (value instanceof String) {
			String str = (String) value;
			if ("true".equals(str)) {
				return Boolean.TRUE;
			}
			else if ("false".equals(str)) {
				return Boolean.FALSE;
			}
			else if (str.length() > 0 && Character.isDigit(str.charAt(0))) {
				// Looks like a number... let's try.
				try {
					return new Integer(str);
				}
				catch (NumberFormatException ex) {
					// OK, then let's keep it as a String value.
					return str;
				}
			}
		}
		return value;
	}

	/**
	 * Return a <code>JRExporterParameter</code> for the given parameter object,
	 * converting it from a String if necessary.
	 * @param parameter the parameter object, either a String or a JRExporterParameter
	 * @return a JRExporterParameter for the given parameter object
	 * @see #convertToExporterParameter(String)
	 */
	protected JRExporterParameter getExporterParameter(Object parameter) {
		if (parameter instanceof JRExporterParameter) {
			return (JRExporterParameter) parameter;
		}
		if (parameter instanceof String) {
			return convertToExporterParameter((String) parameter);
		}
		throw new IllegalArgumentException(
				"Parameter [" + parameter + "] is invalid type. Should be either String or JRExporterParameter.");
	}

	/**
	 * Convert the given fully qualified field name to a corresponding
	 * JRExporterParameter instance.
	 * @param fqFieldName the fully qualified field name, consisting
	 * of the class name followed by a dot followed by the field name
	 * (e.g. "net.sf.jasperreports.engine.export.JRHtmlExporterParameter.IMAGES_URI")
	 * @return the corresponding JRExporterParameter instance
	 */
	protected JRExporterParameter convertToExporterParameter(String fqFieldName) {
		int index = fqFieldName.lastIndexOf('.');
		if (index == -1 || index == fqFieldName.length()) {
			throw new IllegalArgumentException(
					"Parameter name [" + fqFieldName + "] is not a valid static field. " +
					"The parameter name must map to a static field such as " +
					"[net.sf.jasperreports.engine.export.JRHtmlExporterParameter.IMAGES_URI]");
		}
		String className = fqFieldName.substring(0, index);
		String fieldName = fqFieldName.substring(index + 1);

		try {
			Class cls = ClassUtils.forName(className, getApplicationContext().getClassLoader());
			Field field = cls.getField(fieldName);

			if (JRExporterParameter.class.isAssignableFrom(field.getType())) {
				try {
					return (JRExporterParameter) field.get(null);
				}
				catch (IllegalAccessException ex) {
					throw new IllegalArgumentException(
							"Unable to access field [" + fieldName + "] of class [" + className + "]. " +
							"Check that it is static and accessible.");
				}
			}
			else {
				throw new IllegalArgumentException("Field [" + fieldName + "] on class [" + className +
						"] is not assignable from JRExporterParameter - check the type of this field.");
			}
		}
		catch (ClassNotFoundException ex) {
			throw new IllegalArgumentException(
					"Class [" + className + "] in key [" + fqFieldName + "] could not be found.");
		}
		catch (NoSuchFieldException ex) {
			throw new IllegalArgumentException("Field [" + fieldName + "] in key [" + fqFieldName +
					"] could not be found on class [" + className + "].");
		}
	}

	/**
	 * Load the main <code>JasperReport</code> from the specified <code>Resource</code>.
	 * If the <code>Resource</code> points to an uncompiled report design file then the
	 * report file is compiled dynamically and loaded into memory.
	 * @return a <code>JasperReport</code> instance, or <code>null</code> if no main
	 * report has been statically defined
	 */
	protected JasperReport loadReport() {
		String url = getUrl();
		if (url == null) {
			return null;
		}
		Resource mainReport = getApplicationContext().getResource(url);
		return loadReport(mainReport);
	}

	/**
	 * Loads a <code>JasperReport</code> from the specified <code>Resource</code>.
	 * If the <code>Resource</code> points to an uncompiled report design file then
	 * the report file is compiled dynamically and loaded into memory.
	 * @param resource the <code>Resource</code> containing the report definition or design
	 * @return a <code>JasperReport</code> instance
	 */
	protected final JasperReport loadReport(Resource resource) {
		try {
			String fileName = resource.getFilename();
			if (fileName.endsWith(".jasper")) {
				// Load pre-compiled report.
				if (logger.isInfoEnabled()) {
					logger.info("Loading pre-compiled Jasper Report from " + resource);
				}
				InputStream is = resource.getInputStream();
				try {
					return (JasperReport) JRLoader.loadObject(is);
				}
				finally {
					is.close();
				}
			}
			else if (fileName.endsWith(".jrxml")) {
				// Compile report on-the-fly.
				if (logger.isInfoEnabled()) {
					logger.info("Compiling Jasper Report loaded from " + resource);
				}
				InputStream is = resource.getInputStream();
				try {
					JasperDesign design = JRXmlLoader.load(is);
					return JasperCompileManager.compileReport(design);
				}
				finally {
					is.close();
				}
			}
			else {
				throw new IllegalArgumentException(
						"Report filename [" + fileName + "] must end in either .jasper or .jrxml");
			}
		}
		catch (IOException ex) {
			throw new ApplicationContextException(
					"Could not load JasperReports report from " + resource, ex);
		}
		catch (JRException ex) {
			throw new ApplicationContextException(
					"Could not parse JasperReports report from " + resource, ex);
		}
	}


	/**
	 * Finds the report data to use for rendering the report and then invokes the
	 * {@link #renderReport} method that should be implemented by the subclass.
	 * @param model the model map, as passed in for view rendering. Must contain
	 * a report data value that can be converted to a <code>JRDataSource</code>,
	 * acccording to the rules of the {@link #fillReport} method.
	 */
	@Override
	protected void renderMergedOutputModel(
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (this.subReports != null) {
			// Expose sub-reports as model attributes.
			model.putAll(this.subReports);

			// Transform any collections etc into JRDataSources for sub reports.
			if (this.subReportDataKeys != null) {
				for (String key : this.subReportDataKeys) {
					model.put(key, convertReportData(model.get(key)));
				}
			}
		}

		// Expose Spring-managed Locale and MessageSource.
		exposeLocalizationContext(model, request);

		// Fill the report.
		JasperPrint filledReport = fillReport(model);
		postProcessReport(filledReport, model);

		// Prepare response and render report.
		populateHeaders(response);
	}

	/**
	 * Expose current Spring-managed Locale and MessageSource to JasperReports i18n
	 * ($R expressions etc). The MessageSource should only be exposed as JasperReports
	 * resource bundle if no such bundle is defined in the report itself.
	 * <p>Default implementation exposes the Spring RequestContext Locale and a
	 * MessageSourceResourceBundle adapter for the Spring ApplicationContext,
	 * analogous to the <code>JstlUtils.exposeLocalizationContext</code> method.
	 * @see org.springframework.web.servlet.support.RequestContextUtils#getLocale
	 * @see org.springframework.context.support.MessageSourceResourceBundle
	 * @see #getApplicationContext()
	 * @see net.sf.jasperreports.engine.JRParameter#REPORT_LOCALE
	 * @see net.sf.jasperreports.engine.JRParameter#REPORT_RESOURCE_BUNDLE
	 * @see org.springframework.web.servlet.support.JstlUtils#exposeLocalizationContext
	 */
	protected void exposeLocalizationContext(Map<String, Object> model, HttpServletRequest request) {
		RequestContext rc = new RequestContext(request, getServletContext());
		model.put(JRParameter.REPORT_LOCALE, rc.getLocale());
		JasperReport report = getReport();
		if (report == null || report.getResourceBundle() == null) {
			model.put(JRParameter.REPORT_RESOURCE_BUNDLE,
					new MessageSourceResourceBundle(rc.getMessageSource(), rc.getLocale()));
		}
	}

	/**
	 * Create a populated <code>JasperPrint</code> instance from the configured
	 * <code>JasperReport</code> instance.
	 * <p>By default, this method will use any <code>JRDataSource</code> instance
	 * (or wrappable <code>Object</code>) that can be located using {@link #setReportDataKey},
	 * a lookup for type <code>JRDataSource</code> in the model Map, or a special value
	 * retrieved via {@link #getReportData}.
	 * <p>If no <code>JRDataSource</code> can be found, this method will use a JDBC
	 * <code>Connection</code> obtained from the configured <code>javax.sql.DataSource</code>
	 * (or a DataSource attribute in the model). If no JDBC DataSource can be found
	 * either, the JasperReports engine will be invoked with plain model Map,
	 * assuming that the model contains parameters that identify the source
	 * for report data (e.g. Hibernate or JPA queries).
	 * @param model the model for this request
	 * @throws IllegalArgumentException if no <code>JRDataSource</code> can be found
	 * and no <code>javax.sql.DataSource</code> is supplied
	 * @throws SQLException if there is an error when populating the report using
	 * the <code>javax.sql.DataSource</code>
	 * @throws JRException if there is an error when populating the report using
	 * a <code>JRDataSource</code>
	 * @return the populated <code>JasperPrint</code> instance
	 * @see #getReportData
	 * @see #setJdbcDataSource
	 */
	@Override
	public JasperPrint fillReport(Map<String, Object> model) throws Exception {
		// Determine main report.
		
		/*ClassPathResource resource = new ClassPathResource(model.get("url").toString());*/
		
		//JasperReport report = (JasperReport) JRLoader.loadObjectFromFile(resource.getFile().toString());
		JasperReport report = (JasperReport) JRLoader.loadObjectFromFile(model.get("url").toString());
		if (report == null) {
			throw new IllegalStateException("No main report defined for 'fillReport' - " +
					"specify a 'url' on this view or override 'getReport()' or 'fillReport(Map)'");
		}

		//JRDataSource jrDataSource = null;
		DataSource jdbcDataSourceToUse = null;
		
		// Try model attribute with specified name.
		if (this.reportDataKey != null) {
			Object reportDataValue = model.get(this.reportDataKey);
			if (reportDataValue instanceof DataSource) {
				jdbcDataSourceToUse = (DataSource) reportDataValue;
			}
			else {
				jrDataSource = convertReportData(reportDataValue);
			}
		}
		else {
			Collection values = model.values();
			jrDataSource = CollectionUtils.findValueOfType(values, JRDataSource.class);
			if (jrDataSource == null) {
				JRDataSourceProvider provider = CollectionUtils.findValueOfType(values, JRDataSourceProvider.class);
				if (provider != null) {
					jrDataSource = createReport(provider);
				}
				else {
					jdbcDataSourceToUse = CollectionUtils.findValueOfType(values, DataSource.class);
					if (jdbcDataSourceToUse == null) {
						jdbcDataSourceToUse = this.jdbcDataSource;
					}
				}
			}
		}

		if (jdbcDataSourceToUse != null) {
			return doFillReport(report, model, jdbcDataSourceToUse);
		}
		else {
			// Determine JRDataSource for main report.
			if (jrDataSource == null) {
				jrDataSource = getReportData(model);
			}
			if (jrDataSource != null) {
				// Use the JasperReports JRDataSource.
				if (logger.isDebugEnabled()) {
					logger.debug("Filling report with JRDataSource [" + jrDataSource + "]");
				}
				return JasperFillManager.fillReport(report, model, jrDataSource);
			}
			else {
				// Assume that the model contains parameters that identify
				// the source for report data (e.g. Hibernate or JPA queries).
				logger.debug("Filling report with plain model");
				return JasperFillManager.fillReport(report, model);
			}
		}
	}

	/**
	 * Fill the given report using the given JDBC DataSource and model.
	 */
	private JasperPrint doFillReport(JasperReport report, Map<String, Object> model, DataSource ds) throws Exception {
		// Use the JDBC DataSource.
		if (logger.isDebugEnabled()) {
			logger.debug("Filling report using JDBC DataSource [" + ds + "]");
		}
		Connection con = ds.getConnection();
		try {
			return JasperFillManager.fillReport(report, model, con);
		}
		finally {
			try {
				con.close();
			}
			catch (Throwable ex) {
				logger.debug("Could not close JDBC Connection", ex);
			}
		}
	}

	/**
	 * Populates the headers in the <code>HttpServletResponse</code> with the
	 * headers supplied by the user.
	 */
	private void populateHeaders(HttpServletResponse response) {
		// Apply the headers to the response.
		for (Enumeration en = this.headers.propertyNames(); en.hasMoreElements();) {
			String key = (String) en.nextElement();
			response.addHeader(key, this.headers.getProperty(key));
		}
	}

	/**
	 * Determine the <code>JasperReport</code> to fill.
	 * Called by {@link #fillReport}.
	 * <p>The default implementation returns the report as statically configured
	 * through the 'url' property (and loaded by {@link #loadReport()}).
	 * Can be overridden in subclasses in order to dynamically obtain a
	 * <code>JasperReport</code> instance. As an alternative, consider
	 * overriding the {@link #fillReport} template method itself.
	 * @return an instance of <code>JasperReport</code>
	 */
	protected JasperReport getReport() {
		return this.report;
	}

	/**
	 * Create an appropriate <code>JRDataSource</code> for passed-in report data.
	 * Called by {@link #fillReport} when its own lookup steps were not successful.
	 * <p>The default implementation looks for a value of type <code>java.util.Collection</code>
	 * or object array (in that order). Can be overridden in subclasses.
	 * @param model the model map, as passed in for view rendering
	 * @return the <code>JRDataSource</code> or <code>null</code> if the data source is not found
	 * @see #getReportDataTypes
	 * @see #convertReportData
	 */
	protected JRDataSource getReportData(Map<String, Object> model) {
		// Try to find matching attribute, of given prioritized types.
		Object value = CollectionUtils.findValueOfType(model.values(), getReportDataTypes());
		return (value != null ? convertReportData(value) : null);
	}

	/**
	 * Convert the given report data value to a <code>JRDataSource</code>.
	 * <p>The default implementation delegates to <code>JasperReportUtils</code> unless
	 * the report data value is an instance of <code>JRDataSourceProvider</code>.
	 * A <code>JRDataSource</code>, <code>JRDataSourceProvider</code>,
	 * <code>java.util.Collection</code> or object array is detected.
	 * <code>JRDataSource</code>s are returned as is, whilst <code>JRDataSourceProvider</code>s
	 * are used to create an instance of <code>JRDataSource</code> which is then returned.
	 * The latter two are converted to <code>JRBeanCollectionDataSource</code> or
	 * <code>JRBeanArrayDataSource</code>, respectively.
	 * @param value the report data value to convert
	 * @return the JRDataSource
	 * @throws IllegalArgumentException if the value could not be converted
	 * @see org.springframework.ui.jasperreports.JasperReportsUtils#convertReportData
	 * @see net.sf.jasperreports.engine.JRDataSource
	 * @see net.sf.jasperreports.engine.JRDataSourceProvider
	 * @see net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
	 * @see net.sf.jasperreports.engine.data.JRBeanArrayDataSource
	 */
	protected JRDataSource convertReportData(Object value) throws IllegalArgumentException {
		if (value instanceof JRDataSourceProvider) {
			return createReport((JRDataSourceProvider) value);
		}
		else {
			return JasperReportsUtils.convertReportData(value);
		}
	}

	/**
	 * Create a report using the given provider.
	 * @param provider the JRDataSourceProvider to use
	 * @return the created report
	 */
	protected JRDataSource createReport(JRDataSourceProvider provider) {
		try {
			JasperReport report = getReport();
			if (report == null) {
				throw new IllegalStateException("No main report defined for JRDataSourceProvider - " +
						"specify a 'url' on this view or override 'getReport()'");
			}
			return provider.create(report);
		}
		catch (JRException ex) {
			throw new IllegalArgumentException("Supplied JRDataSourceProvider is invalid", ex);
		}
	}

	/**
	 * Return the value types that can be converted to a <code>JRDataSource</code>,
	 * in prioritized order. Should only return types that the
	 * {@link #convertReportData} method is actually able to convert.
	 * <p>Default value types are: <code>java.util.Collection</code> and <code>Object</code> array.
	 * @return the value types in prioritized order
	 */
	protected Class[] getReportDataTypes() {
		return new Class[] {Collection.class, Object[].class};
	}


	/**
	 * Template method to be overridden for custom post-processing of the
	 * populated report. Invoked after filling but before rendering.
	 * <p>The default implementation is empty.
	 * @param populatedReport the populated <code>JasperPrint</code>
	 * @param model the map containing report parameters
	 * @throws Exception if post-processing failed
	 */
	protected void postProcessReport(JasperPrint populatedReport, Map<String, Object> model) throws Exception {
	}

	@Override
	public HttpHeaders setHeaderType(String type) {
		HttpHeaders headers = new HttpHeaders();
		if (type.equals("pdf")) {
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
		} else if (type.equals("excel")) {
			headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		}
		else if(type.equals("html"))
		{
			headers.setContentType(MediaType.parseMediaType("application/html"));
		}
		return headers;	
	}

	@Override
	public byte[] convertToByte(JasperPrint jasperPrint,String fileName) {
		
		
		String fileNames[]=fileName.split("\\.");
		
		OutputStream outStream ;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bytesRead;
		byte[] buffer = new byte[8192];
		File filePath = new File("template");
		if (!filePath.exists()) {
			filePath.mkdir();
		}
		FileSystemResource srcFile = new FileSystemResource(filePath.getAbsoluteFile()+"/"+fileName);
		FileInputStream stream;
		try {
			if(fileNames[1].equals("pdf")){
			outStream = new FileOutputStream(new File(srcFile.getFile().toString()));
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
			stream = new FileInputStream(srcFile.getFile().toString());
			while ((bytesRead = stream.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}
			}
			
			if(fileNames[1].equals("xlsx")){
				outStream = new FileOutputStream(new File(srcFile.getFile().toString()));
				JRXlsxExporter exporter = new JRXlsxExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
				SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
				configuration.setOnePagePerSheet(false);
				configuration.setRemoveEmptySpaceBetweenRows(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();
				stream = new FileInputStream(srcFile.getFile().toString());
				while ((bytesRead = stream.read(buffer)) != -1) {
					baos.write(buffer, 0, bytesRead);
				}
				}
		}catch (IOException e) {
			e.printStackTrace();
		}catch (JRException e) {
			e.printStackTrace();
		}
        return baos.toByteArray();
	}

}
