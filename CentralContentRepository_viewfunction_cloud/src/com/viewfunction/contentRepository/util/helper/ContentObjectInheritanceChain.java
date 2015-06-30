package com.viewfunction.contentRepository.util.helper;

import java.util.List;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;

public interface ContentObjectInheritanceChain {
	
	public int getInheritanceDepth();
	public RootContentObject getRootContentObject();	
	public List<BaseContentObject> getParentContentObjectChain();	
	public String getContentObjectSpacePath();
	
}
