package com.viewfunction.contentRepository.developmentTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.BinaryContent;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.contentRepository.util.helper.TextContent;

public class ContentOperationHelperTestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//initRootContentObject();
		//testAddBinaryContent();
		//testGetBinaryContents();
		//testAddTextContent();
		testGetTextContents();
		//testRemoveBinaryContent();
		//testRemoveTextBinaryContent();
		//testUpdateBinaryContent();
		//testUpdateTextContent();
	}
	
	public static void initRootContentObject(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("FileContentOperationTestSpace");
			RootContentObject rco=ContentComponentFactory.createRootContentObject("RootFileOperationTestObject_f");
			cs.addRootContentObject(rco);		
			System.out.println(rco.getRootContentObjectID());
			System.out.println(rco.getParentContentSpace());	
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
		}		
			
	}
	
	public static void testAddBinaryContent(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("FileContentOperationTestSpace");
			RootContentObject rco=cs.getRootContentObject("RootFileOperationTestObject_f");		
			/*init
			BaseContentObject binaryCo=rco.addSubContentObject("folder_001", null, true);
			*/
			
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();
			//coh.addBinaryContent(folderCo,new File("vaadin621.pdf"), "a pdf file");		
			//coh.addBinaryContent(folderCo,new File("3380232995318967763.jpg"), "a jpg file");			
			//coh.addBinaryContent(folderCo,new File("Beyonce - Halo10M.mp3"), "a mp3 music file");
			//coh.addBinaryContent(folderCo,new File("o_0c0a24112bd5.jpg"), "a jpg file");
			//coh.addBinaryContent(folderCo,new File("6_roger.jpg"), "a jpg file");
			//coh.addBinaryContent(folderCo,new File("CommonBase Event_SituationData_V20.doc"), "����word");
			//coh.addBinaryContent(folderCo,new File("Communications_Management_wp_FINAL.pdf"), "����pdf");
			//boolean b=coh.addBinaryContent(folderCo,new File("01300000029584120462690206338.jpg"), "ScalaTutorial.pdf",true);
			
			boolean b=coh.addBinaryContent(folderCo,new File("IMG_1716.JPG"), "ScalaTutorial.pdf",true);			
			System.out.println(b);
			
			//BinaryContentObject bco=coh.getBinaryContent(folderCo,"vaadin621.pdf");
			/*
			BinaryContentObject bco=coh.getBinaryContent(folderCo,"Beyonce - Halo10M.mp3");
			
			System.out.println(bco);
			System.out.println(bco.getContentSize());
			System.out.println(bco.getContentName());
			System.out.println(bco.getMimeType());
			System.out.println(bco.getContentDescription());
			System.out.println(bco.getLastModified().toString());
			System.out.println(bco.getContentInputStream());		
			
			try
		    {
			    File f=new File(bco.getContentName());	    
			    InputStream is=bco.getContentInputStream();		    
			    OutputStream out=new FileOutputStream(f);
			    byte buf[]=new byte[1024];
			    int len;
			    while((len=is.read(buf))>0){
			    	out.write(buf,0,len);
			    }
			    out.close();
			    is.close();	 
			}catch (IOException e){
				e.printStackTrace();
			}*/			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();	
		}	
				
	}
	
	public static void testGetBinaryContents(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("FileContentOperationTestSpace");
			RootContentObject rco=cs.getRootContentObject("RootFileOperationTestObject_f");
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);	
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();
			
			BinaryContent b=coh.getBinaryContent(folderCo, "vaadin621.pdf");
			//BinaryContent b=coh.getBinaryContent(folderCo, "cccc");
			System.out.println(b.getMimeType());			
			
			List<BinaryContent> bcol=coh.getBinaryContents(folderCo);		
			for(BinaryContent bco:bcol){			
				System.out.print(bco.getContentName()+" ");
				System.out.print(bco.getContentSize()+" ");
				System.out.println(bco.getMimeType());
				System.out.println(bco.getContentDescription());
				System.out.println(bco.getLastModified().getTime());		
				try
			    {
				    File f=new File("binaryContentFile/"+bco.getContentName());	    
				    InputStream is=bco.getContentInputStream();		    
				    OutputStream out=new FileOutputStream(f);
				    byte buf[]=new byte[1024];
				    int len;
				    while((len=is.read(buf))>0){
				    	out.write(buf,0,len);
				    }
				    out.close();
				    is.close();	 
				}catch (IOException e){
					e.printStackTrace();
				}			
			}				
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
		}			
	}

	public static void testAddTextContent(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("FileContentOperationTestSpace");
			RootContentObject rco=cs.getRootContentObject("RootFileOperationTestObject_f");		
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();
			//coh.addTextContent(folderCo,new File("derby.log"), "derby.log",false);
			//boolean b=coh.addTextContent(folderCo,new File("ContentReposityCustomNodes.config"), "ContentReposityCustomNodes.config document",true);
			boolean b=coh.addTextContent(folderCo,new File("jackRabbit.txt"), "ContentReposityCustomNodes.config document",true);			
			System.out.println(b);			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
		}	
				
	}
	public static void testGetTextContents(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("FileContentOperationTestSpace");
			RootContentObject rco=cs.getRootContentObject("RootFileOperationTestObject_f");		
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();
			/*
			TextContent tc=coh.getTextContent(folderCo,"Unicode.txt");		
			System.out.println(tc.getContentName());
			System.out.println(tc.getContentDescription());
			System.out.println(tc.getContentSize());
			System.out.println(tc.getEncoding());
			System.out.println(tc.getMimeType());	
			*/	
			
			TextContent tc=coh.getTextContent(folderCo, "ContentReposityCustomNodes.config");			
			//TextContent tc=coh.getTextContent(folderCo, "01300000029584120462690206338.jpgaaa");			
			System.out.println(tc.getContentDescription());
			System.out.println(tc.getContentName());
			System.out.println(tc.getContentSize());
			System.out.println(tc.getEncoding());
			System.out.println(tc.getMimeType());
			System.out.println(tc.getContentInputStream());
			
			
			List<TextContent> tcl= coh.getTextContents(folderCo);
			for(TextContent ctc:tcl){			
				System.out.println(ctc.getContentName());
				System.out.println(ctc.getContentDescription());
				System.out.println(ctc.getContentSize());
				System.out.println(ctc.getEncoding());
				System.out.println(ctc.getMimeType());				
				try {
					InputStreamReader isr = new InputStreamReader(ctc.getContentInputStream(), ctc.getEncoding());
					 BufferedReader br = new BufferedReader(isr);
					 // read the file content
					 String line;
					 while ((line = br.readLine())!= null){
					     System.out.println(line);
					 }				 
					 ctc.getContentInputStream().close();				 
				} catch (UnsupportedEncodingException e) {				
					e.printStackTrace();
				} catch (IOException e) {				
					e.printStackTrace();
				}			
			}	
				
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
		}			
	}
	
	public static void testRemoveBinaryContent(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("FileContentOperationTestSpace");
			RootContentObject rco=cs.getRootContentObject("RootFileOperationTestObject_f");		
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();			
			boolean b=coh.removeBinaryContent(folderCo, "derby.log", true);
			System.out.println(b);			
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
		}		
	}
	
	public static void testRemoveTextBinaryContent(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("FileContentOperationTestSpace");
			RootContentObject rco=cs.getRootContentObject("RootFileOperationTestObject_f");		
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();			
			boolean b=coh.removeTextContent(folderCo, "CustomNodesSample.config", true);
			System.out.println(b);			
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
		}		
	}
	
	public static void testUpdateBinaryContent(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("FileContentOperationTestSpace");
			RootContentObject rco=cs.getRootContentObject("RootFileOperationTestObject_f");		
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();			
			boolean b=coh.updateBinaryContent(folderCo, "01300000029584120462690206338.jpg",new File("IMG_1716.JPG"), "ss2qqq22", true);
			System.out.println(b);			
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
		}		
	}
	
	public static void testUpdateTextContent(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("FileContentOperationTestSpace");
			RootContentObject rco=cs.getRootContentObject("RootFileOperationTestObject_f");		
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();			
			boolean b=coh.updateTextContent(folderCo, "stockInfo.txt",new File("jackRabbit.txt"), "namingArt.txt", true);
			System.out.println(b);			
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
		}		
	}
}
