package com.viewfunction.contentRepository.util.helperImpl;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectImpl;
import com.viewfunction.contentRepository.util.PerportyHandler;
import com.viewfunction.contentRepository.util.exception.ContentReposityDataException;
import com.viewfunction.contentRepository.util.exception.ContentReposityENVException;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.exception.ContentReposityRuntimeException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.PropertyQueryHelper;

public class JCRPropertyQueryHelperImpl implements PropertyQueryHelper{
	
	private static String ENABLE_GLOBAL_FULLTEXT_SEARCH;

	@Override
	public List<BaseContentObject> selectContentObjectsBySQL2(BaseContentObject contentObject,String sql2String) throws ContentReposityException {
		JCRContentObjectImpl jmpl=(JCRContentObjectImpl)contentObject;
		try {
			QueryManager qm = jmpl.getJcrSession().getWorkspace().getQueryManager();
			Query q = qm.createQuery(sql2String, Query.JCR_SQL2);
			QueryResult result = q.execute();			
			NodeIterator nodeIterator=result.getNodes();
			List<BaseContentObject> bcl=new ArrayList<BaseContentObject>();			
			while(nodeIterator.hasNext()){				
				Node n=nodeIterator.nextNode();					
				ContentObject cco=ContentComponentFactory.createContentObject();				
				cco.setContentObjectName(n.getName());
				cco.setContentData(n);
				cco.setContentSession(jmpl.getJcrSession());	
				bcl.add(cco);										
			}
			return bcl;			
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}			
	}

	@Override
	public List<BaseContentObject> selectContentObjectsBySQL2(BaseContentObject contentObject,String selectorName, String queryString, String selectScopeScope)throws ContentReposityException {
		JCRContentObjectImpl jmpl=(JCRContentObjectImpl)contentObject;
		boolean hasScope=false;
		try {
			QueryManager qm = jmpl.getJcrSession().getWorkspace().getQueryManager();			
			StringBuffer selectStringBf=new StringBuffer();			
			selectStringBf.append("select * from [vfcr:content]");			
			if(selectorName!=null){
				selectStringBf.append(" as "+ selectorName+" ");				
			}
			if(selectScopeScope!=null&&selectorName!=null){				
				String currentNodePath=jmpl.getJcrNode().getPath();				
				if(selectScopeScope.equals(PropertyQueryHelper.SCOPE_Same)){
					hasScope=true;
					selectStringBf.append("WHERE ISSAMENODE("+selectorName+",'"+currentNodePath+"') ");					
				}
				if(selectScopeScope.equals(PropertyQueryHelper.SCOPE_Child)){
					hasScope=true;
					selectStringBf.append("WHERE ISCHILDNODE("+selectorName+",'"+currentNodePath+"') ");					
				}
				if(selectScopeScope.equals(PropertyQueryHelper.SCOPE_Descendant)){
					hasScope=true;
					selectStringBf.append("WHERE ISDESCENDANTNODE("+selectorName+",'"+currentNodePath+"') ");					
				}				
			}
			if(queryString!=null){
				if(!hasScope){
					selectStringBf.append("WHERE "+queryString);
				}else{
					selectStringBf.append("AND "+queryString);
				}				
			}			
			
			Query q = qm.createQuery(selectStringBf.toString(), Query.JCR_SQL2);
			QueryResult result = q.execute();			
			NodeIterator nodeIterator=result.getNodes();
			List<BaseContentObject> bcl=new ArrayList<BaseContentObject>();			
			while(nodeIterator.hasNext()){				
				Node n=nodeIterator.nextNode();					
				ContentObject cco=ContentComponentFactory.createContentObject();				
				cco.setContentObjectName(n.getName());
				cco.setContentData(n);
				cco.setContentSession(jmpl.getJcrSession());	
				bcl.add(cco);										
			}
			return bcl;			
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}			
	}

