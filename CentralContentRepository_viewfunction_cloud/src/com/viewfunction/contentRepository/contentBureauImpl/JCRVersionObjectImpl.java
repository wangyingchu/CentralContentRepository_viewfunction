package com.viewfunction.contentRepository.contentBureauImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;

import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.VersionObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityDataException;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class JCRVersionObjectImpl implements VersionObject{
	private Node jcrBaseVersionNode;
	private Session jcrSession;
	private VersionHistory jcrVersionHistory;
	private Version jcrVersion;
	private boolean isBaseVersion=true;	
	private Node jcrFrozenNode;
	
	@Override
	public Calendar getCurrentVersionCreatedDate() throws ContentReposityDataException,ContentReposityException {			
		try {			
			if(isBaseVersion()){
				return getJcrSession().getWorkspace().getVersionManager().getBaseVersion(getJcrNode().getPath()).getCreated();
			}else{
				return getJcrVersion().getCreated();				
			}			
		} catch (UnsupportedRepositoryOperationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}			
	}	

	@Override
	public String[] getCurrentVersionLabels() throws ContentReposityDataException,ContentReposityException  {		
		Version cv;
		try {
			if(isBaseVersion()){
				cv = getJcrSession().getWorkspace().getVersionManager().getBaseVersion(getJcrNode().getPath());
				String[] labelStringArray=getVersionHistory().getVersionLabels(cv);
				return labelStringArray;
			}else{				
				String[] vla=getVersionHistory().getVersionLabels(getJcrVersion());				
				return vla;
			}
		} catch (UnsupportedRepositoryOperationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}				
	}
	
	@Override
	public String getCurrentVersionNumber() throws ContentReposityException {
		try {
			String versionNumber;
			if(isBaseVersion()){				
				versionNumber= getJcrSession().getWorkspace().getVersionManager().getBaseVersion(getJcrNode().getPath()).getName();
			}else{
				versionNumber= getJcrVersion().getName();
			}	
			if(versionNumber.equals("jcr:rootVersion")){
				return ""+0.0;
			}
			return versionNumber;
		} catch (UnsupportedRepositoryOperationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}
		
	@Override
	public ContentObject getCurrentContentObject() {		
		ContentObject co=ContentComponentFactory.createContentObject();			
		if(isBaseVersion()){				
			co.setContentData(getJcrNode());
		}else{	
			co.setContentData(getJcrFrozenNode());			
		}		
		co.setContentSession(getJcrSession());	
		return co;
	}

	@Override
	public VersionObject getPredecessorVersionObject() throws ContentReposityException {		
		try {
			Version pv;
			JCRVersionObjectImpl pvb=new JCRVersionObjectImpl();			
			if(isBaseVersion()){					
				pv = getJcrSession().getWorkspace().getVersionManager().getBaseVersion(getJcrNode().getPath()).getLinearPredecessor();					
			}else{						
				pv=getJcrVersion().getLinearPredecessor();						
			}			
			if(pv.getName().equals("jcr:rootVersion")){
				return null;
			}			
			pvb.setJcrVersion(pv);
			pvb.setBaseVersionData(getJcrNode());
			pvb.setVersionSession(getJcrSession());
			pvb.setBaseVersion(false);
			pvb.setJcrFrozenNode(pv.getFrozenNode());	
				
			return pvb;
		} catch (UnsupportedRepositoryOperationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}			
	}

	@Override
	public VersionObject getSuccessorVersionObject() throws ContentReposityDataException,ContentReposityException {
		try {					
			Version sv; 
			JCRVersionObjectImpl svb=new JCRVersionObjectImpl();
			if(isBaseVersion()){		
				return null;								
			}else{		
				sv=getJcrVersion().getLinearSuccessor();						
			}			
			svb.setJcrVersion(sv);
			svb.setBaseVersionData(getJcrNode());
			svb.setVersionSession(getJcrSession());				
			
			String baseVersionName=getJcrSession().getWorkspace().getVersionManager().getBaseVersion(getJcrNode().getPath()).getName();				
			if(sv.getName().equals(baseVersionName)){
				svb.setBaseVersion(true);
			}else{
				svb.setBaseVersion(false);
				svb.setJcrFrozenNode(sv.getFrozenNode());				
			}						
			return svb;
		} catch (UnsupportedRepositoryOperationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}

	@Override
	public void setBaseVersionData(Object contentData) {
		this.setJcrNode((Node)contentData);		
	}

	@Override
	public void setVersionSession(Object contentSession) {
		this.setJcrSession((Session)contentSession);		
	}

	private void setJcrNode(Node jcrNode) {
		this.jcrBaseVersionNode = jcrNode;
	}

	private Node getJcrNode() {
		return this.jcrBaseVersionNode;
	}

	private void setJcrSession(Session jcrSession) {
		this.jcrSession = jcrSession;
	}

	private Session getJcrSession() {
		return this.jcrSession;
	}
	
	private VersionHistory getVersionHistory() throws ContentReposityException{
		if(jcrVersionHistory==null){
			try {
				this.jcrVersionHistory=getJcrSession().getWorkspace().getVersionManager().getVersionHistory(getJcrNode().getPath());
			} catch (UnsupportedRepositoryOperationException e) {
				ContentReposityDataException cpe=new ContentReposityDataException();
				cpe.initCause(e);
				throw cpe;
			} catch (RepositoryException e) {
				ContentReposityException cpe=new ContentReposityException();
				cpe.initCause(e);
				throw cpe;
			}
		}		
		return this.jcrVersionHistory;		
	}

	
	private void setBaseVersion(boolean isBaseVersion) {
		this.isBaseVersion = isBaseVersion;
	}	

	public boolean isBaseVersion() {
		return isBaseVersion;
	}

	private void setJcrVersion(Version jcrVersion) {
		this.jcrVersion = jcrVersion;
	}

	private Version getJcrVersion() {
		return jcrVersion;
	}

	private void setJcrFrozenNode(Node jcrFrozenNode) {
		this.jcrFrozenNode = jcrFrozenNode;
	}

	private Node getJcrFrozenNode() {
		return jcrFrozenNode;
	}	
	
	public List<VersionObject> getAllVersionsInSpace() throws ContentReposityDataException,ContentReposityException{
		try {
			VersionHistory jcrVersionHistory=getJcrSession().getWorkspace().getVersionManager().getVersionHistory(getJcrNode().getPath());		
			VersionIterator vi=jcrVersionHistory.getAllVersions();
			List<VersionObject> vl=new ArrayList<VersionObject>();			
			Version cv=null;
			Version bv=getJcrSession().getWorkspace().getVersionManager().getBaseVersion(getJcrNode().getPath());
			while(vi.hasNext()){
				cv=vi.nextVersion();					
				JCRVersionObjectImpl cvo=new JCRVersionObjectImpl();
				cvo.setJcrVersion(cv);
				cvo.setBaseVersionData(getJcrNode());
				cvo.setVersionSession(getJcrSession());
				cvo.setBaseVersion(cv.isSame(bv));
				cvo.setJcrFrozenNode(cv.getFrozenNode());
				vl.add(cvo);
			}
			return vl;
		} catch (UnsupportedRepositoryOperationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
	}
}
