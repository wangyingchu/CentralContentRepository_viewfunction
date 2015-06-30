package com.viewfunction.contentRepository.contentBureau;

import java.util.List;

import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public interface BaseRootContentObject{
	public ContentSpace getParentContentSpace();
	public List<RootContentObject> getNeighborRootContentObjects() throws ContentReposityException;
}
