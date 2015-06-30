package com.viewfunction.contentRepository.testNGTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.*;

import com.viewfunction.contentRepository.contentBureau.BaseRootContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRRootContentObjectImpl;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class TestNG_RootContentObjectTestCase {

	@BeforeClass 
	public void initTestingRootContentObjects(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			Assert.assertNotNull(cs);			
			RootContentObject rco=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			cs.addRootContentObject(rco);			
			RootContentObject addRrco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);			
			Assert.assertNotNull(addRrco);				
			RootContentObject rco0=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+0);
			cs.addRootContentObject(rco0);			
			RootContentObject rco1=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+1);
			cs.addRootContentObject(rco1);			
			RootContentObject rco2=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+2);
			cs.addRootContentObject(rco2);			
			RootContentObject rco3=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+3);
			cs.addRootContentObject(rco3);			
			RootContentObject rco4=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+4);
			cs.addRootContentObject(rco4);			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during initTestingRootContentObject");
		}			
	}
	
	@Test
	public void testGetParentContentSpace(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			Assert.assertNotNull(cs);			
			RootContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);	
			Assert.assertNotNull(rco);			
			Assert.assertEquals(rco.getParentContentSpace().getContentSpaceName(),TestCaseDataConstant.testContentSpaceName,"testContentSpaceName should be the same");							
			Assert.assertEquals(rco.getRootContentObjectID(),TestCaseDataConstant.testRootContentObjectName,"testRootContentObjectName should be the same");			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testGetParentContentSpace");
		}		
	}	
	
	@Test(dependsOnMethods = { "testGetParentContentSpace" })
	public void testGetNeighborRootContentObjects(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			RootContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			List<RootContentObject> rcol=rco.getNeighborRootContentObjects();
			Assert.assertEquals(5,rcol.size(),"List<RootContentObject>'s size should be 5");
			for(RootContentObject crco:rcol){				
				Assert.assertNotNull(crco);
				Assert.assertTrue(crco instanceof RootContentObject);
				Assert.assertTrue(crco instanceof JCRRootContentObjectImpl);
				Assert.assertTrue(crco instanceof BaseRootContentObject);
				Assert.assertEquals(crco.getParentContentSpace().getContentSpaceName(),TestCaseDataConstant.testContentSpaceName,"testContentSpaceName should be the same");
			}		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testGetNeighborRootContentObjects");
		}
	}	
	
	@AfterClass
	public void removeTestingRootContentObjects(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);			
			boolean rmExist1=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertTrue(rmExist1);			
			RootContentObject rmedRco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);	
			Assert.assertNull(rmedRco);			
			boolean rmExist1_0=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+0);
			Assert.assertTrue(rmExist1_0);
			boolean rmExist1_1=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+1);
			Assert.assertTrue(rmExist1_1);
			boolean rmExist1_2=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+2);
			Assert.assertTrue(rmExist1_2);
			boolean rmExist1_3=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+3);
			Assert.assertTrue(rmExist1_3);
			boolean rmExist1_4=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+4);
			Assert.assertTrue(rmExist1_4);			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during removeTestingRootContentObjects");
		}			
	}	
}