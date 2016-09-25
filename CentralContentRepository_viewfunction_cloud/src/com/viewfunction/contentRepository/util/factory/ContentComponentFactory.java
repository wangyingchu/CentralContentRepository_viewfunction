package com.viewfunction.contentRepository.util.factory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;

import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.jcr.repository.RepositoryImpl;
import org.apache.jackrabbit.oak.plugins.document.DocumentMK;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStore;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import com.viewfunction.contentRepository.contentBureau.CommentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.LockObject;
import com.viewfunction.contentRepository.contentBureau.PermissionObject;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureau.VersionObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRCommentObjectImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectPropertyImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentReposityConstant;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentSpaceImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRLockObjectImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRPermissionObjectImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRRootContentObjectImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRVersionObjectImpl;
import com.viewfunction.contentRepository.security.SecurityOperationConstant;
import com.viewfunction.contentRepository.util.BatchLoadJCRContentObjectImpl;
import com.viewfunction.contentRepository.util.BatchLoadJCRRootContentObjectImpl;
import com.viewfunction.contentRepository.util.ContentReposityCustomNodesConfigUtil;
import com.viewfunction.contentRepository.util.PerportyHandler;
import com.viewfunction.contentRepository.util.exception.ContentReposityDataException;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.exception.ContentReposityRuntimeException;
import com.viewfunction.contentRepository.util.helper.BinaryContent;
import com.viewfunction.contentRepository.util.helper.BinaryContentVersionObject;
import com.viewfunction.contentRepository.util.helper.CommentOperationHelper;
import com.viewfunction.contentRepository.util.helper.ContentObjectInheritanceChain;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.contentRepository.util.helper.ContentQueryHelper;
import com.viewfunction.contentRepository.util.helper.ObjectCollectionHelper;
import com.viewfunction.contentRepository.util.helper.PropertyQueryHelper;
import com.viewfunction.contentRepository.util.helper.SecurityOperationHelper;
import com.viewfunction.contentRepository.util.helper.TextContent;
import com.viewfunction.contentRepository.util.helperImpl.JCRBinaryContentImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRBinaryContentVersionObjectImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRCommentOperationHelperImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRContentObjectInheritanceChainImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRContentOperationHelperImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRContentQueryHelperImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRObjectCollectionHelperImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRPropertyQueryHelperImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRSecurityOperationHelperImpl;
import com.viewfunction.contentRepository.util.helperImpl.JCRTextContentImpl;
import com.viewfunction.contentRepository.util.observation.ContentSpaceEventListener;
import com.viewfunction.contentRepository.util.observationImpl.JCRDefaultContentSpaceEventListenerImpl;

public class ContentComponentFactory {
		
	private static String BUILDIN_ADMINISTRATOR_ACCOUNT;
	private static String BUILDIN__ADMINISTRATOR_ACCOUNT_PWD;	
	private static String USER_AUTHENTICATION_METHOD;
	private static String MONGODB_SERVER_ADDRESS;
	private static int MONGODB_SERVER_PORT_INTEGER;
	
