package com.viewfunction.testCode.tempTestCalss;
import javax.jcr.*;

import org.apache.jackrabbit.core.TransientRepository;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
public class ImportingPicturecontent {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws LoginException, RepositoryException, IOException  {
		 Repository repository = new TransientRepository();
		 Session session = repository.login(
		            new SimpleCredentials("username", "password".toCharArray()));
		        try {
		            // Use the root node as a starting point
		            Node root = session.getRootNode();

		            // Import the XML file unless already imported
		            if (!root.hasNode("importxml")) {
		                System.out.print("Importing xml... ");
		                // Create an unstructured node under which to import the XML
		                Node node = root.addNode("importxml", "nt:unstructured");
		                // Import the file "test.xml" under the created node
		                
		                
		                
		                FileInputStream xml = new FileInputStream("test.xml");
		                session.importXML(
		                    "/importxml", xml, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
		                xml.close();
		                // Save the changes to the repository
		                session.save();
		                System.out.println("done.");
		            }

		          
		        } finally {
		            session.logout();
		        }

	}

}
