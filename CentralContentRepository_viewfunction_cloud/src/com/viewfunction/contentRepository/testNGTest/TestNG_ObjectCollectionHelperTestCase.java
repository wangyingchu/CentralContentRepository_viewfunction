package com.viewfunction.contentRepository.testNGTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.*;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.ContentObjectInheritanceChain;
import com.viewfunction.contentRepository.util.helper.ObjectCollectionHelper;

public class TestNG_ObjectCollectionHelperTestCase {

	@BeforeClass 
	public void initObjectColleationHelperTestContentObject(){
		System.out.println("initObjectColleationHelperTestContentObject");
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			Assert.assertNotNull(cs);			
			RootContentObject rco=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.ObjectCollectionHelperTestContentObject);
			cs.addRootContentObject(rco);			
			RootContentObject addedRco=cs.getRootContentObject(TestCaseDataConstant.ObjectCollectionHelperTestContentObject);			
			Assert.assertNotNull(addedRco);				
			BaseContentObject childContentObject1=rco.addSubContentObject("childContentObject1", null, true);			
			BaseContentObject childContentObject2=childContentObject1.addSubContentObject("childContentObject2", null, true);			
			BaseContentObject childContentObject3=childContentObject2.addSubContentObject("childContentObject3", null, true);
			Assert.assertNotNull(childContentObject1);	
			Assert.assertNotNull(childContentObject2);	
			Assert.assertNotNull(childContentObject3);	
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during initObjectColleationHelperTestContentObject");
		}			
	}
	
	@AfterClass
	public void removeObjectCollectionHelperTestContentObject(){
		System.out.println("removeObjectCollectionHelperTestContentObject");
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);			
			boolean rmExist1=cs.removeRootContentObject(TestCaseDataConstant.ObjectCollectionHelperTestContentObject);			
			Assert.assertTrue(rmExist1);				
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during removeObjectCollectionHelperTestContentObject");
		}			
	}
	
	@Test
	public void testGetParentContentObjectsChain(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ObjectCollectionHelperTestContentObject);
			Assert.assertNotNull(rco);			
			ObjectCollectionHelper ooh=	ContentComponentFactory.getObjectOperationHelper();
			Assert.assertNotNull(ooh);			
			
			BaseContentObject childContentObject1=rco.getSubContentObjects("childContentObject1").get(0);
			Assert.assertNotNull(childContentObject1);	
			BaseContentObject childContentObject2=childContentObject1.getSubContentObjects("childContentObject2").get(0);
			Assert.assertNotNull(childContentObject2);	
			BaseContentObject childContentObject3=childContentObject2.getSubContentObjects("childContentObject3").get(0);
			Assert.assertNotNull(childContentObject3);	
			
			ContentObjectInheritanceChain coic1=ooh.getParentContentObjectsChain(cs, childContentObject2);
			Assert.assertNotNull(coic1);			
			String p1=coic1.getContentObjectSpacePath();
			int d1=coic1.getInheritanceDepth();
			List<BaseContentObject> c1=coic1.getParentContentObjectChain();
			RootContentObject r1=coic1.getRootContentObject();			
			Assert.assertNotNull(p1);
			Assert.assertNotNull(d1);
			Assert.assertNotNull(c1);
			Assert.assertNotNull(r1);			
			
			String path="/"+TestCaseDataConstant.ObjectCollectionHelperTestContentObject+"/childContentObject1"+"/childContentObject2";
			Assert.assertEquals(path,p1);			
			Assert.assertEquals(3,d1);				
			Assert.assertEquals(1,c1.size());			
			
			for(int i=0;i<c1.size();i++){				
				Assert.assertEquals("childContentObject"+(i+1),c1.get(i).getContentObjectName());
			}			
			Assert.assertEquals(TestCaseDataConstant.ObjectCollectionHelperTestContentObject,r1.getRootContentObjectID());		
			
			ContentObjectInheritanceChain coic2=ooh.getParentContentObjectsChain(cs, childContentObject3);			
			Assert.assertNotNull(coic2);	
			String p2=coic2.getContentObjectSpacePath();
			int d2=coic2.getInheritanceDepth();
			List<BaseContentObject> c2=coic2.getParentContentObjectChain();
			RootContentObject r2=coic2.getRootContentObject();			
			Assert.assertNotNull(p2);
			Assert.assertNotNull(d2);
			Assert.assertNotNull(c2);
			Assert.assertNotNull(r2);	
			
			String path2="/"+TestCaseDataConstant.ObjectCollectionHelperTestContentObject+"/childContentObject1"+"/childContentObject2"+"/childContentObject3";
			Assert.assertEquals(path2,p2);			
			Assert.assertEquals(4,d2);				
			Assert.assertEquals(2,c2.size());		
			
			for(int i=0;i<c2.size();i++){				
				Assert.assertEquals("childContentObject"+(i+1),c2.get(i).getContentObjectName());
			}
			
			Assert.assertEquals(TestCaseDataConstant.ObjectCollectionHelperTestContentObject,r2.getRootContentObjectID());
			
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testGetParentContentObjectsChain");
		}		
	}	
	
	@Test
	public void testmoveContentObject(){
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.ObjectCollectionHelperTestContentObject);
			Assert.assertNotNull(rco);			
			ObjectCollectionHelper ooh=	ContentComponentFactory.getObjectOperationHelper();
			Assert.assertNotNull(ooh);	
			
			BaseContentObject pco1=rco.addSubContentObject("oldParentObj", null, true);
			BaseContentObject cco1=pco1.addSubContentObject("childObj", null, true);			
			BaseContentObject pco2=rco.addSubContentObject("newParentObj", null, true);					

			int occ0=pco1.getSubContentObjects("childObj").size();			
			Assert.assertEquals(1,occ0);			
			RootContentObject nrco=ContentComponentFactory.createRootContentObject("testRootContentObject");
			RootContentObject rco2=cs.addRootContentObject(nrco);			
			boolean mrcr=ooh.moveContentObject(rco, rco2, true);
			Assert.assertFalse(mrcr);			
			
			boolean mcr=ooh.moveContentObject(cco1, pco2, true);
			Assert.assertTrue(mcr);			
			int occo=pco1.getSubContentObjects("childObj").size();
			Assert.assertEquals(0,occo);		
			int ncco=pco2.getSubContentObjects("childObj").size();
			Assert.assertEquals(1,ncco);
			
			Assert.assertTrue(cco1.getCurrentVersion().getCurrentVersionLabels()[0].contains("moved from oldParentObj to newParentObj"));	
			Assert.assertTrue(pco1.getCurrentVersion().getCurrentVersionLabels()[0].contains("sub ContentObject oldParentObj moved to newParentObj"));
			Assert.assertTrue(pco2.getCurrentVersion().getCurrentVersionLabels()[0].contains("sub ContentObject oldParentObj moved from oldParentObj"));			
	
			BaseContentObject ncco2=pco2.getSubContentObjects("childObj").get(0);
			Assert.assertEquals("childObj",ncco2.getContentObjectName());			
			
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testmoveContentObject");
		}		
	}	
}
