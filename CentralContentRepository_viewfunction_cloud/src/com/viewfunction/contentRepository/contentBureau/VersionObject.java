package com.viewfunction.contentRepository.contentBureau;

import java.util.Calendar;
import java.util.List;

import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public interface VersionObject{
	public void setBaseVersionData(Object contentData);
	public void setVersionSession(Object contentSession);
	public String[] getCurrentVersionLabels() throws ContentReposityException;
	public Calendar getCurrentVersionCreatedDate() throws ContentReposityException;
	
	public String getCurrentVersionNumber() throws ContentReposityException;	
	public List<VersionObject> getAllVersionsInSpace() throws ContentReposityException;
	public VersionObject getSuccessorVersionObject()throws ContentReposityException; 
	public VersionObject getPredecessorVersionObject() throws ContentReposityException; 
	
	public ContentObject getCurrentContentObject();
	public boolean isBaseVersion();
}
