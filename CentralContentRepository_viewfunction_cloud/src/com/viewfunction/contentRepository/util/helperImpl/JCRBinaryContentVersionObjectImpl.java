package com.viewfunction.contentRepository.util.helperImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.viewfunction.contentRepository.contentBureau.VersionObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.BinaryContent;
import com.viewfunction.contentRepository.util.helper.BinaryContentVersionObject;

public class JCRBinaryContentVersionObjectImpl implements BinaryContentVersionObject{
	private VersionObject baseVersionObject=null;	
	private BinaryContent binaryContent=null;
	@Override
	public void setMetaVersionData(Object contentData) {
		this.baseVersionObject=(VersionObject)contentData;		
	}	

	@Override
	public String[] getCurrentVersionLabels() throws ContentReposityException {		
		return this.baseVersionObject.getCurrentVersionLabels();		
	}

	@Override
	public Calendar getCurrentVersionCreatedDate() throws ContentReposityException {		
		return this.baseVersionObject.getCurrentVersionCreatedDate();
	}

	@Override
	public String getCurrentVersionNumber() throws ContentReposityException {	
		if(this.baseVersionObject==null){
			return "0.0";
		}
		return this.baseVersionObject.getCurrentVersionNumber();
	}

	@Override
	public List<BinaryContentVersionObject> getAllVersionsInSpace()	throws ContentReposityException {		
		List<VersionObject> versionObjectList=this.baseVersionObject.getAllVersionsInSpace();	
		List<BinaryContentVersionObject> binaryContentVersionObjectList=new ArrayList<BinaryContentVersionObject>();
		for(VersionObject versionObject:versionObjectList){
			BinaryContentVersionObject currentBinaryContentVersionObject=ContentComponentFactory.createBinaryContentVersionObject();			 
			currentBinaryContentVersionObject.setMetaVersionData(versionObject);			
			binaryContentVersionObjectList.add(currentBinaryContentVersionObject);			
		}		
		return binaryContentVersionObjectList;
	}

	@Override
	public BinaryContentVersionObject getSuccessorVersionObject() throws ContentReposityException {		
		VersionObject versionObject=this.baseVersionObject.getSuccessorVersionObject();
		BinaryContentVersionObject currentBinaryContentVersionObject=ContentComponentFactory.createBinaryContentVersionObject();			 
		currentBinaryContentVersionObject.setMetaVersionData(versionObject);		
		return currentBinaryContentVersionObject;
	}

	@Override
	public BinaryContentVersionObject getPredecessorVersionObject()	throws ContentReposityException {
		VersionObject versionObject=this.baseVersionObject.getPredecessorVersionObject();
		BinaryContentVersionObject currentBinaryContentVersionObject=ContentComponentFactory.createBinaryContentVersionObject();			 
		currentBinaryContentVersionObject.setMetaVersionData(versionObject);		
		return currentBinaryContentVersionObject;
	}	

	@Override
	public boolean isBaseVersion() {		
		return this.baseVersionObject.isBaseVersion();
	}	

	@Override
	public BinaryContent getBinaryContent() throws ContentReposityException {		
		return this.binaryContent;
	}

	@Override
	public void setBinaryContent(BinaryContent binaryContent) {
		this.binaryContent=binaryContent;		
	}
}
