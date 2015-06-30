package com.viewfunction.contentRepository.testNGTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.*;

import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureau.VersionObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class TestNG_VersionObjectTestCase {
	@BeforeClass 
	public void initTestingVersionObjects(){
		System.out.println("initTestingVersionObjects");
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			Assert.assertNotNull(cs);			
			RootContentObject rco=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.versionObjectTestContentObject);
			cs.addRootContentObject(rco);			
			RootContentObject addedRco=cs.getRootContentObject(TestCaseDataConstant.versionObjectTestContentObject);			
			Assert.assertNotNull(addedRco);				
			addedRco.addProperty("propertFor_v1", "propertFor_v1_value", true);//1.0
			addedRco.addProperty("propertFor_v2", "propertFor_v2_value", true);//1.1
			addedRco.addProperty("propertFor_v3", "propertFor_v3_value", true);//1.2
			addedRco.addProperty("propertFor_v4", "propertFor_v4_value", true);//1.3
			addedRco.addProperty("propertFor_v5", "propertFor_v5_value", true);//1.4
			addedRco.addProperty("propertFor_v6", "propertFor_v6_value", true);//1.5
			addedRco.addProperty("propertFor_v7", "propertFor_v7_value", true);//1.6
			addedRco.addProperty("propertFor_v8", "propertFor_v8_value", true);//1.7
			addedRco.addProperty("propertFor_v9", "propertFor_v9_value", true);//1.8
			addedRco.addProperty("propertFor_v10", "propertFor_v10_value", true);//1.9		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during initTestingVersionObjects");
		}			
	}
	
	@AfterClass
	public void removeTestingVersionObjects(){
		System.out.println("removeTestingVersionObjects");
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);			
			boolean rmExist1=cs.removeRootContentObject(TestCaseDataConstant.versionObjectTestContentObject);
			Assert.assertTrue(rmExist1);				
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during removeTestingVersionObjects");
		}			
	}
	
	@Test
	public void testContentObjectVersion(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.versionObjectTestContentObject);
			Assert.assertNotNull(rco);			
			
			VersionObject vo= rco.getCurrentVersion();
			Assert.assertNotNull(vo);
			Assert.assertNotNull(vo.getCurrentVersionCreatedDate().getTime());
			Assert.assertNotNull(vo.getCurrentVersionNumber());			
			Assert.assertEquals("1.9",vo.getCurrentVersionNumber(),"current version number should be 1.9");
			String[] labels=vo.getCurrentVersionLabels();
			Assert.assertNotNull(labels);	
			Assert.assertEquals("V1.9 -> Added New property {propertFor_v10}",labels[0]);	
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testContentObjectVersion");
		}
	}
	
	@Test(dependsOnMethods = { "testContentObjectVersion" })
	public void testContentObjectVersionContent(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.versionObjectTestContentObject);
			Assert.assertNotNull(rco);			
			VersionObject vo= rco.getCurrentVersion();
			Assert.assertNotNull(vo);			
			ContentObject vco=vo.getCurrentContentObject();
			Assert.assertNotNull(vco);			
			ContentObjectProperty cop1=vco.getProperty("propertFor_v2");
			Assert.assertNotNull(cop1);
			Assert.assertEquals("propertFor_v2_value",cop1.getPropertyValue().toString());
			ContentObjectProperty cop2=vco.getProperty("propertFor_v10");
			Assert.assertNotNull(cop2);
			Assert.assertEquals("propertFor_v10_value",cop2.getPropertyValue().toString());				
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testContentObjectVersionContent");
		}		
	}	
	
	@Test(dependsOnMethods = { "testContentObjectVersionContent" })
	public void testPredecessorVersionObject(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.versionObjectTestContentObject);
			Assert.assertNotNull(rco);			
			VersionObject vo= rco.getCurrentVersion();	
			Assert.assertNotNull(vo);
			
			VersionObject vo1=vo.getPredecessorVersionObject();
			Assert.assertNotNull(vo1);
			Assert.assertNotNull(vo1.getCurrentVersionCreatedDate().getTime());
			Assert.assertEquals("1.8",vo1.getCurrentVersionNumber(),"current version number should be 1.8");
			Assert.assertEquals("V1.8 -> Added New property {propertFor_v9}",vo1.getCurrentVersionLabels()[0]);
			ContentObject vco1=vo1.getCurrentContentObject();
			Assert.assertNotNull(vco1);
			
			ContentObjectProperty cop11=vco1.getProperty("propertFor_v2");
			Assert.assertNotNull(cop11);
			Assert.assertEquals("propertFor_v2_value",cop11.getPropertyValue().toString());
			
			ContentObjectProperty cop12=vco1.getProperty("propertFor_v9");
			Assert.assertNotNull(cop12);
			Assert.assertEquals("propertFor_v9_value",cop12.getPropertyValue().toString());			
			
			VersionObject vo2=vo1.getPredecessorVersionObject();
			Assert.assertNotNull(vo2);
			Assert.assertNotNull(vo2.getCurrentVersionCreatedDate().getTime());
			Assert.assertEquals("1.7",vo2.getCurrentVersionNumber(),"current version number should be 1.7");
			Assert.assertEquals("V1.7 -> Added New property {propertFor_v8}",vo2.getCurrentVersionLabels()[0]);			
			
			ContentObject vco2=vo2.getCurrentContentObject();
			Assert.assertNotNull(vco2);			
			ContentObjectProperty cop21=vco2.getProperty("propertFor_v2");			
			Assert.assertNotNull(cop21);
			Assert.assertEquals("propertFor_v2_value",cop21.getPropertyValue().toString());			
			ContentObjectProperty cop22=vco2.getProperty("propertFor_v9");
			Assert.assertNull(cop22);
			ContentObjectProperty cop23=vco2.getProperty("propertFor_v8");
			Assert.assertNotNull(cop23);
			Assert.assertEquals("propertFor_v8_value",cop23.getPropertyValue().toString());			
	
			cs.closeContentSpace();		
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testPredecessorVersionObject");
		}		
	}	
	
	@Test(dependsOnMethods = { "testPredecessorVersionObject" })
	public void testPredecessorVersionObjectChain(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.versionObjectTestContentObject);
			Assert.assertNotNull(rco);		
			
			VersionObject vo= rco.getCurrentVersion();	
			Assert.assertNotNull(vo);
			
			VersionObject curretnvo=vo.getPredecessorVersionObject();			
			int i=Integer.parseInt(vo.getCurrentVersionNumber().substring(2, 3));			
			while(curretnvo!=null){
				i--;				
				Assert.assertNotNull(curretnvo.getCurrentVersionNumber());
				Assert.assertEquals("1."+i,curretnvo.getCurrentVersionNumber());				
				Assert.assertNotNull(curretnvo.getCurrentVersionCreatedDate().getTime());
				Assert.assertNotNull(curretnvo.getCurrentVersionLabels()[0]);
				curretnvo=curretnvo.getPredecessorVersionObject();			
			}		
			cs.closeContentSpace();		
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testPredecessorVersionObjectChain");
		}		
	}
	
	@Test(dependsOnMethods = { "testPredecessorVersionObjectChain" })
	public void testSuccessorVersionObject(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.versionObjectTestContentObject);
			Assert.assertNotNull(rco);
			
			VersionObject vo= rco.getCurrentVersion();//1.9
			VersionObject vo1=vo.getPredecessorVersionObject();//1.8
			VersionObject vo2=vo1.getPredecessorVersionObject();//1.7	
			VersionObject vo3=vo2.getPredecessorVersionObject();//1.6			
			
			VersionObject vs1=vo3.getSuccessorVersionObject();			
			Assert.assertNotNull(vs1);
			Assert.assertNotNull(vs1.getCurrentVersionCreatedDate().getTime());
			Assert.assertEquals("1.7",vs1.getCurrentVersionNumber(),"current version number should be 1.7");
			Assert.assertEquals("V1.7 -> Added New property {propertFor_v8}",vs1.getCurrentVersionLabels()[0]);
			Assert.assertNotNull(vs1.getCurrentContentObject().getProperty("propertFor_v8"));
			Assert.assertNull(vs1.getCurrentContentObject().getProperty("propertFor_v9"));			
			
			VersionObject vs2=vs1.getSuccessorVersionObject();
			Assert.assertNotNull(vs2);
			Assert.assertNotNull(vs2.getCurrentVersionCreatedDate().getTime());
			Assert.assertEquals("1.8",vs2.getCurrentVersionNumber(),"current version number should be 1.8");
			Assert.assertEquals("V1.8 -> Added New property {propertFor_v9}",vs2.getCurrentVersionLabels()[0]);	
			Assert.assertNotNull(vs2.getCurrentContentObject().getProperty("propertFor_v9"));
			Assert.assertNull(vs2.getCurrentContentObject().getProperty("propertFor_v10"));			
			
			VersionObject vs3=vs2.getSuccessorVersionObject();
			Assert.assertNotNull(vs3);
			Assert.assertNotNull(vs3.getCurrentVersionCreatedDate().getTime());
			Assert.assertEquals("1.9",vs3.getCurrentVersionNumber(),"current version number should be 1.9");
			Assert.assertEquals("V1.9 -> Added New property {propertFor_v10}",vs3.getCurrentVersionLabels()[0]);			
			Assert.assertNotNull(vs3.getCurrentContentObject().getProperty("propertFor_v10"));	
		
			cs.closeContentSpace();		
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testSuccessorVersionObject");
		}		
	}	
	
	@Test(dependsOnMethods = { "testSuccessorVersionObject" })
	public void testSuccessorVersionObjectChain(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.versionObjectTestContentObject);
			Assert.assertNotNull(rco);
			
			VersionObject vo= rco.getCurrentVersion();//1.9
			VersionObject vo1=vo.getPredecessorVersionObject();//1.8
			VersionObject vo2=vo1.getPredecessorVersionObject();//1.7	
			VersionObject vo3=vo2.getPredecessorVersionObject();//1.6
			VersionObject vo4=vo3.getPredecessorVersionObject();//1.5		
			
			VersionObject curretnvo=vo4.getSuccessorVersionObject();//1.6			
			int i=Integer.parseInt(curretnvo.getCurrentVersionNumber().substring(2, 3));				
			while(curretnvo!=null){						
				Assert.assertNotNull(curretnvo.getCurrentVersionNumber());
				Assert.assertEquals("1."+i,curretnvo.getCurrentVersionNumber());				
				Assert.assertNotNull(curretnvo.getCurrentVersionCreatedDate().getTime());
				Assert.assertNotNull(curretnvo.getCurrentVersionLabels()[0]);				
				curretnvo=curretnvo.getSuccessorVersionObject();
				++i;
			}
			cs.closeContentSpace();		
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testSuccessorVersionObjectChain");
		}		
	}
	
	@Test(dependsOnMethods = { "testSuccessorVersionObjectChain" })
	public void testGetAllVersionsInSpace(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.versionObjectTestContentObject);
			Assert.assertNotNull(rco);
			List<VersionObject> vol=rco.getCurrentVersion().getAllVersionsInSpace();		
			for(VersionObject vo:vol){				
				Assert.assertNotNull(vo.getCurrentVersionNumber());				
				Assert.assertNotNull(vo.getCurrentVersionCreatedDate().getTime());	
				//0.0 version does not have version labels
				if(!vo.getCurrentVersionNumber().equals("0.0")){
					Assert.assertNotNull(vo.getCurrentVersionLabels()[0]);	
				}					
			}			
			cs.closeContentSpace();		
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testSuccessorVersionObjectChain");
		}		
	}	
}