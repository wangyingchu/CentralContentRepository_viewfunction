package com.viewfunction.contentRepository.util.helper;

import java.util.Calendar;
import java.util.List;

import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public interface BinaryContentVersionObject {
	public void setMetaVersionData(Object contentData);	
	public String[] getCurrentVersionLabels() throws ContentReposityException;
	public Calendar getCurrentVersionCreatedDate() throws ContentReposityException;
	
	public String getCurrentVersionNumber() throws ContentReposityException;	
	public List<BinaryContentVersionObject> getAllVersionsInSpace() throws ContentReposityException;
	public BinaryContentVersionObject getSuccessorVersionObject()throws ContentReposityException; 
	public BinaryContentVersionObject getPredecessorVersionObject() throws ContentReposityException; 
	
	public BinaryContent getBinaryContent() throws ContentReposityException;
	public void setBinaryContent(BinaryContent binaryContent);
	public boolean isBaseVersion();
}