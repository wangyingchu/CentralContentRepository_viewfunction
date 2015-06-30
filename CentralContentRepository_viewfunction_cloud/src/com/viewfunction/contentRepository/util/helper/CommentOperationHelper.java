package com.viewfunction.contentRepository.util.helper;

import com.viewfunction.contentRepository.contentBureau.CommentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public interface CommentOperationHelper {
	public CommentObject getCommentByCommentUUID(ContentSpace contentSpace,String commentUUID) throws ContentReposityException;
	public boolean deleteCommentByCommentUUID(ContentSpace contentSpace,String commentUUID) throws ContentReposityException;
}