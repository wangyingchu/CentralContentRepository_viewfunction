package com.viewfunction.contentRepository.contentBureauImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.jcr.Binary;
import javax.jcr.InvalidItemStateException;
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
import javax.jcr.lock.Lock;
import javax.jcr.lock.LockException;
import javax.jcr.lock.LockManager;
import javax.jcr.nodetype.ConstraintViolationException;

import javax.jcr.observation.EventListener;
import javax.jcr.observation.EventListenerIterator;
import javax.jcr.observation.ObservationManager;
import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionHistory;

import org.apache.jackrabbit.value.ValueFactoryImpl;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.LockObject;
import com.viewfunction.contentRepository.contentBureau.VersionObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityDataException;
import com.viewfunction.contentRepository.util.exception.ContentReposityENVException;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.observation.ContentSpaceEventListener;

public class JCRContentObjectImpl implements ContentObject{
	private Node jcrNode;
	private Session jcrSession;
	private String contentObjectName;
	public JCRContentObjectImpl(){}
	
	public JCRContentObjectImpl(Node jcrNode){
		this.setJcrNode(jcrNode);		
	}
	
	@Override
	public ContentObjectProperty addProperty(Object propertyKey, Object propertyValue,boolean recordVersion) throws ContentReposityException {		
		try {
			if(isLocked()){
				ContentReposityDataException cpe=new ContentReposityDataException();
				throw cpe;
			}
			ContentObjectProperty addedCOP=ContentComponentFactory.createContentObjectProperty();
			addedCOP.setPropertyName(propertyKey.toString());
			addedCOP.setPropertyValue(propertyValue);	
			getJcrSession().getWorkspace().getVersionManager().checkout(getJcrNode().getPath());	
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
			getJcrSession().save();				
			recordVersionLabel(recordVersion,"Added New property {"+propertyKey.toString()+"}");			
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
					if(!cp.getName().contains("jcr:")&&!cp.getName().contains(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_TOKEN)
						&&!cp.getName().contains(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_OWNER)
						&&!cp.getName().contains(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_LOCKTIME)){						
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
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		if(this.getJcrNode()==null){
			return false;
		}else{
			try {
				if(!this.getJcrNode().hasProperty(propertyKey.toString())){
					return false;
				}else{					
					getJcrSession().getWorkspace().getVersionManager().checkout(getJcrNode().getPath());				
					Property p=this.getJcrNode().getProperty(propertyKey.toString());
					p.remove();
					getJcrSession().save();					
					recordVersionLabel(recordVersion,"Removed property {"+propertyKey.toString()+"}");
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
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		if(propertyValue==null){			
			return null;
		}
		if(propertyValue.getPropertyValue()==null){
			//if the property's value is null,just remove whole property 
			removeProperty(propertyValue,recordVersion);
			return null;
		}		
		try {
			if(!this.getJcrNode().hasProperty(propertyValue.getPropertyName())){
				//if property does not exist, just add it.
				return this.addProperty(propertyValue.getPropertyName(), propertyValue.getPropertyValue(), recordVersion);				
			}
		} catch (RepositoryException e1) {			
			e1.printStackTrace();
			return null;
		}		
		try {
			getJcrSession().getWorkspace().getVersionManager().checkout(getJcrNode().getPath());
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
			getJcrSession().save();				
			recordVersionLabel(recordVersion,"Updated property {"+propertyValue.getPropertyName()+"}");
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
				if(!currentNode.getName().equals(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER)){
					ContentObject cco=	ContentComponentFactory.createContentObject();				
					cco.setContentObjectName(currentNode.getName());
					cco.setContentData(currentNode);
					cco.setContentSession(getJcrSession());				
					coList.add(cco);					
				}							
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
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		ContentObject addedCO=ContentComponentFactory.createContentObject();		
		try {			
			getJcrSession().getWorkspace().getVersionManager().checkout(getJcrNode().getPath());
			if(this.getJcrNode().hasNode(subContentKey.toString())){
				return null;
			}
			Node addedJcrNode=this.getJcrNode().addNode(subContentKey.toString());
			//support query
			addedJcrNode.addMixin("vfcr:content");
			//support version control
			addedJcrNode.addMixin("mix:versionable");
			addedJcrNode.addMixin("mix:referenceable");
			addedJcrNode.addMixin("mix:lockable");			
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
			getJcrSession().save();			
			recordVersionLabel(recordVersion,"Added sub Content Object {"+subContentKey.toString()+"}");
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
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		try {
			getJcrSession().getWorkspace().getVersionManager().checkout(getJcrNode().getPath());			
			if(this.getJcrNode().hasNode(subContentKey.toString())){				
				Node subNode=this.getJcrNode().getNode(subContentKey.toString());				
				if(subNode.getSharedSet().getSize()>1){
					subNode.removeShare();
				}else{
					subNode.remove();					
				}				
				getJcrSession().save();	
				recordVersionLabel(recordVersion,"Removed sub Content Object {"+subContentKey.toString()+"}");				
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

	private void recordVersionLabel(boolean recordVersion,String labelMessage) throws ContentReposityException{
		if(recordVersion){
			try {
				getJcrSession().getWorkspace().getVersionManager().checkin(getJcrNode().getPath());
				String currentVersionName=getJcrSession().getWorkspace().getVersionManager().getBaseVersion(getJcrNode().getPath()).getName();	
				getJcrSession().getWorkspace().getVersionManager().getVersionHistory(getJcrNode().getPath()).addVersionLabel(currentVersionName, "V"+currentVersionName+" -> "+labelMessage, false);
			} catch (VersionException e) {
				ContentReposityDataException cpe=new ContentReposityDataException();
				cpe.initCause(e);
				throw cpe;
			} catch (UnsupportedRepositoryOperationException e) {
				ContentReposityDataException cpe=new ContentReposityDataException();
				cpe.initCause(e);
				throw cpe;
			} catch (InvalidItemStateException e) {
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
	}		

	@Override
	public boolean restoreToVersion(String targetVersion,boolean removeExisting) throws ContentReposityException {	
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		try {			
			VersionHistory jcrVersionHistory=getJcrSession().getWorkspace().getVersionManager().getVersionHistory(getJcrNode().getPath());			
			Version jcrTargetVersion=jcrVersionHistory.getVersion(targetVersion);			
			getJcrSession().getWorkspace().getVersionManager().restore(jcrTargetVersion, removeExisting);
			return true;
		} catch (UnsupportedRepositoryOperationException e) {			
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
	public List<VersionObject> getAllLinearVersions() throws ContentReposityException {			
		List<VersionObject> vl=new ArrayList<VersionObject>();
		VersionObject currentVO=this.getCurrentVersion();		
		vl.add(currentVO);		
		VersionObject curretnvo=currentVO.getPredecessorVersionObject();
		vl.add(curretnvo);
		while(curretnvo!=null){			
			curretnvo=curretnvo.getPredecessorVersionObject();	
			if(curretnvo!=null){
				vl.add(curretnvo);
			}			
		}		
		return vl;
	}

	@Override
	public VersionObject getCurrentVersion() {		
		VersionObject cvb=ContentComponentFactory.createVersionObject();
		cvb.setBaseVersionData(getJcrNode());
		cvb.setVersionSession(getJcrSession());		
		return cvb;
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
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		try {
			if(subContentKey.toString().equals(newsubContentKey.toString())){
				return false;
			}
			if(this.getJcrNode().hasNode(subContentKey.toString())){
				getJcrSession().getWorkspace().getVersionManager().checkout(getJcrNode().getPath());
				Node needChangedNode=this.getJcrNode().getNode(subContentKey.toString());				
				String oldPath=needChangedNode.getPath();
				String newPath=this.getJcrNode().getPath()+"/"+newsubContentKey.toString();
				this.getJcrSession().move(oldPath, newPath);								
				getJcrSession().save();	
				recordVersionLabel(recordVersion,"Renamed sub Content Object {"+subContentKey.toString()+" to "+newsubContentKey.toString()+"}");	
				if(recordVersion){
					Node nameChangedNode=this.getJcrNode().getNode(newsubContentKey.toString());
					getJcrSession().getWorkspace().getVersionManager().checkout(nameChangedNode.getPath());				
					getJcrSession().getWorkspace().getVersionManager().checkin(nameChangedNode.getPath());				
					String currentVersionName=getJcrSession().getWorkspace().getVersionManager().getBaseVersion(nameChangedNode.getPath()).getName();	
					getJcrSession().getWorkspace().getVersionManager().getVersionHistory(nameChangedNode.getPath()).addVersionLabel(currentVersionName, "V"+currentVersionName+" -> "+"changed name from "+subContentKey.toString()+" to "+newsubContentKey.toString(), false);				
				}
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
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		BaseContentObject pco=this.getParentContentObject();
		return pco.renameSubContentObject(this.getContentObjectName(), newsubContentKey, recordVersion);
	}

	@Override
	public boolean renameProperty(Object propertyID, Object newPropertyID,boolean recordVersion) throws ContentReposityException {
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		try {	
			getJcrSession().getWorkspace().getVersionManager().checkout(getJcrNode().getPath());
			Object propertyValueObj=this.getProperty(propertyID.toString()).getPropertyValue();
			this.addProperty(newPropertyID.toString(), propertyValueObj, false);
			this.removeProperty(propertyID.toString(), false);
			recordVersionLabel(recordVersion,"Renamed Property {"+propertyID.toString()+" to "+newPropertyID.toString()+"}");			
			return true;			
		} catch (RepositoryException e) {
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
				ContentObject cco=ContentComponentFactory.createContentObject();				
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
	public LockObject lock(boolean isTemporaryLock) throws ContentReposityException {
		String locker=getJcrSession().getUserID();
		return lock(isTemporaryLock,locker);	
	}
	
	@Override
	public LockObject lock(boolean isTemporaryLock, String locker) throws ContentReposityException{
	    try{
	    	LockManager lm = getJcrSession().getWorkspace().getLockManager();
	    	String nodPath = getJcrNode().getPath();
	    	long timeoutHint = Long.MAX_VALUE;
	    	//absPath - absolute path of node to be locked
	    	//isDeep - if true this lock will apply to this node and all its descendants; if false, it applies only to this node.
	    	//isSessionScoped - if true, this lock expires with the current session; if false it expires when explicitly or automatically unlocked for some other reason.
	    	//timeoutHint - desired lock timeout in seconds (servers are free to ignore this value); specify Long.MAX_VALUE for no timeout.
	    	//ownerInfo - a string containing owner information supplied by the client; servers are free to ignore this value.
	    	Lock lk = lm.lock(nodPath, false, isTemporaryLock, timeoutHint, null);
	    	LockObject lo = ContentComponentFactory.createLockObject();
	    	((JCRLockObjectImpl)lo).setLockData(lk);
	    	//In Jackrabbit v3 OAK, lockManager no longer record lock ownerInfo passed in method param, 
	    	//it always use user login info.So need add addational Property CONTENT_OBJECT_JCR_LOCK_OWNER to record real locker id
	    	if(locker!=null){
	    		getJcrNode().setProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_OWNER, locker);
	    		getJcrSession().save();
	    		((JCRLockObjectImpl)lo).setLocker(locker);
	    	}else{
	    		ContentReposityDataException cpe = new ContentReposityDataException();
		    	throw cpe;
	    	}
	    	if (!isTemporaryLock) {
	    		//is persistLock,need set parameters
				//In Jackrabbit v3 OAK, lock no longer recorded after session logout, so need add parameters for persist lock during dirrerent sessions
				//if isTemporaryLock is true, lk.getLockToken() return null
		    	//if isTemporaryLock is false, lk.getLockToken() return path value
	    		String lockToken = lk.getLockToken();
	    		getJcrNode().setProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_TOKEN, lockToken);
	    		long currentDateTimeInLong=new Date().getTime();
				getJcrNode().setProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_LOCKTIME, currentDateTimeInLong);
	    		getJcrSession().save();
	    	}else{
	    		((JCRLockObjectImpl)lo).setTemporaryLock(true);
	    	}
	    	return lo;
	    } catch (UnsupportedRepositoryOperationException e) {
	    	ContentReposityDataException cpe = new ContentReposityDataException();
	    	cpe.initCause(e);
	    	throw cpe;
	    } catch (RepositoryException e) {
	    	ContentReposityDataException cpe = new ContentReposityDataException();
	    	cpe.initCause(e);
	    	throw cpe;
	    }
	 }

	@Override
	public boolean unlock() throws ContentReposityException {
		String currentUser=getJcrSession().getUserID();
		return unlock(currentUser);
	}
	
	@Override
	public boolean unlock(String locker) throws ContentReposityException {
		try {	
			if(getJcrNode().hasProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_OWNER)){
				String currentLocker=getJcrNode().getProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_OWNER).getValue().toString();
				if(!locker.equals(currentLocker)){
					return false;
				}
			}else{
				return false;
			}
			//check if temporary locked
			LockManager lm=getJcrSession().getWorkspace().getLockManager();
			String nodPath=getJcrNode().getPath();
			boolean isNodeLocked=lm.isLocked(nodPath);
			if(isNodeLocked){
				Lock lk=lm.getLock(nodPath);
				String lockTokenString="";
				if(!lk.isSessionScoped()){
					if(!lk.isLockOwningSession()){
						Property tokenP=getJcrNode().getProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_TOKEN);
						lockTokenString=tokenP.getString();
						lm.addLockToken(lockTokenString);
						tokenP.remove();
						getJcrSession().save();
					}
				}
				lm.unlock(nodPath); 
			}
			//check if persist locked
			Property ownerProperty=getJcrNode().getProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_OWNER);
			ownerProperty.remove();
			if(getJcrNode().hasProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_LOCKTIME)){
				Property lockDateProperty=getJcrNode().getProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_LOCKTIME);
				lockDateProperty.remove();
			}
			if(getJcrNode().hasProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_TOKEN)){
				Property lockTockenProperty=getJcrNode().getProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_TOKEN);
				lockTockenProperty.remove();
			}
			getJcrSession().save();
			return true;
		} catch (UnsupportedRepositoryOperationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}	 

	@Override
	public boolean isLocked() throws ContentReposityException {
		try {
			//check if temporary locked
			LockManager lm=getJcrSession().getWorkspace().getLockManager();
			String nodPath=getJcrNode().getPath();	
			boolean isTemparyLocked=lm.isLocked(nodPath);
			if(isTemparyLocked){
				return true;
			}else{
				//check if persist locked
				if(getJcrNode().hasProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_TOKEN)){
					return true;
				}else{
					return false;
				}
			}			
			
		} catch (UnsupportedRepositoryOperationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}

	@Override
	public LockObject getLockOject() throws ContentReposityException {
		try {
			if(!this.isLocked()){
				return null;
			}			
			LockObject lo=ContentComponentFactory.createLockObject();
			LockManager lm=getJcrSession().getWorkspace().getLockManager();
			String nodPath=getJcrNode().getPath();	
			boolean isTemparyLocked=lm.isLocked(nodPath);
			if(isTemparyLocked){
				Lock ol=lm.getLock(nodPath);	
				((JCRLockObjectImpl)lo).setLockData(ol);
				((JCRLockObjectImpl)lo).setTemporaryLock(true);
			}
			if(getJcrNode().hasProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_TOKEN)){
				((JCRLockObjectImpl)lo).setTemporaryLock(false);
			}
			if(getJcrNode().hasProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_OWNER)){
				String currentLocker=getJcrNode().getProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_OWNER).getValue().toString();
				((JCRLockObjectImpl)lo).setLocker(currentLocker);
			}
			if(getJcrNode().hasProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_LOCKTIME)){
				long lockDateInLong=getJcrNode().getProperty(JCRContentReposityConstant.CONTENT_OBJECT_JCR_LOCK_LOCKTIME).getValue().getLong();
				((JCRLockObjectImpl)lo).setLockDate(new Date(lockDateInLong));
			}
			return lo;			
		} catch (UnsupportedRepositoryOperationException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}	 

	@Override
	public String getLocker() throws ContentReposityException{
		if (!this.isLocked()) {
			return null;
		}
		return getLockOject().getLocker();
	}

	@Override
	public boolean addContentObjectEventListener(int eventListenerType) throws ContentReposityException {
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		try {
			ObservationManager om=getJcrSession().getWorkspace().getObservationManager();
			
			ContentSpaceEventListener csel0=ContentComponentFactory.createContentSpaceEventListener(eventListenerType);
			if(csel0==null){
				return false;
			}				
			om.addEventListener(csel0, javax.jcr.observation.Event.NODE_ADDED, getJcrNode().getPath(),true,null,null,false);
			
			ContentSpaceEventListener csel1=ContentComponentFactory.createContentSpaceEventListener(eventListenerType);
			if(csel1==null){
				return false;
			}				
			om.addEventListener(csel1, javax.jcr.observation.Event.NODE_MOVED, getJcrNode().getPath(),true,null,null,false);
			
			ContentSpaceEventListener csel2=ContentComponentFactory.createContentSpaceEventListener(eventListenerType);
			if(csel2==null){
				return false;
			}			
			om.addEventListener(csel2, javax.jcr.observation.Event.NODE_REMOVED, getJcrNode().getPath(),true,null,null,false);
			
			ContentSpaceEventListener csel3=ContentComponentFactory.createContentSpaceEventListener(eventListenerType);
			if(csel3==null){
				return false;
			}			
			om.addEventListener(csel3, javax.jcr.observation.Event.PROPERTY_ADDED, getJcrNode().getPath(),true,null,null,false);
			
			ContentSpaceEventListener csel4=ContentComponentFactory.createContentSpaceEventListener(eventListenerType);
			if(csel4==null){
				return false;
			}
			om.addEventListener(csel4, javax.jcr.observation.Event.PROPERTY_CHANGED, getJcrNode().getPath(),true,null,null,false);			
			ContentSpaceEventListener csel5=ContentComponentFactory.createContentSpaceEventListener(eventListenerType);
			if(csel5==null){
				return false;
			}			
			om.addEventListener(csel5, javax.jcr.observation.Event.PROPERTY_REMOVED, getJcrNode().getPath(),true,null,null,false);			
			ContentSpaceEventListener csel6=ContentComponentFactory.createContentSpaceEventListener(eventListenerType);			
			if(csel6==null){
				return false;
			}				
			om.addEventListener(csel6, javax.jcr.observation.Event.PERSIST, getJcrNode().getPath(),true,null,null,false);
			return true;						
		} catch (UnsupportedRepositoryOperationException e) {
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
	public boolean removeContentObjectEventListener()throws ContentReposityException {
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		try {
			ObservationManager om=getJcrSession().getWorkspace().getObservationManager();
			final List<EventListener> eventsListenerList = new ArrayList<EventListener>();
		    final CountDownLatch latch1 = new CountDownLatch(1);
		    final CountDownLatch latch2 = new CountDownLatch(1);
			EventListenerIterator eli=om.getRegisteredEventListeners();
		    CountDownLatch latch = latch1;
		    synchronized (eventsListenerList) {
		    	while (eli.hasNext()) {
		    		eventsListenerList.add(eli.nextEventListener());
		    	}
                latch.countDown();
                latch = latch2;
                for(EventListener eventListener:eventsListenerList){
                	om.removeEventListener(eventListener);
                }
		    }
		    return true;
		} catch (UnsupportedRepositoryOperationException e) {
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
		try {
			NodeIterator ni=getJcrNode().getNodes();
			long subContentObjectNumber=ni.getSize();
			boolean hasLinkObjeckContainer=this.getJcrNode().hasNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
			if(hasLinkObjeckContainer){
				return subContentObjectNumber-1;
			}else{
				return subContentObjectNumber;
			}
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
	}	
	
	@Override
	public boolean addProperty(List<ContentObjectProperty> contentObjectPropertyList, boolean recordVersion) throws ContentReposityException {
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		try {			
			String propertyName=null;
			int propertyType=0;
			boolean isMultiple=false;
			Object propertyValue=null;
			for(ContentObjectProperty contentObjectProperty :contentObjectPropertyList){
				getJcrSession().getWorkspace().getVersionManager().checkout(getJcrNode().getPath());
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
				getJcrSession().save();
				recordVersionLabel(recordVersion,"Added New property {"+propertyName.toString()+"}");
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
	
	/*
	 Till Jackrabbit v3 OAK version 1.2.2 Workspace clone method is not supported, so can't use logic in CCR v2
	@Override
	public boolean addSubLinkContentObject(Object subContentKey,BaseContentObject linkedContentObject, boolean recordVersion)	throws ContentReposityException {		
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		JCRContentObjectImpl targetObj=(JCRContentObjectImpl)linkedContentObject;
		try {
			if(targetObj.getJcrNode()==null){
				ContentReposityDataException cpe=new ContentReposityDataException();				
				throw cpe;
			}
			getJcrSession().getWorkspace().getVersionManager().checkout(getJcrNode().getPath());
			String  tPath = targetObj.getJcrNode().getPath();
			getJcrSession().getWorkspace().getVersionManager().checkout(targetObj.getJcrNode().getPath());			
			targetObj.getJcrNode().addMixin("mix:shareable");
			getJcrSession().save();			
			String lPath=this.getJcrNode().getPath()+"/"+subContentKey.toString();			
			Workspace ws=this.getJcrNode().getSession().getWorkspace();			
			ws.clone(ws.getName(), tPath, lPath, false);
			recordVersionLabel(recordVersion,"Added Link sub content object {"+subContentKey+" from "+linkedContentObject.getContentObjectName()+"}");
			return true;
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}
	
	@Override
	public boolean isLinkContentObject() throws ContentReposityException {		
		try {
			long shareSetNodesNum=this.getJcrNode().getSharedSet().getSize();			
			if(shareSetNodesNum>1){
				return true;
			}else{
				return false;
			}			
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}
	*/
	
	@Override
	public boolean addSubLinkContentObject(Object subContentKey,BaseContentObject linkedContentObject, boolean recordVersion)	throws ContentReposityException {		
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		JCRContentObjectImpl targetObj=(JCRContentObjectImpl)linkedContentObject;
		try {
			if(targetObj.getJcrNode()==null){
				ContentReposityDataException cpe=new ContentReposityDataException();				
				throw cpe;
			}
			getJcrSession().getWorkspace().getVersionManager().checkout(getJcrNode().getPath());
			String  tPath = targetObj.getJcrNode().getPath();
	
			boolean hasLinkObjeckContainer=this.getJcrNode().hasNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
			if(!hasLinkObjeckContainer){
				this.getJcrNode().addNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
				//Node linkObjectContainerJcrNode=this.getJcrNode().addNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
				//linkObjectContainerJcrNode.addMixin("mix:versionable");
				//linkObjectContainerJcrNode.addMixin("mix:lockable");		
			}
			Node linkContainerObject=this.getJcrNode().getNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
			if(linkContainerObject.hasNode(subContentKey.toString())){
				return false;
			}
			Node subLinkNode=linkContainerObject.addNode(subContentKey.toString());
			subLinkNode.setProperty(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECT_PATH, tPath);
			getJcrSession().save();
			recordVersionLabel(recordVersion,"Added Link sub content object {"+subContentKey+" from "+linkedContentObject.getContentObjectName()+"}");
			return true;
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}
	
	@Override
	public List<BaseContentObject> getSubLinkContentObjects(Object subContentObjectsKey) throws ContentReposityException {
		try {
			List<BaseContentObject> coList=new ArrayList<BaseContentObject>(); 
			boolean hasLinkObjeckContainer=this.getJcrNode().hasNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
			if(hasLinkObjeckContainer){
				Node linkContainerObject=this.getJcrNode().getNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
				NodeIterator ni=linkContainerObject.getNodes();	
				Node currentNode;
				while(ni.hasNext()){	
					currentNode=(Node)ni.next();
					if(currentNode.hasProperty(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECT_PATH)){
						String subLinkChildNodePath=currentNode.getProperty(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECT_PATH).getValue().getString();
						Node subLinkChildNode=this.getJcrSession().getNode(subLinkChildNodePath);
						if(subLinkChildNode!=null){
							if(subContentObjectsKey==null){
								ContentObject cco=ContentComponentFactory.createContentObject();				
								cco.setContentObjectName(currentNode.getName());
								cco.setContentData(subLinkChildNode);
								cco.setContentSession(getJcrSession());				
								coList.add(cco);	
							}else{
								if(subContentObjectsKey.equals(currentNode.getName())){
									ContentObject cco=ContentComponentFactory.createContentObject();				
									cco.setContentObjectName(currentNode.getName());
									cco.setContentData(subLinkChildNode);
									cco.setContentSession(getJcrSession());				
									coList.add(cco);
								}
							}
						}						
					}
				}	
			}
			return coList;
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}

	@Override
	public BaseContentObject getSubLinkContentObject(Object subContentObjectsKey) throws ContentReposityException {
		try {
			boolean hasLinkObjeckContainer=this.getJcrNode().hasNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
			if(hasLinkObjeckContainer){
				Node linkContainerObject=this.getJcrNode().getNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
				if(linkContainerObject.hasNode(subContentObjectsKey.toString())){
					Node subLinkNode=linkContainerObject.getNode(subContentObjectsKey.toString());
					if(subLinkNode.hasProperty(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECT_PATH)){
						String subLinkChildNodePath=subLinkNode.getProperty(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECT_PATH).getValue().getString();
						Node subLinkChildNode=this.getJcrSession().getNode(subLinkChildNodePath);
						if(subLinkChildNode!=null){
							ContentObject cco=ContentComponentFactory.createContentObject();				
							cco.setContentObjectName(subLinkNode.getName());
							cco.setContentData(subLinkChildNode);
							cco.setContentSession(getJcrSession());		
							return cco;
						}
					}
				}
			}
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
		return null;
	}

	@Override
	public boolean removeSubLinkContentObject(Object subContentKey, boolean recordVersion) throws ContentReposityException {
		if(isLocked()){
			ContentReposityDataException cpe=new ContentReposityDataException();
			throw cpe;
		}
		try {
			getJcrSession().getWorkspace().getVersionManager().checkout(getJcrNode().getPath());
			boolean hasLinkObjeckContainer=this.getJcrNode().hasNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
			if(!hasLinkObjeckContainer){
				return false;		
			}
			Node linkContainerObject=this.getJcrNode().getNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
			if(linkContainerObject.hasNode(subContentKey.toString())){				
				Node subNode=linkContainerObject.getNode(subContentKey.toString());				
				subNode.remove();
				getJcrSession().save();	
				recordVersionLabel(recordVersion,"Removed Link sub content object {"+subContentKey.toString()+"}");				
				return true;
			}else{
				return false;
			}
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}
	@Override
	public long getSubLinkContentObjectsCount() throws ContentReposityException {
		try {
			boolean hasLinkObjeckContainer=this.getJcrNode().hasNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
			if(hasLinkObjeckContainer){
				Node linkContainerObject=this.getJcrNode().getNode(JCRContentReposityConstant.CONTENT_OBJECT_SUB_LINKOBJECTS_CONTAINER);
				NodeIterator ni=linkContainerObject.getNodes();
				long subLinkContentObjectNumber=ni.getSize();
				return subLinkContentObjectNumber;
			}else{
				return 0;
			}
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}	
}