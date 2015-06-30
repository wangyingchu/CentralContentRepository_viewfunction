package com.viewfunction.contentRepository.contentBureauImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import com.viewfunction.contentRepository.contentBureau.CommentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class JCRCommentObjectImpl implements CommentObject{	
	public static final String subCommentsContainerNode="vfcr:subCommentsContainer";	
	private Node commentDataNode;	
	private String commentAuthor;	
	private long commentCreateDate;
	private String commentContent;
	
	@Override
	public List<CommentObject> getSubComments() throws ContentReposityException{		
		List<CommentObject> commentsList=new ArrayList<CommentObject>();		
		try {
			if(this.commentDataNode.hasNode(subCommentsContainerNode)){
				Node subCommentContainerNode=this.commentDataNode.getNode(subCommentsContainerNode);				
				NodeIterator nodeItr=subCommentContainerNode.getNodes();			
				while(nodeItr.hasNext()){
					Node currentCommentNode=nodeItr.nextNode();				
					CommentObject currentCommentObject=ContentComponentFactory.createCommentObject();				
					currentCommentObject.setCommentAuthor(currentCommentNode.getProperty(CommentObject.commentAuthor).getString());
					currentCommentObject.setCommentContent(currentCommentNode.getProperty(CommentObject.commentContent).getString());
					currentCommentObject.setCommentCreateDate(currentCommentNode.getProperty(CommentObject.commentCreateDate).getDate().getTimeInMillis());
					currentCommentObject.setCommentData(currentCommentNode);				
					commentsList.add(currentCommentObject);				
				}				
			}else{
				return commentsList;
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
		return commentsList;
	}

	@Override
	public CommentObject addSubComment(CommentObject commentObject) throws ContentReposityException{					
		try {
			Node subCommentContainerNode;			
			if(this.commentDataNode.hasNode(subCommentsContainerNode)){
				subCommentContainerNode=this.commentDataNode.getNode(subCommentsContainerNode);				
			}else{
				subCommentContainerNode=this.commentDataNode.addNode(subCommentsContainerNode,"nt:unstructured");				
			}			
			String commentId=""+new Date().getTime();			
			Node currentComment=subCommentContainerNode.addNode(commentId,"nt:unstructured");
			currentComment.addNode(subCommentsContainerNode,"nt:unstructured");			
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
			return commentObject;			
		} catch (PathNotFoundException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (ItemExistsException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (NoSuchNodeTypeException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (LockException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (VersionException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (ConstraintViolationException e) {
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
	public boolean deleteSubComment(String subCommentId) throws ContentReposityException{
		try {
			if(!this.commentDataNode.hasNode(subCommentsContainerNode)){
				return false;				
			}else{				
				Node subCommentContainerNode=this.commentDataNode.getNode(subCommentsContainerNode);				
				if(subCommentContainerNode.hasNode(subCommentId)){
					Node commentNode=subCommentContainerNode.getNode(subCommentId);
					commentNode.remove();
					subCommentContainerNode.getSession().save();
					return true;				
				}else{
					return false;
				}							
			}
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}

	@Override
	public String getCommentAuthor() {		
		return this.commentAuthor;
	}

	@Override
	public void setCommentAuthor(String commentAuthor) {
		this.commentAuthor=commentAuthor;		
	}

	@Override
	public long getCommentCreateDate() {		
		return this.commentCreateDate;
	}

	@Override
	public void setCommentCreateDate(long commentCreateDate) {
		this.commentCreateDate=commentCreateDate;		
	}

	@Override
	public String getCommentContent() {		
		return this.commentContent;
	}

	@Override
	public void setCommentContent(String commentContent) {
		this.commentContent=commentContent;		
	}

	@Override
	public String getCommentId() throws ContentReposityException{		
		try {
			return this.commentDataNode.getName();
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}

	@Override
	public CommentObject getParentComment() throws ContentReposityException{
		try {
			Node parentNode=this.commentDataNode.getParent().getParent();			
			if(parentNode.hasProperty(CommentObject.commentAuthor)&&
					parentNode.hasProperty(CommentObject.commentContent)&&
					parentNode.hasProperty(CommentObject.commentCreateDate)){				
				CommentObject parentCommentObject=ContentComponentFactory.createCommentObject();				
				parentCommentObject.setCommentAuthor(parentNode.getProperty(CommentObject.commentAuthor).getString());
				parentCommentObject.setCommentContent(parentNode.getProperty(CommentObject.commentContent).getString());
				parentCommentObject.setCommentCreateDate(parentNode.getProperty(CommentObject.commentCreateDate).getDate().getTimeInMillis());
				parentCommentObject.setCommentData(parentNode);					
				return parentCommentObject;				
			}else{
				return null;
			}			
		} catch (AccessDeniedException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (ItemNotFoundException e) {
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
	public void setCommentData(Object contentData) {
		this.commentDataNode=(Node)contentData;		
	}

	@Override
	public String getCommentUUID() throws ContentReposityException {		
		try {
			return this.commentDataNode.getPath();
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}
}