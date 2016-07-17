import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import scala.collection.script.Start;


public class SysUtils {

	private static Logger _log = Logger.getLogger(Start.class.getName());
	
	public static String getFolderName() {
		Date date =  new  Date(); 
		String timestamp =  new  SimpleDateFormat( "yyyyMMddHH" ).format(date);
		return timestamp;
	}
	
	public static String getFileName() {
		Date date =  new  Date(); 
		String timestamp =  new  SimpleDateFormat( "yyyyMMddHHmmssSSS" ).format(date);
		return timestamp+".jpg";
	}
	
	public static boolean createFolder() {
		File theDir = new File(Config.ImageSavePath + "/" + SysUtils.getFolderName());
	    boolean result = false;
		if (!theDir.exists()) {
		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException e){
		    	e.getMessage();
		    }        
		    if(result) {    
		        System.out.println(Config.ImageSavePath + "/" + SysUtils.getFolderName() + " -- Folder Created");  
		    }
		}
		return result;
	}
	
	public static String readImage(File file) {
		String imageDataString = null;

		try {			
			// Reading a Image file from file system
			FileInputStream imageInFile = new FileInputStream(file);
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);

			// Converting Image byte array into Base64 String
			imageDataString = Base64Utils.encodeImage(imageData);
			imageInFile.close();
		
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}
		return imageDataString;
	}
	
	public static boolean checkImage(String file) throws Exception {
		ImageInputStream iis = ImageIO.createImageInputStream(new File(file ));
		Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpg");
		boolean rtn = false;
		while (readers.hasNext()) {
		    try {        
		        ImageReader reader = readers.next();
		        reader.setInput(iis);
		        reader.read(0);
		        rtn = true;
		        break;
		    } catch (IOException e) {
		    	_log.info(file + " -- "+ e.getMessage());
		    }        
		}
		return rtn;
	}
	
	public static void saveImage(String message) {
    	// String savePath=Config.ImageSavePath+"\\"+SysUtils.getFolderName()+"\\"+SysUtils.getFileName();
		String savePath=Config.ImageSavePath+"/"+SysUtils.getFolderName()+"/"+SysUtils.getFileName();
		SysUtils.createFolder();
		// Converting a Base64 String into Image byte array
    	_log.info("Saved Image Start! -- " + savePath);
    	byte[] imageByteArray = Base64Utils.decodeImage(message);
		// Write a image byte array into file system
		FileOutputStream imageOutFile;
		try {
			imageOutFile = new FileOutputStream(savePath);
			imageOutFile.write(imageByteArray);
			imageOutFile.close();
			try {
				if (SysUtils.checkImage(savePath) == true) {
	    			_log.info("Saved Image Done! -- " + savePath);
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
}
