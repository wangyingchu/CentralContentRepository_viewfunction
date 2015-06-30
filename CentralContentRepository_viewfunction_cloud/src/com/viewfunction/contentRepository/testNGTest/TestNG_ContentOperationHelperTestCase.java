package com.viewfunction.contentRepository.testNGTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.*;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.BinaryContent;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.contentRepository.util.helper.TextContent;

public class TestNG_ContentOperationHelperTestCase {
	@BeforeClass 
	public void initContentOperationHelperTestContentObject(){
		System.out.println("initContentOperationHelperTestContentObject");
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			Assert.assertNotNull(cs);			
			RootContentObject rco=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.ContentOperationHelperTestContentObject);
			cs.addRootContentObject(rco);			
			RootContentObject addedRco=cs.getRootContentObject(TestCaseDataConstant.ContentOperationHelperTestContentObject);			
			Assert.assertNotNull(addedRco);	
			BaseContentObject binaryCo=addedRco.addSubContentObject("folder_001", null, true);
			Assert.assertNotNull(binaryCo);	
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during initContentOperationHelperTestContentObject");
		}			
	}
	
	@AfterClass
	public void removeContentOperationHelperTestContentObject(){
		System.out.println("removeContentOperationHelperTestContentObject");
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);			
			boolean rmExist1=cs.removeRootContentObject(TestCaseDataConstant.ContentOperationHelperTestContentObject);			
			Assert.assertTrue(rmExist1);				
			cs.closeContentSpace();	
			deleteDirectoryContent(new File("binaryContentFile"));
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during removeContentOperationHelperTestContentObject");
		}			
	}
	
	private static boolean deleteDirectoryContent(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	        	 deleteDirectoryContent(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return true;
	}	
	
	@Test
	public void testAdd_GetBinaryContent(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentOperationHelperTestContentObject);
			Assert.assertNotNull(rco);			
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();	
			
			File f1=new File("testBinaryContentFile/01300000029584120462690206338.jpg");
			boolean b=coh.addBinaryContent(folderCo,f1, "01300000029584120462690206338.jpg_DECS",true);
			Assert.assertTrue(b);
			boolean b2=coh.addBinaryContent(folderCo,f1, "01300000029584120462690206338.jpg",true);
			Assert.assertFalse(b2);	
			BinaryContent bco=coh.getBinaryContent(folderCo,"01300000029584120462690206338.jpg");
			Assert.assertNotNull(bco);				
			Assert.assertNotNull(bco.getMimeType());
			Assert.assertNotNull(bco.getContentInputStream());
			Assert.assertNotNull(bco.getLastModified());			
			Assert.assertEquals("01300000029584120462690206338.jpg",bco.getContentName());
			Assert.assertEquals("01300000029584120462690206338.jpg_DECS",bco.getContentDescription());
			Assert.assertEquals(bco.getContentSize(),f1.length());	
			
			Assert.assertEquals(bco.isLinkObject(),false);
			Assert.assertEquals(bco.isLocked(),false);
			Assert.assertEquals(bco.getCurrentVersion(),"1.0");	//the first version Number should by 1.0
			
			boolean b3=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/3380232995318967763.jpg"), "3380232995318967763.jpg",true);
			Assert.assertTrue(b3);
			boolean b4=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/3380232995318967763.jpg"), "3380232995318967763.jpg",true);
			Assert.assertFalse(b4);			
			boolean b5=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/6_roger.jpg"), "6_roger.jpg",true);
			Assert.assertTrue(b5);
			boolean b6=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/6_roger.jpg"), "6_roger.jpg",true);
			Assert.assertFalse(b6);				

			File f2=new File("testBinaryContentFile/Beyonce - Halo10M.mp3");
			boolean b7=coh.addBinaryContent(folderCo,f2, "Beyonce - Halo10M.mp3_DECS",true);
			Assert.assertTrue(b7);
			boolean b8=coh.addBinaryContent(folderCo,f2, "01300000029584120462690206338.jpg",true);
			Assert.assertFalse(b8);	
			BinaryContent bco2=coh.getBinaryContent(folderCo,"Beyonce - Halo10M.mp3");
			Assert.assertNotNull(bco2);				
			Assert.assertNotNull(bco2.getMimeType());
			Assert.assertNotNull(bco2.getContentInputStream());
			Assert.assertNotNull(bco2.getLastModified());			
			Assert.assertEquals("Beyonce - Halo10M.mp3",bco2.getContentName());
			Assert.assertEquals("Beyonce - Halo10M.mp3_DECS",bco2.getContentDescription());
			Assert.assertEquals(bco2.getContentSize(),f2.length());			
			
			boolean b9=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/CommonBase Event_SituationData_V20.doc"), "CommonBase Event_SituationData_V20.doc",true);
			Assert.assertTrue(b9);
			boolean b10=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/CommonBase Event_SituationData_V20.doc"), "CommonBase Event_SituationData_V20.doc",true);
			Assert.assertFalse(b10);			
			boolean b11=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/Communications_Management_wp_FINAL.pdf"), "Communications_Management_wp_FINAL.pdf",true);
			Assert.assertTrue(b11);
			boolean b12=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/Communications_Management_wp_FINAL.pdf"), "Communications_Management_wp_FINAL.pdf",true);
			Assert.assertFalse(b12);			
			boolean b13=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/CSS Speech Bubbles.zip"), "CSS Speech Bubbles.zip",true);
			Assert.assertTrue(b13);
			boolean b14=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/CSS Speech Bubbles.zip"), "CSS Speech Bubbles.zip",true);
			Assert.assertFalse(b14);			
			boolean b15=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/IMG_1716.JPG"), "IMG_1716.JPG",true);
			Assert.assertTrue(b15);
			boolean b16=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/IMG_1716.JPG"), "IMG_1716.JPG",true);
			Assert.assertFalse(b16);			
			boolean b17=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/o_0c0a24112bd5.jpg"), "o_0c0a24112bd5.jpg",true);
			Assert.assertTrue(b17);
			boolean b18=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/o_0c0a24112bd5.jpg"), "o_0c0a24112bd5.jpg",true);
			Assert.assertFalse(b18);			
			boolean b19=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/ScalaTutorial.pdf"), "ScalaTutorial.pdf",true);
			Assert.assertTrue(b19);
			boolean b20=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/ScalaTutorial.pdf"), "ScalaTutorial.pdf",true);
			Assert.assertFalse(b20);			
			boolean b21=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/Typophone_4_by_Angelman8.jpg"), "Typophone_4_by_Angelman8.jpg",true);
			Assert.assertTrue(b21);
			boolean b22=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/Typophone_4_by_Angelman8.jpg"), "Typophone_4_by_Angelman8.jpg",true);
			Assert.assertFalse(b22);			
			boolean b23=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/vaadin621.pdf"), "vaadin621.pdf",true);
			Assert.assertTrue(b23);
			boolean b24=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/vaadin621.pdf"), "vaadin621.pdf",true);
			Assert.assertFalse(b24);			
			//boolean b25=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/冬天.jpg"), "冬天.jpg",true);
			//Assert.assertTrue(b25);
			//boolean b26=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/冬天.jpg"), "冬天.jpg",true);
			//Assert.assertFalse(b26);			
			List<BinaryContent> bcol=coh.getBinaryContents(folderCo);			
			Assert.assertEquals(12, bcol.size(),"Added binaryContents number should be 13");		
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testAdd_GetBinaryContent");
		}		
	}
	
	@Test(dependsOnMethods = { "testAdd_GetBinaryContent" })
	public void testGetBinaryContents(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentOperationHelperTestContentObject);
			Assert.assertNotNull(rco);			
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();	
		
			List<BinaryContent> bcol=coh.getBinaryContents(folderCo);		
			for(BinaryContent bco:bcol){				
				Assert.assertNotNull(bco.getContentName());
				Assert.assertNotNull(bco.getContentSize());
				Assert.assertNotNull(bco.getMimeType());
				Assert.assertNotNull(bco.getContentDescription());
				Assert.assertNotNull(bco.getLastModified().getTime());
				try{
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
				File f2=new File("binaryContentFile/"+bco.getContentName());
				Assert.assertEquals(bco.getContentSize(),f2.length());
			}			
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testGetBinaryContents");
		}	
	}
	
	@Test(dependsOnMethods = { "testGetBinaryContents" })
	public void testRemoveBinaryContent(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentOperationHelperTestContentObject);
			Assert.assertNotNull(rco);			
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();				
			BinaryContent bco2=coh.getBinaryContent(folderCo,"vaadin621.pdf");			
			Assert.assertNotNull(bco2);			
			boolean b=coh.removeBinaryContent(folderCo, "vaadin621.pdf", true);
			Assert.assertTrue(b);			
			boolean b2=coh.removeBinaryContent(folderCo, "vaadin621.pdf", true);
			Assert.assertFalse(b2);			
			BinaryContent bco3=coh.getBinaryContent(folderCo,"vaadin621.pdf");			
			Assert.assertNull(bco3);			
			List<BinaryContent> bcol=coh.getBinaryContents(folderCo);			
			Assert.assertEquals(11, bcol.size(),"binaryContents number should be 11");		
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testRemoveBinaryContent");
		}	
	}
	
	@Test(dependsOnMethods = { "testRemoveBinaryContent" })
	public void testUpdateBinaryContent(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentOperationHelperTestContentObject);
			Assert.assertNotNull(rco);			
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();
			
			BinaryContent bco=coh.getBinaryContent(folderCo,"6_roger.jpg");		
			Assert.assertNotNull(bco);
			long ol=bco.getContentSize();
			String on=bco.getContentName();
			String odesc=bco.getContentDescription();
			Calendar odate=bco.getLastModified();
			
			boolean b=coh.updateBinaryContent(folderCo, "6_roger.jpg",new File("testBinaryContentFile/IMG_1716.JPG"), "ss2qqq22", true);
			Assert.assertTrue(b);		
			boolean b2=coh.updateBinaryContent(folderCo, "6_roger_notExistInRepository.jpg",new File("testBinaryContentFile/IMG_1716.JPG"), "ss2qqq22", true);
			Assert.assertFalse(b2);
			
			BinaryContent bco2=coh.getBinaryContent(folderCo,"6_roger.jpg");		
			Assert.assertNotNull(bco2);			
			String nn=bco.getContentName();
			long nl=bco2.getContentSize();
			String ndesc=bco2.getContentDescription();
			Calendar ndate=bco2.getLastModified();
			
			Assert.assertEquals(on, nn);
			Assert.assertTrue(ol!=nl);			
			Assert.assertTrue(!odesc.equals(ndesc));
			Assert.assertTrue(odate.getTimeInMillis()!=ndate.getTimeInMillis());			
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testUpdateBinaryContent");
		}	
	}	
	
	@Test(dependsOnMethods = { "testUpdateBinaryContent" })
	public void testAdd_GetTextContent(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentOperationHelperTestContentObject);
			Assert.assertNotNull(rco);			
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();
			
			File f1=new File("testTextContentFile/accents.txt");
			boolean b=coh.addTextContent(folderCo,f1, "accents.txt_DECS",true);
			Assert.assertTrue(b);
			boolean b2=coh.addTextContent(folderCo,f1, "accents.txt",true);
			Assert.assertFalse(b2);	
			
			TextContent bco=coh.getTextContent(folderCo,"accents.txt");
			Assert.assertNotNull(bco);				
			Assert.assertNotNull(bco.getMimeType());
			Assert.assertNotNull(bco.getContentInputStream());
			Assert.assertNotNull(bco.getLastModified());
			Assert.assertNotNull(bco.getEncoding());
			Assert.assertEquals("accents.txt",bco.getContentName());
			Assert.assertEquals("accents.txt_DECS",bco.getContentDescription());
			Assert.assertEquals(bco.getContentSize(),f1.length());
			Assert.assertEquals(bco.isLinkObject(),false);
			Assert.assertEquals(bco.isLocked(),false);
			Assert.assertEquals(bco.getCurrentVersion(),"1.0");//first version should be 1.0
			
			boolean b3=coh.addTextContent(folderCo,new File("testTextContentFile/AINSI.txt"), "AINSI.txt",true);
			Assert.assertTrue(b3);
			boolean b4=coh.addTextContent(folderCo,new File("testTextContentFile/AINSI.txt"), "AINSI.txt",true);
			Assert.assertFalse(b4);	
			
			boolean b5=coh.addTextContent(folderCo,new File("testTextContentFile/BigEndian.txt"), "BigEndian.txt",true);
			Assert.assertTrue(b5);
			boolean b6=coh.addTextContent(folderCo,new File("testTextContentFile/BigEndian.txt"), "BigEndian.txt",true);
			Assert.assertFalse(b6);	
			
			boolean b7=coh.addTextContent(folderCo,new File("testTextContentFile/ChineseContent.txt"), "ChineseContent.txt",true);
			Assert.assertTrue(b7);
			boolean b8=coh.addTextContent(folderCo,new File("testTextContentFile/ChineseContent.txt"), "ChineseContent.txt",true);
			Assert.assertFalse(b8);	
			
			boolean b9=coh.addTextContent(folderCo,new File("testTextContentFile/Unicode.txt"), "Unicode.txt",true);
			Assert.assertTrue(b9);
			boolean b10=coh.addTextContent(folderCo,new File("testTextContentFile/Unicode.txt"), "Unicode.txt",true);
			Assert.assertFalse(b10);	
			
			boolean b11=coh.addTextContent(folderCo,new File("testTextContentFile/UTF8.txt"), "UTF8.txt",true);
			Assert.assertTrue(b11);
			boolean b12=coh.addTextContent(folderCo,new File("testTextContentFile/UTF8.txt"), "UTF8.txt",true);
			Assert.assertFalse(b12);	
			
			boolean b13=coh.addTextContent(folderCo,new File("testTextContentFile/UTF8BOM.txt"), "UTF8BOM.txt",true);
			Assert.assertTrue(b13);
			boolean b14=coh.addTextContent(folderCo,new File("testTextContentFile/UTF8BOM.txt"), "UTF8BOM.txt",true);
			Assert.assertFalse(b14);	
			
			boolean b15=coh.addTextContent(folderCo,new File("testTextContentFile/UTF-8-test.txt"), "UTF-8-test.txt",true);
			Assert.assertTrue(b15);
			boolean b16=coh.addTextContent(folderCo,new File("testTextContentFile/UTF-8-test.txt"), "UTF-8-test.txt",true);
			Assert.assertFalse(b16);			
			List<TextContent> bcol=coh.getTextContents(folderCo);			
			Assert.assertEquals(8, bcol.size(),"Added binaryContents number should be 8");			
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testAdd_GetTextContent");
		}	
	}
	
	@Test(dependsOnMethods = { "testAdd_GetTextContent" })
	public void testGetTextContents(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentOperationHelperTestContentObject);
			Assert.assertNotNull(rco);			
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();
	
			List<TextContent> tcl= coh.getTextContents(folderCo);
			for(TextContent ctc:tcl){			
				Assert.assertNotNull(ctc.getContentName());
				Assert.assertNotNull(ctc.getContentDescription());
				Assert.assertNotNull(ctc.getContentSize());
				Assert.assertNotNull(ctc.getEncoding());
				Assert.assertNotNull(ctc.getMimeType());				
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
		} catch (ContentReposityException e) {
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testGetTextContents");
		}	
	}
	
	@Test(dependsOnMethods = { "testGetTextContents" })
	public void testRemoveTextContent(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentOperationHelperTestContentObject);
			Assert.assertNotNull(rco);			
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();			
			
			TextContent tc1=coh.getTextContent(folderCo, "ChineseContent.txt");
			Assert.assertNotNull(tc1);
			boolean b=coh.removeTextContent(folderCo, "ChineseContent.txt", true);
			Assert.assertTrue(b);
			boolean b2=coh.removeTextContent(folderCo, "ChineseContent.txt", true);
			Assert.assertFalse(b2);
			TextContent tc2=coh.getTextContent(folderCo, "ChineseContent.txt");
			Assert.assertNull(tc2);			
			List<TextContent> tcol=coh.getTextContents(folderCo);			
			Assert.assertEquals(7, tcol.size(),"TextContents number should be 7");				
			
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testRemoveTextContent");
		}	
	}
	
	@Test(dependsOnMethods = { "testRemoveTextContent" })
	public void testUpdateTextContent(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentOperationHelperTestContentObject);
			Assert.assertNotNull(rco);			
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);		
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();			
			
			TextContent tc1=coh.getTextContent(folderCo, "Unicode.txt");
			Assert.assertNotNull(tc1);
			Assert.assertNotNull(tc1.getMimeType());			
			long ol=tc1.getContentSize();
			String on=tc1.getContentName();
			String odesc=tc1.getContentDescription();
			Calendar odate=tc1.getLastModified();
			String oen=tc1.getEncoding();			
			
			boolean b=coh.updateTextContent(folderCo, "Unicode.txt",new File("testTextContentFile/UTF8BOM.txt"), "ss2qqq22", true);
			Assert.assertTrue(b);		
			boolean b2=coh.updateTextContent(folderCo, "Unicode_notExistInRepository.txt",new File("testTextContentFile/UTF8BOM.txt"), "ss2qqq22", true);
			Assert.assertFalse(b2);			
			
			TextContent tc2=coh.getTextContent(folderCo,"Unicode.txt");	
			Assert.assertNotNull(tc2);
			Assert.assertNotNull(tc2.getMimeType());
			long nl=tc2.getContentSize();
			String nn=tc2.getContentName();
			String ndesc=tc2.getContentDescription();
			Calendar ndate=tc2.getLastModified();
			String nen=tc2.getEncoding();
			
			Assert.assertEquals(on, nn);
			Assert.assertTrue(ol!=nl);			
			Assert.assertTrue(!odesc.equals(ndesc));
			//Assert.assertTrue(odate.getTimeInMillis()!=ndate.getTimeInMillis());
			Assert.assertTrue(!oen.equals(nen));
			
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testUpdateTextContent");
		}	
	}	
}