	private static ContentSpace generateContentSpace(String contentSpaceName,Credentials loginCredentials)throws ContentReposityException{
		if(MONGODB_SERVER_ADDRESS==null){
			MONGODB_SERVER_ADDRESS=PerportyHandler.getPerportyValue(PerportyHandler.MONGODB_SERVER_ADDRESS);
		}
		if(MONGODB_SERVER_PORT_INTEGER==0){
			MONGODB_SERVER_PORT_INTEGER=Integer.parseInt(PerportyHandler.getPerportyValue(PerportyHandler.MONGODB_SERVER_PORT));
		}
		Session session=null;
		DocumentNodeStore nodeStore=null;
		try {
			DB db = new MongoClient(MONGODB_SERVER_ADDRESS, MONGODB_SERVER_PORT_INTEGER).getDB(contentSpaceName);
	        nodeStore = new DocumentMK.Builder().setMongoDB(db).getNodeStore();
	        Repository contentRepository = new Jcr(new Oak(nodeStore)).createRepository();
			session=contentRepository.login(loginCredentials);
			/*
			if(JCR_OAK_REFRESHINTERVALZERO){
				//In Jackrabbbit v3 OAK, Session state and refresh behaviour is different
				//http://jackrabbit.apache.org/oak/docs/differences.html
				session = ((JackrabbitRepository) contentRepository)
						.login(loginCredentials, null, Collections.<String, Object>singletonMap(REFRESH_INTERVAL, 0));
		    } else{
		    	session = contentRepository.login(loginCredentials);
		    }
			*/
			Workspace jcrWorkspace=session.getWorkspace();
			ContentReposityCustomNodesConfigUtil.configCustomNodes(session);
			JCRContentSpaceImpl contentSpaceImpl= new JCRContentSpaceImpl(jcrWorkspace,session,contentSpaceName);
			contentSpaceImpl.setContentNodeStore(nodeStore);
			return contentSpaceImpl;
		} catch (LoginException e) {
			if(nodeStore!=null){
				nodeStore.dispose();
			}
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
	    } catch (UnknownHostException e) {
	    	ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			if(session!=null){
				session.logout();
			}
			if(nodeStore!=null){
				nodeStore.dispose();
			}
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
	}
	/*
	public static void setRepository(Repository jcrRepository){
		repository=jcrRepository;
	}
	*/
	public static ContentSpace connectContentSpace(Object contentSpacePKObj) throws ContentReposityException {		
		if(BUILDIN_ADMINISTRATOR_ACCOUNT==null){
			BUILDIN_ADMINISTRATOR_ACCOUNT=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT);
		}
		if(BUILDIN__ADMINISTRATOR_ACCOUNT_PWD==null){
			BUILDIN__ADMINISTRATOR_ACCOUNT_PWD=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT_PWD);	
		}
		ContentSpace targetContentSpace=connectContentSpace(BUILDIN_ADMINISTRATOR_ACCOUNT,BUILDIN__ADMINISTRATOR_ACCOUNT_PWD,contentSpacePKObj);
		return targetContentSpace;			
	}	
	
	public static ContentSpace connectContentSpace(String userName,String usrPWD,Object contentSpacePKObj) throws ContentReposityException {
		SimpleCredentials loginCredentials=new SimpleCredentials(userName, usrPWD.toCharArray());
		if(USER_AUTHENTICATION_METHOD==null){
			USER_AUTHENTICATION_METHOD=PerportyHandler.getPerportyValue(PerportyHandler.USER_AUTHENTICATION_METHOD);
		}
		if(USER_AUTHENTICATION_METHOD.equals(SecurityOperationConstant.USER_AUTHENTICATION_METHOD_LDAP)){
			loginCredentials.setAttribute(JCRContentReposityConstant.USER_LOGIN_TIME, new Date());	
			loginCredentials.setAttribute(SecurityOperationConstant.USER_GROUP,SecurityOperationConstant.USER_GROUP_ADMINISTRATOR);
		}
		ContentSpace targetContentSpace=generateContentSpace(contentSpacePKObj.toString(),loginCredentials);
		return targetContentSpace;		
	}	
	
	public static ContentSpace createContentSpace(Object contentSpacePKObj) throws ContentReposityException {
		if(!registerContentSpace(contentSpacePKObj.toString())){
			return null;
		}
		String BUILDIN_ADMINISTRATOR_ACCOUNT=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT);
		String BUILDIN__ADMINISTRATOR_ACCOUNT_PWD=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT_PWD);
		SimpleCredentials loginCredentials=new SimpleCredentials(BUILDIN_ADMINISTRATOR_ACCOUNT, BUILDIN__ADMINISTRATOR_ACCOUNT_PWD.toCharArray());			
		if(PerportyHandler.getPerportyValue(PerportyHandler.USER_AUTHENTICATION_METHOD).equals(SecurityOperationConstant.USER_AUTHENTICATION_METHOD_LDAP)){
			loginCredentials.setAttribute(JCRContentReposityConstant.USER_LOGIN_TIME, new Date());	
			loginCredentials.setAttribute(SecurityOperationConstant.USER_GROUP,SecurityOperationConstant.USER_GROUP_ADMINISTRATOR);
		}
		ContentSpace targetContentSpace=generateContentSpace(contentSpacePKObj.toString(),loginCredentials);
		return targetContentSpace;
	}
	
	public static boolean removeContentSpace(Object contentSpacePKObj) {
		/* Not support by jackrabbit yet*/
		return false;		
	}	

	public static RootContentObject createRootContentObject(Object rootContentObjectIDObject) {
		return new JCRRootContentObjectImpl(rootContentObjectIDObject);
	}

	public static ContentObject createContentObject() {
		return new JCRContentObjectImpl();
	}
	
	public static ContentObjectProperty createContentObjectProperty() {
		return new JCRContentObjectPropertyImpl();
	}
	
	public static VersionObject createVersionObject(){
		return new JCRVersionObjectImpl();
	}
	
	public static LockObject createLockObject(){
		return new JCRLockObjectImpl();
	}
	
	public static CommentObject createCommentObject(){
		return new JCRCommentObjectImpl();
	}	
	
	public static PermissionObject createPermissionObject(){
		return new JCRPermissionObjectImpl();
	}	
	
	public static ContentOperationHelper getContentOperationHelper(){
		return new JCRContentOperationHelperImpl();
	}
	
	public static SecurityOperationHelper getSecurityOperationHelper(){
		return new JCRSecurityOperationHelperImpl();
	}
	
	public static ContentQueryHelper getContentQueryHelper(){
		return new JCRContentQueryHelperImpl();
	}
	
	public static BinaryContent createBinaryContentObject() {
		return new JCRBinaryContentImpl();
	}
	
	public static TextContent createTextContentObject() {
		return new JCRTextContentImpl();
	}
	
	public static BinaryContentVersionObject createBinaryContentVersionObject(){
		return new JCRBinaryContentVersionObjectImpl();
	}
	
	public static ObjectCollectionHelper getObjectOperationHelper(){
		return new JCRObjectCollectionHelperImpl();
	}
	
	public static PropertyQueryHelper getPropertyQueryHelper(){
		return new JCRPropertyQueryHelperImpl();
	}
	
	public static CommentOperationHelper getCommentOperationHelper(){
		return new JCRCommentOperationHelperImpl();
	}
	
	public static ContentObjectInheritanceChain createContentObjectInheritanceChain(){
		return new JCRContentObjectInheritanceChainImpl();
	}
	
	public static ContentSpaceEventListener createContentSpaceEventListener(int listererType){		
		switch(listererType){		
			case ContentSpaceEventListener.DEFAULT_CONTENTSPACE_EVENTLISTENER:
				return new JCRDefaultContentSpaceEventListenerImpl();
			default:
				return null;
		}
	}
	
	public static List<String> getRegisteredContentSpace() throws ContentReposityException{
		if(MONGODB_SERVER_ADDRESS==null){
			MONGODB_SERVER_ADDRESS=PerportyHandler.getPerportyValue(PerportyHandler.MONGODB_SERVER_ADDRESS);
		}
		if(MONGODB_SERVER_PORT_INTEGER==0){
			MONGODB_SERVER_PORT_INTEGER=Integer.parseInt(PerportyHandler.getPerportyValue(PerportyHandler.MONGODB_SERVER_PORT));
		}
		Session session=null;
		DocumentNodeStore nodeStore=null;
		Repository contentRepository =null;
		try {
			String metaDataContentSpace=JCRContentReposityConstant.METEDATA_CONTENTSPACE;
			DB db = new MongoClient(MONGODB_SERVER_ADDRESS, MONGODB_SERVER_PORT_INTEGER).getDB(metaDataContentSpace);
	        nodeStore = new DocumentMK.Builder().setMongoDB(db).getNodeStore();
	        contentRepository  = new Jcr(new Oak(nodeStore)).createRepository();
			
			String BUILDIN_ADMINISTRATOR_ACCOUNT=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT);
			String BUILDIN__ADMINISTRATOR_ACCOUNT_PWD=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT_PWD);
			SimpleCredentials loginCredentials=new SimpleCredentials(BUILDIN_ADMINISTRATOR_ACCOUNT, BUILDIN__ADMINISTRATOR_ACCOUNT_PWD.toCharArray());
			if(PerportyHandler.getPerportyValue(PerportyHandler.USER_AUTHENTICATION_METHOD).equals(SecurityOperationConstant.USER_AUTHENTICATION_METHOD_LDAP)){
				loginCredentials.setAttribute(JCRContentReposityConstant.USER_LOGIN_TIME, new Date());	
				loginCredentials.setAttribute(SecurityOperationConstant.USER_GROUP,SecurityOperationConstant.USER_GROUP_ADMINISTRATOR);
			}		
			session = contentRepository.login(loginCredentials);
			String registerRootContentObj=JCRContentReposityConstant.CONTENTSPACE_REGISTER_ROOTCONTENTOBJECT;
			List<String> csl=new ArrayList<String>();
			if(session.getRootNode().hasNode(registerRootContentObj)){
				Node registerNode=session.getRootNode().getNode(registerRootContentObj);			
				PropertyIterator pi=registerNode.getProperties();								
				while(pi.hasNext()){				
					Property cp=pi.nextProperty();		
					if(!cp.getName().startsWith("jcr:")){					
						csl.add(cp.getName());					
					}				
				}			
			}			
			return csl;			
		} catch (LoginException e) {
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		} catch (NoSuchWorkspaceException e) {
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		} catch (UnknownHostException e) {
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		}finally{
			if(session!=null){
				session.logout();
			}
			if(contentRepository!=null){
				RepositoryImpl repository=(RepositoryImpl)contentRepository;
				repository.shutdown();
			}
			if(nodeStore!=null){
				nodeStore.dispose();
			}
		}	
	}
	
	private static boolean registerContentSpace(String contentSpaceID) throws ContentReposityException{		
		if(MONGODB_SERVER_ADDRESS==null){
			MONGODB_SERVER_ADDRESS=PerportyHandler.getPerportyValue(PerportyHandler.MONGODB_SERVER_ADDRESS);
		}
		if(MONGODB_SERVER_PORT_INTEGER==0){
			MONGODB_SERVER_PORT_INTEGER=Integer.parseInt(PerportyHandler.getPerportyValue(PerportyHandler.MONGODB_SERVER_PORT));
		}
		Session session=null;
		DocumentNodeStore nodeStore=null;
		boolean registerResult=false;
		try {
			String metaDataContentSpace=JCRContentReposityConstant.METEDATA_CONTENTSPACE;
			DB db = new MongoClient(MONGODB_SERVER_ADDRESS, MONGODB_SERVER_PORT_INTEGER).getDB(metaDataContentSpace);
	        nodeStore = new DocumentMK.Builder().setMongoDB(db).getNodeStore();
	        Repository contentRepository  = new Jcr(new Oak(nodeStore)).createRepository();
			
	        String BUILDIN_ADMINISTRATOR_ACCOUNT=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT);
			String BUILDIN__ADMINISTRATOR_ACCOUNT_PWD=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT_PWD);
			SimpleCredentials loginCredentials=new SimpleCredentials(BUILDIN_ADMINISTRATOR_ACCOUNT, BUILDIN__ADMINISTRATOR_ACCOUNT_PWD.toCharArray());				
			if(PerportyHandler.getPerportyValue(PerportyHandler.USER_AUTHENTICATION_METHOD).equals(SecurityOperationConstant.USER_AUTHENTICATION_METHOD_LDAP)){
				loginCredentials.setAttribute(JCRContentReposityConstant.USER_LOGIN_TIME, new Date());	
				loginCredentials.setAttribute(SecurityOperationConstant.USER_GROUP,SecurityOperationConstant.USER_GROUP_ADMINISTRATOR);				
			}			
			session = contentRepository.login(loginCredentials);
			String registerRootContentObj=JCRContentReposityConstant.CONTENTSPACE_REGISTER_ROOTCONTENTOBJECT;			
			Node registerNode=null;
			if(session.getRootNode().hasNode(registerRootContentObj)){
				registerNode=session.getRootNode().getNode(registerRootContentObj);					
			}else{
				registerNode=session.getRootNode().addNode(registerRootContentObj);					
			}	
			if(registerNode.hasProperty(contentSpaceID)){
				registerResult=false;				
			}else{
				registerNode.setProperty(contentSpaceID, JCRContentReposityConstant.CONTENTSPACE_STATUS_ACTIVE);	
				registerResult=true;
				session.save();					
			}				
			return registerResult;
		} catch (ContentReposityRuntimeException e) {			
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		} catch (LoginException e) {			
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e){		
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		} catch (UnknownHostException e) {
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		}finally{
			if(session!=null){
				session.logout();
			}
			if(nodeStore!=null){
				nodeStore.dispose();
			}
		}			
	}
	

	//batch import data object, need delete after pass all unit test
	public static ContentSpace connectBatchLoadContentSpace(Object contentSpacePKObj) throws ContentReposityException {
		/*
		//need catch these data to improve performance
		String BUILDIN_ADMINISTRATOR_ACCOUNT=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT);
		String BUILDIN__ADMINISTRATOR_ACCOUNT_PWD=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN__ADMINISTRATOR_ACCOUNT_PWD);			
		
		Session session=null;
		try {			
			Repository contentRepository = getWorkSpaceRepository(contentSpacePKObj.toString());
			SimpleCredentials loginCredentials=new SimpleCredentials(BUILDIN_ADMINISTRATOR_ACCOUNT, BUILDIN__ADMINISTRATOR_ACCOUNT_PWD.toCharArray());
			//need catch this USER_AUTHENTICATION_METHOD flag to improve performance
			if(PerportyHandler.getPerportyValue(PerportyHandler.USER_AUTHENTICATION_METHOD).equals(SecurityOperationConstant.USER_AUTHENTICATION_METHOD_LDAP)){
				loginCredentials.setAttribute(JCRContentReposityConstant.USER_LOGIN_TIME, new Date());	
				loginCredentials.setAttribute(SecurityOperationConstant.USER_GROUP,SecurityOperationConstant.USER_GROUP_ADMINISTRATOR);
			}
			session = contentRepository.login(loginCredentials);
			Workspace jcrWorkspace=session.getWorkspace();				
			ContentReposityCustomNodesConfigUtil.configCustomNodes(session);
			return new BatchLoadJCRContentSpaceImpl(jcrWorkspace,session,contentSpacePKObj.toString());			
		} catch (LoginException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {			
			if(session!=null){
				session.logout();
			}
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	*/
		return null;
	}	
	
	public static RootContentObject createBatchLoadRootContentObject(Object rootContentObjectIDObject) {
		return new BatchLoadJCRRootContentObjectImpl(rootContentObjectIDObject);
	}

	public static ContentObject createBatchLoadContentObject() {
		return new BatchLoadJCRContentObjectImpl();
	}
	
}