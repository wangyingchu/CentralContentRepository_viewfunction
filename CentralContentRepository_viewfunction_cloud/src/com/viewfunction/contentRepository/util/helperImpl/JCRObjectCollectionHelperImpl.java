package com.viewfunction.contentRepository.util.helperImpl;

import java.util.ArrayList;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectImpl;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.ContentObjectInheritanceChain;
import com.viewfunction.contentRepository.util.helper.ObjectCollectionHelper;

public class JCRObjectCollectionHelperImpl implements ObjectCollectionHelper{

	@Override
	public ContentObjectInheritanceChain getParentContentObjectsChain(ContentSpace contentSpace,BaseContentObject bco) throws ContentReposityException {
		try {				
			JCRContentObjectImpl jcrBco=(JCRContentObjectImpl)bco;	
			JCRContentObjectInheritanceChainImpl coic= (JCRContentObjectInheritanceChainImpl)ContentComponentFactory.createContentObjectInheritanceChain();
			int depth=jcrBco.getJcrNode().getDepth();			
			coic.setInheritanceDepth(depth);
			String path=jcrBco.getJcrNode().getPath();
			coic.setContentObjectSpacePath(path);
			String rootContentObjectName=jcrBco.getJcrNode().getAncestor(1).getName();
			RootContentObject rco=contentSpace.getRootContentObject(rootContentObjectName);
			coic.setRootContentObject(rco);			
			ArrayList<BaseContentObject> parentOBC=new ArrayList<BaseContentObject>();			
			String childBcoName;
			BaseContentObject fatherbco=rco;
			BaseContentObject Childbco;
			for(int i=2;i<=depth-1;i++){
				childBcoName=jcrBco.getJcrNode().getAncestor(i).getName();				
				Childbco=fatherbco.getSubContentObjects(childBcoName).get(0);	
				parentOBC.add(Childbco);					
				fatherbco=Childbco;				
			}			
			coic.setParentContentObjectChain(parentOBC);
			return coic;
		}catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}			
	}

	@Override
	public boolean moveContentObject(BaseContentObject childContentObject,BaseContentObject newParentContentObject, boolean recordVersion) throws ContentReposityException {	
		try {		
			BaseContentObject orgParent=childContentObject.getParentContentObject();
			if(orgParent==null){
				return false;			
			}			
			JCRContentObjectImpl childJCRImpl=(JCRContentObjectImpl)childContentObject;
			JCRContentObjectImpl orgParentJCRImpl=(JCRContentObjectImpl)orgParent;
			JCRContentObjectImpl newParentJCRImpl=(JCRContentObjectImpl)newParentContentObject;
		
			String oldPath=childJCRImpl.getJcrNode().getPath();			
			String newPath=newParentJCRImpl.getJcrNode().getPath()+"/"+childContentObject.getContentObjectName();
			
			Session currentSession=childJCRImpl.getJcrSession();			
			currentSession.getWorkspace().getVersionManager().checkout(childJCRImpl.getJcrNode().getPath());
			currentSession.getWorkspace().getVersionManager().checkout(orgParentJCRImpl.getJcrNode().getPath());
			currentSession.getWorkspace().getVersionManager().checkout(newParentJCRImpl.getJcrNode().getPath());					
			currentSession.move(oldPath, newPath);
			currentSession.save();
		
			if(recordVersion){				
				currentSession.getWorkspace().getVersionManager().checkin(childJCRImpl.getJcrNode().getPath());
				String currentVersionName=currentSession.getWorkspace().getVersionManager().getBaseVersion(childJCRImpl.getJcrNode().getPath()).getName();				
				currentSession.getWorkspace().getVersionManager().getVersionHistory(childJCRImpl.getJcrNode().getPath()).addVersionLabel(currentVersionName, "V"+currentVersionName+" -> "+"moved from "+orgParentJCRImpl.getContentObjectName()+" to "+newParentJCRImpl.getContentObjectName(), false);								
				currentSession.getWorkspace().getVersionManager().checkin(orgParentJCRImpl.getJcrNode().getPath());
				currentVersionName=currentSession.getWorkspace().getVersionManager().getBaseVersion(orgParentJCRImpl.getJcrNode().getPath()).getName();
				currentSession.getWorkspace().getVersionManager().getVersionHistory(orgParentJCRImpl.getJcrNode().getPath()).addVersionLabel(currentVersionName, "V"+currentVersionName+" -> "+"sub ContentObject "+orgParentJCRImpl.getContentObjectName()+" moved to "+newParentJCRImpl.getContentObjectName(), false);									
				currentSession.getWorkspace().getVersionManager().checkin(newParentJCRImpl.getJcrNode().getPath());
				currentVersionName=currentSession.getWorkspace().getVersionManager().getBaseVersion(newParentJCRImpl.getJcrNode().getPath()).getName();
				currentSession.getWorkspace().getVersionManager().getVersionHistory(newParentJCRImpl.getJcrNode().getPath()).addVersionLabel(currentVersionName, "V"+currentVersionName+" -> "+"sub ContentObject "+orgParentJCRImpl.getContentObjectName()+" moved from "+orgParentJCRImpl.getContentObjectName(), false);				
			}
			return true;		
		}catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}
}
