package com.viewfunction.contentRepository.util.helper;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import com.viewfunction.contentRepository.contentBureau.CommentObject;
import com.viewfunction.contentRepository.contentBureau.LockObject;
import com.viewfunction.contentRepository.contentBureau.PermissionObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public interface BinaryContent {

	public Calendar getLastModified();
	public Calendar getCreated();	
	public String getCreatedBy();
	public String getLastModifiedBy();	
	public String getMimeType();
	public String getContentName();
	public String getContentDescription();
	public String getContentSpaceAbsPath() throws ContentReposityException;
	public InputStream getContentInputStream() throws ContentReposityException;
	public long getContentSize() throws ContentReposityException;
	
	public boolean isLinkObject() throws ContentReposityException;	
	
	public LockObject lock(boolean isContentSpaceScoped) throws ContentReposityException;
	public boolean unlock() throws ContentReposityException;
	public boolean isLocked() throws ContentReposityException;
	public LockObject lock(boolean isContentSpaceScoped,String locker) throws ContentReposityException;
	public boolean unlock(String locker) throws ContentReposityException;
	public String getLocker() throws ContentReposityException;	
	
	public String getCurrentVersion() throws ContentReposityException;		
	public List<BinaryContentVersionObject>getAllLinearVersions() throws ContentReposityException;
	public List<BinaryContentVersionObject> getAllVersionsInSpace() throws ContentReposityException;
	public boolean restoreToVersion(String targetVersion,boolean removeExisting) throws ContentReposityException;	
	
	public String[] getContentTags() throws ContentReposityException;
	public String[] addContentTags(String[] tag) throws ContentReposityException;
	public String[] removeContentTags(String[] tag) throws ContentReposityException;
	
	public boolean setSequenceNumber(long setSequenceNumber) throws ContentReposityException;
	public long getSequenceNumber() throws ContentReposityException;
	
	public boolean updateContentDescription(String contentDescription) throws ContentReposityException;
	
	public boolean addComment(CommentObject commentObject) throws ContentReposityException;
	public boolean removeComment(String commentId) throws ContentReposityException;
	public List<CommentObject> getComments() throws ContentReposityException;
	
	public List<PermissionObject> getContentPermissions() throws ContentReposityException;
	public boolean setContentPermissions(List<PermissionObject> permissionList) throws ContentReposityException;
}