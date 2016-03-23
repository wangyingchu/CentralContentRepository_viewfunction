package com.viewfunction.contentRepository.contentBureauImpl;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;

import org.apache.jackrabbit.oak.jcr.repository.RepositoryImpl;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStore;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityDataException;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class JCRContentSpaceImpl implements ContentSpace{
	private Workspace jcrWorkspace;	
	private Session jcrSession;
	private String contentSpaceName;
	private DocumentNodeStore contentNodeStore;
	public JCRContentSpaceImpl(){}
	
	public JCRContentSpaceImpl(Workspace jcrWorkspace,String contentSpaceName){
		this.setJcrWorkspace(jcrWorkspace);
		this.setContentSpaceName(contentSpaceName);
	}
	
	public JCRContentSpaceImpl(Workspace jcrWorkspace,Session session,String contentSpaceName){
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
					RootContentObject rco=ContentComponentFactory.createRootContentObject(currentNodeKey);
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
			RootContentObject rco=ContentComponentFactory.createRootContentObject(rootContentObjectID);
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
			//support version control
			current_JCR_RootContentObject.addMixin("mix:versionable");
			current_JCR_RootContentObject.addMixin("mix:referenceable");  
			current_JCR_RootContentObject.addMixin("mix:lockable");			
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
		if(this.jcrSession!=null){
			RepositoryImpl repository=(RepositoryImpl)this.jcrSession.getRepository();
			this.jcrSession.logout();
			//must shutdown repository,otherwise will cause thread leak for thread named [oak-scheduled-executor-XXXX]
			repository.shutdown();
		}	
		if(this.getContentNodeStore()!=null){
			this.getContentNodeStore().dispose();
		}
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

	public DocumentNodeStore getContentNodeStore() {
		return contentNodeStore;
	}

	public void setContentNodeStore(DocumentNodeStore contentNodeStore) {
		this.contentNodeStore = contentNodeStore;
	}

	@Override
	public void syncContentSpace() throws ContentReposityException {
		if(this.jcrSession!=null){
			try {
				this.jcrSession.refresh(true);
			} catch (RepositoryException e) {
				ContentReposityException cpe=new ContentReposityException();
				cpe.initCause(e);
				throw cpe;
			}
		}
	}
}
