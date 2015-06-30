package com.viewfunction.contentRepository.contentBureauImpl;

import java.util.List;

import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public class JCRRootContentObjectImpl extends JCRContentObjectImpl implements RootContentObject {
	private String rootContentObjectID;
	private ContentSpace parentContentSpace;
	
	public JCRRootContentObjectImpl(Object rootContentObjectIDObj) {
		this.rootContentObjectID=rootContentObjectIDObj.toString();
	}

	@Override
	public List<RootContentObject> getNeighborRootContentObjects() throws ContentReposityException {
		List<RootContentObject> nrcbls=this.getParentContentSpace().getRootContentObjects();		
		for(int i=0;i<nrcbls.size();i++){
			RootContentObject crco=nrcbls.get(i);
			if(crco.getRootContentObjectID().equals(this.getRootContentObjectID())){
				nrcbls.remove(i);				
			}			
		}		
		return nrcbls;		
	}

	@Override
	public ContentSpace getParentContentSpace() {		
		return this.parentContentSpace;
	}

	@Override
	public String getRootContentObjectID() {		
		return rootContentObjectID;
	}

	@Override
	public void setParentContentSpace(ContentSpace parentContentSpace) {
		this.parentContentSpace=parentContentSpace;		
	}

}
