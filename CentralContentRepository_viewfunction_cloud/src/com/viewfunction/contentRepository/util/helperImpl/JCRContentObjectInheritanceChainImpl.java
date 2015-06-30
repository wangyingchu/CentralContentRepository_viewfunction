package com.viewfunction.contentRepository.util.helperImpl;

import java.util.List;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.helper.ContentObjectInheritanceChain;

public class JCRContentObjectInheritanceChainImpl implements ContentObjectInheritanceChain{

	private String contentObjectSpacePath;
	private int inheritanceDepth;
	private List<BaseContentObject> parentContentObjectChain;
	private RootContentObject rootContentObject;	
	
	@Override
	public String getContentObjectSpacePath() {		
		return this.contentObjectSpacePath;
	}

	@Override
	public int getInheritanceDepth() {		
		return this.inheritanceDepth;
	}

	@Override
	public List<BaseContentObject> getParentContentObjectChain() {
		return this.parentContentObjectChain;
	}

	@Override
	public RootContentObject getRootContentObject() {		
		return this.rootContentObject;
	}

	public void setContentObjectSpacePath(String contentObjectSpacePath) {
		this.contentObjectSpacePath = contentObjectSpacePath;
	}

	public void setInheritanceDepth(int inheritanceDepth) {
		this.inheritanceDepth = inheritanceDepth;
	}

	public void setParentContentObjectChain(List<BaseContentObject> parentContentObjectChain) {
		this.parentContentObjectChain = parentContentObjectChain;
	}

	public void setRootContentObject(RootContentObject rootContentObject) {
		this.rootContentObject = rootContentObject;
	}

}
