package com.viewfunction.testCode.tempTestCalss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

import com.glaforge.i18n.io.CharsetToolkit;

import eu.medsea.mimeutil.MimeUtil;

public class TextFileEncordingTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		try {
			Charset guessedCharset = CharsetToolkit.guessEncoding(new File("BigEndian.txt"), 4096);
			
			System.out.println(guessedCharset);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		/*
		
	        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
	        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector");
	        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.OpendesktopMimeDetector");	
	        
	        
	        
	        
	        File f = new File ("test.xml");
	        Collection<?> mimeTypes = MimeUtil.getMimeTypes(f);
	        
	        
	        
	        
	        
	        System.out.println(mimeTypes.toString());
	       
	    */
		
		System.out.println("remove junitTestContentSpace");
		boolean delResult=false;
		File junitTestContentSpaceFile=new File("repository/workspaces/junitTestContentSpace");
		System.out.println(junitTestContentSpaceFile);
		if(junitTestContentSpaceFile.exists()){
			delResult=junitTestContentSpaceFile.delete();
		}
		if(delResult){
			System.out.println("junitTestContentSpace removed");
		}
		

	}

}
