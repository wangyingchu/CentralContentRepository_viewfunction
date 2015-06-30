package com.viewfunction.contentRepository.contentBureau;

import java.util.List;

import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public interface CommentObject {	
	public static String commentAuthor="vfcr:commentAuthor";
	public static String commentContent="vfcr:commentContent";
	public static String commentCreateDate="vfcr:commentCreateDate";
	
	public List<CommentObject> getSubComments() throws ContentReposityException;
	
	public CommentObject addSubComment(CommentObject commentObject) throws ContentReposityException;	
	public boolean deleteSubComment(String subCommentId) throws ContentReposityException;
	
	public String getCommentAuthor();
	public void setCommentAuthor(String commentAuthor);
	
	public long getCommentCreateDate();
	public void setCommentCreateDate(long commentCreateDate);
	
	public String getCommentContent();
	public void setCommentContent(String commentContent);
	
	public String getCommentId() throws ContentReposityException;
	public String getCommentUUID() throws ContentReposityException;
	
	public CommentObject getParentComment() throws ContentReposityException;	
	
	public void setCommentData(Object contentData);
}