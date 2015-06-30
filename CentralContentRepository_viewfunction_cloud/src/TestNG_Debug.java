import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.PropertyType;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.document.DocumentMK;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStore;
import org.apache.jackrabbit.value.ValueFactoryImpl;
import org.testng.Assert;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.LockObject;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentReposityConstant;
import com.viewfunction.contentRepository.security.SecurityOperationConstant;
import com.viewfunction.contentRepository.testNGTest.TestCaseDataConstant;
import com.viewfunction.contentRepository.util.PerportyHandler;
import com.viewfunction.contentRepository.util.exception.ContentReposityDataException;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.exception.ContentReposityRuntimeException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

/*

			System.out.println("===========+++++++++++++++===========");
			System.out.println("===========+++++++++++++++===========");
			System.out.println();
			System.out.println("===========+++++++++++++++===========");
			System.out.println("===========+++++++++++++++===========");

*/
public class TestNG_Debug {
	public void testCreateContentSpaces(){	

		ContentSpace cs=null;
		ContentSpace cs2=null;
		ContentSpace cs3=null;
		ContentSpace cs4=null;
		ContentSpace cs5=null;
		try {
			String BUILDIN_ADMINISTRATOR_ACCOUNT=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT);
			String BUILDIN__ADMINISTRATOR_ACCOUNT_PWD=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN__ADMINISTRATOR_ACCOUNT_PWD);
			
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			
			RootContentObject rco0=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			cs.addRootContentObject(rco0);
			
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);				
			BaseContentObject bco1=rco.addSubContentObject("LockTestContentObject", null, true);
			Assert.assertNotNull(bco1);
			
			LockObject lo1=bco1.lock(false);
			Assert.assertNotNull(lo1);			
			
			Assert.assertEquals(lo1.getLocker(), BUILDIN_ADMINISTRATOR_ACCOUNT);			
			Assert.assertEquals(false, lo1.isTemporaryLock());
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}	
			
			cs2 = ContentComponentFactory.connectContentSpace(BUILDIN_ADMINISTRATOR_ACCOUNT,BUILDIN__ADMINISTRATOR_ACCOUNT_PWD,TestCaseDataConstant.testContentSpaceName);
			ContentObject rco2=cs2.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			BaseContentObject bco1Again=rco2.getSubContentObject("LockTestContentObject");
			
			Assert.assertTrue(bco1Again.isLocked());
			LockObject lo2=bco1Again.getLockOject();
			Assert.assertNotNull(lo2);
			Assert.assertEquals(lo2.getLocker(), BUILDIN_ADMINISTRATOR_ACCOUNT);			
			Assert.assertEquals(lo2.isTemporaryLock(),false);			
		
			try{
				bco1Again.addProperty("pro_1", "pro_1_v", true);
				Assert.fail("Should throw ContentReposityDataException");
			}catch(ContentReposityDataException e){}			
			cs2.closeContentSpace();
			
			//locked object can't edit
			try{
				bco1.addProperty("pro_0", "pro_0_v", true);
				Assert.fail("Should throw ContentReposityDataException");
			}catch(ContentReposityDataException e){}	
			Assert.assertTrue(bco1.isLocked());
			boolean unlockResult=bco1.unlock();
			Assert.assertTrue(unlockResult);
			bco1.addProperty("pro_0", "pro_0_v", true);
			Assert.assertEquals(bco1.getProperty("pro_0").getPropertyValue().toString(),"pro_0_v");
			Assert.assertFalse(bco1.isLocked());
			LockObject lo3=bco1.getLockOject();
			Assert.assertNull(lo3);			
			cs.closeContentSpace();	
			
			cs3 = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco3=cs3.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco3);				
			BaseContentObject bco3=rco3.addSubContentObject("LockTestContentObject2", null, true);
			Assert.assertNotNull(bco3);	
			LockObject lo4=bco3.lock(true);
			Assert.assertTrue(bco3.isLocked());	
			Assert.assertTrue(lo4.isTemporaryLock());
		
			BaseContentObject bco3_1=rco3.addSubContentObject("LockTestContentObject3", null, true);
			LockObject lo4_1=bco3_1.lock(false);
			Assert.assertTrue(bco3_1.isLocked());
			Assert.assertFalse(lo4_1.isTemporaryLock());	
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}	
			
			cs4 = ContentComponentFactory.connectContentSpace(BUILDIN_ADMINISTRATOR_ACCOUNT,BUILDIN__ADMINISTRATOR_ACCOUNT_PWD,TestCaseDataConstant.testContentSpaceName);
			ContentObject rco4=cs4.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco4);				
			BaseContentObject bco4=rco4.getSubContentObject("LockTestContentObject2");
			Assert.assertNotNull(bco4);				
			Assert.assertTrue(bco4.isLocked());	
			try{
				bco4.addProperty("pro_boc4", "pro_boc4_v", true);
				Assert.fail("Should throw ContentReposityDataException");
			}catch(ContentReposityDataException e){}
			
			LockObject lo5=bco4.getLockOject();		
			
			Assert.assertTrue(lo5.isTemporaryLock());			
			cs3.closeContentSpace();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}	
			
			Assert.assertFalse(bco4.isLocked());	
			bco4.addProperty("pro_boc5", "pro_boc5_v", true);	
			
			BaseContentObject bco4_2=rco4.getSubContentObject("LockTestContentObject3");
			Assert.assertTrue(bco4_2.isLocked());
			LockObject lo6=bco4_2.getLockOject();			
			
			Assert.assertFalse(lo6.isTemporaryLock());	
			
			boolean br=bco4_2.unlock("someuser");
			Assert.assertFalse(br);	
			Assert.assertTrue(bco4_2.isLocked());			
			cs4.closeContentSpace();
					
			cs5 = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco5=cs5.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco5);				
			BaseContentObject bco5=rco5.getSubContentObject("LockTestContentObject3");
			Assert.assertNotNull(bco5);		
			
			Assert.assertTrue(bco5.isLocked());
			LockObject lo7=bco5.getLockOject();			
			
			Assert.assertFalse(lo7.isTemporaryLock());	
			boolean br2=bco5.unlock();
			Assert.assertTrue(br2);	
			Assert.assertFalse(bco5.isLocked());
			cs5.closeContentSpace();
			
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();	
			cs2.closeContentSpace();
			cs3.closeContentSpace();
			cs4.closeContentSpace();
			cs5.closeContentSpace();
			Assert.fail("got ContentReposityException during testLock_Unlock");
		}	
		
        
	}
	
	public static void main(String[] args){
		new TestNG_Debug().testCreateContentSpaces();
	}
}
