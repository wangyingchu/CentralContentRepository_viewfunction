package com.viewfunction.contentRepository.contentBureau;

import java.util.List;

import com.viewfunction.contentRepository.util.exception.ContentReposityException;

// content Bureau have muitl-Content Space,each Content Object belongs to only one content space
public interface ContentSpace {
	public List<RootContentObject> getRootContentObjects() throws ContentReposityException;
	public RootContentObject getRootContentObject(Object rootContentObjectPK) throws ContentReposityException;
	public RootContentObject addRootContentObject(RootContentObject rootContentObject) throws ContentReposityException;
	public boolean removeRootContentObject(String rootContentObjectID) throws ContentReposityException;
	public String getContentSpaceName();
	public void closeContentSpace();
	public BaseContentObject getContentObjectByAbsPath(String contentObjectPath)throws ContentReposityException;
	public void syncContentSpace()throws ContentReposityException ;
}
