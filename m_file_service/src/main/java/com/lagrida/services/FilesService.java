package com.lagrida.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesService {
	
	public static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg","jpeg","gif","png","bmp","tiff","doc","pdf","ppt","zip","rar","jar","xls","pptx","docx","xlsx", "txt");
	public static final String WEB_UPLOAD_DIR = "files";
	public static final String DIRECTORY = System.getProperty("user.home") + "\\Desktop\\Laravel\\spring_cloud\\m_file_service\\src\\main\\resources\\static\\files\\";
	
	public void uploadTheFile(MultipartFile fileData, String newFileName, String extension, String filesDirectory) throws FileNotFoundException, IOException {
		newFileName = DIRECTORY + filesDirectory + "\\"  + newFileName + "." + extension;
		File serverFile = new File(newFileName);
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		stream.write(fileData.getBytes());
		stream.close();
	}
	
	public String getExtension(String fileName) {
		int indexOf = fileName.lastIndexOf(".");
		if(indexOf == -1){
			return "";
		}
		return fileName.substring(indexOf+1);
	}
	public boolean checkExtension(String myExtension) {
		return ALLOWED_EXTENSIONS.contains(myExtension.toLowerCase());
	}
	public String generateRandomText() {
	    int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 15;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
	 
	    return generatedString;
	}
}
