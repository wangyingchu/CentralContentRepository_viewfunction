package com.viewfunction.contentRepository.developmentTest;

import java.util.List;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureau.VersionObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class VersionObjectTestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testContentObjectVersion();		
		//testContentObjectVersionContent();
		//testPredecessorVersionObject();
		testPredecessorVersionObjectChain();
		//testSuccessorVersionObject();
		//testSuccessorVersionObjectChain();		
		//testGetAllVersionsInSpace();
	}	
	
	public static void testContentObjectVersion(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");			
			VersionObject vo= rco.getCurrentVersion();			
			System.out.println(vo.getCurrentVersionCreatedDate().getTime());
			System.out.println(vo.getCurrentVersionNumber());		
			String[] labels=vo.getCurrentVersionLabels(); 
			for(String l:labels){
				System.out.println(l);			
			}	
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();	
		}			
	}	
	
	public static void testContentObjectVersionContent(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");
			VersionObject vo= rco.getCurrentVersion();		
			ContentObject vco=vo.getCurrentContentObject();		
			System.out.println(vco.getProperty("testRemoveProperty7"));		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
		}
		
	}
	
	public static void testPredecessorVersionObject(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");
			VersionObject vo= rco.getCurrentVersion();		
			VersionObject vo1=vo.getPredecessorVersionObject();
			VersionObject vo2=vo1.getPredecessorVersionObject();				
			System.out.println(vo.getCurrentVersionNumber());	
			System.out.println(vo.getCurrentVersionCreatedDate().getTime());
			System.out.println(vo.getCurrentVersionLabels()[0]);		
			System.out.println(vo1.getCurrentVersionNumber());	
			System.out.println(vo1.getCurrentVersionCreatedDate().getTime());
			System.out.println(vo1.getCurrentVersionLabels()[0]);
			System.out.println(vo2.getCurrentVersionNumber());	
			System.out.println(vo2.getCurrentVersionCreatedDate().getTime());
			System.out.println(vo2.getCurrentVersionLabels()[0]);		
			cs.closeContentSpace();		
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();	
		}
		
	}
	
	public static void testPredecessorVersionObjectChain(){
		ContentSpace cs=null;
		try {
			//cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			//ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");
			
			cs = ContentComponentFactory.connectContentSpace("FileContentOperationTestSpace");
			RootContentObject rco=cs.getRootContentObject("RootFileOperationTestObject_f");		
			BaseContentObject folderCo=rco.getSubContentObjects("folder_001").get(0);//.getSubContentObjects("stockInfo.txt").get(0);			
			
			
			VersionObject vo= folderCo.getCurrentVersion();	
			
			
			//VersionObject vo= rco.getCurrentVersion();		
			//VersionObject vo= rco.getSubContentObjects("RootContentObject4test_08_subContent_3").get(0).getCurrentVersionObject();
			
			System.out.println(vo.getCurrentVersionNumber());	
			System.out.println(vo.getCurrentVersionCreatedDate().getTime());
			if(vo.getCurrentVersionLabels().length>0){
				System.out.println(vo.getCurrentVersionLabels()[0]);	
			}		
			
			VersionObject curretnvo=vo.getPredecessorVersionObject();
			
			while(curretnvo!=null){
				System.out.println(curretnvo.getCurrentVersionNumber());	
				System.out.println(curretnvo.getCurrentVersionCreatedDate().getTime());
				if(curretnvo.getCurrentVersionLabels().length>0){
					System.out.println(curretnvo.getCurrentVersionLabels()[0]);	
				}		
				curretnvo=curretnvo.getPredecessorVersionObject();			
			}		
			cs.closeContentSpace();
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
		}
		
	}
	
	public static void testSuccessorVersionObject(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");
			VersionObject vo= rco.getCurrentVersion();		
			VersionObject vo1=vo.getPredecessorVersionObject();
			VersionObject vo2=vo1.getPredecessorVersionObject();	
			VersionObject vo3=vo2.getPredecessorVersionObject();
			
			System.out.println(vo3.getCurrentVersionNumber());	
			System.out.println(vo3.getCurrentVersionCreatedDate().getTime());
			System.out.println(vo3.getCurrentVersionLabels()[0]);
			VersionObject vs=vo3.getSuccessorVersionObject();
			System.out.println(vs.getCurrentVersionNumber());	
			System.out.println(vs.getCurrentVersionCreatedDate().getTime());
			System.out.println(vs.getCurrentVersionLabels()[0]);	
			VersionObject vs2=vs.getSuccessorVersionObject();
			System.out.println(vs2.getCurrentVersionNumber());	
			System.out.println(vs2.getCurrentVersionCreatedDate().getTime());
			System.out.println(vs2.getCurrentVersionLabels()[0]);
			VersionObject vs3=vs2.getSuccessorVersionObject();
			System.out.println(vs3.getCurrentVersionNumber());	
			System.out.println(vs3.getCurrentVersionCreatedDate().getTime());
			System.out.println(vs3.getCurrentVersionLabels()[0]);
			
			//VersionObject vs4=vs3.getSuccessorVersionObject();
			//System.out.println(vs4.getCurrentVersion());	
			//System.out.println(vs4.getCurrentVersionCreatedDate().getTime());
			//System.out.println(vs4.getCurrentVersionLabels()[0]);
			
			cs.closeContentSpace();		
		} catch (ContentReposityException e) {		
			e.printStackTrace();
			cs.closeContentSpace();	
		}
		
	}
	
	public static void testSuccessorVersionObjectChain(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");
			VersionObject vo= rco.getCurrentVersion();		
			VersionObject vo1=vo.getPredecessorVersionObject();
			VersionObject vo2=vo1.getPredecessorVersionObject();	
			VersionObject vo3=vo2.getPredecessorVersionObject();
			VersionObject vo4=vo3.getPredecessorVersionObject();		
			
			VersionObject curretnvo=vo4.getSuccessorVersionObject();
			while(curretnvo!=null){
				System.out.println(curretnvo.getCurrentVersionNumber());	
				System.out.println(curretnvo.getCurrentVersionCreatedDate().getTime());
				if(curretnvo.getCurrentVersionLabels().length>0){
					System.out.println(curretnvo.getCurrentVersionLabels()[0]);	
				}		
				curretnvo=curretnvo.getSuccessorVersionObject();			
			}		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {		
			e.printStackTrace();
			cs.closeContentSpace();	
		}
		
	}
	
	public static void testGetAllVersionsInSpace(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");		
			List<VersionObject> vol=rco.getCurrentVersion().getAllVersionsInSpace();		
			for(VersionObject vo:vol){
				System.out.println(vo.getCurrentVersionNumber());
				//if(vo.getCurrentVersion().equals("1.39")||vo.getCurrentVersion().equals("1.0")){
				if(vo.getCurrentVersionNumber().equals("1.39")||vo.getCurrentVersionNumber().equals("1.0")){
					System.out.println(vo.getCurrentContentObject().getProperty("TestVersionProperty2").getPropertyValue());
				}			
			}		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();	
		}
				
	}
}
