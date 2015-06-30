package com.viewfunction.contentRepository.testNGTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.*;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.BaseRootContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRRootContentObjectImpl;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class TestNG_ContentSpaceTestCase {
	
	@Test
	public  void testAddRootContentObject(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			Assert.assertNotNull(cs);
			
			RootContentObject rco=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			cs.addRootContentObject(rco);			
			RootContentObject addRrco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);			
			Assert.assertNotNull(addRrco);			
			//System.out.println(addRrco.getParentContentSpace().getContentSpaceName());
			Assert.assertEquals(addRrco.getParentContentSpace().getContentSpaceName(),TestCaseDataConstant.testContentSpaceName,"testContentSpaceName should be the same");			
			//System.out.println(addRrco.getRootContentObjectID());			
			Assert.assertEquals(addRrco.getRootContentObjectID(),TestCaseDataConstant.testRootContentObjectName,"testRootContentObjectName should be the same");			
			RootContentObject rco2=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			RootContentObject rco2Return=cs.addRootContentObject(rco2);			
			Assert.assertNull(rco2Return);			
			
			RootContentObject rco_0=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_0");
			cs.addRootContentObject(rco_0);			
			RootContentObject addRrco_0=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_0");			
			Assert.assertNotNull(addRrco_0);
			
			RootContentObject rco_1=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_1");
			cs.addRootContentObject(rco_1);			
			RootContentObject addRrco_1=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_1");			
			Assert.assertNotNull(addRrco_1);
			
			RootContentObject rco_2=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_2");
			cs.addRootContentObject(rco_2);			
			RootContentObject addRrco_2=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_2");			
			Assert.assertNotNull(addRrco_2);
			
			RootContentObject rco_3=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_3");
			cs.addRootContentObject(rco_3);			
			RootContentObject addRrco_3=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_3");			
			Assert.assertNotNull(addRrco_3);
			
			RootContentObject rco_4=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_4");
			cs.addRootContentObject(rco_4);			
			RootContentObject addRrco_4=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_4");			
			Assert.assertNotNull(addRrco_4);
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testAddRootContentObject");
		}
	}	
	
	@Test(dependsOnMethods = { "testAddRootContentObject" })
	public void testGetRootContentObject(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		ContentSpace cs=null;
		try {			
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			Assert.assertNotNull(cs);
			RootContentObject addRrco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);			
			Assert.assertNotNull(addRrco);			
			Assert.assertEquals(addRrco.getParentContentSpace().getContentSpaceName(),TestCaseDataConstant.testContentSpaceName,"testContentSpaceName should be the same");				
			Assert.assertEquals(addRrco.getRootContentObjectID(),TestCaseDataConstant.testRootContentObjectName,"testRootContentObjectName should be the same");
			
			RootContentObject addRrco_0=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_0");			
			Assert.assertNotNull(addRrco_0);			
			Assert.assertEquals(addRrco_0.getParentContentSpace().getContentSpaceName(),TestCaseDataConstant.testContentSpaceName,"testContentSpaceName should be the same");				
			Assert.assertEquals(addRrco_0.getRootContentObjectID(),TestCaseDataConstant.testRootContentObjectName+"_0","testRootContentObjectName should be the same");
			
			RootContentObject addRrco_1=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_1");			
			Assert.assertNotNull(addRrco_1);			
			Assert.assertEquals(addRrco_1.getParentContentSpace().getContentSpaceName(),TestCaseDataConstant.testContentSpaceName,"testContentSpaceName should be the same");				
			Assert.assertEquals(addRrco_1.getRootContentObjectID(),TestCaseDataConstant.testRootContentObjectName+"_1","testRootContentObjectName should be the same");
			
			RootContentObject addRrco_2=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_2");			
			Assert.assertNotNull(addRrco_2);			
			Assert.assertEquals(addRrco_2.getParentContentSpace().getContentSpaceName(),TestCaseDataConstant.testContentSpaceName,"testContentSpaceName should be the same");				
			Assert.assertEquals(addRrco_2.getRootContentObjectID(),TestCaseDataConstant.testRootContentObjectName+"_2","testRootContentObjectName should be the same");
			
			RootContentObject addRrco_3=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_3");			
			Assert.assertNotNull(addRrco_3);			
			Assert.assertEquals(addRrco_3.getParentContentSpace().getContentSpaceName(),TestCaseDataConstant.testContentSpaceName,"testContentSpaceName should be the same");				
			Assert.assertEquals(addRrco_3.getRootContentObjectID(),TestCaseDataConstant.testRootContentObjectName+"_3","testRootContentObjectName should be the same");
			
			RootContentObject addRrco_4=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_4");			
			Assert.assertNotNull(addRrco_4);			
			Assert.assertEquals(addRrco_4.getParentContentSpace().getContentSpaceName(),TestCaseDataConstant.testContentSpaceName,"testContentSpaceName should be the same");				
			Assert.assertEquals(addRrco_4.getRootContentObjectID(),TestCaseDataConstant.testRootContentObjectName+"_4","testRootContentObjectName should be the same");		
		
			cs.closeContentSpace();
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testGetRootContentObject");
		}	
	}
	
	@Test(dependsOnMethods = { "testAddRootContentObject" })
	public void testGetRootContentObjects(){
		ContentSpace cs=null;
		try {			
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			List<RootContentObject> rcol=cs.getRootContentObjects();	
			Assert.assertEquals(rcol.size(),6,"List<RootContentObject>'s size should be 6");
			for(RootContentObject currentRC : rcol){				
				Assert.assertNotNull(currentRC);
				Assert.assertTrue(currentRC instanceof RootContentObject);
				Assert.assertTrue(currentRC instanceof JCRRootContentObjectImpl);
				Assert.assertTrue(currentRC instanceof BaseRootContentObject);
				Assert.assertEquals(currentRC.getParentContentSpace().getContentSpaceName(),TestCaseDataConstant.testContentSpaceName,"testContentSpaceName should be the same");	
			}
			cs.closeContentSpace();
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testGetRootContentObjects");
		}	
	}	
	
	@Test(dependsOnMethods = { "testGetRootContentObjects" })	
	public  void testRemoveRootContentObject(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);			
			boolean rmNotExist=cs.removeRootContentObject("NotExistRootContent");
			Assert.assertFalse(rmNotExist);		
			boolean rmExist1=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertTrue(rmExist1);			
			RootContentObject rmedRco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);	
			Assert.assertNull(rmedRco);	
			
			boolean rmExist1_0=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_0");
			Assert.assertTrue(rmExist1_0);
			boolean rmExist1_1=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_1");
			Assert.assertTrue(rmExist1_1);
			boolean rmExist1_2=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_2");
			Assert.assertTrue(rmExist1_2);
			boolean rmExist1_3=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_3");
			Assert.assertTrue(rmExist1_3);
			boolean rmExist1_4=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+"_4");
			Assert.assertTrue(rmExist1_4);			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testAddRootContentObject");
		}				
	}
	
	@Test(dependsOnMethods = { "testRemoveRootContentObject" })	
	public  void testGetContentObjectByAbsPath(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);			
			RootContentObject rco=ContentComponentFactory.createRootContentObject("RootContentSpaceForGetContentObjectByAbsPathTest");
			cs.addRootContentObject(rco);
			rco.addSubContentObject("path1", null, false).addSubContentObject("path2", null, false);
			String absPath="/RootContentSpaceForGetContentObjectByAbsPathTest/path1/path2";
			BaseContentObject bco=cs.getContentObjectByAbsPath(absPath);
			Assert.assertNotNull(bco);
			Assert.assertEquals(bco.getContentObjectName(),"path2");
			String absPath1="/RootContentSpaceForGetContentObjectByAbsPathTest/path1";
			BaseContentObject bco1=cs.getContentObjectByAbsPath(absPath1);
			Assert.assertNotNull(bco1);
			Assert.assertEquals(bco1.getContentObjectName(),"path1");			
			cs.removeRootContentObject("RootContentSpaceForGetContentObjectByAbsPathTest");
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testGetContentObjectByAbsPath");
		}				
	}	
}
