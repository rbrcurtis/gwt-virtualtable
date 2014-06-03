package com.mut8ed.battlemap.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;

public class Util {

	public static final Logger logger = Logger.getLogger(Util.class);

	public static byte[] createChecksum(String filename) throws
	Exception
	{
		InputStream fis =  new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}

	// see this How-to for a faster way to convert 
	// a byte array to a HEX string 
	public static String getFileMD5(String filename) throws Exception {
		byte[] b = createChecksum(filename);
		String result = "";
		for (int i=0; i < b.length; i++) {
			result +=
					Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}

	public static String getStringMD5(String str){
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(str.getBytes());
			byte[] hash = digest.digest();

			return new BigInteger(1,hash).toString(16).toLowerCase();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args){
		getStringMD5("some bullshit");
	}

	public static boolean isImage(File file){
		try {
			ImageInputStream in = ImageIO.createImageInputStream(file);
			Iterator<ImageReader> readers = ImageIO.getImageReaders(in);

			boolean isImage;
			if(!readers.hasNext()) {
				isImage = false;
			}else {
				ImageReader reader = readers.next();
				String format = reader.getFormatName();
				logger.info("found image format of "+format);
				if("jpeg".equalsIgnoreCase(format)) {
					isImage = true;
				}else {
					isImage = false;
				}
			}
			in.close();

			return isImage;

		} catch (Exception e) {
			logger.error(e,e);
			return false;
		}

	}

	public static String getStringHash(String str){

		try {

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(str.getBytes());

			byte byteData[] = md.digest();

			//convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			System.out.println("Hex format : " + sb.toString());

			//convert the byte to hex format method 2
			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<byteData.length;i++) {
				String hex=Integer.toHexString(0xff & byteData[i]);
				if(hex.length()==1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}

