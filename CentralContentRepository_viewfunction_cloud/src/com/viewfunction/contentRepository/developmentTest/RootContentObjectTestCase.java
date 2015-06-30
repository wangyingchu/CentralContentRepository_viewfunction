package com.viewfunction.contentRepository.developmentTest;

import java.util.List;

import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class RootContentObjectTestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testGetParentContentSpace();
		testGetNeighborRootContentObjects();
	}

	public static void testGetParentContentSpace(){		
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			RootContentObject rco=cs.getRootContentObject("RootContentObject4test_TestCase");			
			System.out.println(rco.getRootContentObjectID());		
			System.out.println(rco.getParentContentSpace().getContentSpaceName());				
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
		}
				
	}
	
	public static void testGetNeighborRootContentObjects(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			RootContentObject rco=cs.getRootContentObject("RootContentObject4test_TestCase");
			List<RootContentObject> rcol=rco.getNeighborRootContentObjects();		
			for(RootContentObject crco:rcol){			
				System.out.println(crco.getRootContentObjectID());
				//System.out.println(crco.getContentObjectName());
			}		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();	
		}
			
	}
	
}
