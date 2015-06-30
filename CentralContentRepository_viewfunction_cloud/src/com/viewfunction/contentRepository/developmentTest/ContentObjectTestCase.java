package com.viewfunction.contentRepository.developmentTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.PropertyType;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureau.VersionObject;
import com.viewfunction.contentRepository.testNGTest.TestCaseDataConstant;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.observation.ContentSpaceEventListener;

public class ContentObjectTestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testAddProperty("TESTStringPerporty_1010091","testPerportyroperty_Value_1010091",true);
		//testAddProperty("TESTStringArrayPerporty",new String[]{"testStringperty_Value_1","testStringperty_Value_2"},false);
		//testGetProperty("TESTStringPerporty");
		//testGetProperty("TESTStringArrayPerporty");	
		
		//testAddProperty("TESTBooleanArrayPerporty_1",new boolean[]{false,true,false,false,true,true,true,true},false);
		//testAddProperty("TESTBooleanArrayPerporty_2",new Boolean(false),false);	
		//testGetProperty("TESTStringPerporty_1010091");
		//testGetProperty("TESTBooleanArrayPerporty_2");
		
		//testAddProperty("TESTDoublePproperty_1",new Double(124.6),false);
		//testAddProperty("TESTDoublePproperty_2",new double[]{124.6,1234.0,244.4,1359.11},false);
		//testGetProperty("TESTDoublePproperty_1");
		//testGetProperty("TESTDoublePproperty_2");
		
		//testAddProperty("TESTLongProperty_1",new Long(12489992),false);
		//testAddProperty("TESTLongProperty_2",new long[]{124,1234,2444,135911},false);
		//testGetProperty("TESTLongProperty_1");
		//testGetProperty("TESTLongProperty_2");	
		//testGetProperties();
		
		//testAddProperty("testRemoveProperty7","ss1",true);
		//testRemoveProperty("TESTStringPerporty_101009",true);
		//testRemovePropertyByObj("test testRemovePropertyByObj",false);
		
		//testAddProperty("TestVersionProperty2",new String[]{"TestVersionProperty_Value_1","TestVersionProperty_Value_3"},true);
		//testUpdateProperty(true);
		
		testAddSubContentObject();
		//testGetSubContentObjects();
		//testRemoveSubContentObject();
		
		//restoreToVersion();		
		
		//testGetAllLinearVersions();
		//testGetAllVersionsInSpace();
		//testLockObject();
		//testAddeventListener();
	}

	public static void testAddProperty(Object propertyKey,Object propertyValue,boolean recodrVersion){		
		ContentSpace cs = null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");
			System.out.println(rco);		
			ContentObjectProperty cop=(ContentObjectProperty) rco.addProperty(propertyKey.toString(),propertyValue,recodrVersion);		
			System.out.println(cop.getPropertyName());
			System.out.println(cop.getPropertyType());
			System.out.println(cop.getPropertyValue());
			System.out.println(cop.isMultiple());		
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();
		}	
	}
	
	public static void testLockObject(){			
		ContentSpace cs = null;
		ContentSpace cs2 = null;
		try {
			/*
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");
			System.out.println(rco);
			BaseContentObject bo=rco.addSubContentObject("LockTestObject_3", null, true);
			bo.lock(false);
			ContentObjectProperty cop=(ContentObjectProperty) rco.addProperty("PropertForLockTest","PropertForLockTest_value",true);		
			System.out.println(cop.getPropertyName());
			System.out.println(cop.getPropertyType());
			System.out.println(cop.getPropertyValue());
			System.out.println(cop.isMultiple());				
			cs.closeContentSpace();			
			*/			
			//cs2 = ContentComponentFactory.connectContentSpace("wangychu","wangychu","viewfunctionContentSpace_testSpace1");			
			cs2 = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco2=cs2.getRootContentObject("RootContentObject4test_08");
			BaseContentObject bo2=rco2.getSubContentObject("LockTestObject_3");
			System.out.println(bo2.isLocked());			
			bo2.unlock();
			bo2.addProperty("PropertForLockTest2","PropertForLockTest2_value",true);
			System.out.println(bo2.isLocked());
			cs2.closeContentSpace();			
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();
		}	
	}
	
	public static void testAddeventListener(){		
		ContentSpace cs = null;		
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			
			RootContentObject lisrobj=ContentComponentFactory.createRootContentObject("addListenerTest");
			ContentObject rco02=cs.addRootContentObject(lisrobj);			
			rco02.addContentObjectEventListener(ContentSpaceEventListener.DEFAULT_CONTENTSPACE_EVENTLISTENER);			
			rco02.addProperty("perp1", "perp1_v", true);
			//rco02.updateProperty("perp1", "new prop value",true);
			rco02.removeProperty("perp1", true);			
			BaseContentObject bco1=rco02.addSubContentObject("subobj1", null, true);
			
			BaseContentObject bco2=bco1.addSubContentObject("subsubco2", null, true);
			bco2.addSubContentObject("subsubsubco3", null, true);
			bco2.addProperty("pp", "pp_v", true);
			rco02.removeSubContentObject("subobj1", true);
			
			
			System.out.println("remove listener");
			rco02.removeContentObjectEventListener();
			
			rco02.addProperty("perp2", "perp1_v", true);
			
			
			rco02.addContentObjectEventListener(ContentSpaceEventListener.DEFAULT_CONTENTSPACE_EVENTLISTENER);			
			
			rco02.addProperty("perp3", "perp3_v", true);
			
			System.out.println("remove listener again");
			rco02.removeContentObjectEventListener();
			rco02.addProperty("perp4", "perp4_v", true);
			
			cs.removeRootContentObject("addListenerTest");
			cs.closeContentSpace();
		} catch (ContentReposityException e) {		
			e.printStackTrace();
			cs.closeContentSpace();
		}	
		
	}
	
	
	public static void testGetProperty(Object propertyKey){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");
			ContentObjectProperty cop=rco.getProperty(propertyKey.toString());
			if(cop==null){
				System.out.println("Not found");
				return;
			}
			System.out.println(cop.getPropertyName());
			//System.out.println(cop.getPropertyType());
			//System.out.println(cop.isMultiple());
			printPropertyValue(cop);
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();	
		}
		
	}	

	public static void testGetProperties(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");
			//List<ContentObjectProperty> cops=rco.getProperties();
			List<ContentObjectProperty> cops=rco.getSubContentObjects("RootContentObject4test_08_subContent_5").get(0).getProperties();
			for(ContentObjectProperty cop:cops){
				System.out.println(cop.getPropertyName());
				//System.out.println(cop.getPropertyType());
				//System.out.println(cop.isMultiple());
				printPropertyValue(cop);
			}		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();	
		}
		
	}		
	
	public static void testRemoveProperty(Object propertyKey,boolean recodrVersion){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");			
			//testAddProperty(propertyKey.toString(), "testpropValue",recodrVersion);
			//testGetProperty(propertyKey.toString());	
			System.out.println("-----");
			System.out.println(rco.removeProperty(propertyKey.toString(),recodrVersion));
			System.out.println("-----");
			//testGetProperty(propertyKey.toString());	
			cs.closeContentSpace();
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();
		}			
	}
		
	public static void testUpdateProperty(boolean recodrVersion){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");			
			ContentObjectProperty cop=rco.getProperty("TESTLongProperty_1");
			printPropertyValue(cop);		
			ContentObjectProperty updateProperty=ContentComponentFactory.createContentObjectProperty();
			updateProperty.setMultiple(false);
			updateProperty.setPropertyName("TESTLongProperty_1");
			updateProperty.setPropertyType(PropertyType.LONG);
			updateProperty.setPropertyValue(new Long(12357676));		
			rco.updateProperty(updateProperty, recodrVersion);		
			ContentObjectProperty cop2=rco.getProperty("TESTLongProperty_1");
			printPropertyValue(cop2);		
			cs.closeContentSpace();		
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();
		}
			
	}
	
	public static void testRemovePropertyByObj(Object propertyKey,boolean recodrVersion){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");			
			ContentObjectProperty cop=rco.addProperty(propertyKey.toString(), "XXXXX",recodrVersion);
			printPropertyValue(cop);
			System.out.println("-----");
			System.out.println(rco.removeProperty(cop,false));
			System.out.println("-----");
			cs.closeContentSpace();	
			testGetProperty(cop.getPropertyName());	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();
		}		
							
	}
		
	public static void printPropertyValue(ContentObjectProperty cop){
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

	public static void testAddSubContentObject(){		
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("Programing Projects");
			ContentObject rco=cs.getRootContentObject("Nerve Cell Node Server");
			
			ContentObjectProperty newProperty=ContentComponentFactory.createContentObjectProperty();
			newProperty.setMultiple(false);
			newProperty.setPropertyName("Dev Plan");
			newProperty.setPropertyType(PropertyType.STRING);
			newProperty.setPropertyValue("property Value_4");
			
			List<ContentObjectProperty> paramLst=new ArrayList<ContentObjectProperty>();
			paramLst.add(newProperty);
			
			//rco.addSubContentObject("Code repository", paramLst, true);
			
			rco.getSubContentObject("Code repository").addSubContentObject("Clearcase library", null, true);
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public static void testGetSubContentObjects(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");
			//rco.getSubContentObjects("RootContentObject4test_08_subContent_1");
			//rco.getSubContentObjects("RootContentObject4test_08_subContent_3");		
			List<BaseContentObject> col0=rco.getSubContentObjects("RootContentObject4test_08_subContent_3");		
			/*
			ContentObjectProperty updateProperty=ContentComponentFactory.createContentObjectProperty();
			updateProperty.setMultiple(false);
			updateProperty.setPropertyName("String Test property2");
			updateProperty.setPropertyType(PropertyType.STRING);
			updateProperty.setPropertyValue("property Value_2");		
			col0.get(0).updateProperty(updateProperty, true);
			col0.get(0).addSubContentObject("SubSub contentObject2", null, true);
			col0.get(0).addProperty("Boolean property _1", true, true);
			*/
			List<BaseContentObject> col=rco.getSubContentObjects(null);
			for(BaseContentObject cco: col){
				System.out.println(cco.getContentObjectName());	
				System.out.println(cco.getCurrentVersion().getCurrentVersionNumber());
				if(cco.getCurrentVersion().getCurrentVersionLabels().length>0){
					System.out.println(cco.getCurrentVersion().getCurrentVersionLabels()[0]);
				}			
			}		
			cs.closeContentSpace();		
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();	
		}
			
	}
	
	public static void testRemoveSubContentObject(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");		
			rco.removeSubContentObject("RootContentObject4test_08_subContent_2", true);
			cs.closeContentSpace();		
			testGetSubContentObjects();	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();		
		}
			
	}
	
	public static void testGetAllLinearVersions(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");
			List<VersionObject> vl=rco.getAllLinearVersions();		
			for(VersionObject curretnvo: vl ){			
				System.out.println(curretnvo.getCurrentVersionNumber());	
				System.out.println(curretnvo.getCurrentVersionCreatedDate().getTime());
				if(curretnvo.getCurrentVersionLabels().length>0){
					System.out.println(curretnvo.getCurrentVersionLabels()[0]);	
				}		
			}		
			System.out.println(vl.size());		
			cs.closeContentSpace();		
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();
		}			
	}
	
	public static void restoreToVersion(){		
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_08");
			rco.restoreToVersion("1.35", true);		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();
		}			
	}
	
	public static void testGetAllVersionsInSpace(){
		ContentSpace cs=null;
		try {
			cs = ContentComponentFactory.connectContentSpace("viewfunctionContentSpace_testSpace1");
			ContentObject rco=cs.getRootContentObject("RootContentObject4test_081");		
			List<VersionObject> vol=rco.getCurrentVersion().getAllVersionsInSpace();		
			for(VersionObject vo:vol){
				System.out.println(vo.getCurrentVersionNumber());
				//if(vo.getCurrentVersion().equals("1.39")||vo.getCurrentVersion().equals("1.0")){
				if(vo.getCurrentVersionNumber().equals("1.39")){
					System.out.println(vo.getCurrentContentObject().getProperty("TestVersionProperty2").getPropertyValue());
				}			
			}		
			cs.closeContentSpace();	
		} catch (ContentReposityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cs.closeContentSpace();	
		}
			
	}	
}
