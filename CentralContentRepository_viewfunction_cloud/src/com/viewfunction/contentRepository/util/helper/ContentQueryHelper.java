package com.viewfunction.contentRepository.util.helper;

import java.util.Date;
import java.util.List;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public interface ContentQueryHelper {
	
	public List<BinaryContent> selectBinaryContentsByMimeType(BaseContentObject contentObject,String mimeTypeValue) throws ContentReposityException;
	public List<TextContent> selectTextContentsByMimeType(BaseContentObject contentObject, String mimeTypeValue)throws ContentReposityException;	
	public List<BinaryContent> selectBinaryContentsByTitle(BaseContentObject contentObject,String titleValue) throws ContentReposityException;	
	public List<TextContent> selectTextContentsByTitle(BaseContentObject contentObject, String titleValue)	throws ContentReposityException;	
	
	public List<BinaryContent> selectBinaryContentsByCreator(BaseContentObject contentObject,String creatorValue) throws ContentReposityException;	
	public List<TextContent> selectTextContentsByCreator(BaseContentObject contentObject, String creatorValue)	throws ContentReposityException;	
	public List<BinaryContent> selectBinaryContentsByLastUpdater(BaseContentObject contentObject,String lastUpdaterValue) throws ContentReposityException;	
	public List<TextContent> selectTextContentsByLastUpdater(BaseContentObject contentObject, String lastUpdaterValue)	throws ContentReposityException;	
	public List<BinaryContent> selectBinaryContentsByDescription(BaseContentObject contentObject,String descriptionValue) throws ContentReposityException;	
	public List<TextContent> selectTextContentsByByDescription(BaseContentObject contentObject, String descriptionValue) throws ContentReposityException;
	
	public List<BinaryContent> selectBinaryContentsByCreateDate(BaseContentObject contentObject,Date fromDateValue,Date toDateValue) throws ContentReposityException;	
	public List<TextContent> selectTextContentsByCreateDate(BaseContentObject contentObject,Date fromDateValue,Date toDateValue) throws ContentReposityException;
	public List<BinaryContent> selectBinaryContentsByLastUpdateDate(BaseContentObject contentObject,Date fromDateValue,Date toDateValue) throws ContentReposityException;	
	public List<TextContent> selectTextContentsByLastUpdateDate(BaseContentObject contentObject,Date fromDateValue,Date toDateValue) throws ContentReposityException;	
	
	public List<TextContent> selectTextContentsByEncoding(BaseContentObject contentObject,String encodingValue) throws ContentReposityException;
	//this method need redesign
	public List<BinaryContent> selectBinaryContentsByFullTextSearch(BaseContentObject contentObject,String contentValue) throws ContentReposityException;
}
