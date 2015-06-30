package com.viewfunction.contentRepository.testNGTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;

import org.apache.jackrabbit.value.ValueFactoryImpl;
import org.testng.Assert;
import org.testng.annotations.*;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.LockObject;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureau.VersionObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectImpl;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectPropertyImpl;
import com.viewfunction.contentRepository.util.PerportyHandler;
import com.viewfunction.contentRepository.util.exception.ContentReposityDataException;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.observation.ContentSpaceEventListener;

public class TestNG_ContentObjectTestCase {	
	
	@BeforeClass 
	public void initTestingRootContentObjects(){
		System.out.println("initTestingRootContentObjects");
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			Assert.assertNotNull(cs);			
			RootContentObject rco=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			cs.addRootContentObject(rco);			
			RootContentObject addRrco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);			
			Assert.assertNotNull(addRrco);				
			RootContentObject rco0=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+0);
			cs.addRootContentObject(rco0);			
			RootContentObject rco1=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+1);
			cs.addRootContentObject(rco1);			
			RootContentObject rco2=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+2);
			cs.addRootContentObject(rco2);			
			RootContentObject rco3=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+3);
			cs.addRootContentObject(rco3);			
			RootContentObject rco4=ContentComponentFactory.createRootContentObject(TestCaseDataConstant.testRootContentObjectName+4);
			cs.addRootContentObject(rco4);			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during initTestingRootContentObjects");
		}			
	}
	
	@AfterClass
	public void removeTestingRootContentObjects(){
		System.out.println("removeTestingRootContentObjects");
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);			
			boolean rmExist1=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertTrue(rmExist1);			
			RootContentObject rmedRco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);	
			Assert.assertNull(rmedRco);			
			boolean rmExist1_0=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+0);
			Assert.assertTrue(rmExist1_0);
			boolean rmExist1_1=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+1);
			Assert.assertTrue(rmExist1_1);
			boolean rmExist1_2=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+2);
			Assert.assertTrue(rmExist1_2);
			boolean rmExist1_3=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+3);
			Assert.assertTrue(rmExist1_3);
			boolean rmExist1_4=cs.removeRootContentObject(TestCaseDataConstant.testRootContentObjectName+4);
			Assert.assertTrue(rmExist1_4);			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during removeTestingRootContentObjects");
		}			
	}	
	
	@Test
	public void testAdd_GetProperty(){		
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);			
			
			rco.addProperty("TestNG_testProperty_String","TestNG_testProperty_String_value",true);			
			ContentObjectProperty cop_str=rco.getProperty("TestNG_testProperty_String");
			Assert.assertEquals(cop_str.getPropertyName(), "TestNG_testProperty_String","Property's Name should be TestNG_testProperty_String");
			Assert.assertEquals(cop_str.getPropertyType(), PropertyType.STRING,"Property's Type should be String");
			Assert.assertEquals(cop_str.getPropertyValue().toString(), "TestNG_testProperty_String_value","Property's value should be TestNG_testProperty_String_value");
			Assert.assertEquals(cop_str.isMultiple(), false,"Property's isMultiple should be false");	
			
			rco.addProperty("TestNG_testProperty_String_arr",new String[]{"TestNG_testProperty_String_value1","TestNG_testProperty_String_value1"},true);
			ContentObjectProperty cop_str_arr=rco.getProperty("TestNG_testProperty_String_arr");
			Assert.assertEquals(cop_str_arr.getPropertyName(), "TestNG_testProperty_String_arr","Property's Name should be TestNG_testProperty_String_arr");
			Assert.assertEquals(cop_str_arr.getPropertyType(), PropertyType.STRING,"Property's Type should be String");			
			Assert.assertEquals(((String[])cop_str_arr.getPropertyValue()).length,2,"Property's Number should be 2");			
			Assert.assertEquals(cop_str_arr.isMultiple(), true,"Property's isMultiple should be true");			
			
			rco.addProperty("TestNG_testProperty_Boolean",new Boolean(false),true);	
			ContentObjectProperty cop_boolean=rco.getProperty("TestNG_testProperty_Boolean");
			Assert.assertEquals(cop_boolean.getPropertyName(), "TestNG_testProperty_Boolean","Property's Name should be TestNG_testProperty_Boolean");
			Assert.assertEquals(cop_boolean.getPropertyType(), PropertyType.BOOLEAN,"Property's Type should be Boolean");
			Assert.assertEquals(((Boolean)cop_boolean.getPropertyValue()).booleanValue(), false,"Property's value should be false");
			Assert.assertEquals(cop_boolean.isMultiple(), false,"Property's isMultiple should be false");	
			
			rco.addProperty("TestNG_testProperty_Boolean_arr",new boolean[]{true,false},true);
			ContentObjectProperty cop_boolean_arr=rco.getProperty("TestNG_testProperty_Boolean_arr");
			Assert.assertEquals(cop_boolean_arr.getPropertyName(), "TestNG_testProperty_Boolean_arr","Property's Name should be TestNG_testProperty_Boolean_arr");
			Assert.assertEquals(cop_boolean_arr.getPropertyType(), PropertyType.BOOLEAN,"Property's Type should be BOOLEAN");			
			Assert.assertEquals(((boolean[])cop_boolean_arr.getPropertyValue()).length,2,"Property's Number should be 2");			
			Assert.assertEquals(cop_boolean_arr.isMultiple(), true,"Property's isMultiple should be true");
			
			rco.addProperty("TestNG_testProperty_Double",new Double(1234.5678833),true);
			ContentObjectProperty cop_double=rco.getProperty("TestNG_testProperty_Double");		
			Assert.assertEquals(cop_double.getPropertyName(), "TestNG_testProperty_Double","Property's Name should be TestNG_testProperty_Double");
			Assert.assertEquals(cop_double.getPropertyType(), PropertyType.DOUBLE,"Property's Type should be Double");
			Assert.assertEquals(((Double)cop_double.getPropertyValue()).doubleValue(), 1234.5678833,"Property's value should be 1234.5678833");
			Assert.assertEquals(cop_double.isMultiple(), false,"Property's isMultiple should be false");
			
			rco.addProperty("TestNG_testProperty_Double_arr",new double[]{19093344.233,99902222.2235},true);
			ContentObjectProperty cop_double_arr=rco.getProperty("TestNG_testProperty_Double_arr");
			Assert.assertEquals(cop_double_arr.getPropertyName(), "TestNG_testProperty_Double_arr","Property's Name should be TestNG_testProperty_Double_arr");
			Assert.assertEquals(cop_double_arr.getPropertyType(), PropertyType.DOUBLE,"Property's Type should be DOUBLE");			
			Assert.assertEquals(((double[])cop_double_arr.getPropertyValue()).length,2,"Property's Number should be 2");			
			Assert.assertEquals(cop_double_arr.isMultiple(), true,"Property's isMultiple should be true");			
			
			rco.addProperty("TestNG_testProperty_Long",new Long(9223372036854775806l),true);
			ContentObjectProperty cop_long=	rco.getProperty("TestNG_testProperty_Long");	
			Assert.assertEquals(cop_long.getPropertyName(), "TestNG_testProperty_Long","Property's Name should be TestNG_testProperty_Long");
			Assert.assertEquals(cop_long.getPropertyType(), PropertyType.LONG,"Property's Type should be Long");
			Assert.assertEquals(((Long)cop_long.getPropertyValue()).longValue(), 9223372036854775806l,"Property's value should be 9223372036854775806l");
			Assert.assertEquals(cop_long.isMultiple(), false,"Property's isMultiple should be false");
			
			rco.addProperty("TestNG_testProperty_Long_arr",new long[]{1334556667,2223578},true);
			ContentObjectProperty cop_long_arr=rco.getProperty("TestNG_testProperty_Long_arr");
			Assert.assertEquals(cop_long_arr.getPropertyName(), "TestNG_testProperty_Long_arr","Property's Name should be TestNG_testProperty_Long_arr");
			Assert.assertEquals(cop_long_arr.getPropertyType(), PropertyType.LONG,"Property's Type should be LONG");			
			Assert.assertEquals(((long[])cop_long_arr.getPropertyValue()).length,2,"Property's Number should be 2");			
			Assert.assertEquals(cop_long_arr.isMultiple(), true,"Property's isMultiple should be true");			
			
			rco.addProperty("TestNG_testProperty_Decimal", new BigDecimal(Double.toString(1234567.989882)),true);
			ContentObjectProperty cop_decimal=rco.getProperty("TestNG_testProperty_Decimal"); 		
			Assert.assertEquals(cop_decimal.getPropertyName(), "TestNG_testProperty_Decimal","Property's Name should be TestNG_testProperty_Decimal");
			Assert.assertEquals(cop_decimal.getPropertyType(), PropertyType.DECIMAL,"Property's Type should be Decimal");
			Assert.assertEquals(((BigDecimal)cop_decimal.getPropertyValue()).doubleValue(), 1234567.989882,"Property's value should be 1234567.989882");
			Assert.assertEquals(cop_decimal.isMultiple(), false,"Property's isMultiple should be false");			
			
			rco.addProperty("TestNG_testProperty_BigDecimal_arr",new BigDecimal[]{new BigDecimal(Double.toString(1234567.989882)),new BigDecimal(Double.toString(78934567.989882))},true);
			ContentObjectProperty cop_BigDecimal_arr=rco.getProperty("TestNG_testProperty_BigDecimal_arr");
			Assert.assertEquals(cop_BigDecimal_arr.getPropertyName(), "TestNG_testProperty_BigDecimal_arr","Property's Name should be TestNG_testProperty_BigDecimal_arr");
			Assert.assertEquals(cop_BigDecimal_arr.getPropertyType(), PropertyType.DECIMAL,"Property's Type should be DECIMAL");			
			Assert.assertEquals(((BigDecimal[])cop_BigDecimal_arr.getPropertyValue()).length,2,"Property's Number should be 2");			
			Assert.assertEquals(cop_BigDecimal_arr.isMultiple(), true,"Property's isMultiple should be true");
			
			Binary testBo=ValueFactoryImpl.getInstance().createBinary(new FileInputStream(new File("testBinaryContentFile/IMG_1716.JPG")));
			rco.addProperty("TestNG_testProperty_Binary",testBo,true);
			ContentObjectProperty cop_Binary=rco.getProperty("TestNG_testProperty_Binary");  		
			Assert.assertEquals(cop_Binary.getPropertyName(), "TestNG_testProperty_Binary","Property's Name should be TestNG_testProperty_Binary");
			Assert.assertEquals(cop_Binary.getPropertyType(), PropertyType.BINARY,"Property's Type should be Binary");
			Assert.assertEquals(((Binary)cop_Binary.getPropertyValue()).getSize(), testBo.getSize(),"Property's value should be same");
			Assert.assertEquals(cop_Binary.isMultiple(), false,"Property's isMultiple should be false");			
			
			Binary testBo1=ValueFactoryImpl.getInstance().createBinary(new FileInputStream(new File("testBinaryContentFile/01300000029584120462690206338.jpg")));
			Binary testBo2=ValueFactoryImpl.getInstance().createBinary(new FileInputStream(new File("testBinaryContentFile/6_roger.jpg")));			
			rco.addProperty("TestNG_testProperty_Binary_arr",new Binary[]{testBo1,testBo2},true);
			ContentObjectProperty cop_Binary_arr=rco.getProperty("TestNG_testProperty_Binary_arr");
			Assert.assertEquals(cop_Binary_arr.getPropertyName(), "TestNG_testProperty_Binary_arr","Property's Name should be TestNG_testProperty_Binary_arr");
			Assert.assertEquals(cop_Binary_arr.getPropertyType(), PropertyType.BINARY,"Property's Type should be BINARY");			
			Assert.assertEquals(((Binary[])cop_Binary_arr.getPropertyValue()).length,2,"Property's Number should be 2");			
			Assert.assertEquals(cop_Binary_arr.isMultiple(), true,"Property's isMultiple should be true");			
			
			Calendar tesrCo=Calendar.getInstance();
			tesrCo.setTime(new Date());
			rco.addProperty("TestNG_testProperty_Date",tesrCo,true);
			ContentObjectProperty cop_Date=rco.getProperty("TestNG_testProperty_Date");  		
			Assert.assertEquals(cop_Date.getPropertyName(), "TestNG_testProperty_Date","Property's Name should be TestNG_testProperty_Date");
			Assert.assertEquals(cop_Date.getPropertyType(), PropertyType.DATE,"Property's Type should be Date");
						
			Calendar newCal=((Calendar)cop_Date.getPropertyValue());			
			String dateOrg=""+
			tesrCo.get(Calendar.YEAR)+tesrCo.get(Calendar.MONTH)+tesrCo.get(Calendar.DAY_OF_MONTH)+
			tesrCo.get(Calendar.HOUR_OF_DAY)+tesrCo.get(Calendar.MINUTE)+tesrCo.get(Calendar.SECOND)+
			tesrCo.getTimeInMillis();			
			String dateNew=""+
			newCal.get(Calendar.YEAR)+newCal.get(Calendar.MONTH)+newCal.get(Calendar.DAY_OF_MONTH)+
			newCal.get(Calendar.HOUR_OF_DAY)+newCal.get(Calendar.MINUTE)+newCal.get(Calendar.SECOND)+
			newCal.getTimeInMillis();			
			Assert.assertTrue(dateOrg.equals(dateNew),"Property's value should be same");
			
			Assert.assertEquals(cop_Date.isMultiple(), false,"Property's isMultiple should be false");			
			
			Calendar tesrCo1=Calendar.getInstance();
			tesrCo1.setTime(new Date());
			Calendar tesrCo2=Calendar.getInstance();
			tesrCo2.setTime(new Date());			
			rco.addProperty("TestNG_testProperty_Date_arr",new Calendar[]{tesrCo1,tesrCo2},true);
			ContentObjectProperty cop_Date_arr=rco.getProperty("TestNG_testProperty_Date_arr");
			Assert.assertEquals(cop_Date_arr.getPropertyName(), "TestNG_testProperty_Date_arr","Property's Name should be TestNG_testProperty_Date_arr");
			Assert.assertEquals(cop_Date_arr.getPropertyType(), PropertyType.DATE,"Property's Type should be DATE");			
			Assert.assertEquals(((Calendar[])cop_Date_arr.getPropertyValue()).length,2,"Property's Number should be 2");			
			Assert.assertEquals(cop_Date_arr.isMultiple(), true,"Property's isMultiple should be true");			
		
			ContentObjectProperty pro1=ContentComponentFactory.createContentObjectProperty();
			pro1.setPropertyName("prop1");
			pro1.setMultiple(false);
			pro1.setPropertyValue("prop1's value");
			pro1.setPropertyType(PropertyType.STRING);
			List<ContentObjectProperty> li=new ArrayList<ContentObjectProperty>();
			li.add(pro1);
			rco.addProperty(li, true);
			
			ContentObjectProperty pro=rco.getProperty("prop1");
			Assert.assertEquals(pro.isMultiple(), false,"Property's isMultiple should be false");
			Assert.assertEquals(pro.getPropertyType(), PropertyType.STRING,"Property's t shoulyped be PropertyType.STRING");
			Assert.assertEquals(pro.getPropertyValue().toString(),"prop1's value","Property's value should be 'prop1's value'");
			
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testAddProperty");
		} catch (RepositoryException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testAddProperty");
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testAddProperty");
		}	
	}	
	
	@Test(dependsOnMethods = { "testAdd_GetProperty" })
	public void testGetProperties(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);
			
			List<ContentObjectProperty> cops=rco.getProperties();
			Assert.assertEquals(cops.size(),16,"List<ContentObjectProperty>'s size should be 16");
			//have buildin Property:SPACE_ROOT_CONTENT_OBJECT_ID,so total number should be 14+1
			for(ContentObjectProperty cop:cops){				
				//printPropertyValue(cop);
				Assert.assertTrue(cop instanceof ContentObjectProperty);
				Assert.assertTrue(cop instanceof JCRContentObjectPropertyImpl);				
			}		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testGetProperties");
		}	
	}
	
	@Test(dependsOnMethods = { "testGetProperties" })
	public void testUpdateProperty(){		
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);			
			ContentObjectProperty cop_str=rco.getProperty("TestNG_testProperty_String");			
			Assert.assertEquals( "TestNG_testProperty_String_value",cop_str.getPropertyValue().toString(),"Property's value should be TestNG_testProperty_String_value");
			
			ContentObjectProperty updateProperty1=ContentComponentFactory.createContentObjectProperty();			
			updateProperty1.setPropertyName("TestNG_testProperty_String");
			updateProperty1.setPropertyType(PropertyType.STRING);
			updateProperty1.setPropertyValue("TestNG_testProperty_String_value_Updated");
			
			ContentObjectProperty updatePropertyR=rco.updateProperty(updateProperty1, true);
			Assert.assertNotNull(updatePropertyR);			
			ContentObjectProperty cop_str2=rco.getProperty("TestNG_testProperty_String");
			Assert.assertEquals( "TestNG_testProperty_String_value_Updated",cop_str2.getPropertyValue().toString(),"Property's value should be TestNG_testProperty_String_value_Updated");
			Assert.assertEquals( false,cop_str2.isMultiple(),"Property's value should be false");			
			
			ContentObjectProperty updateProperty2=ContentComponentFactory.createContentObjectProperty();	
			updateProperty2.setPropertyName("TestNG_testProperty_String");
			updateProperty2.setPropertyType(PropertyType.LONG);
			updateProperty2.setPropertyValue(new Long(12357676));			
			ContentObjectProperty updatePropertyR2=rco.updateProperty(updateProperty2, true);
			Assert.assertNull(updatePropertyR2);
			
			
			
			ContentObjectProperty cop_str_arr=rco.getProperty("TestNG_testProperty_String_arr");				
			Assert.assertEquals(((String[])cop_str_arr.getPropertyValue()).length,2,"Property's Number should be 2");			
			Assert.assertEquals(cop_str_arr.isMultiple(), true,"Property's isMultiple should be true");	
			
			ContentObjectProperty updateProperty3=ContentComponentFactory.createContentObjectProperty();			
			updateProperty3.setPropertyName("TestNG_testProperty_String_arr");
			updateProperty3.setPropertyType(PropertyType.STRING);
			updateProperty3.setPropertyValue(new String[]{"TestNG","v2","v3"});			
			
			ContentObjectProperty updatePropertyR3=rco.updateProperty(updateProperty3, true);
			Assert.assertNotNull(updatePropertyR3);
			ContentObjectProperty cop_str_arr2=rco.getProperty("TestNG_testProperty_String_arr");			
			Assert.assertEquals(((String[])cop_str_arr2.getPropertyValue()).length,3,"Property's Number should be 3");			
			Assert.assertEquals(cop_str_arr2.isMultiple(), true,"Property's isMultiple should be true");
			
			cs.closeContentSpace();		
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testUpdateProperty");
		}		
	}

	@Test(dependsOnMethods = { "testUpdateProperty" })
	public void testRemoveProperty(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);
			
			ContentObjectProperty cop_Binary=rco.getProperty("TestNG_testProperty_Binary");  
			Assert.assertNotNull(cop_Binary);			
			ContentObjectProperty cop_Date_arr=rco.getProperty("TestNG_testProperty_Date_arr");
			Assert.assertNotNull(cop_Date_arr);
			
			boolean rf1=rco.removeProperty("TestNG_testProperty_Binary", true);
			boolean rf2=rco.removeProperty("TestNG_testProperty_Date_arr", true);			
			Assert.assertTrue(rf1);
			Assert.assertTrue(rf2);			
			
			ContentObjectProperty cop_Binary2=rco.getProperty("TestNG_testProperty_Binary");  
			Assert.assertNull(cop_Binary2);			
			ContentObjectProperty cop_Date_arr2=rco.getProperty("TestNG_testProperty_Date_arr");
			Assert.assertNull(cop_Date_arr2);
			
			boolean rf3=rco.removeProperty("TestNG_testProperty_Binary", true);
			boolean rf4=rco.removeProperty("TestNG_testProperty_Date_arr", true);			
			Assert.assertFalse(rf3);
			Assert.assertFalse(rf4);			
			
			List<ContentObjectProperty> cops=rco.getProperties();
			Assert.assertEquals(cops.size(),14,"List<ContentObjectProperty>'s size should be 14");			
			
			ContentObjectProperty rmp=rco.getProperty("TestNG_testProperty_BigDecimal_arr");
			Assert.assertNotNull(rmp);
			boolean rf5=rco.removeProperty(rmp, true);
			Assert.assertTrue(rf5);
			ContentObjectProperty rmp2=rco.getProperty("TestNG_testProperty_BigDecimal_arr");
			Assert.assertNull(rmp2);			
			boolean rf6=rco.removeProperty(rmp, true);
			Assert.assertFalse(rf6);
			
			List<ContentObjectProperty> cops2=rco.getProperties();
			Assert.assertEquals(cops2.size(),13,"List<ContentObjectProperty>'s size should be 13");			
			
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testRemoveProperty");
		}			
	}
	
	@Test(dependsOnMethods = { "testRemoveProperty" })
	public static void testAdd_getSubContentObject(){		
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);			
			
			ContentObjectProperty testAddProperty1=ContentComponentFactory.createContentObjectProperty();
			testAddProperty1.setMultiple(false);
			testAddProperty1.setPropertyName("String Test property_1");
			testAddProperty1.setPropertyType(PropertyType.STRING);
			testAddProperty1.setPropertyValue("String Test property_1_value");
			
			ContentObjectProperty testAddProperty2=ContentComponentFactory.createContentObjectProperty();
			testAddProperty2.setMultiple(false);
			testAddProperty2.setPropertyName("Long Test property_1");
			testAddProperty2.setPropertyType(PropertyType.LONG);
			testAddProperty2.setPropertyValue(new Long(12345678));
			
			ContentObjectProperty testAddProperty3=ContentComponentFactory.createContentObjectProperty();
			testAddProperty3.setMultiple(true);
			testAddProperty3.setPropertyName("String Test property_2");
			testAddProperty3.setPropertyType(PropertyType.STRING);
			testAddProperty3.setPropertyValue(new String[]{"srt_1","str_2"});
			
			List<ContentObjectProperty> paramLst=new ArrayList<ContentObjectProperty>();
			paramLst.add(testAddProperty1);
			paramLst.add(testAddProperty2);
			paramLst.add(testAddProperty3);			
			
			BaseContentObject co1=	rco.addSubContentObject("testAddSubContentObject_1", paramLst, true);			
			Assert.assertNotNull(co1);			
			List<BaseContentObject> cl=rco.getSubContentObjects("testAddSubContentObject_1");
			Assert.assertEquals(1,cl.size(),"subject object's Number should be 1");
			BaseContentObject cbo=cl.get(0);			
			Assert.assertNotNull(cbo);
			Assert.assertTrue(cbo instanceof BaseContentObject);
			Assert.assertTrue(cbo instanceof ContentObject);
			Assert.assertTrue(cbo instanceof JCRContentObjectImpl);
			
			ContentObjectProperty testAddedProperty1=cbo.getProperty("String Test property_1");
			Assert.assertNotNull(testAddedProperty1);
			Assert.assertEquals("String Test property_1", testAddedProperty1.getPropertyName());
			Assert.assertEquals(PropertyType.STRING, testAddedProperty1.getPropertyType());
			Assert.assertEquals("String Test property_1_value", testAddedProperty1.getPropertyValue().toString());
			Assert.assertEquals(false,testAddedProperty1.isMultiple());			
			
			ContentObjectProperty testAddedProperty2=cbo.getProperty("Long Test property_1");
			Assert.assertNotNull(testAddedProperty2);
			Assert.assertEquals("Long Test property_1", testAddedProperty2.getPropertyName());
			Assert.assertEquals(PropertyType.LONG, testAddedProperty2.getPropertyType());
			Assert.assertEquals(12345678l, ((Long)testAddedProperty2.getPropertyValue()).longValue());
			Assert.assertEquals(false,testAddedProperty2.isMultiple());			
			
			ContentObjectProperty testAddedProperty3=cbo.getProperty("String Test property_2");
			Assert.assertNotNull(testAddedProperty3);
			Assert.assertEquals("String Test property_2", testAddedProperty3.getPropertyName());
			Assert.assertEquals(PropertyType.STRING, testAddedProperty3.getPropertyType());
			Assert.assertEquals(2, ((String[])testAddedProperty3.getPropertyValue()).length);
			Assert.assertEquals(true,testAddedProperty3.isMultiple());
			
			BaseContentObject co2=	rco.addSubContentObject("testAddSubContentObject_2", null, true);			
			Assert.assertNotNull(co2);			
			List<BaseContentObject> cl2=rco.getSubContentObjects("testAddSubContentObject_2");
			Assert.assertEquals(1,cl2.size(),"subject object's Number should be 1");
			BaseContentObject cbo2=cl2.get(0);			
			Assert.assertNotNull(cbo2);
			Assert.assertTrue(cbo2 instanceof BaseContentObject);
			Assert.assertTrue(cbo2 instanceof ContentObject);
			Assert.assertTrue(cbo2 instanceof JCRContentObjectImpl);
			
			List<BaseContentObject> cl3=rco.getSubContentObjects(null);
			Assert.assertEquals(cl3.size(),2,"subject object's Number should be 2");			
			for(BaseContentObject bo:cl3){
				Assert.assertTrue(bo instanceof BaseContentObject);
				Assert.assertTrue(bo instanceof ContentObject);
				Assert.assertTrue(bo instanceof JCRContentObjectImpl);				
			}
			
			BaseContentObject co21=rco.getSubContentObject("testAddSubContentObject_2");
			Assert.assertNotNull(co21);
			Assert.assertEquals("testAddSubContentObject_2",co21.getContentObjectName());
			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testAddSubContentObject");
		}
			
	}
	
	@Test(dependsOnMethods = { "testAdd_getSubContentObject" })
	public void testRemoveSubContentObject(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);	
			
			boolean rr1=rco.removeSubContentObject("testAddSubContentObject_1", true);
			Assert.assertTrue(rr1);
			List<BaseContentObject> cl=rco.getSubContentObjects(null);
			Assert.assertEquals(cl.size(),1,"subject object's Number should be 1");
			boolean rr2=rco.removeSubContentObject("testAddSubContentObject_1", true);
			Assert.assertFalse(rr2);	
			boolean rr3=rco.removeSubContentObject("testAddSubContentObject_2", true);
			Assert.assertTrue(rr3);
			List<BaseContentObject> cl2=rco.getSubContentObjects(null);
			Assert.assertEquals(0,cl2.size(),"subject object's Number should be 0");	
		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testRemoveSubContentObject");
		}		
	}
	
	@Test(dependsOnMethods = { "testRemoveSubContentObject"})
	public void testGetAllLinearVersions(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);
			List<VersionObject> vl=rco.getAllLinearVersions();	
			Assert.assertNotNull(vl);
			Assert.assertTrue(vl.size()>1);
			for(VersionObject curretnvo: vl ){				
				Assert.assertNotNull(curretnvo.getCurrentVersionNumber());
				Assert.assertNotNull(curretnvo.getCurrentVersionCreatedDate());
				Assert.assertNotNull(curretnvo.getCurrentVersionLabels().length>0);					
			}							
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testGetAllLinearVersions");
		}		
	}
	
	@Test(dependsOnMethods = { "testGetAllLinearVersions"})
	public void testGetAllVersionsInSpace(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);		
			List<VersionObject> vol=rco.getCurrentVersion().getAllVersionsInSpace();			
			for(VersionObject vo:vol){		
				Assert.assertNotNull(vo.getCurrentVersionNumber());
				Assert.assertNotNull(vo.getCurrentVersionCreatedDate());
				Assert.assertNotNull(vo.getCurrentVersionLabels().length>0);				
			}		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testGetAllVersionsInSpace");
		}			
	}	
	
	@Test(dependsOnMethods = {"testGetAllVersionsInSpace"})
	public void testRestoreToVersion(){		
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);			
			BaseContentObject vtco=rco.addSubContentObject("RestoreVersionTestContentObject", null, true);
			Assert.assertNotNull(vtco);			
			BaseContentObject rtvo=rco.getSubContentObjects("RestoreVersionTestContentObject").get(0);
			Assert.assertNotNull(rtvo);	
			
			rtvo.addProperty("String property_1", "String property_1_value", true);	//1.0	
			rtvo.addProperty("String property_2", "String property_2_value", true);	//1.1	
			rtvo.addProperty("String property_3", "String property_3_value", true);	//1.2	
			rtvo.addProperty("String property_4", "String property_4_value", true);	//1.3			
			rtvo.addProperty("Long property_1", new Long(123), true);//1.4			
			rtvo.addProperty("Long property_2", new Long(1234), true);//1.5			
			rtvo.addProperty("Long property_3", new Long(1235), true);//1.6
						
			VersionObject cvo=rtvo.getCurrentVersion();
			Assert.assertNotNull(cvo);			
			Assert.assertEquals(cvo.getCurrentVersionNumber(),"1.6","current version  should be 1.22");
			ContentObjectProperty long_3=rtvo.getProperty("Long property_3");
			Assert.assertNotNull(long_3);	
			Assert.assertEquals(((Long)long_3.getPropertyValue()).longValue(),1235l);		
			
			rtvo.restoreToVersion("1.4", false);			
			Assert.assertEquals(rtvo.getCurrentVersion().getCurrentVersionNumber(),"1.4","current version should be 1.4");
			long_3=rtvo.getProperty("Long property_3");
			Assert.assertNull(long_3);
			ContentObjectProperty long_2=rtvo.getProperty("Long property_2");
			Assert.assertNull(long_2);
			
			rtvo.addProperty("String property_5", "String property_5_value", true);	//1.4.0
			Assert.assertEquals(rtvo.getCurrentVersion().getCurrentVersionNumber(),"1.4.0","current version should be 1.4.0");			
			
			rtvo.restoreToVersion("1.5", false);
			Assert.assertEquals(rtvo.getCurrentVersion().getCurrentVersionNumber(),"1.5","current version should be 1.5");
			long_3=rtvo.getProperty("Long property_3");
			Assert.assertNull(long_3);
			long_2=rtvo.getProperty("Long property_2");
			Assert.assertNotNull(long_2);
			Assert.assertEquals(((Long)long_2.getPropertyValue()).longValue(),1234l);
			ContentObjectProperty str_5=rtvo.getProperty("String property_5");
			Assert.assertNull(str_5);
			
			rtvo.restoreToVersion("1.4.0", false);
			Assert.assertEquals(rtvo.getCurrentVersion().getCurrentVersionNumber(),"1.4.0","current version should be 1.4.0");
			str_5=rtvo.getProperty("String property_5");
			Assert.assertNotNull(str_5);			
			Assert.assertEquals(str_5.getPropertyValue().toString(),"String property_5_value","String property_5_value");
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {		
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testRestoreToVersion");
		}			
	}
	
	@Test(dependsOnMethods = {"testRestoreToVersion"})
	public void testRenameSubContentObject(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);				
			BaseContentObject trco=rco.addSubContentObject("needRename", null, true);			
			Assert.assertNotNull(trco);			
			BaseContentObject trco1=rco.getSubContentObjects("needRename").get(0);
			Assert.assertNotNull(trco1);			
			boolean failRename=rco.renameSubContentObject("needRename", "needRename", true);
			Assert.assertFalse(failRename);			
			boolean succRename=rco.renameSubContentObject("needRename", "renameedName", true);
			Assert.assertTrue(succRename);		
			Assert.assertEquals(0,rco.getSubContentObjects("needRename").size());			
			BaseContentObject trco3=rco.getSubContentObjects("renameedName").get(0);
			Assert.assertNotNull(trco3);			
			Assert.assertTrue(rco.getCurrentVersion().getCurrentVersionLabels()[0].contains("Renamed sub Content Object {needRename to renameedName}"));			
			Assert.assertTrue(trco3.getCurrentVersion().getCurrentVersionLabels()[0].contains("changed name from needRename to renameedName"));
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testRenameSubContentObject");
		}	
	}
	
	@Test(dependsOnMethods = {"testRenameSubContentObject"})
	public void testGetParentContentObject(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);
			
			BaseContentObject pco=rco.getParentContentObject();
			Assert.assertNull(pco);
			
			rco.addSubContentObject("childObject1", null, true)
			.addSubContentObject("childObject2", null, true)
			.addSubContentObject("childObject3", null, true);
			
			BaseContentObject cco=rco.getSubContentObjects("childObject1").get(0);
			BaseContentObject ccopco=cco.getParentContentObject();
			Assert.assertNotNull(ccopco);
			Assert.assertEquals(TestCaseDataConstant.testRootContentObjectName,ccopco.getContentObjectName());
						
			BaseContentObject cco2=rco.getSubContentObjects("childObject1").get(0).
										getSubContentObjects("childObject2").get(0).
										getSubContentObjects("childObject3").get(0);
			BaseContentObject ccopco2=cco2.getParentContentObject();
			Assert.assertNotNull(ccopco2);
			Assert.assertEquals("childObject2",ccopco2.getContentObjectName());			
			cs.closeContentSpace();	

		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testGetParentContentObject");
		}	
	}
	
	@Test(dependsOnMethods = {"testGetParentContentObject"})
	public void testRename(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);				
			BaseContentObject cco2=rco.getSubContentObjects("childObject1").get(0).
									getSubContentObjects("childObject2").get(0);			
			BaseContentObject cco1=cco2.getParentContentObject();			
			boolean rnr=cco2.rename("childObject2Renamed", true);			
			Assert.assertTrue(rnr);			
			Assert.assertTrue(cco2.getCurrentVersion().getCurrentVersionLabels()[0].contains("changed name from childObject2 to childObject2Renamed"));
			Assert.assertTrue(cco1.getCurrentVersion().getCurrentVersionLabels()[0].contains("Renamed sub Content Object {childObject2 to childObject2Renamed}"));
					
			BaseContentObject cco3=rco.getSubContentObjects("childObject1").get(0).
			getSubContentObjects("childObject2Renamed").get(0);
			Assert.assertNotNull(cco3);			
			int on=rco.getSubContentObjects("childObject1").get(0).getSubContentObjects("childObject2").size();			
			Assert.assertEquals(0,on);			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testRename");
		}	
	}
	
	@Test(dependsOnMethods = {"testRename"})
	public void testRenameProperty(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);		
			
			BaseContentObject bco=rco.addSubContentObject("renamePropertyTestCO", null, true);
			Assert.assertNotNull(bco);	
			
			bco.addProperty("propertyA", "this is property A Value", true);			
			bco.addProperty("propertyB", new long[]{1334556667,2223578},true);			
			boolean rpr1=bco.renameProperty("propertyA", "propertyARenamed", true);
			Assert.assertTrue(rpr1);			
			Assert.assertNull(bco.getProperty("propertyA"));
			
			ContentObjectProperty np1=bco.getProperty("propertyARenamed");
			Assert.assertNotNull(np1);
			Assert.assertEquals("this is property A Value", np1.getPropertyValue().toString());			
			String l1=bco.getCurrentVersion().getCurrentVersionLabels()[0];			
			Assert.assertTrue(l1.contains("Renamed Property {propertyA to propertyARenamed}"));
			
			boolean rpr2=bco.renameProperty("propertyB", "propertyBRenamed", true);
			Assert.assertTrue(rpr2);			
			Assert.assertNull(bco.getProperty("propertyB"));
			ContentObjectProperty np2=bco.getProperty("propertyBRenamed");
			Assert.assertNotNull(np2);
			Assert.assertEquals(2, ((long[])np2.getPropertyValue()).length);
			Assert.assertEquals(1334556667l, ((long[])np2.getPropertyValue())[0]);
			Assert.assertEquals(2223578l, ((long[])np2.getPropertyValue())[1]);
			String l2=bco.getCurrentVersion().getCurrentVersionLabels()[0];
			Assert.assertTrue(l2.contains("Renamed Property {propertyB to propertyBRenamed}"));
			
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			Assert.fail("got ContentReposityException during testRenameProperty");
		}	
	}
	/* in Jackrabbit OAK v1.2.2 workspace clone (ws.clone(ws.getName(), tPath, lPath, false)) method not implemented yet, so ignore this case
	@Test(dependsOnMethods = {"testRenameProperty"})
	public void testAddSubLinkContentObject(){
		ContentSpace cs=null;
		ContentSpace cs2=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);				
			BaseContentObject bco1=rco.addSubContentObject("AddLinkTestCO1", null, true);
			Assert.assertNotNull(bco1);
			Assert.assertFalse(bco1.isLinkContentObject());	
			bco1.addProperty("buildinProperty1", "buildinProperty1_value", true);	
			
			BaseContentObject bco2=rco.addSubContentObject("AddLinkTestCO2", null, true);
			Assert.assertNotNull(bco2);
			Assert.assertFalse(bco2.isLinkContentObject());		
			
			boolean addLkr=bco2.addSubLinkContentObject("linkedFromAddLinkTestCO1", bco1, true);	
			Assert.assertTrue(addLkr);
			cs.closeContentSpace();				
			
			// need use new session to make this change appears			
			cs2=ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco2=cs2.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco2);			
			
			BaseContentObject bco_o=rco2.getSubContentObject("AddLinkTestCO1");	
			Assert.assertTrue(bco_o.isLinkContentObject());
			
			BaseContentObject bco3=rco2.getSubContentObject("AddLinkTestCO2");					
			Assert.assertFalse(bco3.isLinkContentObject());	
			
			Assert.assertEquals(1,bco3.getSubContentObjects(null).size());				
			Assert.assertTrue((bco3.getCurrentVersion().getCurrentVersionLabels()[0]).contains("Added Link sub content object {linkedFromAddLinkTestCO1 from AddLinkTestCO1}"));			
			
			BaseContentObject bco4=bco3.getSubContentObject("linkedFromAddLinkTestCO1");
			Assert.assertTrue(bco4.isLinkContentObject());			
			
			ContentObjectProperty p=bco4.getProperty("buildinProperty1");			
			Assert.assertEquals("buildinProperty1_value", p.getPropertyValue().toString());				
				
			bco_o.addProperty("newProperty", "newProperty_value", true);
			ContentObjectProperty p2=bco4.getProperty("newProperty");				
			Assert.assertEquals("newProperty_value", p2.getPropertyValue().toString());		
			
			bco4.addProperty("addedFromLinkedOject", "addedFromLinkedOject_value", true);				
			ContentObjectProperty p3=bco_o.getProperty("addedFromLinkedOject");			
			Assert.assertEquals("addedFromLinkedOject_value", p3.getPropertyValue().toString());			
			Assert.assertTrue(bco_o.getCurrentVersion().getCurrentVersionLabels()[0].contains("Added New property {addedFromLinkedOject}"));
			
			bco3.removeSubContentObject("linkedFromAddLinkTestCO1", true);			
			Assert.assertFalse(bco_o.isLinkContentObject());			
			BaseContentObject bco_o2=rco2.getSubContentObject("AddLinkTestCO1");	
			Assert.assertNotNull(bco_o2);			
			BaseContentObject bco_o3=bco3.getSubContentObject("linkedFromAddLinkTestCO1");
			Assert.assertNull(bco_o3);			
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();
			cs2.closeContentSpace();
			Assert.fail("got ContentReposityException during testAddSubLinkContentObject");
		}	
	}
	*/
	
	@Test(dependsOnMethods = {"testRenameProperty"})
	//@Test(dependsOnMethods = {"testAddSubLinkContentObject"})  
	public void testLock_Unlock(){
		ContentSpace cs=null;
		ContentSpace cs2=null;
		ContentSpace cs3=null;
		ContentSpace cs4=null;
		ContentSpace cs5=null;
		try {
			String BUILDIN_ADMINISTRATOR_ACCOUNT=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT);
			String BUILDIN__ADMINISTRATOR_ACCOUNT_PWD=PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN__ADMINISTRATOR_ACCOUNT_PWD);
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			
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
	
	@Test(dependsOnMethods = {"testLock_Unlock"})  
	public void testAdd_RemoveContentObjectEventListerer(){		
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);				
			BaseContentObject bco1=rco.addSubContentObject("eventListererTestContentObject", null, true);
			Assert.assertNotNull(bco1);
			BaseContentObject bco2=rco.getSubContentObject("eventListererTestContentObject");
			boolean aelr=bco2.addContentObjectEventListener(ContentSpaceEventListener.DEFAULT_CONTENTSPACE_EVENTLISTENER);		
			Assert.assertTrue(aelr);	
			boolean relr=bco2.removeContentObjectEventListener();
			Assert.assertTrue(relr);		
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testAdd_RemoveContentObjectEventListerer");
		}	
	}			
	
	@Test(dependsOnMethods = {"testAdd_RemoveContentObjectEventListerer"})  
	public void testGetSubContentObjectsCount(){		
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace(TestCaseDataConstant.testContentSpaceName);
			ContentObject rco=cs.getRootContentObject(TestCaseDataConstant.testRootContentObjectName);
			Assert.assertNotNull(rco);	
			BaseContentObject bco0=rco.addSubContentObject("subObjectCountTestObj", null, true);
			Assert.assertNotNull(bco0);
			Assert.assertEquals(0, bco0.getSubContentObjectsCount());						
			BaseContentObject bco1=bco0.addSubContentObject("count1", null, true);
			Assert.assertNotNull(bco1);
			Assert.assertEquals(1, bco0.getSubContentObjectsCount());
			BaseContentObject bco2=bco0.addSubContentObject("count2", null, true);
			Assert.assertNotNull(bco2);
			Assert.assertEquals(2, bco0.getSubContentObjectsCount());
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {			
			e.printStackTrace();
			cs.closeContentSpace();	
			Assert.fail("got ContentReposityException during testGetSubContentObjectsCount");
		}	
	}			
	
	private static void printPropertyValue(ContentObjectProperty cop){
		switch(cop.getPropertyType()){
			case PropertyType.BOOLEAN:{ 
				if(cop.isMultiple()){
					boolean[] ba=(boolean[])cop.getPropertyValue();			
					for(boolean b:ba){
						System.out.print(b+" ");								
					} 	
					System.out.println();
				}else{
					System.out.println(cop.getPropertyValue());	
				}							
				break;
			}
			case PropertyType.DOUBLE:{ 
				if(cop.isMultiple()){
					double[] ba=(double[])cop.getPropertyValue();			
					for(double b:ba){
						System.out.print(b+" ");								
					} 
					System.out.println();
				}else{
					System.out.println(cop.getPropertyValue());	
				}							
				break;
			}
			case PropertyType.LONG:{ 
				if(cop.isMultiple()){
					long[] ba=(long[])cop.getPropertyValue();			
					for(long b:ba){
						System.out.print(b+" ");								
					} 	
					System.out.println();
				}else{
					System.out.println(cop.getPropertyValue());	
				}							
				break;
			}
			case PropertyType.DECIMAL:{ 
				if(cop.isMultiple()){
					BigDecimal[] ba=(BigDecimal[])cop.getPropertyValue();			
					for(BigDecimal b:ba){
						System.out.print(b+" ");								
					}
					System.out.println();
				}else{
					System.out.println(cop.getPropertyValue());	
				}							
				break;
			}
			case PropertyType.BINARY:{ 
				if(cop.isMultiple()){
					Binary[] ba=(Binary[])cop.getPropertyValue();			
					for(Binary b:ba){
						System.out.print(b+" ");								
					} 
					System.out.println();
				}else{
					System.out.println(cop.getPropertyValue());	
				}							
				break;
			}
			case PropertyType.DATE:{ 
				if(cop.isMultiple()){
					Calendar[] ba=(Calendar[])cop.getPropertyValue();			
					for(Calendar b:ba){
						System.out.print(b+" ");								
					} 
					System.out.println();
				}else{
					System.out.println(cop.getPropertyValue());	
				}							
				break;
			}
			case PropertyType.STRING:{ 
				if(cop.isMultiple()){
					String[] ba=(String[])cop.getPropertyValue();			
					for(String b:ba){
						System.out.print(b+" ");								
					} 
					System.out.println();
				}else{
					System.out.println(cop.getPropertyValue());
				}							
				break;
			}
		}	
	}	
}
