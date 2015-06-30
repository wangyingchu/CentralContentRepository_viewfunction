package com.viewfunction.testCode.tempTestCalss;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;


public class Login {	
	
	public static void main0(String[] args) throws Exception {
        Repository repository = new TransientRepository();
        Session session = repository.login();
        session = repository.login(new SimpleCredentials(
				"username", "password".toCharArray()),"testCase1Space");
        try {
            String user = session.getUserID();
            String name = repository.getDescriptor(Repository.REP_NAME_DESC);
            System.out.println("Logged in as " + user + " to a " + name + " repository.");
            Node root = session.getRootNode();
            Node node = root.getNode("test root object2");
            System.out.println(node.getProperty("message").getString());
        } finally {
            session.logout();
        }
    }
	public static void main(String[] args) throws Exception {
		
		System.out.println(new Date(-2000000000).toGMTString());
		
	}
}
