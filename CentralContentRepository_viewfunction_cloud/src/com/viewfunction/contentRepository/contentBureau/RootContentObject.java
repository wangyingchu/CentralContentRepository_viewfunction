package com.viewfunction.contentRepository.contentBureau;

public interface RootContentObject extends BaseRootContentObject,ContentObject{
	public String getRootContentObjectID();
	public void setParentContentSpace(ContentSpace parentContentSpace);
}
