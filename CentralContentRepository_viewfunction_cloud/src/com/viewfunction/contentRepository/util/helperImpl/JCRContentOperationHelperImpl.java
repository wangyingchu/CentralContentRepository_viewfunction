package com.viewfunction.contentRepository.util.helperImpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFactory;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.apache.jackrabbit.value.ValueFactoryImpl;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

import com.glaforge.i18n.io.CharsetToolkit;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.LockObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectImpl;
import com.viewfunction.contentRepository.util.exception.ContentReposityDataException;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.exception.ContentReposityRuntimeException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.BinaryContent;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.contentRepository.util.helper.TextContent;

import eu.medsea.mimeutil.MimeUtil;

public class JCRContentOperationHelperImpl implements ContentOperationHelper{

	@Override
	public boolean addBinaryContent(BaseContentObject contentObject,File file, String contentDesc,boolean recordVersion) throws ContentReposityRuntimeException,ContentReposityDataException,ContentReposityException {		
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();		
		if(isBinaryContentExist(contentObject_jcrNode,file)){
			return false;
		}	              
        String mimeTypes=getFileMimesString(file);        
        /*
		String mimeType = new MimetypesFileTypeMap().getContentType(file.getName());
		if(mimeType == null){
			mimeType = "application/octet-stream";
		};
		*/	
		Node fileNode;
		try {					
			contentObject_jcrNode.getSession().getWorkspace().getVersionManager().checkout(contentObject_jcrNode.getPath());		
			fileNode = contentObject_jcrNode.addNode(file.getName(), "vfcr:binary");
			fileNode.addMixin("mix:versionable");
			fileNode.addMixin("mix:referenceable");
			fileNode.addMixin("mix:lockable");
			fileNode.addMixin("mix:created");
			fileNode.addMixin("mix:lastModified");
			Node resNode = fileNode.addNode("jcr:content", "vfcr:resource");			
			resNode.setProperty("vfcr:contentName", file.getName());			
			if(contentDesc!=null){
				resNode.setProperty("vfcr:contentDescription",contentDesc);
			}			
			resNode.setProperty("jcr:mimeType", mimeTypes);
			resNode.setProperty("jcr:encoding", "");			  
			ValueFactory vf=ValueFactoryImpl.getInstance();			  
			resNode.setProperty("jcr:data", vf.createBinary(new FileInputStream(file)));			
			Calendar lastModified = Calendar.getInstance();
			lastModified.setTimeInMillis(file.lastModified());
			resNode.setProperty("jcr:lastModified", lastModified);	
			resNode.setProperty("vfcr:createDate", lastModified);
			binaryContentObject.getJcrNode().getSession().save();			
			recordVersionLabel(fileNode,recordVersion,"Added New binary content {"+file.getName()+"}");
			return true;
		} catch (ItemExistsException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (PathNotFoundException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (NoSuchNodeTypeException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (LockException e) {			
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
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (FileNotFoundException e) {			
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		}		
	}
	
	@Override
	public boolean addBinaryContent(BaseContentObject contentObject, File file,	String contentDesc, String createdBy, boolean recordVersion)throws ContentReposityException {
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();		
		if(isBinaryContentExist(contentObject_jcrNode,file)){
			return false;
		}	
        String mimeTypes=getFileMimesString(file);
        /*
		String mimeType = new MimetypesFileTypeMap().getContentType(file.getName());
		if(mimeType == null){
			mimeType = "application/octet-stream";
		};
		*/	
		Node fileNode;
		try {					
			contentObject_jcrNode.getSession().getWorkspace().getVersionManager().checkout(contentObject_jcrNode.getPath());		
			fileNode = contentObject_jcrNode.addNode(file.getName(), "vfcr:binary");			
			fileNode.addMixin("mix:versionable");
			fileNode.addMixin("mix:referenceable");
			fileNode.addMixin("mix:lockable");
			fileNode.addMixin("mix:created");
			fileNode.addMixin("mix:lastModified");
			Node resNode = fileNode.addNode("jcr:content", "vfcr:resource");			
			resNode.setProperty("vfcr:contentName", file.getName());			
			if(contentDesc!=null){
				resNode.setProperty("vfcr:contentDescription",contentDesc);
			}			
			resNode.setProperty("jcr:mimeType", mimeTypes);
			resNode.setProperty("jcr:encoding", "");			  
			ValueFactory vf=ValueFactoryImpl.getInstance();			  
			resNode.setProperty("jcr:data", vf.createBinary(new FileInputStream(file)));			
			Calendar lastModified = Calendar.getInstance();
			lastModified.setTimeInMillis(file.lastModified());
			resNode.setProperty("jcr:lastModified", lastModified);
			
			resNode.setProperty("vfcr:creator", createdBy);
			resNode.setProperty("vfcr:lastUpdatePerson", createdBy);			
			Calendar createDate = Calendar.getInstance();			
			resNode.setProperty("vfcr:createDate", createDate);
			resNode.setProperty("vfcr:lastUpdateDate", createDate);				
			
			binaryContentObject.getJcrNode().getSession().save();			
			recordVersionLabel(fileNode,recordVersion,"Added New binary content {"+file.getName()+"}");
			return true;
		} catch (ItemExistsException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (PathNotFoundException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (NoSuchNodeTypeException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (LockException e) {			
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
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (FileNotFoundException e) {			
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		}			
	}

	@Override
	public BinaryContent getBinaryContent(BaseContentObject contentObject,String contentName) throws ContentReposityException {
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();		
		try {			
			if(!contentObject_jcrNode.hasNode(contentName)){				
				return null;
			}			
			NodeIterator ni =contentObject_jcrNode.getNodes(contentName);
			while(ni.hasNext()){  
	            Node fileNode = ni.nextNode();	        
	            if(fileNode.isNodeType("vfcr:binary")){
	            	Node resNode = fileNode.getNode("jcr:content");	            	
               		JCRBinaryContentImpl jcrBco= (JCRBinaryContentImpl)ContentComponentFactory.createBinaryContentObject(); 	                		 
               		jcrBco.setContentBinary(resNode.getProperty("jcr:data").getBinary());
               		jcrBco.setContentDescription(resNode.getProperty("vfcr:contentDescription").getString());
               		jcrBco.setContentName(resNode.getProperty("vfcr:contentName").getString());	                		 
               		jcrBco.setLastModified(resNode.getProperty("jcr:lastModified").getDate());
               		jcrBco.setMimeType(resNode.getProperty("jcr:mimeType").getString());  
               		if(resNode.hasProperty("vfcr:createDate")){
               			jcrBco.setCreated(resNode.getProperty("vfcr:createDate").getDate());               			
               		}
               		if(resNode.hasProperty("vfcr:creator")){
               			jcrBco.setCreatedBy(resNode.getProperty("vfcr:creator").getString());               			
               		}               		
               		if(resNode.hasProperty("vfcr:lastUpdatePerson")){
               			jcrBco.setLastModifiedBy(resNode.getProperty("vfcr:lastUpdatePerson").getString()); 
               		}   
               		if(resNode.hasProperty("vfcr:lastUpdateDate")){
               			jcrBco.setLastModified(resNode.getProperty("vfcr:lastUpdateDate").getDate()); 
               		} 
               		jcrBco.setBinaryContainerNode(fileNode);
               		return jcrBco;               
	            }
			}
			return null;
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}			
	}
	
	@Override
	public BinaryContent getBinaryContent(BaseContentObject binaryContentObject) throws ContentReposityException {		
		JCRContentObjectImpl jcrBinaryContentObject=(JCRContentObjectImpl)binaryContentObject;		
		try{		
			Node resNode = jcrBinaryContentObject.getJcrNode();	            	
	   		JCRBinaryContentImpl jcrBco= (JCRBinaryContentImpl)ContentComponentFactory.createBinaryContentObject(); 	                		 
	   		jcrBco.setContentBinary(resNode.getProperty("jcr:data").getBinary());
	   		jcrBco.setContentDescription(resNode.getProperty("vfcr:contentDescription").getString());
	   		jcrBco.setContentName(resNode.getProperty("vfcr:contentName").getString());	                		 
	   		jcrBco.setLastModified(resNode.getProperty("jcr:lastModified").getDate());
	   		jcrBco.setMimeType(resNode.getProperty("jcr:mimeType").getString());	   		
	   		if(resNode.hasProperty("vfcr:createDate")){
	   			jcrBco.setCreated(resNode.getProperty("vfcr:createDate").getDate());               			
	   		}
	   		if(resNode.hasProperty("vfcr:creator")){
	   			jcrBco.setCreatedBy(resNode.getProperty("vfcr:creator").getString());               			
	   		}               		
	   		if(resNode.hasProperty("vfcr:lastUpdatePerson")){
	   			jcrBco.setLastModifiedBy(resNode.getProperty("vfcr:lastUpdatePerson").getString()); 
	   		}   
	   		if(resNode.hasProperty("vfcr:lastUpdateDate")){
	   			jcrBco.setLastModified(resNode.getProperty("vfcr:lastUpdateDate").getDate()); 
	   		}                		
	   		return jcrBco;    			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}			
	}
	
	@Override
	public BinaryContent getBinaryProperty(BaseContentObject contentObject,	String propertyNameName) throws ContentReposityException {
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();		
		try {			
			if(!contentObject_jcrNode.hasProperty(propertyNameName)){				
				return null;
			}			
			Property property=contentObject_jcrNode.getProperty(propertyNameName);
			Binary binaryContent=property.getBinary();
			if(binaryContent==null){
				return null;
			}else{
				JCRBinaryContentImpl jcrBco= (JCRBinaryContentImpl)ContentComponentFactory.createBinaryContentObject(); 	                		 
           		jcrBco.setContentBinary(binaryContent);
           		return jcrBco;
			}			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}			
	}

	@Override
	public List<BinaryContent> getBinaryContents(BaseContentObject contentObject) throws ContentReposityException{		
			JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
			Node contentObject_jcrNode=binaryContentObject.getJcrNode();
			NodeIterator ni;
			try {
				ni = contentObject_jcrNode.getNodes();				
				List<BinaryContent> bcol=new ArrayList<BinaryContent>();
				while(ni.hasNext()){ 
					Node fileNode = ni.nextNode();
					if(fileNode.isNodeType("vfcr:binary")){
						Node resNode = fileNode.getNode("jcr:content");						
						JCRBinaryContentImpl jcrBco= (JCRBinaryContentImpl)ContentComponentFactory.createBinaryContentObject(); 	                		 
						jcrBco.setContentBinary(resNode.getProperty("jcr:data").getBinary());
						jcrBco.setContentDescription(resNode.getProperty("vfcr:contentDescription").getString());
						jcrBco.setContentName(resNode.getProperty("vfcr:contentName").getString());	                		 
						jcrBco.setLastModified(resNode.getProperty("jcr:lastModified").getDate());
						jcrBco.setMimeType(resNode.getProperty("jcr:mimeType").getString());	
	               		if(resNode.hasProperty("vfcr:createDate")){
	               			jcrBco.setCreated(resNode.getProperty("vfcr:createDate").getDate());               			
	               		}
	               		if(resNode.hasProperty("vfcr:creator")){
	               			jcrBco.setCreatedBy(resNode.getProperty("vfcr:creator").getString());               			
	               		}               		
	               		if(resNode.hasProperty("vfcr:lastUpdatePerson")){
	               			jcrBco.setLastModifiedBy(resNode.getProperty("vfcr:lastUpdatePerson").getString()); 
	               		}   
	               		if(resNode.hasProperty("vfcr:lastUpdateDate")){
	               			jcrBco.setLastModified(resNode.getProperty("vfcr:lastUpdateDate").getDate()); 
	               		} 
	               		jcrBco.setBinaryContainerNode(fileNode);
						bcol.add(jcrBco);
					}					
				}				
				return bcol;
			} catch (RepositoryException e) {			
				ContentReposityException cpe=new ContentReposityException();
				cpe.initCause(e);
				throw cpe;
			}					
	}

	@Override
	public boolean addTextContent(BaseContentObject contentObject, File file,String contentDesc,boolean recordVersion) throws ContentReposityRuntimeException,ContentReposityDataException,ContentReposityException{
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();		
		if(isTextContentExist(contentObject_jcrNode,file)){
			return false;
		}
        String mimeTypes=getFileMimesString(file);
		Node fileNode;
		try {						
			contentObject_jcrNode.getSession().getWorkspace().getVersionManager().checkout(contentObject_jcrNode.getPath());		
			fileNode = contentObject_jcrNode.addNode(file.getName(), "vfcr:binary");
			fileNode.addMixin("mix:versionable");
			fileNode.addMixin("mix:referenceable");
			fileNode.addMixin("mix:lockable");
			fileNode.addMixin("mix:created");
			fileNode.addMixin("mix:lastModified");
			Node resNode = fileNode.addNode("jcr:content", "vfcr:resource");
			resNode.setProperty("vfcr:contentName", file.getName());			
			if(contentDesc!=null){
				resNode.setProperty("vfcr:contentDescription",contentDesc);
			}			
			resNode.setProperty("jcr:mimeType", mimeTypes);			
			Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);			
			resNode.setProperty("jcr:encoding",guessedCharset.toString());			
			ValueFactory vf=ValueFactoryImpl.getInstance();			  
			resNode.setProperty("jcr:data", vf.createBinary(new FileInputStream(file)));			
			Calendar lastModified = Calendar.getInstance();
			lastModified.setTimeInMillis(file.lastModified());
			resNode.setProperty("jcr:lastModified", lastModified);	
			resNode.setProperty("vfcr:createDate", lastModified);
			binaryContentObject.getJcrNode().getSession().save();
			recordVersionLabel(fileNode,recordVersion,"Added New text content {"+file.getName()+"}");
			return true;
		} catch (ItemExistsException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (PathNotFoundException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (NoSuchNodeTypeException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (LockException e) {			
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
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (FileNotFoundException e) {			
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		} catch (IOException e) {			
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		}		
	}
	
	@Override
	public boolean addTextContent(BaseContentObject contentObject, File file,String contentDesc, String createdBy, boolean recordVersion)throws ContentReposityException {
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();		
		if(isTextContentExist(contentObject_jcrNode,file)){
			return false;
		}	
        String mimeTypes=getFileMimesString(file);
		Node fileNode;
		try {						
			contentObject_jcrNode.getSession().getWorkspace().getVersionManager().checkout(contentObject_jcrNode.getPath());		
			fileNode = contentObject_jcrNode.addNode(file.getName(), "vfcr:binary");
			fileNode.addMixin("mix:versionable");
			fileNode.addMixin("mix:referenceable");
			fileNode.addMixin("mix:lockable");
			fileNode.addMixin("mix:created");
			fileNode.addMixin("mix:lastModified");
			Node resNode = fileNode.addNode("jcr:content", "vfcr:resource");
			resNode.setProperty("vfcr:contentName", file.getName());			
			if(contentDesc!=null){
				resNode.setProperty("vfcr:contentDescription",contentDesc);
			}			
			resNode.setProperty("jcr:mimeType", mimeTypes);			
			Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);			
			resNode.setProperty("jcr:encoding",guessedCharset.toString());			
			ValueFactory vf=ValueFactoryImpl.getInstance();			  
			resNode.setProperty("jcr:data", vf.createBinary(new FileInputStream(file)));			
			Calendar lastModified = Calendar.getInstance();
			lastModified.setTimeInMillis(file.lastModified());
			resNode.setProperty("jcr:lastModified", lastModified);				
			
			resNode.setProperty("vfcr:creator", createdBy);
			resNode.setProperty("vfcr:lastUpdatePerson", createdBy);			
			Calendar createDate = Calendar.getInstance();			
			resNode.setProperty("vfcr:createDate", createDate);
			resNode.setProperty("vfcr:lastUpdateDate", createDate);			
			
			binaryContentObject.getJcrNode().getSession().save();
			recordVersionLabel(fileNode,recordVersion,"Added New text content {"+file.getName()+"}");
			return true;
		} catch (ItemExistsException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (PathNotFoundException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (NoSuchNodeTypeException e) {			
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		} catch (LockException e) {			
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
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (FileNotFoundException e) {			
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		} catch (IOException e) {			
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		}
	}

	@Override
	public TextContent getTextContent(BaseContentObject contentObject,String contentName) throws ContentReposityException {
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();			
		try {
			if(!contentObject_jcrNode.hasNode(contentName)){				
				return null;
			}	
			NodeIterator ni =contentObject_jcrNode.getNodes(contentName);
			while(ni.hasNext()){  
	            Node fileNode = ni.nextNode();	        
	            if(fileNode.isNodeType("vfcr:binary")){
	            	Node resNode = fileNode.getNode("jcr:content");	
	            	if(!resNode.getProperty("jcr:encoding").getString().equals("")){
	            		JCRTextContentImpl jcrBco= (JCRTextContentImpl)ContentComponentFactory.createTextContentObject(); 	                		 
	            		jcrBco.setContentBinary(resNode.getProperty("jcr:data").getBinary());
	            		jcrBco.setContentDescription(resNode.getProperty("vfcr:contentDescription").getString());
	            		jcrBco.setContentName(resNode.getProperty("vfcr:contentName").getString());	                		 
	            		jcrBco.setLastModified(resNode.getProperty("jcr:lastModified").getDate());
	            		jcrBco.setMimeType(resNode.getProperty("jcr:mimeType").getString()); 
	            		jcrBco.setEncoding(resNode.getProperty("jcr:encoding").getString()); 
	            		if(resNode.hasProperty("vfcr:createDate")){
	               			jcrBco.setCreated(resNode.getProperty("vfcr:createDate").getDate());               			
	               		}
	               		if(resNode.hasProperty("vfcr:creator")){
	               			jcrBco.setCreatedBy(resNode.getProperty("vfcr:creator").getString());               			
	               		}               		
	               		if(resNode.hasProperty("vfcr:lastUpdatePerson")){
	               			jcrBco.setLastModifiedBy(resNode.getProperty("vfcr:lastUpdatePerson").getString()); 
	               		}   
	               		if(resNode.hasProperty("vfcr:lastUpdateDate")){
	               			jcrBco.setLastModified(resNode.getProperty("vfcr:lastUpdateDate").getDate()); 
	               		} 
	            		jcrBco.setBinaryContainerNode(fileNode);
	            		return jcrBco;   
	            	}
	            }
			}
			return null;
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}			
	}

	@Override
	public List<TextContent> getTextContents(BaseContentObject contentObject) throws ContentReposityException {
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();
		NodeIterator ni;
		try {
			ni = contentObject_jcrNode.getNodes();				
			List<TextContent> tcol=new ArrayList<TextContent>();			
			while(ni.hasNext()){				
				Node fileNode = ni.nextNode();
				if(fileNode.isNodeType("vfcr:binary")){
					Node resNode = fileNode.getNode("jcr:content");	
					if(!resNode.getProperty("jcr:encoding").getString().equals("")){
						JCRTextContentImpl jcrBco= (JCRTextContentImpl)ContentComponentFactory.createTextContentObject(); 	                		 
	                	jcrBco.setContentBinary(resNode.getProperty("jcr:data").getBinary());
	                	jcrBco.setContentDescription(resNode.getProperty("vfcr:contentDescription").getString());
	                	jcrBco.setContentName(resNode.getProperty("vfcr:contentName").getString());	                		 
	                	jcrBco.setLastModified(resNode.getProperty("jcr:lastModified").getDate());
	                	jcrBco.setMimeType(resNode.getProperty("jcr:mimeType").getString());
	                	jcrBco.setEncoding(resNode.getProperty("jcr:encoding").getString());   
	                	if(resNode.hasProperty("vfcr:createDate")){
	               			jcrBco.setCreated(resNode.getProperty("vfcr:createDate").getDate());               			
	               		}
	               		if(resNode.hasProperty("vfcr:creator")){
	               			jcrBco.setCreatedBy(resNode.getProperty("vfcr:creator").getString());               			
	               		}               		
	               		if(resNode.hasProperty("vfcr:lastUpdatePerson")){
	               			jcrBco.setLastModifiedBy(resNode.getProperty("vfcr:lastUpdatePerson").getString()); 
	               		}   
	               		if(resNode.hasProperty("vfcr:lastUpdateDate")){
	               			jcrBco.setLastModified(resNode.getProperty("vfcr:lastUpdateDate").getDate()); 
	               		} 
	                	jcrBco.setBinaryContainerNode(fileNode);
	                	tcol.add(jcrBco);
					}
				}				
			}
			return tcol;
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}				
	}
	
	private void recordVersionLabel(Node jcrNode,boolean recordVersion,String labelMessage) throws ContentReposityException{
		if(recordVersion){
			try {
				jcrNode.getSession().getWorkspace().getVersionManager().checkin(jcrNode.getPath());
				String currentVersionName=jcrNode.getSession().getWorkspace().getVersionManager().getBaseVersion(jcrNode.getPath()).getName();	
				jcrNode.getSession().getWorkspace().getVersionManager().getVersionHistory(jcrNode.getPath()).addVersionLabel(currentVersionName, "V"+currentVersionName+" -> "+labelMessage, false);
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
	
	private boolean isBinaryContentExist(Node jcrNode,File contentFile) throws ContentReposityException{
		return isBinaryContentExist(jcrNode,contentFile.getName());
	}
	
	private boolean isBinaryContentExist(Node jcrNode,String contentName) throws ContentReposityException{
		try {
			if(jcrNode.hasNode(contentName)){
				NodeIterator ni=jcrNode.getNodes(contentName);				
				while(ni.hasNext()){
					 Node n = ni.nextNode();  
		                if(n.isNodeType("vfcr:binary")){
		                	return true;
		                }					
				}	
				return false;			
			}else{				
				return false;				
			}
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}
	
	private boolean isTextContentExist(Node jcrNode,File contentFile) throws ContentReposityException{
		return isTextContentExist(jcrNode,contentFile.getName());		
	}
	
	private boolean isTextContentExist(Node jcrNode,String contentName) throws ContentReposityException{
		try {
			if(jcrNode.hasNode(contentName)){
				NodeIterator ni=jcrNode.getNodes(contentName);				
				while(ni.hasNext()){
					 Node n = ni.nextNode();  
		                if(n.isNodeType("vfcr:binary")){		                	
		                	if(!n.getNode("jcr:content").getProperty("jcr:encoding").getString().equals("")){
		                		return true;		                		
		                	}   
		                }					
				}	
				return false;			
			}else{				
				return false;				
			}
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}		
	}	

	@Override
	public boolean removeBinaryContent(BaseContentObject contentObject, String contentName, boolean recordVersion) throws ContentReposityException {
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();
		NodeIterator ni;
		try {
			contentObject_jcrNode.getSession().getWorkspace().getVersionManager().checkout(contentObject_jcrNode.getPath());
			ni = contentObject_jcrNode.getNodes();			
			int i=0;			
			while(ni.hasNext()){ 				
	            Node n = ni.nextNode();  
	            NodeIterator ni1 = n.getNodes();  
	            while(ni1.hasNext()){  
	                Node n1 = ni1.nextNode(); 	                
	                if(n1.getName().equals("jcr:content")&&(n1.getProperty("vfcr:contentName").getString().equals(contentName))){
	                	i++;
	                	contentObject_jcrNode.getNode(contentName).remove();
	                }  
	            } 
	        }
			if(i>0){
				contentObject_jcrNode.getSession().save();
				recordVersionLabel(contentObject_jcrNode,recordVersion,"Removed binary content {"+contentName+"}");
				return true;				
			}else{
				return false;
			}					
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
	}

	@Override
	public boolean removeTextContent(BaseContentObject contentObject, String contentName,boolean recordVersion) throws ContentReposityException {
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();
		NodeIterator ni;
		try {
			contentObject_jcrNode.getSession().getWorkspace().getVersionManager().checkout(contentObject_jcrNode.getPath());
			ni = contentObject_jcrNode.getNodes();			
			int i=0;			
			while(ni.hasNext()){ 				
	            Node n = ni.nextNode();  
	            NodeIterator ni1 = n.getNodes();  
	            while(ni1.hasNext()){  
	                Node n1 = ni1.nextNode(); 	                
	                if(n1.getName().equals("jcr:content")&&(n1.getProperty("vfcr:contentName").getString().equals(contentName))&&!n1.getProperty("jcr:encoding").getString().equals("")){
	                	i++;
	                	contentObject_jcrNode.getNode(contentName).remove();
	                }  
	            } 
	        }
			if(i>0){
				contentObject_jcrNode.getSession().save();
				recordVersionLabel(contentObject_jcrNode,recordVersion,"Removed text content {"+contentName+"}");
				return true;				
			}else{
				return false;
			}					
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}
	}

	@Override
	public boolean updateBinaryContent(BaseContentObject contentObject,String binaryContentName, File newFile, String contentDesc,boolean recordVersion) throws ContentReposityException {
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();
		NodeIterator ni;
		try {		
			if(!isBinaryContentExist(contentObject_jcrNode,binaryContentName)){
				return false;
			}
			// still need handle same name of binary Content object and common folder object			
			ni = contentObject_jcrNode.getNodes(binaryContentName);	
			while(ni.hasNext()){ 
				Node fileNode = ni.nextNode();
				if(fileNode.isNodeType("vfcr:binary")){
					contentObject_jcrNode.getSession().getWorkspace().getVersionManager().checkout(fileNode.getPath());			
					Node resNode = fileNode.getNode("jcr:content");			
					if(contentDesc!=null){
						resNode.setProperty("vfcr:contentDescription",contentDesc);
					}			
					ValueFactory vf=ValueFactoryImpl.getInstance();			  
					resNode.setProperty("jcr:data", vf.createBinary(new FileInputStream(newFile)));			
					Calendar lastModified = Calendar.getInstance();
					lastModified.setTimeInMillis(newFile.lastModified());
					resNode.setProperty("jcr:lastModified", lastModified);			
					fileNode.getSession().save();
					recordVersionLabel(fileNode,recordVersion,"Updated binary content to new one stored in file {"+newFile.getName()+"}");			
					return true;
				}
			}			
			return false;			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (FileNotFoundException e) {
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		}
	}
	
	@Override
	public boolean updateBinaryContent(BaseContentObject contentObject,String binaryContentName, File newFile, String contentDesc,String lastModifiedBy, boolean recordVersion)throws ContentReposityException {
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();
		NodeIterator ni;
		try {		
			if(!isBinaryContentExist(contentObject_jcrNode,binaryContentName)){
				return false;
			}
			// still need handle same name of binary Content object and common folder object			
			ni = contentObject_jcrNode.getNodes(binaryContentName);	
			while(ni.hasNext()){ 
				Node fileNode = ni.nextNode();
				if(fileNode.isNodeType("vfcr:binary")){
					contentObject_jcrNode.getSession().getWorkspace().getVersionManager().checkout(fileNode.getPath());			
					Node resNode = fileNode.getNode("jcr:content");			
					if(contentDesc!=null){
						resNode.setProperty("vfcr:contentDescription",contentDesc);
					}			
					ValueFactory vf=ValueFactoryImpl.getInstance();			  
					resNode.setProperty("jcr:data", vf.createBinary(new FileInputStream(newFile)));			
					Calendar lastModified = Calendar.getInstance();
					lastModified.setTimeInMillis(newFile.lastModified());
					resNode.setProperty("jcr:lastModified", lastModified);						
					
					resNode.setProperty("vfcr:lastUpdatePerson", lastModifiedBy);			
					Calendar updateDate = Calendar.getInstance();	
					resNode.setProperty("vfcr:lastUpdateDate", updateDate);
					
					fileNode.getSession().save();
					recordVersionLabel(fileNode,recordVersion,"Updated binary content to new one stored in file {"+newFile.getName()+"}");			
					return true;
				}
			}			
			return false;			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (FileNotFoundException e) {
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		}
	}

	@Override
	public boolean updateTextContent(BaseContentObject contentObject,String textContentName, File newFile, String contentDesc,boolean recordVersion) throws ContentReposityException {
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();
		NodeIterator ni;
		try {			
			if(!isTextContentExist(contentObject_jcrNode,textContentName)){
				return false;
			}			
			// still need handle same name of text Content object and common folder object			
			ni = contentObject_jcrNode.getNodes(textContentName);	
			while(ni.hasNext()){ 
				Node fileNode = ni.nextNode();
				if(fileNode.isNodeType("vfcr:binary")){							
					Node resNode = fileNode.getNode("jcr:content");
					if(!resNode.getProperty("jcr:encoding").getString().equals("")){	
						contentObject_jcrNode.getSession().getWorkspace().getVersionManager().checkout(fileNode.getPath());	
						if(contentDesc!=null){
							resNode.setProperty("vfcr:contentDescription",contentDesc);
						}			
						ValueFactory vf=ValueFactoryImpl.getInstance();			  
						resNode.setProperty("jcr:data", vf.createBinary(new FileInputStream(newFile)));			
						Calendar lastModified = Calendar.getInstance();
						lastModified.setTimeInMillis(newFile.lastModified());
						resNode.setProperty("jcr:lastModified", lastModified);	
						Charset guessedCharset = CharsetToolkit.guessEncoding(newFile, 4096);			
						resNode.setProperty("jcr:encoding",guessedCharset.toString());
						fileNode.getSession().save();
						recordVersionLabel(fileNode,recordVersion,"Updated text content to new one stored in file {"+newFile.getName()+"}");
						return true;
					}					
				}
			}			
			return false;			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (FileNotFoundException e) {
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		} catch (IOException e) {
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		}
	}
	
	@Override
	public boolean updateTextContent(BaseContentObject contentObject,String textContentName, File newFile, String contentDesc,String lastModifiedBy, boolean recordVersion) throws ContentReposityException {
		JCRContentObjectImpl binaryContentObject=(JCRContentObjectImpl)contentObject;
		Node contentObject_jcrNode=binaryContentObject.getJcrNode();
		NodeIterator ni;
		try {			
			if(!isTextContentExist(contentObject_jcrNode,textContentName)){
				return false;
			}			
			// still need handle same name of text Content object and common folder object			
			ni = contentObject_jcrNode.getNodes(textContentName);	
			while(ni.hasNext()){ 
				Node fileNode = ni.nextNode();
				if(fileNode.isNodeType("vfcr:binary")){							
					Node resNode = fileNode.getNode("jcr:content");
					if(!resNode.getProperty("jcr:encoding").getString().equals("")){	
						contentObject_jcrNode.getSession().getWorkspace().getVersionManager().checkout(fileNode.getPath());	
						if(contentDesc!=null){
							resNode.setProperty("vfcr:contentDescription",contentDesc);
						}			
						ValueFactory vf=ValueFactoryImpl.getInstance();			  
						resNode.setProperty("jcr:data", vf.createBinary(new FileInputStream(newFile)));			
						Calendar lastModified = Calendar.getInstance();
						lastModified.setTimeInMillis(newFile.lastModified());
						resNode.setProperty("jcr:lastModified", lastModified);	
						
						resNode.setProperty("vfcr:lastUpdatePerson", lastModifiedBy);			
						Calendar updateDate = Calendar.getInstance();	
						resNode.setProperty("vfcr:lastUpdateDate", updateDate);
						
						Charset guessedCharset = CharsetToolkit.guessEncoding(newFile, 4096);			
						resNode.setProperty("jcr:encoding",guessedCharset.toString());
						fileNode.getSession().save();
						recordVersionLabel(fileNode,recordVersion,"Updated text content to new one stored in file {"+newFile.getName()+"}");
						return true;
					}					
				}
			}			
			return false;			
		} catch (RepositoryException e) {			
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		} catch (FileNotFoundException e) {
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		} catch (IOException e) {
			ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
			cpe.initCause(e);
			throw cpe;
		}
	}	

	@Override
	public String getContentObjectType(BaseContentObject contentObject) throws ContentReposityException {
		JCRContentObjectImpl jcrCOI=(JCRContentObjectImpl)contentObject;
		Node node=jcrCOI.getJcrNode();
		try {
			if(node.isNodeType("vfcr:binary")){
				Node resNode = node.getNode("jcr:content");
				if(!resNode.getProperty("jcr:encoding").getString().equals("")){
					return CONTENTTYPE_TEXTBINARY;
				}else{
					return CONTENTTYPE_BINARTCONTENT;
				}
			}else{
				if(contentObject.getSubContentObjectsCount()>0){
					return CONTENTTYPE_FOLDEROBJECT;
				}else{
					return CONTENTTYPE_STANDALONEOBJECT;
				}				
			}
		} catch (RepositoryException e) {
			ContentReposityException cpe=new ContentReposityException();
			cpe.initCause(e);
			throw cpe;
		}	
	}

	@Override
	public LockObject lockBinaryContent(BaseContentObject contentObject,String contentName, boolean isTemporaryLock)throws ContentReposityException {
		BinaryContent targetBinaryContent=getBinaryContent(contentObject,contentName);
		return targetBinaryContent.lock(isTemporaryLock);
	}

	@Override
	public boolean unlockBinaryContent(BaseContentObject contentObject,String contentName) throws ContentReposityException {
		BinaryContent targetBinaryContent=getBinaryContent(contentObject,contentName);
		return targetBinaryContent.unlock();
	}

	@Override
	public boolean isBinaryContentLocked(BaseContentObject contentObject,String contentName) throws ContentReposityException {
		BinaryContent targetBinaryContent=getBinaryContent(contentObject,contentName);
		return targetBinaryContent.isLocked();
	}	
	
	public static String getFileMimesString(File fileNeedCheck){
		List<String> mimeList=new ArrayList<String>();
		//use MimeUtil check mime type
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector"); 
        String _OSType=System.getProperty("os.name");       
        if(_OSType.toUpperCase().indexOf("WINDOWS")!=-1){
        	//this is Windows platform        	
        	MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.WindowsRegistryMimeDetector");
        }
        if(_OSType.toUpperCase().indexOf("LINUX")!=-1){
        	//this is Linux platform        	
        	MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.OpendesktopMimeDetector");
        }
        if(_OSType.toUpperCase().indexOf("MAC")!=-1){
        	//this is mac platform        
        	MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.OpendesktopMimeDetector");
        }     
		Collection<?> mimeTypes = MimeUtil.getMimeTypes(fileNeedCheck);			
		Iterator<?> mimeTypeIterator=mimeTypes.iterator();		
		while(mimeTypeIterator.hasNext()){			
			Object currentMimeType=mimeTypeIterator.next();	
			mimeList.add(currentMimeType.toString());
		}				
		//use tika check mime type
		try {
			TikaConfig tika = new TikaConfig();
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileNeedCheck));
	        Metadata metadata = new Metadata();
	        metadata.set(Metadata.RESOURCE_NAME_KEY, fileNeedCheck.getName());
	        MediaType mimetype = tika.getDetector().detect(bis, metadata);
	        String mineType=mimetype.toString();        
	        if(!mimeList.contains(mineType)){
	        	mimeList.add(mineType);
	        }    	        
		} catch (TikaException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}				
		String mimeTypeStr=mimeList.toString();		
		mimeTypeStr=mimeTypeStr.replace("[", "");
		mimeTypeStr=mimeTypeStr.replace("]", "");		
		return mimeTypeStr;
	}
}