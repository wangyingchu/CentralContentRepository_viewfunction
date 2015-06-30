package com.viewfunction.contentRepository.util.helper;

import java.io.File;
import java.util.List;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.LockObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public interface ContentOperationHelper {
	public final String CONTENTTYPE_FOLDEROBJECT="CONTENTTYPE_FOLDEROBJECT";
	public final String CONTENTTYPE_STANDALONEOBJECT="CONTENTTYPE_STANDALONEOBJECT";
	public final String CONTENTTYPE_BINARTCONTENT="CONTENTTYPE_BINARTCONTENT";
	public final String CONTENTTYPE_TEXTBINARY="CONTENTTYPE_TEXTBINARY";	
	
	public boolean addBinaryContent(BaseContentObject contentObject,File file,String contentDesc,boolean recordVersion) throws ContentReposityException;
	public boolean addBinaryContent(BaseContentObject contentObject,File file,String contentDesc,String createdBy,boolean recordVersion) throws ContentReposityException;
	
	public boolean updateBinaryContent(BaseContentObject contentObject,String binaryContentName,File newFile,String contentDesc,boolean recordVersion) throws ContentReposityException;
	public boolean updateBinaryContent(BaseContentObject contentObject,String binaryContentName,File newFile,String contentDesc,String lastModifiedBy,boolean recordVersion) throws ContentReposityException;
	
	public boolean removeBinaryContent(BaseContentObject contentObject,String contentName,boolean recordVersion) throws ContentReposityException;
	public BinaryContent getBinaryContent(BaseContentObject contentObject,String contentName) throws ContentReposityException;
	public BinaryContent getBinaryContent(BaseContentObject binaryContentObject) throws ContentReposityException;
	public List<BinaryContent> getBinaryContents(BaseContentObject contentObject) throws ContentReposityException;
	public BinaryContent getBinaryProperty(BaseContentObject contentObject,String propertyNameName) throws ContentReposityException;
	
	public boolean addTextContent(BaseContentObject contentObject,File file,String contentDesc,boolean recordVersion) throws ContentReposityException;
	public boolean addTextContent(BaseContentObject contentObject,File file,String contentDesc,String createdBy,boolean recordVersion) throws ContentReposityException;	
	public boolean updateTextContent(BaseContentObject contentObject,String textContentName,File newFile,String contentDesc,boolean recordVersion) throws ContentReposityException;
	public boolean updateTextContent(BaseContentObject contentObject,String textContentName,File newFile,String contentDesc,String lastModifiedBy,boolean recordVersion) throws ContentReposityException;
	public boolean removeTextContent(BaseContentObject contentObject,String contentName,boolean recordVersion) throws ContentReposityException;
	public TextContent getTextContent(BaseContentObject contentObject,String contentName) throws ContentReposityException;
	public List<TextContent> getTextContents(BaseContentObject contentObject) throws ContentReposityException;	
	public String getContentObjectType(BaseContentObject contentObject) throws ContentReposityException;			
	
	public LockObject lockBinaryContent(BaseContentObject contentObject,String contentName,boolean isContentSpaceScoped) throws ContentReposityException;
	public boolean unlockBinaryContent(BaseContentObject contentObject,String contentName) throws ContentReposityException;
	public boolean isBinaryContentLocked(BaseContentObject contentObject,String contentName) throws ContentReposityException;
}