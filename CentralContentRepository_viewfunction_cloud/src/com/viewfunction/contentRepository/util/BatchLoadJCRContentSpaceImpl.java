package com.viewfunction.contentRepository.util;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentReposityConstant;
import com.viewfunction.contentRepository.util.exception.ContentReposityDataException;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class BatchLoadJCRContentSpaceImpl  implements ContentSpace{
	private Workspace jcrWorkspace;	
	private Session jcrSession;
	private String contentSpaceName;
	public BatchLoadJCRContentSpaceImpl(){}
	
	public BatchLoadJCRContentSpaceImpl(Workspace jcrWorkspace,String contentSpaceName){
		this.setJcrWorkspace(jcrWorkspace);
		this.setContentSpaceName(contentSpaceName);
	}
	
	public BatchLoadJCRContentSpaceImpl(Workspace jcrWorkspace,Session session,String contentSpaceName){
		this.setJcrWorkspace(jcrWorkspace);
		this.setJcrSession(session);
		this.setContentSpaceName(contentSpaceName);
	}	
	
	@Override
	public List<RootContentObject> getRootContentObjects() throws ContentReposityException {
		try {
			Node root = getJcrSession().getRootNode();					
			NodeIterator rni=root.getNodes();				
			List<RootContentObject> rnl=new ArrayList<RootContentObject>();
			Node currentNode=null;	
			String currentNodeKey=null;			
			while(rni.hasNext()){
				currentNode=(Node)rni.next();				
				if(currentNode.hasProperty(JCRContentReposityConstant.SPACE_ROOT_CONTENT_OBJECT_ID)){
					currentNodeKey=((Property)currentNode.getProperty(JCRContentReposityConstant.SPACE_ROOT_CONTENT_OBJECT_ID)).getString();		
					RootContentObject rco=ContentComponentFactory.createBatchLoadRootContentObject(currentNodeKey);
					rco.setContentData(currentNode);
					rco.setContentSession(getJcrSession());
					rco.setParentContentSpace(this);
					rco.setContentObjectName(rco.getRootContentObjectID());
					rnl.add(rco);					
				}					
			}
			return rnl;
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}

	@Override
	public RootContentObject getRootContentObject(Object rootContentObjectPK) throws ContentReposityException {
		String rootContentObjectID=rootContentObjectPK.toString();		 
		try {
			Node root = jcrSession.getRootNode();
			if(!root.hasNode(rootContentObjectID)){
				return null;
			}
			Node targetNode=root.getNode(rootContentObjectID);
			RootContentObject rco=ContentComponentFactory.createBatchLoadRootContentObject(rootContentObjectID);
			rco.setContentData(targetNode);
			rco.setContentSession(jcrSession);
			rco.setParentContentSpace(this);
			rco.setContentObjectName(rco.getRootContentObjectID());
			return rco;			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();		
			cpe.initCause(e);
			throw cpe;
		}	
	}

	@Override
	public RootContentObject addRootContentObject(RootContentObject rootContentObject) throws ContentReposityException {
		String rootContentObjectID=rootContentObject.getRootContentObjectID();			
		try {
			Node root = jcrSession.getRootNode();			
			if(root.hasNode(rootContentObjectID)){			
				return null;
			}			
			Node current_JCR_RootContentObject = root.addNode(rootContentObjectID);		
			//support query
			current_JCR_RootContentObject.addMixin("vfcr:content");
			//set some system property
			current_JCR_RootContentObject.setProperty(JCRContentReposityConstant.SPACE_ROOT_CONTENT_OBJECT_ID, rootContentObjectID);
			jcrSession.save();
			rootContentObject.setContentData(current_JCR_RootContentObject);
			rootContentObject.setContentSession(getJcrSession());
			rootContentObject.setParentContentSpace(this);
			return rootContentObject;			
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}
	
	@Override
	public boolean removeRootContentObject(String rootContentObjectID)throws ContentReposityException {
		try {
			Node root = jcrSession.getRootNode();		
			if(!root.hasNode(rootContentObjectID)){
				return false;
			}
			Node current_JCR_RootContentObject = root.getNode(rootContentObjectID);		
			current_JCR_RootContentObject.remove();
			getJcrSession().save();			
			return true;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}

	private void setJcrWorkspace(Workspace jcrWorkspace) {
		this.jcrWorkspace = jcrWorkspace;
	}

	public Workspace getJcrWorkspace() {
		return jcrWorkspace;
	}

	public void setJcrSession(Session jcrSession) {
		this.jcrSession = jcrSession;
	}

	public Session getJcrSession() {
		return jcrSession;
	}

	@Override
	public void closeContentSpace() {
		this.jcrSession.logout();		
	}

	@Override
	public String getContentSpaceName() {		
		return this.contentSpaceName;
	}

	private void setContentSpaceName(String contentSpaceName) {
		this.contentSpaceName = contentSpaceName;
	}

	@Override
	public BaseContentObject getContentObjectByAbsPath(String contentObjectPath)throws ContentReposityException {
		try {
			Node con=this.getJcrSession().getNode(contentObjectPath);			
			ContentObject cco=ContentComponentFactory.createContentObject();				
			cco.setContentObjectName(con.getName());
			cco.setContentData(con);
			cco.setContentSession(getJcrSession());			
			return cco;
		} catch (PathNotFoundException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
	}
	
	public void saveBatchDate()throws ContentReposityException{
		try {
			getJcrSession().save();
		} catch (AccessDeniedException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (ItemExistsException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (ReferentialIntegrityException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (ConstraintViolationException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (InvalidItemStateException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (VersionException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (LockException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (NoSuchNodeTypeException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}

	@Override
	public void syncContentSpace() throws ContentReposityException {
		// TODO Auto-generated method stub
		
	}
}
