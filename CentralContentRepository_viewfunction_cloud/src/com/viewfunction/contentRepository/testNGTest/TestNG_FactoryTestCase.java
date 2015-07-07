package com.viewfunction.contentRepository.testNGTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.*;

import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.LockObject;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureau.VersionObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectPropertyImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRLockObjectImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRRootContentObjectImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRVersionObjectImpl;
import com.viewfunction.contentRepository.util.PerportyHandler;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.BinaryContent;
import com.viewfunction.contentRepository.util.helper.ContentObjectInheritanceChain;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.contentRepository.util.helper.ContentQueryHelper;
import com.viewfunction.contentRepository.util.helper.ObjectCollectionHelper;
import com.viewfunction.contentRepository.util.helper.SecurityOperationHelper;
import com.viewfunction.contentRepository.util.helper.TextContent;
import com.viewfunction.contentRepository.util.helperImpl.JCRBinaryContentImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRContentObjectInheritanceChainImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRContentOperationHelperImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRContentQueryHelperImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRObjectCollectionHelperImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRSecurityOperationHelperImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRTextContentImpl;
import com.viewfunction.contentRepository.util.observation.ContentSpaceEventListener;
import com.viewfunction.contentRepository.util.observationImpl.JCRDefaultContentSpaceEventListenerImpl;
	 
	public class TestNG_FactoryTestCase {	
		
		@BeforeClass 
		public static void initCleanMethod(){		
			System.out.println("Init Unit Test ENV");
		}
		
		@Test
		public void testCreateContentSpaces(){		
			try {				
				ContentSpace cs0=ContentComponentFactory.createContentSpace(TestCaseDataConstant.testContentSpaceName);				
				Assert.assertNotNull(cs0);
				List<String> l=ContentComponentFactory.getRegisteredContentSpace();
				Assert.assertEquals(l.size(),1,"contentspace number should be 1");
				Assert.assertEquals(TestCaseDataConstant.testContentSpaceName,l.get(0));
				ContentSpace cs1=ContentComponentFactory.createContentSpace(TestCaseDataConstant.testContentSpaceName);				
				Assert.assertNull(cs1);
				ContentSpace cs2=ContentComponentFactory.createContentSpace(TestCaseDataConstant.testContentSpaceName+"2");				
				Assert.assertNotNull(cs2);
				List<String> l2=ContentComponentFactory.getRegisteredContentSpace();
				Assert.assertEquals(2,l2.size(),"contentspace number should be 2");
				//In jackrabbit OAK, last added property will returned first
				Assert.assertEquals(TestCaseDataConstant.testContentSpaceName+"2",l2.get(0));				
			} catch (ContentReposityException e) {			
				e.printStackTrace();				
				Assert.fail("got ContentReposityException during createContentSpace");
			}
		}
		
		@Test(dependsOnMethods = { "testCreateContentSpaces" })
		public void testConnectContentSpaces(){	
			ContentSpace cs=null;
			ContentSpace cs2=null;
			try {
				String BUILDIN_ADMINISTRATOR_ACCOUNT=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT);
				String BUILDIN__ADMINISTRATOR_ACCOUNT_PWD=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT_PWD);
				cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
				Assert.assertNotNull(cs);			
				Assert.assertEquals(cs.getContentSpaceName(),TestCaseDataConstant.testContentSpaceName,"testContentSpaceName should be the same");			
				cs.closeContentSpace();	
				
				cs2= ContentComponentFactory.connectContentSpace(BUILDIN_ADMINISTRATOR_ACCOUNT, BUILDIN__ADMINISTRATOR_ACCOUNT_PWD, TestCaseDataConstant.testContentSpaceName);
				Assert.assertNotNull(cs2);			
				Assert.assertEquals(cs2.getContentSpaceName(),TestCaseDataConstant.testContentSpaceName,"testContentSpaceName should be the same");			
				cs2.closeContentSpace();					
			} catch (ContentReposityException e) {			
				e.printStackTrace();
				Assert.fail("got ContentReposityException during connectContentSpace");
				cs.closeContentSpace();	
				cs2.closeContentSpace();	
			}
		}		
		
		@Test(dependsOnMethods = { "testCreateContentSpaces" })
		public void testCreateRootContentObject(){
			RootContentObject rco=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);	
			Assert.assertEquals(rco.getRootContentObjectID(), TestCaseDataConstant.testRootContentObjectName,"testRootContentObjectName should be the same" );	
			Assert.assertTrue(rco instanceof RootContentObject,"Class Type should by RootContentObject");
			Assert.assertTrue(rco instanceof JCRRootContentObjectImpl,"Class Type should by JCRRootContentObjectImpl");
		}
		
		@Test(dependsOnMethods = { "testCreateContentSpaces" })
		public void testCreateContentObject(){
			ContentObject co=ContentComponentFactory.createContentObject();
			Assert.assertNotNull(co);		
			Assert.assertTrue(co instanceof ContentObject,"Class Type should by ContentObject");
			Assert.assertTrue(co instanceof JCRContentObjectImpl,"Class Type should by JCRContentObjectImpl");
		}
		
		@Test(dependsOnMethods = { "testCreateContentSpaces" })
		public void testCreateContentObjectProperty(){
			ContentObjectProperty cop=ContentComponentFactory.createContentObjectProperty();
			Assert.assertNotNull(cop);		
			Assert.assertTrue(cop instanceof ContentObjectProperty,"Class Type should by ContentObjectProperty");
			Assert.assertTrue(cop instanceof JCRContentObjectPropertyImpl,"Class Type should by JCRContentObjectPropertyImpl");
		}
		
		@Test(dependsOnMethods = { "testCreateContentSpaces" })
		public void testCreateVersionObject(){
			VersionObject cop=ContentComponentFactory.createVersionObject();
			Assert.assertNotNull(cop);		
			Assert.assertTrue(cop instanceof VersionObject,"Class Type should by VersionObject");
			Assert.assertTrue(cop instanceof JCRVersionObjectImpl,"Class Type should by JCRVersionObjectImpl");
		}
		
		@Test(dependsOnMethods = { "testCreateContentSpaces" })
		public void testCreateContentOperationHelper(){
			ContentOperationHelper cop=ContentComponentFactory.getContentOperationHelper();
			Assert.assertNotNull(cop);		
			Assert.assertTrue(cop instanceof ContentOperationHelper,"Class Type should by ContentOperationHelper");
			Assert.assertTrue(cop instanceof JCRContentOperationHelperImpl,"Class Type should by JCRContentOperationHelperImpl");
		}	
		
		@Test(dependsOnMethods = { "testCreateContentSpaces" })
		public void testCreategetObjectOperationHelper(){
			ObjectCollectionHelper cop=ContentComponentFactory.getObjectOperationHelper();
			Assert.assertNotNull(cop);		
			Assert.assertTrue(cop instanceof ObjectCollectionHelper,"Class Type should by ObjectOperationHelper");
			Assert.assertTrue(cop instanceof JCRObjectCollectionHelperImpl,"Class Type should by JCRObjectOperationHelperImpl");
		}	
		
		@Test(dependsOnMethods = { "testCreateContentSpaces" })
		public void testCreateBinaryContent(){
			BinaryContent cop=ContentComponentFactory.createBinaryContentObject();
			Assert.assertNotNull(cop);		
			Assert.assertTrue(cop instanceof BinaryContent,"Class Type should by BinaryContent");
			Assert.assertTrue(cop instanceof JCRBinaryContentImpl,"Class Type should by JCRBinaryContentImpl");
		}	

		@Test(dependsOnMethods = { "testCreateContentSpaces" })
		public void testCreateTextContent(){
			TextContent cop=ContentComponentFactory.createTextContentObject();
			Assert.assertNotNull(cop);		
			Assert.assertTrue(cop instanceof TextContent,"Class Type should by TextContent");
			Assert.assertTrue(cop instanceof JCRTextContentImpl,"Class Type should by JCRTextContentImpl");
		}
		
		@Test(dependsOnMethods = { "testCreateContentSpaces" })
		public void testCreateContentObjectInheritanceChain(){
			ContentObjectInheritanceChain cop=ContentComponentFactory.createContentObjectInheritanceChain();
			Assert.assertNotNull(cop);		
			Assert.assertTrue(cop instanceof ContentObjectInheritanceChain,"Class Type should by ContentObjectInheritanceChain");
			Assert.assertTrue(cop instanceof JCRContentObjectInheritanceChainImpl,"Class Type should by JCRContentObjectInheritanceChainImpl");
		}
		
		@Test(dependsOnMethods = { "testCreateContentObjectInheritanceChain" })
		public void testGetSecurityOperationHelper(){
			SecurityOperationHelper soh=ContentComponentFactory.getSecurityOperationHelper();
			Assert.assertNotNull(soh);		
			Assert.assertTrue(soh instanceof SecurityOperationHelper,"Class Type should by SecurityOperationHelper");
			Assert.assertTrue(soh instanceof JCRSecurityOperationHelperImpl,"Class Type should by JCRSecurityOperationHelperImpl");
		}
		
		@Test(dependsOnMethods = { "testGetSecurityOperationHelper" })
		public void testCreateLockObject(){
			LockObject lo=ContentComponentFactory.createLockObject();
			Assert.assertNotNull(lo);		
			Assert.assertTrue(lo instanceof LockObject,"Class Type should by LockObject");
			Assert.assertTrue(lo instanceof JCRLockObjectImpl,"Class Type should by JCRLockObjectImpl");
		}
		
		@Test(dependsOnMethods = { "testCreateLockObject" })
		public void testGetContentQueryHelper(){
			ContentQueryHelper cqh=ContentComponentFactory.getContentQueryHelper();
			Assert.assertNotNull(cqh);		
			Assert.assertTrue(cqh instanceof ContentQueryHelper,"Class Type should by ContentQueryHelper");
			Assert.assertTrue(cqh instanceof JCRContentQueryHelperImpl,"Class Type should by JCRContentQueryHelperImpl");
		}
		
		@Test(dependsOnMethods = { "testGetContentQueryHelper" })
		public void testCreateContentSpaceEventListener(){
			ContentSpaceEventListener csel=ContentComponentFactory.createContentSpaceEventListener(ContentSpaceEventListener.DEFAULT_CONTENTSPACE_EVENTLISTENER);
			Assert.assertNotNull(csel);		
			Assert.assertTrue(csel instanceof ContentSpaceEventListener,"Class Type should by ContentSpaceEventListener");
			Assert.assertTrue(csel instanceof JCRDefaultContentSpaceEventListenerImpl,"Class Type should by JCRDefaultContentSpaceEventListenerImpl");
			ContentSpaceEventListener cse2=ContentComponentFactory.createContentSpaceEventListener(1000000);
			Assert.assertNull(cse2);		
		}
		
		@AfterClass
		public void tearDown() throws Exception {
			System.out.println("Clean Unit Test ENV");
		}
}