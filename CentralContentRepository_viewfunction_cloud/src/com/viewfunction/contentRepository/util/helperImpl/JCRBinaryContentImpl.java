package com.viewfunction.contentRepository.util.helperImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.lock.Lock;
import javax.jcr.lock.LockException;
import javax.jcr.lock.LockManager;

import org.apache.jackrabbit.value.ValueFactoryImpl;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.CommentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.LockObject;
import com.viewfunction.contentRepository.contentBureau.PermissionObject;
import com.viewfunction.contentRepository.contentBureau.VersionObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRCommentObjectImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRLockObjectImpl;
import com.viewfunction.contentRepository.util.exception.ContentReposityDataException;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.BinaryContent;
import com.viewfunction.contentRepository.util.helper.BinaryContentVersionObject;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.contentRepository.util.helper.SecurityOperationHelper;

public class JCRBinaryContentImpl implements BinaryContent{
	private String contentDescription;
	private Binary contentBinary;
	private String contentName;
	private Calendar lastModified;
	private String mimeType;
	private Node binaryContainerNode;	
	private Calendar created;
	private String createdBy;
	private String lastModifiedBy;
	@Override
	public String getContentDescription() {		
		return this.contentDescription;
	}

	@Override
	public InputStream getContentInputStream() throws ContentReposityException {		
		try {
			return getContentBinary().getStream();
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
		
	}

	@Override
	public String getContentName() {		
		return this.contentName;
	}

	@Override
	public Calendar getLastModified() {	
		return this.lastModified;
	}

	@Override
	public String getMimeType() {		
		return this.mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setLastModified(Calendar lastModified) {
		this.lastModified = lastModified;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public void setContentBinary(Binary contentBinary) {
		this.contentBinary = contentBinary;
	}

	private Binary getContentBinary() {
		return this.contentBinary;
	}

	public void setContentDescription(String contentDescription) {
		this.contentDescription = contentDescription;
	}

	@Override
	public long getContentSize() throws ContentReposityException {
		try {
			return getContentBinary().getSize();
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}

	@Override
	public String getCurrentVersion() throws ContentReposityException {			
		try {
			ContentObject cco=ContentComponentFactory.createContentObject();
			cco.setContentData(getBinaryContainerNode());
			cco.setContentSession(this.getBinaryContainerNode().getSession());			
			return cco.getCurrentVersion().getCurrentVersionNumber();
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}
	
	@Override
	public List<BinaryContentVersionObject> getAllLinearVersions() throws ContentReposityException {	
		try {
			ContentObject cco=ContentComponentFactory.createContentObject();		
			cco.setContentData(getBinaryContainerNode());	
			cco.setContentSession(this.getBinaryContainerNode().getSession());	
			List<BinaryContentVersionObject> binaryContentVersionObjectList=new ArrayList<BinaryContentVersionObject>();
			ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();
			if(getCurrentVersion()=="0.0"){				
				List<VersionObject> versionList=cco.getAllVersionsInSpace();
				VersionObject initVersionObject=versionList.get(0);				
				BaseContentObject contentObject= initVersionObject.getCurrentContentObject();
				BaseContentObject jcrContentObject= contentObject.getSubContentObject("jcr:content");
				BinaryContentVersionObject currentBinaryContentVersionObject=ContentComponentFactory.createBinaryContentVersionObject();	
				BinaryContent bc=coh.getBinaryContent(jcrContentObject);							
				currentBinaryContentVersionObject.setBinaryContent(bc);					
				currentBinaryContentVersionObject.setMetaVersionData(initVersionObject);					
				binaryContentVersionObjectList.add(currentBinaryContentVersionObject);				
				return binaryContentVersionObjectList;			
			}			
			List<VersionObject> versionObjectList=cco.getAllLinearVersions();				
			for(VersionObject currentVersionObject:versionObjectList){
				if(currentVersionObject!=null){
					BinaryContentVersionObject currentBinaryContentVersionObject=ContentComponentFactory.createBinaryContentVersionObject();									
					BaseContentObject jcrContentObject= currentVersionObject.getCurrentContentObject().getSubContentObject("jcr:content");
					BinaryContent bc=coh.getBinaryContent(jcrContentObject);							
					currentBinaryContentVersionObject.setBinaryContent(bc);					
					currentBinaryContentVersionObject.setMetaVersionData(currentVersionObject);					
					binaryContentVersionObjectList.add(currentBinaryContentVersionObject);		
				}
			 }	
			return binaryContentVersionObjectList;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}

	@Override
	public List<BinaryContentVersionObject> getAllVersionsInSpace() throws ContentReposityException {
		ContentObject cco=ContentComponentFactory.createContentObject();
		cco.setContentData(getBinaryContainerNode());		
		List<VersionObject> versionObjectList=cco.getAllVersionsInSpace();		
		List<BinaryContentVersionObject> binaryContentVersionObjectList=new ArrayList<BinaryContentVersionObject>();
		ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();		
		if(getCurrentVersion()=="0.0"){					
			VersionObject initVersionObject=versionObjectList.get(0);				
			BaseContentObject contentObject= initVersionObject.getCurrentContentObject();
			BaseContentObject jcrContentObject= contentObject.getSubContentObject("jcr:content");
			BinaryContentVersionObject currentBinaryContentVersionObject=ContentComponentFactory.createBinaryContentVersionObject();	
			BinaryContent bc=coh.getBinaryContent(jcrContentObject);							
			currentBinaryContentVersionObject.setBinaryContent(bc);					
			currentBinaryContentVersionObject.setMetaVersionData(initVersionObject);					
			binaryContentVersionObjectList.add(currentBinaryContentVersionObject);				
			return binaryContentVersionObjectList;			
		}				
		for(VersionObject currentVersionObject:versionObjectList){			 
			BinaryContentVersionObject currentBinaryContentVersionObject=ContentComponentFactory.createBinaryContentVersionObject();			
			if(currentVersionObject.getCurrentVersionNumber()!="0.0"){
				BaseContentObject jcrContentObject= currentVersionObject.getCurrentContentObject().getSubContentObject("jcr:content");
				BinaryContent bc=coh.getBinaryContent(jcrContentObject);
				currentBinaryContentVersionObject.setBinaryContent(bc);			
				currentBinaryContentVersionObject.setMetaVersionData(currentVersionObject);
				binaryContentVersionObjectList.add(currentBinaryContentVersionObject);				
			}			
		 }		
		return binaryContentVersionObjectList;
	}

	@Override
	public boolean restoreToVersion(String targetVersion, boolean removeExisting) throws ContentReposityException {
		// TODO Auto-generated method stub
		return false;
	}	
	/*
	@Override
	public boolean isLinkObject() throws ContentReposityException {
		ContentObject cco=ContentComponentFactory.createContentObject();
		cco.setContentData(getBinaryContainerNode());
		return cco.isLinkContentObject();
	}	
	*/
	public void setBinaryContainerNode(Node binaryContainerNode) {
		this.binaryContainerNode = binaryContainerNode;
	}

	private Node getBinaryContainerNode() {
		return binaryContainerNode;
	}
	
	@Override
	public Calendar getCreated() {
		return this.created;
	}

	public void setCreated(Calendar created) {
		this.created = created;
	}

	@Override
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Override
	public LockObject lock(boolean isContentSpaceScoped)throws ContentReposityException {		
		try {			
			Node fileNode = getBinaryContainerNode();
	        Node resNode = fileNode.getNode("jcr:content");	 	            	
	        LockManager lm=resNode.getSession().getWorkspace().getLockManager();	            	
	    	String nodPath=resNode.getPath();
	    	String locker=resNode.getSession().getUserID();
	    	long timeoutHint=Long.MAX_VALUE;			    					
	    	Lock lk=lm.lock(nodPath, false, isContentSpaceScoped, timeoutHint, locker);
	    	if(!isContentSpaceScoped){
	    		String lockToken=lk.getLockToken();
	    		resNode.setProperty("vfcr:contentLockToken", lockToken);
	    		resNode.getSession().save();
	    	}			
	    	LockObject lo=ContentComponentFactory.createLockObject();
	    	((JCRLockObjectImpl)lo).setLockData(lk);
	    	return lo;			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}

	@Override
	public boolean unlock() throws ContentReposityException {
		try {			
			Node fileNode = getBinaryContainerNode();
			Node resNode = fileNode.getNode("jcr:content");	            	
        	LockManager lm=resNode.getSession().getWorkspace().getLockManager();
			String nodPath=resNode.getPath();
			Lock lk=lm.getLock(nodPath);
			if(!resNode.getSession().getUserID().equals(lk.getLockOwner())){
				return false;
			}else{
				if(!lk.isSessionScoped()){
					if(!lk.isLockOwningSession()){
						Property tokenP=resNode.getProperty("vfcr:contentLockToken");
						String lockTokenString=tokenP.getString();
						lm.addLockToken(lockTokenString);
						tokenP.remove();
						resNode.getSession().save();
					}
				}
				lm.unlock(nodPath); 				
				return true;				
			}					
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}	
	
	@Override
	public LockObject lock(boolean isContentSpaceScoped,String locker)throws ContentReposityException {		
		try {			
			Node fileNode = getBinaryContainerNode();
	        Node resNode = fileNode.getNode("jcr:content");	 	            	
	        LockManager lm=resNode.getSession().getWorkspace().getLockManager();	            	
	    	String nodPath=resNode.getPath();	    	
	    	long timeoutHint=Long.MAX_VALUE;			    					
	    	Lock lk=lm.lock(nodPath, false, isContentSpaceScoped, timeoutHint, locker);
	    	if(!isContentSpaceScoped){
	    		String lockToken=lk.getLockToken();
	    		resNode.setProperty("vfcr:contentLockToken", lockToken);
	    		resNode.getSession().save();
	    	}			
	    	LockObject lo=ContentComponentFactory.createLockObject();
	    	((JCRLockObjectImpl)lo).setLockData(lk);
	    	return lo;			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}

	@Override
	public boolean unlock(String locker) throws ContentReposityException {
		try {			
			Node fileNode = getBinaryContainerNode();
			Node resNode = fileNode.getNode("jcr:content");	            	
        	LockManager lm=resNode.getSession().getWorkspace().getLockManager();
			String nodPath=resNode.getPath();
			Lock lk=lm.getLock(nodPath);
			if(!locker.equals(lk.getLockOwner())){
				return false;
			}else{
				if(!lk.isSessionScoped()){
					if(!lk.isLockOwningSession()){
						Property tokenP=resNode.getProperty("vfcr:contentLockToken");
						String lockTokenString=tokenP.getString();
						lm.addLockToken(lockTokenString);
						tokenP.remove();
						resNode.getSession().save();
					}
				}
				lm.unlock(nodPath); 				
				return true;				
			}					
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}	
	
	@Override
	public String getLocker() throws ContentReposityException {
		try {
			if(!this.isLocked()){
				return null;
			}			
			Node fileNode = getBinaryContainerNode();
			Node resNode = fileNode.getNode("jcr:content");	            	
        	LockManager lm=resNode.getSession().getWorkspace().getLockManager();
			String nodPath=resNode.getPath();
			Lock lk=lm.getLock(nodPath);			
			return lk.getLockOwner();			
		} catch (UnsupportedRepositoryOperationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}		
	
	@Override
	public boolean isLocked() throws ContentReposityException {
		try {			
			Node fileNode = getBinaryContainerNode();
			Node resNode = fileNode.getNode("jcr:content");	            	
        	LockManager lm=resNode.getSession().getWorkspace().getLockManager();   
			String nodPath=resNode.getPath();
			return lm.isLocked(nodPath);	
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}

	@Override
	public String[] getContentTags() throws ContentReposityException {
		try {			
			Node binaryContainerNode = getBinaryContainerNode();			            	
			if(binaryContainerNode.hasProperty("vfcr:contentTags")){				
				Property p=binaryContainerNode.getProperty("vfcr:contentTags");				
				Value[] bva=p.getValues();
				String[] ba=new String[bva.length];
				for(int i=0;i<bva.length;i++){
					ba[i]=bva[i].getString();									
				} 				
				return ba;
			}else{
				return new String[]{};
			}	
			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}

	@Override
	public String[] addContentTags(String[] tags) throws ContentReposityException {
		try {			
			Node binaryContainerNode = getBinaryContainerNode();				
			binaryContainerNode.getSession().getWorkspace().getVersionManager().checkout(binaryContainerNode.getPath());			
			if(binaryContainerNode.hasProperty("vfcr:contentTags")){					
				Property p=binaryContainerNode.getProperty("vfcr:contentTags");					
				Value[] bva=p.getValues();
				String[] orginalTags=new String[bva.length];
				for(int i=0;i<bva.length;i++){
					orginalTags[i]=bva[i].getString();									
				} 				
				ArrayList<String> newTagsList=new ArrayList<String>();
				for(String newTag:tags){
					boolean alreadyHasThisTag=false;
					for(String orginalTag:orginalTags){
						if(orginalTag.equals(newTag)){
							alreadyHasThisTag=true;
						}				
					}
					if(!alreadyHasThisTag){
						newTagsList.add(newTag);				
					}			
				}				
				List<Value> newTagsPropertyList=new ArrayList<Value>(Arrays.asList(bva));
				ValueFactory vf=ValueFactoryImpl.getInstance();		
				for(String newTagStr:newTagsList){					
					Value value=vf.createValue(newTagStr);
					newTagsPropertyList.add(value);								
				}				
				Value[] propertyArr = new Value[newTagsPropertyList.size()];
				propertyArr = newTagsPropertyList.toArray(propertyArr);							
				p.setValue(propertyArr);				
			}else{
				binaryContainerNode.setProperty("vfcr:contentTags", tags);
			}				
			binaryContainerNode.getSession().save();			
			return getContentTags();			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}

	@Override
	public String[] removeContentTags(String[] tags) throws ContentReposityException {
		try {			
			Node binaryContainerNode = getBinaryContainerNode();		
			binaryContainerNode.getSession().getWorkspace().getVersionManager().checkout(binaryContainerNode.getPath());
			if(binaryContainerNode.hasProperty("vfcr:contentTags")){					
				Property p=binaryContainerNode.getProperty("vfcr:contentTags");					
				Value[] bva=p.getValues();
				String[] orginalTags=new String[bva.length];
				for(int i=0;i<bva.length;i++){
					orginalTags[i]=bva[i].getString();									
				} 					
				ArrayList<String> newTagsList=new ArrayList<String>();				
				for(String orginalTag:orginalTags){
					boolean notInRemoveList=true;					
					for(String tag:tags){
						if(tag.equals(orginalTag)){
							notInRemoveList=false;
						}				
					}
					if(notInRemoveList){
						newTagsList.add(orginalTag);						
					}	
				}
				
				ValueFactory vf=ValueFactoryImpl.getInstance();
				Value[] propertyArr = new Value[newTagsList.size()];	
				
				for(int i=0;i<newTagsList.size();i++){					
					propertyArr[i]=vf.createValue(newTagsList.get(i));					
				}											
				p.setValue(propertyArr);
				binaryContainerNode.getSession().save();					
				return getContentTags();
			}								
			return null;			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}		

	@Override
	public long getSequenceNumber() throws ContentReposityException {
		try {			
			Node binaryContainerNode = getBinaryContainerNode();				            	
			if(binaryContainerNode.hasProperty("vfcr:sequenceNumber")){				
				Property p=binaryContainerNode.getProperty("vfcr:sequenceNumber");					
				Value pv=p.getValue();					
				return pv.getLong();
			}else{
				return 0;
			}				
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}			
	}		
	
	@Override
	public boolean setSequenceNumber(long setSequenceNumber) throws ContentReposityException {
		try {			
			Node binaryContainerNode = getBinaryContainerNode();		
			binaryContainerNode.getSession().getWorkspace().getVersionManager().checkout(binaryContainerNode.getPath());
			if(binaryContainerNode.hasProperty("vfcr:sequenceNumber")){						
				Property p=binaryContainerNode.getProperty("vfcr:sequenceNumber");
				p.setValue(new Long(setSequenceNumber));				
			}else{
				binaryContainerNode.setProperty("vfcr:sequenceNumber", new Long(setSequenceNumber));
			}				
			binaryContainerNode.getSession().save();			
			return true;			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}			
	}

	@Override
	public boolean updateContentDescription(String contentDescription) throws ContentReposityException{
		try {
			Node resourceNode=getBinaryContainerNode().getNode("jcr:content");					
			resourceNode.getSession().getWorkspace().getVersionManager().checkout(resourceNode.getPath());
			if(resourceNode.hasProperty("vfcr:contentDescription")){						
				Property p=resourceNode.getProperty("vfcr:contentDescription");
				p.setValue(contentDescription);				
			}else{
				resourceNode.setProperty("vfcr:contentDescription", contentDescription);
			}				
			resourceNode.getSession().save();
			this.setContentDescription(contentDescription);
			return true;			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}			
	}

	@Override
	public boolean addComment(CommentObject commentObject) throws ContentReposityException{			
		try {
			Node binaryContainerNode=getBinaryContainerNode();			
			Node commentsContainerNode=	binaryContainerNode.getNode("jcr:content");				
			Node binaryContentCommentsContainerNode;			
			if(commentsContainerNode.hasNode("vfcr:contentCommentsNode")){
				binaryContentCommentsContainerNode=commentsContainerNode.getNode("vfcr:contentCommentsNode");				
			}else{
				binaryContentCommentsContainerNode=commentsContainerNode.addNode("vfcr:contentCommentsNode","nt:unstructured");				
			}				
			
			String commentId=""+new Date().getTime();			
			Node currentComment=binaryContentCommentsContainerNode.addNode(commentId,"nt:unstructured");
			currentComment.addNode(JCRCommentObjectImpl.subCommentsContainerNode);			
			String commentAuthor=commentObject.getCommentAuthor();
			String commentContent=commentObject.getCommentContent();
			long commentCreateDate=commentObject.getCommentCreateDate();		
			Calendar commentCalendar=Calendar.getInstance();			
			commentCalendar.setTime(new Date(commentCreateDate));			
			currentComment.setProperty(CommentObject.commentAuthor,commentAuthor);
			currentComment.setProperty(CommentObject.commentContent,commentContent);
			currentComment.setProperty(CommentObject.commentCreateDate, commentCalendar);
			commentObject.setCommentData(currentComment);
			currentComment.getSession().save();				
			return true;
		} catch (PathNotFoundException e) {
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
	public List<CommentObject> getComments() throws ContentReposityException{
		List<CommentObject> commentsList=new ArrayList<CommentObject>();
		try {
			Node commentContainerNode=getBinaryContainerNode().getNode("jcr:content");			
			if(!commentContainerNode.hasNode("vfcr:contentCommentsNode")){
				return commentsList;			
			}			
			Node commentsContainerNode=commentContainerNode.getNode("vfcr:contentCommentsNode");			
			NodeIterator nodeItr=commentsContainerNode.getNodes();			
			while(nodeItr.hasNext()){
				Node currentCommentNode=nodeItr.nextNode();				
				CommentObject currentCommentObject=ContentComponentFactory.createCommentObject();				
				currentCommentObject.setCommentAuthor(currentCommentNode.getProperty(CommentObject.commentAuthor).getString());
				currentCommentObject.setCommentContent(currentCommentNode.getProperty(CommentObject.commentContent).getString());
				currentCommentObject.setCommentCreateDate(currentCommentNode.getProperty(CommentObject.commentCreateDate).getDate().getTimeInMillis());
				currentCommentObject.setCommentData(currentCommentNode);				
				commentsList.add(currentCommentObject);				
			}
			return commentsList;
		} catch (PathNotFoundException e) {
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
	public boolean removeComment(String commentId) throws ContentReposityException {
		try {
			Node commentContainerNode=getBinaryContainerNode().getNode("jcr:content");			
			if(!commentContainerNode.hasNode("vfcr:contentCommentsNode")){
				return false;			
			}			
			Node commentsContainerNode=commentContainerNode.getNode("vfcr:contentCommentsNode");			
			if(commentsContainerNode.hasNode(commentId)){
				Node commentNode=commentsContainerNode.getNode(commentId);
				commentNode.remove();
				commentsContainerNode.getSession().save();
				return true;				
			}else{
				return false;
			}			
		} catch (PathNotFoundException e) {
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
	public List<PermissionObject> getContentPermissions() throws ContentReposityException {				
		Node binaryContainerNode = getBinaryContainerNode();	
		JCRContentObjectImpl JCRContentObjectImpl=new JCRContentObjectImpl(binaryContainerNode);
		SecurityOperationHelper soh=ContentComponentFactory.getSecurityOperationHelper();
		List<PermissionObject> permissionList=soh.getContentPermissions(JCRContentObjectImpl);
		return permissionList;
	}

	@Override
	public boolean setContentPermissions(List<PermissionObject> permissionList)throws ContentReposityException {
		Node binaryContainerNode = getBinaryContainerNode();		
		try {
			binaryContainerNode.getSession().getWorkspace().getVersionManager().checkout(binaryContainerNode.getPath());
			JCRContentObjectImpl JCRContentObjectImpl=new JCRContentObjectImpl(binaryContainerNode);
			SecurityOperationHelper soh=ContentComponentFactory.getSecurityOperationHelper();		
			boolean operationResult=soh.setContentPermissions(JCRContentObjectImpl, permissionList);		
			return operationResult;
		} catch (UnsupportedRepositoryOperationException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (LockException e) {
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
	public String getContentSpaceAbsPath() throws ContentReposityException{
		try {
			return getBinaryContainerNode().getPath();
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}	
}