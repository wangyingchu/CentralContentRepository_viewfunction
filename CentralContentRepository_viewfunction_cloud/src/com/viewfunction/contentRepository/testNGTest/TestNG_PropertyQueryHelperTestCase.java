package com.viewfunction.contentRepository.testNGTest;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.PerportyHandler;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.exception.ContentReposityRuntimeException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.contentRepository.util.helper.PropertyQueryHelper;

public class TestNG_PropertyQueryHelperTestCase {
	
	@BeforeClass 
	public void initContentQueryHelperTestContentObject(){
		System.out.println("initContentQueryHelperTestContentObject");
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			Assert.assertNotNull(cs);			
			RootContentObject rco=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.PropertyQueryHelperTestContentObject);
			cs.addRootContentObject(rco);			
			RootContentObject addedRco=cs.getRootContentObject(TestCaseDataConstant.PropertyQueryHelperTestContentObject);			
			Assert.assertNotNull(addedRco);	
			BaseContentObject binaryCo=addedRco.addSubContentObject("propertyQueryFolder", null, true);
			Assert.assertNotNull(binaryCo);	
			
			BaseContentObject folderCo=rco.getSubContentObject("propertyQueryFolder");
			Assert.assertNotNull(folderCo);	
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();	
			Assert.assertNotNull(coh);	
			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during initContentQueryHelperTestContentObject");
		}			
	}
	
	@Test	
	public void testSelectContentObjectsByPropertyFullTextSearch(){ 
		String ENABLE_GLOBAL_FULLTEXT_SEARCH;
		try {
			ENABLE_GLOBAL_FULLTEXT_SEARCH = PerportyHandler.getPerportyValue(PerportyHandler.ENABLE_GLOBAL_FULLTEXT_SEARCH);
		} catch (ContentReposityRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
        boolean enableGlobalFullTextSearch=Boolean.parseBoolean(ENABLE_GLOBAL_FULLTEXT_SEARCH);
        if(!enableGlobalFullTextSearch){
        	return;
        }		
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);			
			RootContentObject rco=cs.getRootContentObject(TestCaseDataConstant.PropertyQueryHelperTestContentObject);
			BaseContentObject propertyTestRootCo=rco.getSubContentObject("propertyQueryFolder");
			Assert.assertNotNull(propertyTestRootCo);			
			
			BaseContentObject propertyTestSubCo1=propertyTestRootCo.addSubContentObject("propertyTestSubCo1", null, true);
			BaseContentObject propertyTestSubCo2=propertyTestRootCo.addSubContentObject("propertyTestSubCo2", null, true);
			BaseContentObject propertyTestSubCo3=propertyTestRootCo.addSubContentObject("propertyTestSubCo3", null, true);
			BaseContentObject propertyTestSubCo4=propertyTestRootCo.addSubContentObject("propertyTestSubCo4", null, true);			
			
			propertyTestSubCo1.addProperty("textProp", "hello", true);
			propertyTestSubCo2.addProperty("textProp", "hallo", true);
			propertyTestSubCo3.addProperty("textProp", "helloAhallo", true);
			propertyTestSubCo4.addProperty("textProp", "hello hallo", true);	
			
			
			BaseContentObject propertyTestSubSubCo1=propertyTestSubCo1.addSubContentObject("propertyTestSubSubCo1", null, true);
			propertyTestSubSubCo1.addProperty("textProp", "hello 1234", true);			
			
			PropertyQueryHelper pqh=ContentComponentFactory.getPropertyQueryHelper();			
						
			List<BaseContentObject> bcl1=pqh.selectContentObjectsByPropertyFullTextSearch(propertyTestRootCo, "textProp", "hello", PropertyQueryHelper.SCOPE_Same);			
			Assert.assertEquals(0, bcl1.size());
			
			List<BaseContentObject> bcl2=pqh.selectContentObjectsByPropertyFullTextSearch(propertyTestRootCo, "textProp", "hello", PropertyQueryHelper.SCOPE_Child);			
			Assert.assertEquals(2, bcl2.size());
			
			List<BaseContentObject> bcl4=pqh.selectContentObjectsByPropertyFullTextSearch(propertyTestRootCo, "textProp", "hallo", PropertyQueryHelper.SCOPE_Child);			
			Assert.assertEquals(2, bcl4.size());
			
			List<BaseContentObject> bcl3=pqh.selectContentObjectsByPropertyFullTextSearch(propertyTestRootCo, "textProp", "hello", PropertyQueryHelper.SCOPE_Descendant);			
			Assert.assertEquals(3, bcl3.size());
			
			List<BaseContentObject> bcl5=pqh.selectContentObjectsByPropertyFullTextSearch(propertyTestRootCo, "textProp", "helloAhallo", PropertyQueryHelper.SCOPE_Child);			
			Assert.assertEquals(1, bcl5.size());			
			Assert.assertEquals(bcl5.get(0).getContentObjectName(), "propertyTestSubCo3");
			
			List<BaseContentObject> bcl6=pqh.selectContentObjectsByPropertyFullTextSearch(propertyTestRootCo, "textProp", "hello or hallo", PropertyQueryHelper.SCOPE_Child);			
			Assert.assertEquals(0, bcl6.size());	
			
			List<BaseContentObject> bcl7=pqh.selectContentObjectsByPropertyFullTextSearch(propertyTestRootCo, "textProp", "hello OR hallo", PropertyQueryHelper.SCOPE_Child);			
			Assert.assertEquals(3, bcl7.size());
			
			List<BaseContentObject> bcl8=pqh.selectContentObjectsByPropertyFullTextSearch(propertyTestRootCo, "textProp", "hello && hallo", PropertyQueryHelper.SCOPE_Child);			
			Assert.assertEquals(1, bcl8.size());
			
			List<BaseContentObject> bcl9=pqh.selectContentObjectsByPropertyFullTextSearch(propertyTestRootCo, "textProp", "-helloAhallo", PropertyQueryHelper.SCOPE_Child);			
			Assert.assertEquals(3, bcl9.size());
			
			List<BaseContentObject> bcl10=pqh.selectContentObjectsByPropertyFullTextSearch(propertyTestRootCo, "textProp", "hello || hallo", PropertyQueryHelper.SCOPE_Child);			
			Assert.assertEquals(1, bcl10.size());
			Assert.assertEquals(bcl10.get(0).getContentObjectName(), "propertyTestSubCo4");
			
			cs.closeContentSpace();		
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testSelectContentObjectsByPropertyFullTextSearch");
		}		
	}

}
