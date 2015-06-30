package com.viewfunction.contentRepository.contentBureauImpl;

import com.viewfunction.contentRepository.contentBureau.PermissionObject;

public class JCRPermissionObjectImpl implements PermissionObject {
	
	private boolean displayContentPermission;
	private boolean addContentPermission;
	private boolean addSubFolderPermission;
	private boolean deleteContentPermission;
	private boolean deleteSubFolderPermission;
	private boolean editContentPermission;
	private boolean configPermissionPermission;	
	
	private String permissionParticipant;
	private String permissionScope;	
	
	@Override
	public String getPermissionParticipant() {
		return permissionParticipant;
	}
	
	@Override
	public void setPermissionParticipant(String permissionParticipant) {
		this.permissionParticipant = permissionParticipant;
	}
	
	@Override
	public String getPermissionScope() {
		return permissionScope;
	}
	
	@Override
	public void setPermissionScope(String permissionScope) {
		this.permissionScope = permissionScope;
	}
	@Override
	public boolean getDisplayContentPermission() {
		return displayContentPermission;
	}
	@Override
	public void setDisplayContentPermission(boolean displayContentPermission) {
		this.displayContentPermission = displayContentPermission;
	}
	@Override
	public boolean getAddContentPermission() {
		return addContentPermission;
	}
	@Override
	public void setAddContentPermission(boolean addContentPermission) {
		this.addContentPermission = addContentPermission;
	}
	@Override
	public boolean getDeleteContentPermission() {
		return deleteContentPermission;
	}
	@Override
	public void setDeleteContentPermission(boolean deleteContentPermission) {
		this.deleteContentPermission = deleteContentPermission;
	}
	@Override
	public boolean getEditContentPermission() {
		return editContentPermission;
	}
	@Override
	public void setEditContentPermission(boolean editContentPermission) {
		this.editContentPermission = editContentPermission;
	}
	@Override
	public boolean getConfigPermissionPermission() {
		return configPermissionPermission;
	}
	@Override
	public void setConfigPermissionPermission(boolean configPermissionPermission) {
		this.configPermissionPermission = configPermissionPermission;
	}
	@Override
	public boolean getAddSubFolderPermission() {		
		return addSubFolderPermission;
	}
	@Override
	public void setAddSubFolderPermission(boolean addSubFolderPermission) {
		this.addSubFolderPermission=addSubFolderPermission;		
	}

	@Override
	public boolean getDeleteSubFolderPermission() {		
		return deleteSubFolderPermission;
	}

	@Override
	public void setDeleteSubFolderPermission(boolean deleteSubFolderPermission) {
		this.deleteSubFolderPermission=deleteSubFolderPermission;		
	}	
}