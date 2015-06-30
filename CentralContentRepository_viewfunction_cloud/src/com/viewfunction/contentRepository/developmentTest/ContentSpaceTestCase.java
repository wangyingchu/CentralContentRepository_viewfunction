package com.viewfunction.contentRepository.developmentTest;

import java.util.List;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.testNGTest.TestCaseDataConstant;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class ContentSpaceTestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//testAddRootContentObject("Incoming Report 2006");
		//testAddRootContentObject("Incoming Report 2007");
		//testAddRootContentObject("Incoming Report 2008");
		//testAddRootContentObject("Incoming Report 2009");
		//testGetRootContentObject("Wang Ying Chu");
		/*
		testAddRootContentObject("RootContentObject4test_02");
		testAddRootContentObject("RootContentObject4test_03");
		testAddRootContentObject("RootContentObject4test_04");
		testAddRootContentObject("RootContentObject4test_05");
		testAddRootContentObject("RootContentObject4test_06");
		testAddRootContentObject("RootContentObject4test_07");
		testAddRootContentObject("RootContentObject4test_08");
		testAddRootContentObject("RootContentObject4test_09");
		testAddRootContentObject("RootContentObject4test_10");
		*/
		//testAddRootContentObject("RootContentObject4test_TestCase");
		//testGetRootContentObject("RootContentObject4test_08");
		//testGetRootContentObjects();
		testGetContentObjectByAbsPath();
	}
	
	public static void testAddRootContentObject(String rootContentObjectID){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("Financial Reports");
			RootContentObject rco=ContentComponentFactory.createRootContentObject(rootContentObjectID);
			cs.addRootContentObject(rco);		
			System.out.println(rco.getRootContentObjectID());
			System.out.println(rco.getParentContentSpace());	
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
	
	public static void testGetContentObjectByAbsPath(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);			
			RootContentObject rco=ContentComponentFactory.createRootContentObject("RootContentSpaceForGetContentObjectByAbsPathTest");
			cs.addRootContentObject(rco);
			rco.addSubContentObject("path1", null, false).addSubContentObject("path2", null, false);
			String absPath="/RootContentSpaceForGetContentObjectByAbsPathTest/path1/path2";			
			BaseContentObject bco=cs.getContentObjectByAbsPath(absPath);
			
			System.out.println(bco.getContentObjectName());
			
			
			
			cs.removeRootContentObject("RootContentSpaceForGetContentObjectByAbsPathTest");
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
		}		
	}
	
	public static void testGetRootContentObject(String objectID){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("Familly Photos");
			RootContentObject rco=cs.getRootContentObject(objectID);
			System.out.println(rco);
			System.out.println(rco.getRootContentObjectID());
			/*
			try {			
				System.out.println(				
						((Property)rco.getProperty(JCRContentReposityConstant.SPACE_ROOT_CONTENT_OBJECT_ID)).getString());
			} catch (ValueFormatException e) {			
				e.printStackTrace();
			} catch (RepositoryException e) {
				e.printStackTrace();
			}	
			*/
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();
		}
				
	}
	
	public static void testGetRootContentObjects(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			List<RootContentObject> rcol=cs.getRootContentObjects();	
			for(RootContentObject currentRC : rcol){
				System.out.println("currentRC ObjectID: "+currentRC.getRootContentObjectID());
				//System.out.println("currentRC Object id Perorty: "+currentRC.getProperty(JCRContentReposityConstant.SPACE_ROOT_CONTENT_OBJECT_ID));	
			}		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();
		}	
		
	}
}
