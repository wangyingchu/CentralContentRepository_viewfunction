package com.viewfunction.contentRepository.testNGTest;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.viewfunction.contentRepository.util.exception.ContentReposityLoginException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.SecurityOperationHelper;

public class TestNG_SecurityOperationHelperTestCase {
	@Test
	public void testLDAPUserAuthentication(){		
		SecurityOperationHelper soh=ContentComponentFactory.getSecurityOperationHelper();		
		try {
			boolean croot1=soh.LDAPUserAuthentication("root", "sysAdministrator", "viewfunctionroot");			
			Assert.assertTrue(croot1);				
			boolean croot2=soh.LDAPUserAuthentication("root", "sysAdministratorBad", "viewfunctionroot");			
			Assert.assertFalse(croot2);			
			boolean croot3=soh.LDAPUserAuthentication("root", "sysAdministrator", "viewfunctionroot1");			
			Assert.assertFalse(croot3);			
			boolean croot4=soh.LDAPUserAuthentication("wangychu", "sysAdministrator", "wangychu");			
			Assert.assertTrue(croot4);			
			boolean croot5=soh.LDAPUserAuthentication("wangychu", "sysAdministratorBad", "wangyhcu");			
			Assert.assertFalse(croot5);			
			boolean croot6=soh.LDAPUserAuthentication("wangychu", "sysAdministrator", "wangychuBad");			
			Assert.assertFalse(croot6);			
		} catch (ContentReposityLoginException e) {
			e.printStackTrace();			
			Assert.fail("got ContentReposityException during testLDAPUserAuthentication");
		}		
	}
}