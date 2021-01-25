/**
 * 
 */
package com.visionous.dms.configuration.helpers;

import java.io.File;
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
	public static String write(MultipartFile file, String fileType) throws IOException {
	    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
	    String fileName = date + file.getOriginalFilename();
	    String path = new File(".").getCanonicalPath()+"/tmp";

	    String folderPath = path;
	    String filePath = folderPath + "/" + fileName;
	    System.out.println(path + " ---- "+ Paths.get(filePath));
	    // folderPath here is /sismed/temp/exames
	    // Copies Spring's multipartfile inputStream to /sismed/temp/exames (absolute path)
	    Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
	    return filePath;
	}
}
