package com.viewfunction.contentRepository.util.helperImpl;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.viewfunction.contentRepository.contentBureau.CommentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentSpaceImpl;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.CommentOperationHelper;

public class JCRCommentOperationHelperImpl implements CommentOperationHelper {
	@Override
	public CommentObject getCommentByCommentUUID(ContentSpace contentSpace,	String commentUUID) throws ContentReposityException {			
		try {
			JCRContentSpaceImpl _JCRContentSpaceImpl=(JCRContentSpaceImpl)contentSpace;		
			Session jcrSession=_JCRContentSpaceImpl.getJcrSession();
			Node commentDataNode=jcrSession.getNode(commentUUID);			
			if(commentDataNode.hasProperty(CommentObject.commentAuthor)&&
					commentDataNode.hasProperty(CommentObject.commentContent)&&
					commentDataNode.hasProperty(CommentObject.commentCreateDate)){				
				CommentObject parentCommentObject=ContentComponentFactory.createCommentObject();				
				parentCommentObject.setCommentAuthor(commentDataNode.getProperty(CommentObject.commentAuthor).getString());
				parentCommentObject.setCommentContent(commentDataNode.getProperty(CommentObject.commentContent).getString());
				parentCommentObject.setCommentCreateDate(commentDataNode.getProperty(CommentObject.commentCreateDate).getDate().getTimeInMillis());
				parentCommentObject.setCommentData(commentDataNode);					
				return parentCommentObject;				
			}else{
				return null;
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
	public boolean deleteCommentByCommentUUID(ContentSpace contentSpace,String commentUUID) throws ContentReposityException {
		try {
			JCRContentSpaceImpl _JCRContentSpaceImpl=(JCRContentSpaceImpl)contentSpace;		
			Session jcrSession=_JCRContentSpaceImpl.getJcrSession();
			Node commentDataNode=jcrSession.getNode(commentUUID);			
			if(commentDataNode.hasProperty(CommentObject.commentAuthor)&&
					commentDataNode.hasProperty(CommentObject.commentContent)&&
					commentDataNode.hasProperty(CommentObject.commentCreateDate)){					
				commentDataNode.remove();
				jcrSession.save();									
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
}