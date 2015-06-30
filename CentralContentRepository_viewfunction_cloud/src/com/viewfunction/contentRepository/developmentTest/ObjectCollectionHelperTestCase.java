package com.viewfunction.contentRepository.developmentTest;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.ContentObjectInheritanceChain;
import com.viewfunction.contentRepository.util.helper.ObjectCollectionHelper;

public class ObjectCollectionHelperTestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testGetParentContentObjectsChain();

	}

	public static void testGetParentContentObjectsChain(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");				
		//	BaseContentObject childContentObject1=rco.addSubContentObject("childContentObject1", null, true);			
		//	BaseContentObject childContentObject2=childContentObject1.addSubContentObject("childContentObject2", null, true);			
		//	BaseContentObject childContentObject3=childContentObject2.addSubContentObject("childContentObject2", null, true);				
			
			ObjectCollectionHelper ooh=	ContentComponentFactory.getObjectOperationHelper();
			
			
		
			
			
			
			
			BaseContentObject childContentObject1=rco.getSubContentObjects("childContentObject1").get(0);
	//		System.out.println(childContentObject1);
			BaseContentObject childContentObject2=childContentObject1.getSubContentObjects("childContentObject2").get(0);
		//	System.out.println(childContentObject2);
			
		//	 childContentObject3=childContentObject2.addSubContentObject("childContentObject3", null, true);		
			
			
			 BaseContentObject childContentObject3=childContentObject2.getSubContentObjects("childContentObject3").get(0);
	//		System.out.println(childContentObject3);
			
			 ContentObjectInheritanceChain coic=		ooh.getParentContentObjectsChain(cs, childContentObject3);
			
			 
			 System.out.println(coic.getContentObjectSpacePath());
			 
			 
		//	BaseContentObject childContentObject3=rco.getSubContentObjects("childContentObject1").get(0).
		//	getSubContentObjects("childContentObject2").get(0).getSubContentObjects("childContentObject3").get(0);
			
		//	childContentObject3.getParentContentObjectsChain();
			
			

			cs.closeContentSpace();	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();	
		}
		
		
		
		
	}
	
}
