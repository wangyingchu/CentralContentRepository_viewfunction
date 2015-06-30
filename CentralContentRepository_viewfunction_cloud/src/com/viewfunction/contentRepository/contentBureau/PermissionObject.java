package com.viewfunction.contentRepository.contentBureau;

public interface PermissionObject {
	
	public static String PermissionScope_Owner="OWNER";
	public static String PermissionScope_Group="GROUP";
	public static String PermissionScope_Other="OTHER";
	
	public boolean getDisplayContentPermission();
	public boolean getAddContentPermission();
	public boolean getAddSubFolderPermission();
	public boolean getDeleteContentPermission();
	public boolean getDeleteSubFolderPermission();	
	public boolean getEditContentPermission();
	public boolean getConfigPermissionPermission();	
	
	public void setDisplayContentPermission(boolean displayContentPermission);
	public void setAddContentPermission(boolean addContentPermission);
	public void setAddSubFolderPermission(boolean addSubFolderPermission);
	public void setDeleteContentPermission(boolean deleteContentPermission);
	public void setDeleteSubFolderPermission(boolean deleteSubFolderPermission);
	public void setEditContentPermission(boolean editContentPermission);
	public void setConfigPermissionPermission(boolean configPermissionPermission);	
	
	public String getPermissionParticipant();
	public void setPermissionParticipant(String permissionParticipant);
	public String getPermissionScope();
	public void setPermissionScope(String permissionScope);
}