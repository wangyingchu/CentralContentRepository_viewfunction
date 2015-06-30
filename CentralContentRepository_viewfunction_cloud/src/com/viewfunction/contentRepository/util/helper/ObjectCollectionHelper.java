package com.viewfunction.contentRepository.util.helper;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public interface ObjectCollectionHelper {
	public ContentObjectInheritanceChain getParentContentObjectsChain(ContentSpace contentSpace,BaseContentObject bco) throws ContentReposityException;
	
	public boolean moveContentObject(BaseContentObject childContentObject,BaseContentObject newParentContentObject,boolean recordVersion) throws ContentReposityException;
}
