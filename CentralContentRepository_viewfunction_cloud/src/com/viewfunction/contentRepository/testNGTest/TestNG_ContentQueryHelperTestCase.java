package com.viewfunction.contentRepository.testNGTest;

import java.io.File;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.*;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.BinaryContent;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.contentRepository.util.helper.TextContent;

public class TestNG_ContentQueryHelperTestCase {
	@BeforeClass 
	public void initContentQueryHelperTestContentObject(){
		System.out.println("initContentQueryHelperTestContentObject");
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			Assert.assertNotNull(cs);			
			RootContentObject rco=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);
			cs.addRootContentObject(rco);			
			RootContentObject addedRco=cs.getRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);			
			Assert.assertNotNull(addedRco);	
			BaseContentObject binaryCo=addedRco.addSubContentObject("contentQueryFolder", null, true);
			Assert.assertNotNull(binaryCo);	
			
			BaseContentObject folderCo=rco.getSubContentObject("contentQueryFolder");
			Assert.assertNotNull(folderCo);	
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();	
			Assert.assertNotNull(coh);	
			
			File f1=new File("testBinaryContentFile/01300000029584120462690206338.jpg");
			boolean b=coh.addBinaryContent(folderCo,f1, "01300000029584120462690206338.jpg_DECS",true);
			Assert.assertTrue(b);					
			boolean b3=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/3380232995318967763.jpg"), "3380232995318967763.jpg",true);
			Assert.assertTrue(b3);		
			boolean b5=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/6_roger.jpg"), "6_roger.jpg",true);
			Assert.assertTrue(b5);
			File f2=new File("testBinaryContentFile/Beyonce - Halo10M.mp3");
			boolean b7=coh.addBinaryContent(folderCo,f2, "Beyonce - Halo10M.mp3_DECS",true);
			Assert.assertTrue(b7);			
			boolean b9=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/CommonBase Event_SituationData_V20.doc"), "CommonBase Event_SituationData_V20.doc",true);
			Assert.assertTrue(b9);			
			boolean b11=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/Communications_Management_wp_FINAL.pdf"), "Communications_Management_wp_FINAL.pdf",true);
			Assert.assertTrue(b11);			
			boolean b13=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/CSS Speech Bubbles.zip"), "CSS Speech Bubbles.zip",true);
			Assert.assertTrue(b13);			
			boolean b15=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/IMG_1716.JPG"), "IMG_1716.JPG",true);
			Assert.assertTrue(b15);		
			boolean b17=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/o_0c0a24112bd5.jpg"), "o_0c0a24112bd5.jpg",true);
			Assert.assertTrue(b17);			
			boolean b19=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/ScalaTutorial.pdf"), "ScalaTutorial.pdf",true);
			Assert.assertTrue(b19);			
			boolean b21=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/Typophone_4_by_Angelman8.jpg"), "Typophone_4_by_Angelman8.jpg",true);
			Assert.assertTrue(b21);		
			boolean b23=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/vaadin621.pdf"), "vaadin621.pdf",true);
			Assert.assertTrue(b23);					
			List<BinaryContent> bcol=coh.getBinaryContents(folderCo);			
			Assert.assertEquals(12, bcol.size(),"Added binaryContents number should be 12");				
			
			File f11=new File("testTextContentFile/accents.txt");
			boolean b1a=coh.addTextContent(folderCo,f11, "accents.txt_DECS",true);
			Assert.assertTrue(b1a);				
			boolean b3a=coh.addTextContent(folderCo,new File("testTextContentFile/AINSI.txt"), "AINSI.txt",true);
			Assert.assertTrue(b3a);			
			boolean b5a=coh.addTextContent(folderCo,new File("testTextContentFile/BigEndian.txt"), "BigEndian.txt",true);
			Assert.assertTrue(b5a);			
			boolean b7a=coh.addTextContent(folderCo,new File("testTextContentFile/ChineseContent.txt"), "ChineseContent.txt",true);
			Assert.assertTrue(b7a);			
			boolean b9a=coh.addTextContent(folderCo,new File("testTextContentFile/Unicode.txt"), "Unicode.txt",true);
			Assert.assertTrue(b9a);			
			boolean b11a=coh.addTextContent(folderCo,new File("testTextContentFile/UTF8.txt"), "UTF8.txt",true);
			Assert.assertTrue(b11a);			
			boolean b13a=coh.addTextContent(folderCo,new File("testTextContentFile/UTF8BOM.txt"), "UTF8BOM.txt",true);
			Assert.assertTrue(b13a);			
			boolean b15a=coh.addTextContent(folderCo,new File("testTextContentFile/UTF-8-test.txt"), "UTF-8-test.txt",true);
			Assert.assertTrue(b15a);			
			List<TextContent> bcola=coh.getTextContents(folderCo);			
			Assert.assertEquals(8, bcola.size(),"Added binaryContents number should be 8");	
			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during initContentQueryHelperTestContentObject");
		}			
	}
	
	@AfterClass
	public void removeContentQueryHelperTestTestContentObject(){
		System.out.println("removeContentQueryHelperTestTestContentObject");
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);			
			boolean rmExist1=cs.removeRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);			
			Assert.assertTrue(rmExist1);				
			cs.closeContentSpace();	

		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during removeContentQueryHelperTestTestContentObject");
		}			
	}
	
	@Test	
	public void testSelectBinaryContentsByMimeType(){ ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			RootContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);		
			BaseContentObject folderCo=rco.getSubContentObject("contentQueryFolder");
			Assert.assertNotNull(folderCo);
			List<BinaryContent> bcl=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByMimeType(folderCo,"jp");				
			Assert.assertTrue(bcl.size()>0);
			for(BinaryContent bc:bcl){		
				Assert.assertTrue(bc.getMimeType().contains("jp"));
			}			
			List<BinaryContent> bcl2=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByMimeType(folderCo,"msword");
			Assert.assertTrue(bcl2.size()>0);
			for(BinaryContent bc:bcl2){		
				Assert.assertTrue(bc.getContentName().equals("CommonBase Event_SituationData_V20.doc"));
			}			
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testSelectBinaryContentsByMimeType");
		}		
	}
	
	@Test(dependsOnMethods = { "testSelectBinaryContentsByMimeType" })	
	public void testSelectTextContentsByEncoding(){ 
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			RootContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);		
			BaseContentObject folderCo=rco.getSubContentObject("contentQueryFolder");
			Assert.assertNotNull(folderCo);
			List<TextContent> bcl2=ContentComponentFactory.getContentQueryHelper().selectTextContentsByEncoding(folderCo,"U");	
			Assert.assertTrue(bcl2.size()>0);
			for(TextContent bc:bcl2){	
				Assert.assertTrue(bc.getEncoding().contains("U"));
			}	
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testSelectTextContentsByEncoding");
		}		
	}
	
	@Test(dependsOnMethods = { "testSelectTextContentsByEncoding" })	
	public void testSelectTextContentsByMimeType(){ 
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			RootContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);		
			BaseContentObject folderCo=rco.getSubContentObject("contentQueryFolder");
			Assert.assertNotNull(folderCo);
			List<TextContent> bcl3=ContentComponentFactory.getContentQueryHelper().selectTextContentsByMimeType(folderCo,"a");
			Assert.assertTrue(bcl3.size()>0);
			for(TextContent bc:bcl3){
				Assert.assertTrue(bc.getMimeType().contains("a"));
			}
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testSelectTextContentsByMimeType");
		}		
	}
	
	@Test(dependsOnMethods = { "testSelectTextContentsByMimeType" })	
	public void testSelectTextContentsByTitle(){ 
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			RootContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);		
			BaseContentObject folderCo=rco.getSubContentObject("contentQueryFolder");
			Assert.assertNotNull(folderCo);
			List<TextContent> bcl3=ContentComponentFactory.getContentQueryHelper().selectTextContentsByTitle(folderCo,"a");
			Assert.assertTrue(bcl3.size()>0);
			for(TextContent bc:bcl3){
				Assert.assertTrue(bc.getContentName().contains("a"));
			}
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testSelectTextContentsByTitle");
		}		
	}
	
	@Test(dependsOnMethods = { "testSelectTextContentsByTitle" })	
	public void testSelectBinaryContentsByTitle(){ 
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			RootContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);		
			BaseContentObject folderCo=rco.getSubContentObject("contentQueryFolder");
			Assert.assertNotNull(folderCo);
			List<BinaryContent> bcl3=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByTitle(folderCo,"3");
			Assert.assertTrue(bcl3.size()>0);
			for(BinaryContent bc:bcl3){
				Assert.assertTrue(bc.getContentName().contains("3"));
			}
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testSelectBinaryContentsByTitle");
		}		
	}
	
	@Test(dependsOnMethods = { "testSelectBinaryContentsByTitle" })	
	public void testSelectBinaryContentsByFullTextSearch(){ 
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			RootContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);		
			BaseContentObject folderCo=rco.getSubContentObject("contentQueryFolder");
			Assert.assertNotNull(folderCo);
			List<BinaryContent> bcl3=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByFullTextSearch(folderCo,"Heather Kreger");
			for(BinaryContent bc:bcl3){
				Assert.assertTrue(bc.getContentName().equals("CommonBase Event_SituationData_V20.doc"));
			}
			List<BinaryContent> bcl31=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByFullTextSearch(folderCo,"RESTful Web Services");
			for(BinaryContent bc:bcl31){
				Assert.assertTrue(bc.getContentName().equals("ScalaTutorial.pdf"));
			}
			List<BinaryContent> bcl32=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByFullTextSearch(folderCo,"�¼�����ʹ�����ˡ���Լ����ʱ�䡱");
			for(BinaryContent bc:bcl32){
				Assert.assertTrue(bc.getContentName().equals("ChineseContent.txt"));
			}
			List<BinaryContent> bcl33=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByFullTextSearch(folderCo,"Application Architecture for Vaadin Applications");
			for(BinaryContent bc:bcl33){
				Assert.assertTrue(bc.getContentName().equals("vaadin621.pdf"));
			}			
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testSelectBinaryContentsByFullTextSearch");
		}		
	}
}
