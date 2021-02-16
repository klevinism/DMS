/**
 * 
 */
package com.visionous.dms.configuration.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author delimeta
 *
 */
public class FileManager {
	
	public static final String CUSTOMER_PROFILE_IMAGE_PATH = "/tmp/customer/profile/";
	public static final String PERSONNEL_PROFILE_IMAGE_PATH = "/tmp/personnel/profile/";
	public static final String BUSINESS_LOGO_IMAGE_PATH = "/tmp/business/logo/";

	public static String write(MultipartFile file, String pathstatic) throws IOException {
		
	    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
	    String fileName = date + file.getOriginalFilename();
	    String folderPath = DmsCore.pathToTemp() + pathstatic;

	    String filePath = folderPath + "/" + fileName;

	    Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
	    
	    return fileName;
	}
	
	public static String uploadImage(MultipartFile file, String basePathToUploadFolder) throws IOException {
		if(file != null && file.getOriginalFilename() != null && !file.isEmpty()){
			return write(file, basePathToUploadFolder);
		}
		return null;
	}
}