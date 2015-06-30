package com.viewfunction.contentRepository.util.helper;

import java.util.List;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;

public interface PropertyQueryHelper {
	
	public static String SCOPE_Descendant="Descendant";
	public static String SCOPE_Child="Child";
	public static String SCOPE_Same="Same";	
	
	public List<BaseContentObject> selectContentObjectsBySQL2(BaseContentObject contentObject,String sql2String) throws ContentReposityException;
	public List<BaseContentObject> selectContentObjectsBySQL2(BaseContentObject contentObject,String selectorName,String queryString,String selectScopeScope) throws ContentReposityException;
	public List<BaseContentObject> selectContentObjectsBySQL2(BaseContentObject contentObject,String selectorName,String queryString,String nodeType,String selectScopeScope) throws ContentReposityException;


}