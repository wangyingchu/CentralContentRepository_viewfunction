package com.viewfunction.testCode.tempTestCalss;

import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;

import org.apache.jackrabbit.core.TransientRepository;

public class JCRVersionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		getNodeVersions();

	}
	
	public static void getNodeVersions(){
		Repository contentRepository =new TransientRepository();
		Session session;
		try {
			session = contentRepository.login(new SimpleCredentials(
					"username", "password".toCharArray()),"viewfunctionContentSpace_testSpace1");
			Workspace jcrWorkspace=session.getWorkspace();	
			
			Node targetNode=session.getRootNode().getNode("RootContentObject4test_08");
			
			System.out.println(targetNode);
			VersionHistory vh=jcrWorkspace.getVersionManager().getVersionHistory("/RootContentObject4test_08");
			System.out.println(vh);
			Version v=jcrWorkspace.getVersionManager().getBaseVersion(targetNode.getPath());
			System.out.println("Base version:"+v);
			
			
			// VersionIterator vi = targetNode.getVersionHistory().getAllVersions();  
			 VersionIterator vi = jcrWorkspace.getVersionManager().getVersionHistory("/RootContentObject4test_08").getAllVersions();
			 
			// System.out.println( jcrWorkspace.getVersionManager().getVersionHistory("/RootContentObject4test_08").getBaseVersion().getName());
			 
			 System.out.println("root version:"+ jcrWorkspace.getVersionManager().getVersionHistory("/RootContentObject4test_08").getRootVersion());
			 
			 
			 
			 
			
			 
			 
					
			 
			 
			 
			 //jcrWorkspace.getVersionManager().getVersionHistory("/RootContentObject4test_08").g
			 
		        while(vi.hasNext()){  
		            Version vl = vi.nextVersion();  
		            //list.add(v.getName()); 		           
		            vl.getContainingHistory();
		            String[] labelString=jcrWorkspace.getVersionManager().getVersionHistory("/RootContentObject4test_08").getVersionLabels(vl);
		           if(labelString.length>0){
		        	   System.out.println(vl.getName()+" : "+labelString[0] ); 
		        	   
		           }
		            
		            
		         
		            System.out.println(vl.getName());
		        }  
			
			
			/*
			VersionIterator vi=vh.getAllVersions();
			
			while(vi.hasNext()){
				Version cv=vi.nextVersion();
				
				
				System.out.println(cv.getBaseVersion());
				
				
				
			}
			*/
			session.logout();
			
			
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchWorkspaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		
		
		
		
		
		
		
		
	}

}
