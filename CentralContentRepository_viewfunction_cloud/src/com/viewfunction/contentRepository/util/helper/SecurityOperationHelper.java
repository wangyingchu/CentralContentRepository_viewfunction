package com.viewfunction.contentRepository.util.helper;

import java.util.List;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.PermissionObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.exception.ContentReposityLoginException;

public interface SecurityOperationHelper {	
	public boolean LDAPUserAuthentication(String userName, String userGroup,String userPWD)throws ContentReposityLoginException;	
	public List<PermissionObject> getContentPermissions(BaseContentObject contentObject) throws ContentReposityException;
	public boolean setContentPermissions(BaseContentObject contentObject,List<PermissionObject> permissionList) throws ContentReposityException;
}