	@Override
	public List<BaseContentObject> selectContentObjectsBySQL2(BaseContentObject contentObject, String selectorName,String queryString, String nodeType, String selectScopeScope)throws ContentReposityException {
		JCRContentObjectImpl jmpl=(JCRContentObjectImpl)contentObject;
		boolean hasScope=false;
		try {
			QueryManager qm = jmpl.getJcrSession().getWorkspace().getQueryManager();			
			StringBuffer selectStringBf=new StringBuffer();			
			selectStringBf.append("select * from [");
			selectStringBf.append(nodeType);
			selectStringBf.append("]");			
			if(selectorName!=null){
				selectStringBf.append(" as "+ selectorName+" ");				
			}
			if(selectScopeScope!=null&&selectorName!=null){				
				String currentNodePath=jmpl.getJcrNode().getPath();				
				if(selectScopeScope.equals(PropertyQueryHelper.SCOPE_Same)){
					hasScope=true;
					selectStringBf.append("WHERE ISSAMENODE("+selectorName+",'"+currentNodePath+"') ");					
				}
				if(selectScopeScope.equals(PropertyQueryHelper.SCOPE_Child)){
					hasScope=true;
					selectStringBf.append("WHERE ISCHILDNODE("+selectorName+",'"+currentNodePath+"') ");					
				}
				if(selectScopeScope.equals(PropertyQueryHelper.SCOPE_Descendant)){
					hasScope=true;
					selectStringBf.append("WHERE ISDESCENDANTNODE("+selectorName+",'"+currentNodePath+"') ");					
				}				
			}
			if(queryString!=null){
				if(!hasScope){
					selectStringBf.append("WHERE "+queryString);
				}else{
					selectStringBf.append("AND "+queryString);
				}				
			}			
			
			Query q = qm.createQuery(selectStringBf.toString(), Query.JCR_SQL2);
			QueryResult result = q.execute();			
			NodeIterator nodeIterator=result.getNodes();
			List<BaseContentObject> bcl=new ArrayList<BaseContentObject>();			
			while(nodeIterator.hasNext()){				
				Node n=nodeIterator.nextNode();					
				ContentObject cco=ContentComponentFactory.createContentObject();				
				cco.setContentObjectName(n.getName());
				cco.setContentData(n);
				cco.setContentSession(jmpl.getJcrSession());	
				bcl.add(cco);										
			}
			return bcl;			
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}			
	}

	@Override
	public List<BaseContentObject> selectContentObjectsByPropertyFullTextSearch(BaseContentObject selectStartContentObject,String propertyName, 
			String fullTextSearchContent,String selectScopeScope) throws ContentReposityException {
		if(ENABLE_GLOBAL_FULLTEXT_SEARCH==null){
        	ENABLE_GLOBAL_FULLTEXT_SEARCH=PerportyHandler.getPerportyValue(PerportyHandler.ENABLE_GLOBAL_FULLTEXT_SEARCH);
        }
        boolean enableGlobalFullTextSearch=Boolean.parseBoolean(ENABLE_GLOBAL_FULLTEXT_SEARCH);
        if(enableGlobalFullTextSearch){
        	if(propertyName==null||fullTextSearchContent==null){
        		ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
        		throw cpe;
        	}        	
        	try{
	        	JCRContentObjectImpl jmpl=(JCRContentObjectImpl)selectStartContentObject;
	        	boolean hasScope=false;        	
	        	StringBuffer selectStringBf=new StringBuffer();			
				selectStringBf.append("select * from [vfcr:content] as startPoint ");				
				if(selectScopeScope!=null){				
					String currentNodePath = jmpl.getJcrNode().getPath();
							
					if(selectScopeScope.equals(PropertyQueryHelper.SCOPE_Same)){
						hasScope=true;
						selectStringBf.append("WHERE ISSAMENODE(startPoint,["+currentNodePath+"]) ");					
					}
					if(selectScopeScope.equals(PropertyQueryHelper.SCOPE_Child)){
						hasScope=true;
						selectStringBf.append("WHERE ISCHILDNODE(startPoint,["+currentNodePath+"]) ");					
					}
					if(selectScopeScope.equals(PropertyQueryHelper.SCOPE_Descendant)){
						hasScope=true;
						selectStringBf.append("WHERE ISDESCENDANTNODE(startPoint,["+currentNodePath+"]) ");					
					}				
				}				
				String fullTextSearchLogic="CONTAINS(["+propertyName+"], '"+fullTextSearchContent.trim()+"')";	
				if(!hasScope){
					selectStringBf.append("WHERE "+fullTextSearchLogic);
				}else{
					selectStringBf.append("AND "+fullTextSearchLogic);
				}
				
				QueryManager qm = jmpl.getJcrSession().getWorkspace().getQueryManager();
				Query q = qm.createQuery(selectStringBf.toString(), Query.JCR_SQL2);
				
				QueryResult result = q.execute();			
				NodeIterator nodeIterator=result.getNodes();
				List<BaseContentObject> bcl=new ArrayList<BaseContentObject>();			
				while(nodeIterator.hasNext()){				
					Node n=nodeIterator.nextNode();					
					ContentObject cco=ContentComponentFactory.createContentObject();				
					cco.setContentObjectName(n.getName());
					cco.setContentData(n);
					cco.setContentSession(jmpl.getJcrSession());	
					bcl.add(cco);										
				}
				return bcl;				
        	}catch(RepositoryException e){
        		e.printStackTrace();
        		ContentReposityRuntimeException cpe=new ContentReposityRuntimeException();
				cpe.initCause(e);
        		throw cpe;
        	}     
        }else{        	
        	ContentReposityENVException cpe=new ContentReposityENVException();			
			throw cpe;        	
        }		
	}
}