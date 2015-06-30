package com.viewfunction.contentRepository.security;

import java.security.Principal;
import java.util.Date;

import javax.jcr.Credentials;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.core.security.authentication.Authentication;
import org.apache.jackrabbit.core.security.simple.SimpleLoginModule;

import com.viewfunction.contentRepository.contentBureauImpl.JCRContentReposityConstant;
import com.viewfunction.contentRepository.util.PerportyHandler;
import com.viewfunction.contentRepository.util.exception.ContentReposityLoginException;
import com.viewfunction.contentRepository.util.exception.ContentReposityRuntimeException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.SecurityOperationHelper;

public class LDAPLoginModule extends SimpleLoginModule{	
	
	private static String userAuthenticationMethod;
	
	public LDAPLoginModule(){
		super();		
	}	

    protected Authentication getAuthentication(Principal principal, Credentials creds) throws ContentReposityLoginException {
        if (principal instanceof Group) {
            return null;
        }
        return new Authentication() {
			@Override
			public boolean authenticate(Credentials credentials)throws RepositoryException {
				if(!(credentials instanceof SimpleCredentials)){
					return false;
				}else{					
					try {
						if(getUserAuthenticationMethod().equals(SecurityOperationConstant.USER_AUTHENTICATION_METHOD_LDAP)){							
							Date loginRequestTime=(Date)((SimpleCredentials)credentials).getAttribute(JCRContentReposityConstant.USER_LOGIN_TIME);																	
							if(loginRequestTime!=null){
								//System.out.println(loginRequestTime.getTime());
								//check if use cached data to avoid LDAP query
							}
							//get LDAP login here
							String userGroup=((SimpleCredentials)credentials).getAttribute(SecurityOperationConstant.USER_GROUP).toString();
							String userID=((SimpleCredentials)credentials).getUserID();
							char[] passwordArray=((SimpleCredentials)credentials).getPassword();
							String pwd=new String(passwordArray);
							SecurityOperationHelper soh=ContentComponentFactory.getSecurityOperationHelper();							
							boolean authResult=soh.LDAPUserAuthentication(userID, userGroup, pwd);						
							return authResult;
						}
					} catch (ContentReposityRuntimeException e) {
						ContentReposityLoginException cpe=new ContentReposityLoginException();
						cpe.initCause(e);
						throw cpe;
					}
				}				
				return true;
			}
			@Override
			public boolean canHandle(Credentials credentials) {				
				return true;
			}
        };
    }

    private static String getUserAuthenticationMethod() throws ContentReposityRuntimeException{
    	if(userAuthenticationMethod==null){
    		userAuthenticationMethod=PerportyHandler.getPerportyValue(PerportyHandler.USER_AUTHENTICATION_METHOD);
    	}   	
    	return userAuthenticationMethod;
    }
    
    protected Principal getPrincipal(Credentials credentials) {
        String userId = getUserID(credentials);
        Principal principal = principalProvider.getPrincipal(userId);
        if (principal == null || principal instanceof Group) {            
            return null;
        } else {
            return principal;
        }
    }
}