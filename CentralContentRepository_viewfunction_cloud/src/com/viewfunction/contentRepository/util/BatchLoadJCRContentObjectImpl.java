package com.viewfunction.contentRepository.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.jackrabbit.value.ValueFactoryImpl;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.LockObject;
import com.viewfunction.contentRepository.contentBureau.VersionObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentReposityConstant;
import com.viewfunction.contentRepository.util.exception.ContentReposityDataException;
import com.viewfunction.contentRepository.util.exception.ContentReposityENVException;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class BatchLoadJCRContentObjectImpl  implements ContentObject{
	private Node jcrNode;
	private Session jcrSession;
	private String contentObjectName;
	public BatchLoadJCRContentObjectImpl(){}
	
	public BatchLoadJCRContentObjectImpl(Node jcrNode){
		this.setJcrNode(jcrNode);		
	}
	
	@Override
	public ContentObjectProperty addProperty(Object propertyKey, Object propertyValue,boolean recordVersion) throws ContentReposityException {		
		try {
			ContentObjectProperty addedCOP=ContentComponentFactory.createContentObjectProperty();
			addedCOP.setPropertyName(propertyKey.toString());
			addedCOP.setPropertyValue(propertyValue);										
			if(propertyValue instanceof Boolean){				
				getJcrNode().setProperty(propertyKey.toString(), ((Boolean) propertyValue).booleanValue());
				addedCOP.setPropertyType(PropertyType.BOOLEAN);
			}
			else if(propertyValue instanceof boolean[]){				
				getJcrNode().setProperty(propertyKey.toString(),getValueArray(propertyValue));
				addedCOP.setPropertyType(PropertyType.BOOLEAN);
				addedCOP.setMultiple(true);	
			}
			else if(propertyValue instanceof Double){			
				getJcrNode().setProperty(propertyKey.toString(),((Double)propertyValue).doubleValue());
				addedCOP.setPropertyType(PropertyType.DOUBLE);
			}
			else if(propertyValue instanceof double[]){				
				getJcrNode().setProperty(propertyKey.toString(),getValueArray(propertyValue));
				addedCOP.setPropertyType(PropertyType.DOUBLE);
				addedCOP.setMultiple(true);	
			}			
			else if(propertyValue instanceof Long){
				getJcrNode().setProperty(propertyKey.toString(),((Long)propertyValue).longValue());
				addedCOP.setPropertyType(PropertyType.LONG);
			}
			else if(propertyValue instanceof long[]){
				getJcrNode().setProperty(propertyKey.toString(),getValueArray(propertyValue));
				addedCOP.setPropertyType(PropertyType.LONG);
				addedCOP.setMultiple(true);
			}			
			else if(propertyValue instanceof BigDecimal){
				getJcrNode().setProperty(propertyKey.toString(),(BigDecimal)propertyValue);
				addedCOP.setPropertyType(PropertyType.DECIMAL);
			}
			else if(propertyValue instanceof BigDecimal[]){
				getJcrNode().setProperty(propertyKey.toString(),getValueArray(propertyValue));
				addedCOP.setPropertyType(PropertyType.DECIMAL);
				addedCOP.setMultiple(true);
			}			
			else if(propertyValue instanceof Binary){
				getJcrNode().setProperty(propertyKey.toString(),(Binary)propertyValue);
				addedCOP.setPropertyType(PropertyType.BINARY);
			}
			else if(propertyValue instanceof Binary[]){
				getJcrNode().setProperty(propertyKey.toString(),getValueArray(propertyValue));
				addedCOP.setPropertyType(PropertyType.BINARY);
				addedCOP.setMultiple(true);
			}	
			else if(propertyValue instanceof Calendar){
				getJcrNode().setProperty(propertyKey.toString(),(Calendar)propertyValue);
				addedCOP.setPropertyType(PropertyType.DATE);
			}
			else if(propertyValue instanceof Calendar[]){
				getJcrNode().setProperty(propertyKey.toString(),getValueArray(propertyValue));
				addedCOP.setPropertyType(PropertyType.DATE);
				addedCOP.setMultiple(true);
			}
			else if(propertyValue instanceof String){
				getJcrNode().setProperty(propertyKey.toString(),(String)propertyValue);
				addedCOP.setPropertyType(PropertyType.STRING);
			}
			else if(propertyValue instanceof String[]){
				getJcrNode().setProperty(propertyKey.toString(),(String[])propertyValue);
				addedCOP.setPropertyType(PropertyType.STRING);
				addedCOP.setMultiple(true);
			}			
			else{
				return null;
			}					
			return addedCOP;			
		} catch (ValueFormatException e) {	
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;			
		} catch (VersionException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (LockException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (ConstraintViolationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}

	@Override
	public ContentObjectProperty getProperty(Object propertyKey) throws ContentReposityException {
		/*
		PropertyType.STRING
		PropertyType.BINARY
		PropertyType.DATE
		PropertyType.DOUBLE
		PropertyType.LONG
		PropertyType.BOOLEAN
		PropertyType.NAME
		PropertyType.PATH
		PropertyType.REFERENCE
		PropertyType.WEAKREFERENCE
		PropertyType.URI
		*/
		if(this.getJcrNode()==null){			
			return null;
		}else{			
			try {
				if(!this.getJcrNode().hasProperty(propertyKey.toString())){					
					return null;
				}else{
					ContentObjectProperty cop=ContentComponentFactory.createContentObjectProperty();					
					Property p=this.getJcrNode().getProperty(propertyKey.toString());
					cop.setPropertyName(p.getName());
					cop.setPropertyType(p.getType());
					cop.setMultiple(p.isMultiple());					
					setContentObjectPropertyValue(p,cop);					
					return cop;
				}
			} catch (PathNotFoundException e) {
				ContentReposityDataException cpe=new ContentReposityDataException();
				cpe.initCause(e);
				throw cpe;
			} catch (RepositoryException e) {
				ContentReposityException cpe=new ContentReposityException();
				cpe.initCause(e);
				throw cpe;
			}			
		}
	}
	
	@Override
	public List<ContentObjectProperty> getProperties() throws ContentReposityException {
		if(this.getJcrNode()==null){
			return null;
		}else{			
			try {				
				PropertyIterator pi=this.getJcrNode().getProperties();
				List<ContentObjectProperty> pList=new ArrayList<ContentObjectProperty>();
				while(pi.hasNext()){					
					Property cp=pi.nextProperty();
					if(!cp.getName().contains("jcr:")&&!cp.getName().contains(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_TOKEN)){						
						ContentObjectProperty cop=ContentComponentFactory.createContentObjectProperty();
						cop.setPropertyName(cp.getName());
						cop.setPropertyType(cp.getType());
						cop.setMultiple(cp.isMultiple());
						setContentObjectPropertyValue(cp,cop);						
						pList.add(cop);	
					}					
				}
				return pList;				
			} catch (RepositoryException e) {
				ContentReposityException cpe=new ContentReposityException();
				cpe.initCause(e);
				throw cpe;
			}
		}
	}

	@Override
	public boolean removeProperty(Object propertyKey,boolean recordVersion) throws ContentReposityException {
		if(this.getJcrNode()==null){
			return false;
		}else{
			try {
				if(!this.getJcrNode().hasProperty(propertyKey.toString())){
					return false;
				}else{												
					Property p=this.getJcrNode().getProperty(propertyKey.toString());
					p.remove();					
					return true;
				}
			} catch (RepositoryException e) {
				ContentReposityException cpe=new ContentReposityException();
				cpe.initCause(e);
				throw cpe;
			}		
		}		
	}
	
	@Override
	public boolean removeProperty(ContentObjectProperty property,boolean recordVersion) throws ContentReposityException {		
		return removeProperty(property.getPropertyName(),recordVersion);
	}
	
	@Override
	public ContentObjectProperty updateProperty(ContentObjectProperty propertyValue,boolean recordVersion) throws ContentReposityException {		
		if(propertyValue==null){			
			return null;
		}		
		try {			
			Property p=this.getJcrNode().getProperty(propertyValue.getPropertyName());			
			if(propertyValue.getPropertyType()!=p.getType()){				
				return null;
			}
			Object newPropertyValueObj=propertyValue.getPropertyValue();
			ContentObjectProperty updatedProperty=ContentComponentFactory.createContentObjectProperty();
			updatedProperty.setPropertyName(propertyValue.getPropertyName());
			updatedProperty.setPropertyType(propertyValue.getPropertyType());
			updatedProperty.setPropertyValue(newPropertyValueObj);				
			
			if(p.isMultiple()){		
				Value[] jcrPropertyValueArray=getValueArray(newPropertyValueObj);				
				p.setValue(jcrPropertyValueArray);				
				updatedProperty.setMultiple(true);				
			}else{
				updatedProperty.setMultiple(false);					
				switch(propertyValue.getPropertyType()){
					case PropertyType.BOOLEAN:{						
						Boolean btd=(Boolean)newPropertyValueObj;
						p.setValue(btd.booleanValue());											
						break;
					}
					case PropertyType.DOUBLE:{ 
						Double dtd=(Double)newPropertyValueObj;
						p.setValue(dtd.doubleValue());						
						break;
					}
					case PropertyType.LONG:{ 
						Long ltd=(Long)newPropertyValueObj;					
						p.setValue(ltd.longValue());
						break;
					}						
					case PropertyType.DECIMAL:{ 
						BigDecimal bed=(BigDecimal)newPropertyValueObj;	
						p.setValue(bed);
						break;
					}												
					case PropertyType.BINARY:{ 
						Binary batd=(Binary)newPropertyValueObj;
						p.setValue(batd);
						break;
					}	
					case PropertyType.DATE:{ 
						Calendar dtd=(Calendar)newPropertyValueObj;
						p.setValue(dtd);						
						break;
					}	
					case PropertyType.STRING:{ 
						p.setValue(newPropertyValueObj.toString());					
						break;
					}
				}
			}			
			return updatedProperty;
		} catch (PathNotFoundException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
	}
	
	@Override
	public List<BaseContentObject> getSubContentObjects(Object subContentObjectsKey) throws ContentReposityException {		
		try {
			NodeIterator ni;
			if(subContentObjectsKey==null){
				ni=getJcrNode().getNodes();				
			}else{
				ni=getJcrNode().getNodes(subContentObjectsKey.toString());				
			}			
			List<BaseContentObject> coList=new ArrayList<BaseContentObject>(); 
			Node currentNode;
			while(ni.hasNext()){				
				currentNode=(Node)ni.next();				
				ContentObject cco=	new BatchLoadJCRContentObjectImpl();				
				cco.setContentObjectName(currentNode.getName());
				cco.setContentData(currentNode);
				cco.setContentSession(getJcrSession());				
				coList.add(cco);								
			}	
			return coList;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
	}

	@Override
	public ContentObject addSubContentObject(Object subContentKey,List<ContentObjectProperty> contentObjectList,boolean recordVersion) throws ContentReposityException {		
		ContentObject addedCO=ContentComponentFactory.createBatchLoadContentObject();		
		try {						
			Node addedJcrNode=this.getJcrNode().addNode(subContentKey.toString());	
			//support query
			addedJcrNode.addMixin("vfcr:content");
			if(contentObjectList!=null&&contentObjectList.size()>0){
				for(ContentObjectProperty cp:contentObjectList){					
					if(cp.isMultiple()){						
						Value[] jcrPropertyValueArray=getValueArray(cp.getPropertyValue());	
						addedJcrNode.setProperty(cp.getPropertyName(), jcrPropertyValueArray);
					}else{								
						switch(cp.getPropertyType()){
							case PropertyType.BOOLEAN:{						
								Boolean btd=(Boolean)cp.getPropertyValue();								
								addedJcrNode.setProperty(cp.getPropertyName(), btd.booleanValue());	
								break;
							}
							case PropertyType.DOUBLE:{ 
								Double dtd=(Double)cp.getPropertyValue();									
								addedJcrNode.setProperty(cp.getPropertyName(), dtd.doubleValue());
								break;
							}
							case PropertyType.LONG:{ 
								Long ltd=(Long)cp.getPropertyValue();
								addedJcrNode.setProperty(cp.getPropertyName(), ltd.longValue());
								break;
							}						
							case PropertyType.DECIMAL:{ 
								BigDecimal bed=(BigDecimal)cp.getPropertyValue();
								addedJcrNode.setProperty(cp.getPropertyName(), bed);
								break;
							}												
							case PropertyType.BINARY:{ 
								Binary batd=(Binary)cp.getPropertyValue();								
								addedJcrNode.setProperty(cp.getPropertyName(),batd);
								break;
							}	
							case PropertyType.DATE:{ 
								Calendar dtd=(Calendar)cp.getPropertyValue();								
								addedJcrNode.setProperty(cp.getPropertyName(), dtd);
								break;
							}	
							case PropertyType.STRING:{ 								
								addedJcrNode.setProperty(cp.getPropertyName(), cp.getPropertyValue().toString());
								break;
							}
						}
					}
				}				
			}			
			addedCO.setContentData(addedJcrNode);
			addedCO.setContentSession(getJcrSession());	
			addedCO.setContentObjectName(subContentKey.toString());								
			return addedCO;
		} catch (ItemExistsException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (PathNotFoundException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (VersionException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (ConstraintViolationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (LockException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}			
	}
	
	@Override
	public boolean removeSubContentObject(Object subContentKey,boolean recordVersion) throws ContentReposityException {
		try {
						
			if(this.getJcrNode().hasNode(subContentKey.toString())){				
				Node subNode=this.getJcrNode().getNode(subContentKey.toString());				
				if(subNode.getSharedSet().getSize()>1){
					subNode.removeShare();
				}else{
					subNode.remove();					
				}												
				return true;
			}else{
				return false;
			}			
		} catch (UnsupportedRepositoryOperationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (LockException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}

	private void setJcrNode(Node jcrNode) {
		this.jcrNode = jcrNode;
	}

	public Node getJcrNode() {
		return jcrNode;
	}

	@Override
	public void setContentData(Object contentData) {
		this.setJcrNode((Node)contentData);			
	}

	private void setJcrSession(Session jcrSession) {
		this.jcrSession = jcrSession;
	}

	public Session getJcrSession() {
		if(this.jcrSession==null){
			try {
				return this.jcrNode.getSession();
			} catch (RepositoryException e) {	
				//need refactor for exception handle
				e.printStackTrace();
			}
		}else{
			return jcrSession;			
		}				
		return null;		
	}

	@Override
	public void setContentSession(Object contentSession) {
		this.setJcrSession((Session)contentSession);		
	}
	
	private static Value[] getValueArray(Object dataArray){		
		ValueFactory vf=ValueFactoryImpl.getInstance();
		Value[] va=null;
		if (dataArray instanceof boolean[]){			
			boolean[] bva=(boolean[])dataArray;			
			va=new Value[bva.length];
			for(int i=0;i<bva.length;i++){
				va[i]=vf.createValue(bva[i]);		
			}
		}else if (dataArray instanceof double[]){			
			double[] bva=(double[])dataArray;			
			va=new Value[bva.length];
			for(int i=0;i<bva.length;i++){
				va[i]=vf.createValue(bva[i]);		
			}
		}else if (dataArray instanceof long[]){			
			long[] bva=(long[])dataArray;			
			va=new Value[bva.length];
			for(int i=0;i<bva.length;i++){
				va[i]=vf.createValue(bva[i]);		
			}
		}else if (dataArray instanceof BigDecimal[]){			
			BigDecimal[] bva=(BigDecimal[])dataArray;			
			va=new Value[bva.length];
			for(int i=0;i<bva.length;i++){
				va[i]=vf.createValue(bva[i]);		
			}
		}else if (dataArray instanceof Binary[]){			
			Binary[] bva=(Binary[])dataArray;			
			va=new Value[bva.length];
			for(int i=0;i<bva.length;i++){
				va[i]=vf.createValue(bva[i]);		
			}
		}else if (dataArray instanceof Calendar[]){			
			Calendar[] bva=(Calendar[])dataArray;			
			va=new Value[bva.length];
			for(int i=0;i<bva.length;i++){
				va[i]=vf.createValue(bva[i]);		
			}
		}else if (dataArray instanceof String[]){			
			String[] bva=(String[])dataArray;			
			va=new Value[bva.length];
			for(int i=0;i<bva.length;i++){
				va[i]=vf.createValue(bva[i]);		
			}
		}	
		return va;		
	}	
	
	private static ContentObjectProperty setContentObjectPropertyValue(Property p,ContentObjectProperty cop) throws ContentReposityException{		
		try {
			switch(p.getType()){
				case PropertyType.BOOLEAN:{ 
					if(p.isMultiple()){
						Value[] bva=p.getValues();
						boolean[] ba=new boolean[bva.length];
						for(int i=0;i<bva.length;i++){
							ba[i]=bva[i].getBoolean();									
						} 
						cop.setPropertyValue(ba);								
					}else{
						cop.setPropertyValue(new Boolean(p.getBoolean()));
					}							
					break;
				}
				case PropertyType.DOUBLE:{ 
					if(p.isMultiple()){
						Value[] bva=p.getValues();
						double[] ba=new double[bva.length];
						for(int i=0;i<bva.length;i++){
							ba[i]=bva[i].getDouble();									
						} 
						cop.setPropertyValue(ba);								
					}else{
						cop.setPropertyValue(new Double(p.getDouble()));
					}							
					break;
				}
				case PropertyType.LONG:{ 
					if(p.isMultiple()){
						Value[] bva=p.getValues();
						long[] ba=new long[bva.length];
						for(int i=0;i<bva.length;i++){
							ba[i]=bva[i].getLong();									
						} 
						cop.setPropertyValue(ba);								
					}else{
						cop.setPropertyValue(new Long(p.getLong()));
					}							
					break;
				}						
				case PropertyType.DECIMAL:{ 
					if(p.isMultiple()){
						Value[] bva=p.getValues();
						BigDecimal[] ba=new BigDecimal[bva.length];
						for(int i=0;i<bva.length;i++){
							ba[i]=bva[i].getDecimal();									
						} 
						cop.setPropertyValue(ba);								
					}else{
						cop.setPropertyValue(p.getDecimal());
					}							
					break;
				}												
				case PropertyType.BINARY:{ 
					if(p.isMultiple()){
						Value[] bva=p.getValues();
						Binary[] ba=new Binary[bva.length];
						for(int i=0;i<bva.length;i++){
							ba[i]=bva[i].getBinary();									
						} 
						cop.setPropertyValue(ba);								
					}else{
						cop.setPropertyValue(p.getBinary());
					}							
					break;
				}	
				case PropertyType.DATE:{ 
					if(p.isMultiple()){
						Value[] bva=p.getValues();
						Calendar[] ba=new Calendar[bva.length];
						for(int i=0;i<bva.length;i++){
							ba[i]=bva[i].getDate();									
						} 
						cop.setPropertyValue(ba);								
					}else{
						cop.setPropertyValue(p.getDate());
					}							
					break;
				}	
				case PropertyType.STRING:{ 
					if(p.isMultiple()){
						Value[] bva=p.getValues();
						String[] ba=new String[bva.length];
						for(int i=0;i<bva.length;i++){
							ba[i]=bva[i].getString();									
						} 
						cop.setPropertyValue(ba);								
					}else{
						cop.setPropertyValue(p.getString());
					}							
					break;
				}
			}
		} catch (ValueFormatException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (IllegalStateException e) {
			ContentReposityENVException cpe=new ContentReposityENVException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
		return cop;	
	}

	@Override
	public void setContentObjectName(String contentObjectName) {		
		this.contentObjectName=contentObjectName;		
	}

	@Override
	public String getContentObjectName() {
		return this.contentObjectName;				
	}

	@Override
	public List<VersionObject> getAllVersionsInSpace() throws ContentReposityException {
		return this.getCurrentVersion().getAllVersionsInSpace();		
	}

	@Override
	public boolean renameSubContentObject(Object subContentKey,	Object newsubContentKey, boolean recordVersion)	throws ContentReposityException {		
		try {
			if(subContentKey.toString().equals(newsubContentKey.toString())){
				return false;
			}
			if(this.getJcrNode().hasNode(subContentKey.toString())){
				
				Node needChangedNode=this.getJcrNode().getNode(subContentKey.toString());				
				String oldPath=needChangedNode.getPath();
				String newPath=this.getJcrNode().getPath()+"/"+newsubContentKey.toString();
				this.getJcrSession().move(oldPath, newPath);				
				return true;			
			}else{
				return false;
			}
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}

	@Override
	public BaseContentObject getParentContentObject() throws ContentReposityException {
		try {	
			Node parentNode=this.getJcrNode().getParent();		
			if(parentNode.getName().equals("")){				
				return null;
			}else{				
				ContentObject pco=	ContentComponentFactory.createContentObject();				
				pco.setContentObjectName(parentNode.getName());
				pco.setContentData(parentNode);
				pco.setContentSession(getJcrSession());	
				return pco;
			}
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}

	@Override
	public boolean rename(Object newsubContentKey, boolean recordVersion)throws ContentReposityException {		
		BaseContentObject pco=this.getParentContentObject();
		return pco.renameSubContentObject(this.getContentObjectName(), newsubContentKey, recordVersion);
	}

	@Override
	public boolean renameProperty(Object propertyID, Object newPropertyID,boolean recordVersion) throws ContentReposityException {
		try {				
			Object propertyValueObj=this.getProperty(propertyID.toString()).getPropertyValue();
			this.addProperty(newPropertyID.toString(), propertyValueObj, false);
			this.removeProperty(propertyID.toString(), false);						
			return true;			
		} catch (Exception e) {
			ContentReposityException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}

	@Override
	public BaseContentObject getSubContentObject(Object subContentObjectsKey)throws ContentReposityException {
		try {			
			if(!this.getJcrNode().hasNode(subContentObjectsKey.toString())){
				return null;
			}else{
				Node subNode=this.getJcrNode().getNode(subContentObjectsKey.toString());
				ContentObject cco=ContentComponentFactory.createBatchLoadContentObject();				
				cco.setContentObjectName(subNode.getName());
				cco.setContentData(subNode);
				cco.setContentSession(getJcrSession());
				return cco;
			}			
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
	}	
	
	@Override
	public boolean addProperty(List<ContentObjectProperty> contentObjectPropertyList, boolean recordVersion) throws ContentReposityException {
		try {			
			String propertyName=null;
			int propertyType=0;
			boolean isMultiple=false;
			Object propertyValue=null;
			for(ContentObjectProperty contentObjectProperty :contentObjectPropertyList){				
				propertyName=contentObjectProperty.getPropertyName();
				propertyType=contentObjectProperty.getPropertyType();				
				isMultiple=contentObjectProperty.isMultiple();
				propertyValue=contentObjectProperty.getPropertyValue();
				if(isMultiple){		
					Value[] jcrPropertyValueArray=getValueArray(propertyValue);				
					getJcrNode().setProperty(propertyName, jcrPropertyValueArray);
				}else{									
					switch(propertyType){
						case PropertyType.BOOLEAN:{						
							Boolean btd=(Boolean)propertyValue;
							getJcrNode().setProperty(propertyName,btd.booleanValue());											
							break;
						}
						case PropertyType.DOUBLE:{ 
							Double dtd=(Double)propertyValue;
							getJcrNode().setProperty(propertyName,dtd.doubleValue());						
							break;
						}
						case PropertyType.LONG:{ 
							Long ltd=(Long)propertyValue;					
							getJcrNode().setProperty(propertyName,ltd.longValue());
							break;
						}						
						case PropertyType.DECIMAL:{ 
							BigDecimal bed=(BigDecimal)propertyValue;	
							getJcrNode().setProperty(propertyName,bed);
							break;
						}												
						case PropertyType.BINARY:{ 
							Binary batd=(Binary)propertyValue;
							getJcrNode().setProperty(propertyName,batd);
							break;
						}	
						case PropertyType.DATE:{ 
							Calendar dtd=(Calendar)propertyValue;
							getJcrNode().setProperty(propertyName,dtd);						
							break;
						}	
						case PropertyType.STRING:{ 
							getJcrNode().setProperty(propertyName,propertyValue.toString());					
							break;
						}
					}
				}				
			}		
			return true;			
		} catch (ValueFormatException e) {	
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;			
		} catch (VersionException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (LockException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (ConstraintViolationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
	}

	@Override
	public long getSubContentObjectsCount() throws ContentReposityException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean addSubLinkContentObject(Object subContentKey,
			BaseContentObject linkedContentObject, boolean recordVersion)
			throws ContentReposityException {
		// TODO Auto-generated method stub
		return false;
	}
	/*
	@Override
	public boolean isLinkContentObject() throws ContentReposityException {
		// TODO Auto-generated method stub
		return false;
	}
	 */
	@Override
	public LockObject lock(boolean isContentSpaceScoped)
			throws ContentReposityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean unlock() throws ContentReposityException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLocked() throws ContentReposityException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LockObject getLockOject() throws ContentReposityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addContentObjectEventListener(int eventListenerType)
			throws ContentReposityException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeContentObjectEventListener()
			throws ContentReposityException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public VersionObject getCurrentVersion() throws ContentReposityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VersionObject> getAllLinearVersions()
			throws ContentReposityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean restoreToVersion(String targetVersion, boolean removeExisting)
			throws ContentReposityException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LockObject lock(boolean isContentSpaceScoped, String locker)
			throws ContentReposityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean unlock(String locker) throws ContentReposityException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getLocker() throws ContentReposityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseContentObject> getSubLinkContentObjects(Object subContentObjectsKey)
			throws ContentReposityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseContentObject getSubLinkContentObject(Object subContentObjectsKey) throws ContentReposityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeSubLinkContentObject(Object subContentKey, boolean recordVersion)
			throws ContentReposityException {
		// TODO Auto-generated method stub
		return false;
	}	
}