package com.viewfunction.contentRepository.developmentTest;

import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class FactoryTestCase {

	/**
	 * @param args
	 * @throws ContentReposityException 
	 */
	public static void main(String[] args) throws ContentReposityException {
		
	//	System.out.println(ContentComponentFactory.getRegisteredContentSpace());
		
		
		testCreateContentSpace("Familly Photos");
		
		testCreateContentSpace("Programing Projects");
		testCreateContentSpace("Financial Reports");
		
		System.out.println(ContentComponentFactory.getRegisteredContentSpace());
		//testConnectContentSpace("FileContentOperationTestSpace");		
		//testCreateRootContentObject("rootContentObject_0911");	
		//testCreateContentObject();
		//testCreateContentObjectProperty();
		
		//System.out.println(((JCRContentSpaceImpl)ContentComponentFactory.getContentSpace("default")).getJcrWorkspace().getName());
		//System.out.println(cob.getProperty("message"));
		//cob.addProperty("helloWYC", "HELLOWYCABC");
		//System.out.println(cob.getProperty("helloWYC"));
		//System.out.println();
		
	}
	
	public static void testCreateContentSpace(String spaceName){
		try {
			ContentSpace cs=ContentComponentFactory.createContentSpace(spaceName);
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void testConnectContentSpace(String spaceName){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(spaceName);
			System.out.println(cs);
			System.out.println(cs.getContentSpaceName());
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	public static void testCreateRootContentObject(String rootContentObjectName){
		RootContentObject rco=ContentComponentFactory.createRootContentObject(rootContentObjectName);	
		System.out.println(rco.getRootContentObjectID());
	}
	
	public static void testCreateContentObject(){
		ContentObject co=ContentComponentFactory.createContentObject();	
		System.out.println(co);
	}
	
	public static void testCreateContentObjectProperty(){
		ContentObjectProperty cop=ContentComponentFactory.createContentObjectProperty();
		System.out.println(cop);
	}
}
