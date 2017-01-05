package com.viewfunction.contentRepository.testNGTest;

import java.io.File;
import java.util.Date;
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
	public void testSelectBinaryContentsByMimeType(){ 
		ContentSpace cs=null;
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
	
	@Test(dependsOnMethods = { "testSelectBinaryContentsByFullTextSearch" })	
	public void testSelectContentsByOperatorAndDesc(){ 
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			RootContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);			
			rco.addSubContentObject("contentQueryFolder02", null, false);	
			BaseContentObject folderCo=rco.getSubContentObject("contentQueryFolder02");
			Assert.assertNotNull(folderCo);
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();	
			Assert.assertNotNull(coh);	
			
			boolean b=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/01300000029584120462690206338.jpg"), "jpg file1_DECS","creator01",true);
			Assert.assertTrue(b);					
			boolean b3=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/3380232995318967763.jpg"), "jpg file2_DECS","creator02",true);
			Assert.assertTrue(b3);		
			boolean b5=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/6_roger.jpg"), "jpg file3_DECS","creator03",true);
			Assert.assertTrue(b5);
			File f2=new File("testBinaryContentFile/Beyonce - Halo10M.mp3");
			boolean b7=coh.addBinaryContent(folderCo,f2, "mp3 file1_DECS","creator01",true);
			Assert.assertTrue(b7);			
			boolean b9=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/CommonBase Event_SituationData_V20.doc"), "doc file1_DECS","creator01",true);
			Assert.assertTrue(b9);			
			boolean b11=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/Communications_Management_wp_FINAL.pdf"), "pdf file1_DECS","creator01",true);
			Assert.assertTrue(b11);			
			
			List<BinaryContent> bcl1=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByCreator(folderCo,"creator");
			Assert.assertEquals(bcl1.size(), 6);			
			
			List<BinaryContent> bcl2=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByCreator(folderCo,"creator01");
			Assert.assertEquals(bcl2.size(), 4);
			for(BinaryContent bc:bcl2){
				Assert.assertTrue(bc.getCreatedBy().equals("creator01"));
			}
			
			List<BinaryContent> bcl3=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByCreator(folderCo,"creator02");
			Assert.assertEquals(bcl3.size(), 1);
			for(BinaryContent bc:bcl3){
				Assert.assertTrue(bc.getCreatedBy().equals("creator02"));
				Assert.assertTrue(bc.getContentName().equals("3380232995318967763.jpg"));				
			}
			
			List<BinaryContent> bcl4=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByCreator(folderCo,"creator03");
			Assert.assertEquals(bcl4.size(), 1);
			for(BinaryContent bc:bcl4){
				Assert.assertTrue(bc.getCreatedBy().equals("creator03"));
				Assert.assertTrue(bc.getContentDescription().equals("jpg file3_DECS"));				
			}
			
			List<BinaryContent> bcl5=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByDescription(folderCo,"_DECS");
			Assert.assertEquals(bcl5.size(), 6);
			
			List<BinaryContent> bcl6=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByDescription(folderCo,"pdf file1_DECS");
			Assert.assertEquals(bcl6.size(), 1);
			for(BinaryContent bc:bcl6){
				Assert.assertTrue(bc.getCreatedBy().equals("creator01"));
				Assert.assertTrue(bc.getContentName().equals("Communications_Management_wp_FINAL.pdf"));				
			}			
			
			boolean b2=coh.updateBinaryContent(folderCo, "01300000029584120462690206338.jpg",new File("testBinaryContentFile/IMG_1716.JPG"), "jpg file1_DECS_UPD", "updater01",true);
			boolean b4=coh.updateBinaryContent(folderCo, "3380232995318967763.jpg",new File("testBinaryContentFile/IMG_1716.JPG"), "jpg file2_DECS_UPD", "updater01",true);
			boolean b6=coh.updateBinaryContent(folderCo, "6_roger.jpg",new File("testBinaryContentFile/IMG_1716.JPG"), "jpg file3_DECS_UPD", "updater02",true);
			
			List<BinaryContent> bcl7=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByLastUpdater(folderCo,"updater");
			Assert.assertEquals(bcl7.size(), 3);
			
			List<BinaryContent> bcl8=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByLastUpdater(folderCo,"updater01");
			Assert.assertEquals(bcl8.size(), 2);
			for(BinaryContent bc:bcl8){
				Assert.assertTrue(bc.getLastModifiedBy().equals("updater01"));
			}
			
			List<BinaryContent> bcl9=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByLastUpdater(folderCo,"updater02");
			Assert.assertEquals(bcl9.size(), 1);
			for(BinaryContent bc:bcl9){
				Assert.assertTrue(bc.getLastModifiedBy().equals("updater02"));
				Assert.assertTrue(bc.getContentDescription().equals("jpg file3_DECS_UPD"));				
			}	
			
			boolean b8=coh.addTextContent(folderCo,new File("testTextContentFile/AINSI.txt"), "txt file1_DECS","creator01",true);
			Assert.assertTrue(b8);
			boolean b10=coh.addTextContent(folderCo,new File("testTextContentFile/BigEndian.txt"), "txt file2_DECS","creator02",true);
			Assert.assertTrue(b10);
			boolean b12=coh.addTextContent(folderCo,new File("testTextContentFile/ChineseContent.txt"), "txt file3_DECS","creator03",true);
			Assert.assertTrue(b12);
			boolean b14=coh.addTextContent(folderCo,new File("testTextContentFile/Unicode.txt"), "txt file4_DECS","creator01",true);
			Assert.assertTrue(b14);
			boolean b16=coh.addTextContent(folderCo,new File("testTextContentFile/UTF8.txt"), "txt file5_DECS","creator01",true);
			Assert.assertTrue(b16);
			boolean b18=coh.addTextContent(folderCo,new File("testTextContentFile/UTF8BOM.txt"), "txt file6_DECS","creator01",true);
			Assert.assertTrue(b18);
			boolean b20=coh.addTextContent(folderCo,new File("testTextContentFile/UTF-8-test.txt"), "txt file7_DEC","creator01",true);
			Assert.assertTrue(b20);				
			
			List<TextContent> bcl10=ContentComponentFactory.getContentQueryHelper().selectTextContentsByCreator(folderCo,"creator");
			Assert.assertEquals(bcl10.size(), 7);			
			
			List<TextContent> bcl11=ContentComponentFactory.getContentQueryHelper().selectTextContentsByCreator(folderCo,"creator01");
			Assert.assertEquals(bcl11.size(), 5);
			for(BinaryContent bc:bcl11){
				Assert.assertTrue(bc.getCreatedBy().equals("creator01"));
			}
			
			List<TextContent> bcl12=ContentComponentFactory.getContentQueryHelper().selectTextContentsByCreator(folderCo,"creator02");
			Assert.assertEquals(bcl12.size(), 1);
			for(BinaryContent bc:bcl12){
				Assert.assertTrue(bc.getCreatedBy().equals("creator02"));
				Assert.assertTrue(bc.getContentName().equals("BigEndian.txt"));
			}
			
			List<TextContent> bcl13=ContentComponentFactory.getContentQueryHelper().selectTextContentsByCreator(folderCo,"creator03");
			Assert.assertEquals(bcl13.size(), 1);
			for(BinaryContent bc:bcl13){
				Assert.assertTrue(bc.getCreatedBy().equals("creator03"));
				Assert.assertTrue(bc.getContentDescription().equals("txt file3_DECS"));
			}
			
			List<TextContent> bcl14=ContentComponentFactory.getContentQueryHelper().selectTextContentsByByDescription(folderCo,"DECS");
			Assert.assertEquals(bcl14.size(), 6);	
			
			List<TextContent> bcl15=ContentComponentFactory.getContentQueryHelper().selectTextContentsByByDescription(folderCo,"txt file7_DEC");
			Assert.assertEquals(bcl15.size(), 1);
			for(BinaryContent bc:bcl15){
				Assert.assertTrue(bc.getCreatedBy().equals("creator01"));
				Assert.assertTrue(bc.getContentName().equals("UTF-8-test.txt"));
			}			
			
			boolean b21=coh.updateTextContent(folderCo, "Unicode.txt",new File("testTextContentFile/UTF8BOM.txt"), "file4_DECS", "updater01",true);
			Assert.assertTrue(b21);
			boolean b22=coh.updateTextContent(folderCo, "UTF8.txt",new File("testTextContentFile/UTF8BOM.txt"), "txt file5_DECS", "updater01", true);
			Assert.assertTrue(b22);
			boolean b23=coh.updateTextContent(folderCo, "ChineseContent.txt",new File("testTextContentFile/UTF8BOM.txt"), "txt file3_DECS", "updater02", true);
			Assert.assertTrue(b23);	
			
			List<TextContent> bcl16=ContentComponentFactory.getContentQueryHelper().selectTextContentsByLastUpdater(folderCo,"updater");
			Assert.assertEquals(bcl16.size(), 3);
			
			List<TextContent> bcl17=ContentComponentFactory.getContentQueryHelper().selectTextContentsByLastUpdater(folderCo,"updater01");
			Assert.assertEquals(bcl17.size(), 2);
			for(BinaryContent bc:bcl17){
				Assert.assertTrue(bc.getLastModifiedBy().equals("updater01"));
			}
			
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testSelectBinaryContentsByFullTextSearch");
		}		
	}
	
	@Test(dependsOnMethods = { "testSelectContentsByOperatorAndDesc" })	
	public void testSelectContentsByDate(){ 
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			RootContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ContentQueryHelperTestContentObject);			
			rco.addSubContentObject("contentQueryFolder03", null, false);	
			BaseContentObject folderCo=rco.getSubContentObject("contentQueryFolder03");
			Assert.assertNotNull(folderCo);
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();	
			Assert.assertNotNull(coh);	
			
			Date testStartDate=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean b=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/01300000029584120462690206338.jpg"), "jpg file1_DECS","creator01",true);
			Assert.assertTrue(b);	
			
			Date testMiddleDate01=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean b3=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/3380232995318967763.jpg"), "jpg file2_DECS","creator02",true);
			Assert.assertTrue(b3);
			
			Date testMiddleDate02=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean b5=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/6_roger.jpg"), "jpg file3_DECS","creator03",true);
			Assert.assertTrue(b5);	
			
			Date testMiddleDate03=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean b9=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/CommonBase Event_SituationData_V20.doc"), "doc file1_DECS","creator01",true);
			Assert.assertTrue(b9);	
			
			Date testMiddleDate04=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean b11=coh.addBinaryContent(folderCo,new File("testBinaryContentFile/Communications_Management_wp_FINAL.pdf"), "pdf file1_DECS","creator01",true);
			Assert.assertTrue(b11);			
			
			Date testEndDate=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List<BinaryContent> bcl1=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByCreateDate(folderCo,testStartDate,testEndDate);
			Assert.assertEquals(5,bcl1.size());		
			
			List<BinaryContent> bcl2=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByCreateDate(folderCo,testStartDate,null);
			Assert.assertEquals(5,bcl2.size());
			
			List<BinaryContent> bcl3=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByCreateDate(folderCo,null,testEndDate);
			Assert.assertEquals(5,bcl3.size());
			
			List<BinaryContent> bcl4=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByCreateDate(folderCo,testEndDate,null);
			Assert.assertEquals(0,bcl4.size());
			
			List<BinaryContent> bcl5=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByCreateDate(folderCo,null,testStartDate);
			Assert.assertEquals(0,bcl5.size());
			
			List<BinaryContent> bcl6=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByCreateDate(folderCo,testMiddleDate01,testMiddleDate02);
			Assert.assertEquals(1,bcl6.size());			
			for(BinaryContent bc:bcl6){
				Assert.assertTrue(bc.getCreatedBy().equals("creator02"));				
				Assert.assertTrue(bc.getContentName().equals("3380232995318967763.jpg"));
			}
			
			List<BinaryContent> bcl7=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByCreateDate(folderCo,testMiddleDate02,testMiddleDate04);
			Assert.assertEquals(2,bcl7.size());
			
			Date testUpdateStartDate=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			boolean b2=coh.updateBinaryContent(folderCo, "01300000029584120462690206338.jpg",new File("testBinaryContentFile/IMG_1716.JPG"), "jpg file1_DECS_UPD", "updater01",true);
			
			Date testUpdateMiddleDate=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean b4=coh.updateBinaryContent(folderCo, "3380232995318967763.jpg",new File("testBinaryContentFile/IMG_1716.JPG"), "jpg file2_DECS_UPD", "updater02",true);
			
			Date testUpdateEndDate=new Date();	
			
			List<BinaryContent> bcl8=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByLastUpdateDate(folderCo,testUpdateStartDate,testUpdateEndDate);
			Assert.assertEquals(2,bcl8.size());		
			
			List<BinaryContent> bcl9=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByLastUpdateDate(folderCo,testUpdateStartDate,null);
			Assert.assertEquals(2,bcl9.size());
			
			List<BinaryContent> bcl10=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByLastUpdateDate(folderCo,null,testUpdateEndDate);
			//when a content first added in repository the update date equals the create date
			Assert.assertEquals(5,bcl10.size());
			
			List<BinaryContent> bcl11=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByLastUpdateDate(folderCo,testUpdateMiddleDate,testUpdateEndDate);			
			Assert.assertEquals(1,bcl11.size());
			for(BinaryContent bc:bcl11){
				Assert.assertTrue(bc.getLastModifiedBy().equals("updater02"));				
				Assert.assertTrue(bc.getContentName().equals("3380232995318967763.jpg"));
			}
			
			List<BinaryContent> bcl12=ContentComponentFactory.getContentQueryHelper().selectBinaryContentsByLastUpdateDate(folderCo,testUpdateStartDate,testUpdateMiddleDate);			
			Assert.assertEquals(1,bcl12.size());
			for(BinaryContent bc:bcl12){
				Assert.assertTrue(bc.getLastModifiedBy().equals("updater01"));				
				Assert.assertTrue(bc.getContentName().equals("01300000029584120462690206338.jpg"));
			}
			
			Date testTextContentStartDate=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean b8=coh.addTextContent(folderCo,new File("testTextContentFile/AINSI.txt"), "txt file1_DECS","creator01",true);
			Assert.assertTrue(b8);
			
			Date testTextContentMiddle1Date=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean b10=coh.addTextContent(folderCo,new File("testTextContentFile/BigEndian.txt"), "txt file2_DECS","creator02",true);
			Assert.assertTrue(b10);
			
			Date testTextContentMiddle2Date=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean b12=coh.addTextContent(folderCo,new File("testTextContentFile/ChineseContent.txt"), "txt file3_DECS","creator03",true);
			Assert.assertTrue(b12);
			
			Date testTextContentMiddle3Date=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean b14=coh.addTextContent(folderCo,new File("testTextContentFile/Unicode.txt"), "txt file4_DECS","creator01",true);
			Assert.assertTrue(b14);
			
			Date testTextContentMiddle4Date=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean b16=coh.addTextContent(folderCo,new File("testTextContentFile/UTF8.txt"), "txt file5_DECS","creator01",true);
			Assert.assertTrue(b16);			
			
			Date testTextContentEndDate=new Date();			
			
			List<TextContent> bcl13=ContentComponentFactory.getContentQueryHelper().selectTextContentsByCreateDate(folderCo,testTextContentStartDate,testTextContentEndDate);
			Assert.assertEquals(5,bcl13.size());
			
			List<TextContent> bcl14=ContentComponentFactory.getContentQueryHelper().selectTextContentsByCreateDate(folderCo,testTextContentStartDate,null);
			Assert.assertEquals(5,bcl14.size());	
			
			List<TextContent> bcl15=ContentComponentFactory.getContentQueryHelper().selectTextContentsByCreateDate(folderCo,null,testTextContentEndDate);
			Assert.assertEquals(5,bcl15.size());	
			
			List<TextContent> bcl16=ContentComponentFactory.getContentQueryHelper().selectTextContentsByCreateDate(folderCo,null,testTextContentStartDate);
			Assert.assertEquals(0,bcl16.size());	
			
			List<TextContent> bcl17=ContentComponentFactory.getContentQueryHelper().selectTextContentsByCreateDate(folderCo,testTextContentEndDate,null);
			Assert.assertEquals(0,bcl17.size());	
			
			List<TextContent> bcl18=ContentComponentFactory.getContentQueryHelper().selectTextContentsByCreateDate(folderCo,testTextContentMiddle1Date,testTextContentMiddle2Date);
			Assert.assertEquals(1,bcl18.size());
			for(TextContent bc:bcl18){
				Assert.assertTrue(bc.getCreatedBy().equals("creator02"));				
				Assert.assertTrue(bc.getContentName().equals("BigEndian.txt"));
			}
			
			List<TextContent> bcl19=ContentComponentFactory.getContentQueryHelper().selectTextContentsByCreateDate(folderCo,testTextContentMiddle2Date,testTextContentMiddle4Date);
			Assert.assertEquals(2,bcl19.size());
			
			Date testUpdateTextStartDate=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			boolean b21=coh.updateTextContent(folderCo, "Unicode.txt",new File("testTextContentFile/UTF8BOM.txt"), "file4_DECS", "updater01",true);
			Assert.assertTrue(b21);
			Date testUpdateTextMiddle1Date=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			boolean b22=coh.updateTextContent(folderCo, "UTF8.txt",new File("testTextContentFile/UTF8BOM.txt"), "txt file5_DECS", "updater01", true);
			Assert.assertTrue(b22);
			Date testUpdateTextMiddle21Date=new Date();				
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			boolean b23=coh.updateTextContent(folderCo, "ChineseContent.txt",new File("testTextContentFile/UTF8BOM.txt"), "txt file3_DECS", "updater02", true);
			Assert.assertTrue(b23);	
			Date testUpdateTextEndDate=new Date();			
			
			List<TextContent> bcl20=ContentComponentFactory.getContentQueryHelper().selectTextContentsByLastUpdateDate(folderCo,testUpdateTextStartDate,testUpdateTextEndDate);
			Assert.assertEquals(3,bcl20.size());
			
			List<TextContent> bcl21=ContentComponentFactory.getContentQueryHelper().selectTextContentsByLastUpdateDate(folderCo,testUpdateTextStartDate,null);
			Assert.assertEquals(3,bcl21.size());
			
			List<TextContent> bcl22=ContentComponentFactory.getContentQueryHelper().selectTextContentsByLastUpdateDate(folderCo,null,testUpdateTextEndDate);
			//when a content first added in repository the update date equals the create date
			Assert.assertEquals(5,bcl22.size());
			
			List<TextContent> bcl23=ContentComponentFactory.getContentQueryHelper().selectTextContentsByLastUpdateDate(folderCo,testUpdateTextMiddle1Date,testUpdateTextMiddle21Date);
			Assert.assertEquals(1,bcl23.size());
			for(BinaryContent bc:bcl23){
				Assert.assertTrue(bc.getLastModifiedBy().equals("updater01"));				
				Assert.assertTrue(bc.getContentName().equals("UTF8.txt"));
			}
			
			List<TextContent> bcl24=ContentComponentFactory.getContentQueryHelper().selectTextContentsByLastUpdateDate(folderCo,testUpdateTextMiddle21Date,testUpdateTextEndDate);
			Assert.assertEquals(1,bcl24.size());
			for(BinaryContent bc:bcl24){
				Assert.assertTrue(bc.getLastModifiedBy().equals("updater02"));				
				Assert.assertTrue(bc.getContentName().equals("ChineseContent.txt"));
			}
		
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testSelectBinaryContentsByFullTextSearch");
		}		
	}
}
