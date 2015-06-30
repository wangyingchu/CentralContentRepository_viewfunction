package com.viewfunction.contentRepository.util.helper;

import java.util.List;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public interface ContentQueryHelper {	
	public List<BinaryContent> selectBinaryContentsByMimeType(BaseContentObject contentObject,String mimeTypeValue) throws ContentReposityException;
	public List<TextContent> selectTextContentsByMimeType(BaseContentObject contentObject, String mimeTypeValue)throws ContentReposityException;
	public List<BinaryContent> selectBinaryContentsByTitle(BaseContentObject contentObject,String titleValue) throws ContentReposityException;	
	public List<TextContent> selectTextContentsByTitle(BaseContentObject contentObject, String titleValue)	throws ContentReposityException;	
	public List<BinaryContent> selectBinaryContentsByFullTextSearch(BaseContentObject contentObject,String contentValue) throws ContentReposityException;
	public List<TextContent> selectTextContentsByEncoding(BaseContentObject contentObject,String encodingValue) throws ContentReposityException;
}
