package com.viewfunction.contentRepository.contentBureau;

import java.util.List;

import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public interface BaseContentObject {
	public String getContentObjectName() throws ContentReposityException;	
	//Operation property
	public List<ContentObjectProperty> getProperties() throws ContentReposityException;	
	public ContentObjectProperty addProperty(Object propertyKey,Object propertyValue,boolean recordVersion) throws ContentReposityException;
	public boolean addProperty(List<ContentObjectProperty> contentObjectList,boolean recordVersion) throws ContentReposityException;
	public ContentObjectProperty getProperty(Object propertyKey) throws ContentReposityException;
	public boolean removeProperty(Object propertyKey,boolean recordVersion) throws ContentReposityException;
	public boolean removeProperty(ContentObjectProperty property,boolean recordVersion) throws ContentReposityException;
	public ContentObjectProperty updateProperty(ContentObjectProperty propertyValue,boolean recordVersion) throws ContentReposityException;
	public boolean renameProperty(Object propertyID,Object newPropertyID,boolean recordVersion) throws ContentReposityException;
		
	//Operation object	
	public List<BaseContentObject> getSubContentObjects(Object subContentObjectsKey) throws ContentReposityException;
	public long getSubContentObjectsCount() throws ContentReposityException;
	public BaseContentObject getSubContentObject(Object subContentObjectsKey) throws ContentReposityException;	
	public BaseContentObject getParentContentObject() throws ContentReposityException;	
	public BaseContentObject addSubContentObject(Object subContentKey,List<ContentObjectProperty> contentObjectList,boolean recordVersion) throws ContentReposityException;
	public boolean removeSubContentObject(Object subContentKey,boolean recordVersion) throws ContentReposityException;
	public boolean renameSubContentObject(Object subContentKey,Object newsubContentKey,boolean recordVersion) throws ContentReposityException;
	public boolean rename(Object newsubContentKey,boolean recordVersion) throws ContentReposityException;
	
	//Operation link
	public boolean addSubLinkContentObject(Object subContentKey,BaseContentObject linkedContentObject,boolean recordVersion) throws ContentReposityException;
	public boolean isLinkContentObject() throws ContentReposityException;
		
	//Operation version	
	public VersionObject getCurrentVersion() throws ContentReposityException;
	public List<VersionObject>getAllLinearVersions() throws ContentReposityException;
	public List<VersionObject> getAllVersionsInSpace() throws ContentReposityException;
	public boolean restoreToVersion(String targetVersion,boolean removeExisting) throws ContentReposityException;
	
	//Operation lock
	public LockObject lock(boolean isContentSpaceScoped) throws ContentReposityException;
	public boolean unlock() throws ContentReposityException;
	public boolean isLocked() throws ContentReposityException;
	public LockObject getLockOject() throws ContentReposityException;	
	public LockObject lock(boolean isContentSpaceScoped,String locker) throws ContentReposityException;
	public boolean unlock(String locker) throws ContentReposityException;
	public String getLocker() throws ContentReposityException;	
	
	//event Listener
	public boolean addContentObjectEventListener(int eventListenerType)throws ContentReposityException;
	public boolean removeContentObjectEventListener()throws ContentReposityException;
}
