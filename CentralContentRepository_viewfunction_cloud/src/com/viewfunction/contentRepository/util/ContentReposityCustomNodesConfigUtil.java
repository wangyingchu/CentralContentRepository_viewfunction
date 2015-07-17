package com.viewfunction.contentRepository.util;

import javax.jcr.NamespaceRegistry;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeDefinitionTemplate;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.NodeTypeTemplate;
import javax.jcr.nodetype.PropertyDefinitionTemplate;

public class ContentReposityCustomNodesConfigUtil {
	/* ContentReposityCustomNodes.config content
	 * 
	<nt = 'http://www.jcp.org/jcr/nt/1.0'>
	<jcr = 'http://www.jcp.org/jcr/1.0'>
	<mix = 'http://www.jcp.org/jcr/mix/1.0'>
	<vfcr = 'http://www.viewfunction.com/1.0'>
	[vfcr:binary] > nt:file, mix:versionable ,mix:lockable
	- vfcr:contentTags (string) MULTIPLE
	- vfcr:sequenceNumber (long)
	- vfcr:contentOwnerPermission (string)
	- vfcr:contentOtherPermission (string)
	- vfcr:contentGroupPermission (string) MULTIPLE
	+ vfcr:binaryCommonMetaNode (nt:unstructured)  //extend point for store information in the future
	[vfcr:resource] > nt:resource, mix:versionable ,mix:lockable
	- vfcr:contentName (string) mandatory
	- vfcr:contentDescription (string)
	- vfcr:creator (string)
	- vfcr:lastUpdatePerson (string)
	- vfcr:createDate (Date)
	- vfcr:lastUpdateDate (Date)
	- vfcr:lockToken (string)
	- vfcr:lockOwner (string)
	- vfcr:lockTime (long)
	+ vfcr:contentCommentsNode (nt:unstructured)
	+ vfcr:contentCommonMetaNode (nt:unstructured)  //extend point for store information in the future
	[vfcr:content] > nt:base, mix:versionable
	orderable mixin
	*
	*/
	public static void configCustomNodes(Session session) throws RepositoryException{
		NodeTypeManager nodeTypeManager = session.getWorkspace().getNodeTypeManager();
		NamespaceRegistry namespaceRegistry=session.getWorkspace().getNamespaceRegistry();
		namespaceRegistry.registerNamespace("vfcr", "http://www.viewfunction.com/1.0");
		if(!nodeTypeManager.hasNodeType("vfcr:resource")){
			//Register vfcr:binary start
			NodeTypeTemplate binaryNodeTypeTemplate=nodeTypeManager.createNodeTypeTemplate();
			String[] binaryNodeSuperTypeNames=new String[]{"nt:file", "mix:versionable" ,"mix:lockable"};
			binaryNodeTypeTemplate.setDeclaredSuperTypeNames(binaryNodeSuperTypeNames);
			binaryNodeTypeTemplate.setName("vfcr:binary");
				
			PropertyDefinitionTemplate contentTagsPropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			contentTagsPropertyDefinition.setName("vfcr:contentTags");
			contentTagsPropertyDefinition.setRequiredType(PropertyType.STRING);
			contentTagsPropertyDefinition.setMultiple(true);
			contentTagsPropertyDefinition.setMandatory(false);
			binaryNodeTypeTemplate.getPropertyDefinitionTemplates().add(contentTagsPropertyDefinition);
				
			PropertyDefinitionTemplate sequenceNumberPropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			sequenceNumberPropertyDefinition.setName("vfcr:sequenceNumber");
			sequenceNumberPropertyDefinition.setRequiredType(PropertyType.LONG);
			sequenceNumberPropertyDefinition.setMultiple(false);
			sequenceNumberPropertyDefinition.setMandatory(false);
			binaryNodeTypeTemplate.getPropertyDefinitionTemplates().add(sequenceNumberPropertyDefinition);
				
			PropertyDefinitionTemplate contentOwnerPermissionPropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			contentOwnerPermissionPropertyDefinition.setName("vfcr:contentOwnerPermission");
			contentOwnerPermissionPropertyDefinition.setRequiredType(PropertyType.STRING);
			contentOwnerPermissionPropertyDefinition.setMultiple(false);
			contentOwnerPermissionPropertyDefinition.setMandatory(false);
			binaryNodeTypeTemplate.getPropertyDefinitionTemplates().add(contentOwnerPermissionPropertyDefinition);
				
			PropertyDefinitionTemplate contentOtherPermissionPropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			contentOtherPermissionPropertyDefinition.setName("vfcr:contentOtherPermission");
			contentOtherPermissionPropertyDefinition.setRequiredType(PropertyType.STRING);
			contentOtherPermissionPropertyDefinition.setMultiple(false);
			contentOtherPermissionPropertyDefinition.setMandatory(false);
			binaryNodeTypeTemplate.getPropertyDefinitionTemplates().add(contentOtherPermissionPropertyDefinition);
				
			PropertyDefinitionTemplate contentGroupPermissionPropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			contentGroupPermissionPropertyDefinition.setName("vfcr:contentGroupPermission");
			contentGroupPermissionPropertyDefinition.setRequiredType(PropertyType.STRING);
			contentGroupPermissionPropertyDefinition.setMultiple(true);
			contentGroupPermissionPropertyDefinition.setMandatory(false);
			binaryNodeTypeTemplate.getPropertyDefinitionTemplates().add(contentGroupPermissionPropertyDefinition);
			
			NodeDefinitionTemplate commonBinaryChildNodeTypeDefinition=nodeTypeManager.createNodeDefinitionTemplate();
			commonBinaryChildNodeTypeDefinition.setName("vfcr:binaryCommonMetaNode");
			commonBinaryChildNodeTypeDefinition.setDefaultPrimaryTypeName("nt:unstructured");
			commonBinaryChildNodeTypeDefinition.setRequiredPrimaryTypeNames(new String[]{"nt:unstructured"});
			commonBinaryChildNodeTypeDefinition.setMandatory(false);
			binaryNodeTypeTemplate.getNodeDefinitionTemplates().add(commonBinaryChildNodeTypeDefinition);
				
			nodeTypeManager.registerNodeType(binaryNodeTypeTemplate, true);
			//Register vfcr:binary end
				
			//Register vfcr:resource start
			NodeTypeTemplate resourceNodeTypeTemplate=nodeTypeManager.createNodeTypeTemplate();
			String[] resourceNodeSuperTypeNames=new String[]{"nt:resource", "mix:versionable" ,"mix:lockable"};
			resourceNodeTypeTemplate.setDeclaredSuperTypeNames(resourceNodeSuperTypeNames);
			resourceNodeTypeTemplate.setName("vfcr:resource");
				
			PropertyDefinitionTemplate contentNamePropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			contentNamePropertyDefinition.setName("vfcr:contentName");
			contentNamePropertyDefinition.setRequiredType(PropertyType.STRING);
			contentNamePropertyDefinition.setMultiple(false);
			contentNamePropertyDefinition.setMandatory(true);
			resourceNodeTypeTemplate.getPropertyDefinitionTemplates().add(contentNamePropertyDefinition);
				
			PropertyDefinitionTemplate contentDescriptionPropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			contentDescriptionPropertyDefinition.setName("vfcr:contentDescription");
			contentDescriptionPropertyDefinition.setRequiredType(PropertyType.STRING);
			contentDescriptionPropertyDefinition.setMultiple(false);
			contentDescriptionPropertyDefinition.setMandatory(false);
			resourceNodeTypeTemplate.getPropertyDefinitionTemplates().add(contentDescriptionPropertyDefinition);
				
			PropertyDefinitionTemplate creatorPropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			creatorPropertyDefinition.setName("vfcr:creator");
			creatorPropertyDefinition.setRequiredType(PropertyType.STRING);
			creatorPropertyDefinition.setMultiple(false);
			creatorPropertyDefinition.setMandatory(false);
			resourceNodeTypeTemplate.getPropertyDefinitionTemplates().add(creatorPropertyDefinition);
				
			PropertyDefinitionTemplate lastUpdatePersonPropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			lastUpdatePersonPropertyDefinition.setName("vfcr:lastUpdatePerson");
			lastUpdatePersonPropertyDefinition.setRequiredType(PropertyType.STRING);
			lastUpdatePersonPropertyDefinition.setMultiple(false);
			lastUpdatePersonPropertyDefinition.setMandatory(false);
			resourceNodeTypeTemplate.getPropertyDefinitionTemplates().add(lastUpdatePersonPropertyDefinition);
				
			PropertyDefinitionTemplate createDatePropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			createDatePropertyDefinition.setName("vfcr:createDate");
			createDatePropertyDefinition.setRequiredType(PropertyType.DATE);
			createDatePropertyDefinition.setMultiple(false);
			createDatePropertyDefinition.setMandatory(false);
			resourceNodeTypeTemplate.getPropertyDefinitionTemplates().add(createDatePropertyDefinition);
				
			PropertyDefinitionTemplate lastUpdateDatePropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			lastUpdateDatePropertyDefinition.setName("vfcr:lastUpdateDate");
			lastUpdateDatePropertyDefinition.setRequiredType(PropertyType.DATE);
			lastUpdateDatePropertyDefinition.setMultiple(false);
			lastUpdateDatePropertyDefinition.setMandatory(false);
			resourceNodeTypeTemplate.getPropertyDefinitionTemplates().add(lastUpdateDatePropertyDefinition);
			
			
			PropertyDefinitionTemplate lockTokenPropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			lockTokenPropertyDefinition.setName("vfcr:lockToken");
			lockTokenPropertyDefinition.setRequiredType(PropertyType.STRING);
			lockTokenPropertyDefinition.setMultiple(false);
			lockTokenPropertyDefinition.setMandatory(false);
			resourceNodeTypeTemplate.getPropertyDefinitionTemplates().add(lockTokenPropertyDefinition);
			
			PropertyDefinitionTemplate lockOwnerPropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			lockOwnerPropertyDefinition.setName("vfcr:lockOwner");
			lockOwnerPropertyDefinition.setRequiredType(PropertyType.STRING);
			lockOwnerPropertyDefinition.setMultiple(false);
			lockOwnerPropertyDefinition.setMandatory(false);
			resourceNodeTypeTemplate.getPropertyDefinitionTemplates().add(lockOwnerPropertyDefinition);
			
			PropertyDefinitionTemplate lockTimePropertyDefinition=nodeTypeManager.createPropertyDefinitionTemplate();
			lockTimePropertyDefinition.setName("vfcr:lockTime");
			lockTimePropertyDefinition.setRequiredType(PropertyType.LONG);
			lockTimePropertyDefinition.setMultiple(false);
			lockTimePropertyDefinition.setMandatory(false);
			resourceNodeTypeTemplate.getPropertyDefinitionTemplates().add(lockTimePropertyDefinition);
			
			NodeDefinitionTemplate commonChildNodeTypeDefinition=nodeTypeManager.createNodeDefinitionTemplate();
			commonChildNodeTypeDefinition.setName("vfcr:contentCommonMetaNode");
			commonChildNodeTypeDefinition.setDefaultPrimaryTypeName("nt:unstructured");
			commonChildNodeTypeDefinition.setRequiredPrimaryTypeNames(new String[]{"nt:unstructured"});
			commonChildNodeTypeDefinition.setMandatory(false);
			resourceNodeTypeTemplate.getNodeDefinitionTemplates().add(commonChildNodeTypeDefinition);
			
			NodeDefinitionTemplate commentChildNodeTypeDefinition=nodeTypeManager.createNodeDefinitionTemplate();
			commentChildNodeTypeDefinition.setName("vfcr:contentCommentsNode");
			commentChildNodeTypeDefinition.setDefaultPrimaryTypeName("nt:unstructured");
			commentChildNodeTypeDefinition.setRequiredPrimaryTypeNames(new String[]{"nt:unstructured"});
			commentChildNodeTypeDefinition.setMandatory(false);
			resourceNodeTypeTemplate.getNodeDefinitionTemplates().add(commentChildNodeTypeDefinition);
			
			nodeTypeManager.registerNodeType(resourceNodeTypeTemplate, true);
			//Register vfcr:resource end
				
			//Register vfcr:content start
			NodeTypeTemplate contentNodeTypeTemplate=nodeTypeManager.createNodeTypeTemplate();
			String[] contentNodeSuperTypeNames=new String[]{"nt:base", "mix:versionable"};
			contentNodeTypeTemplate.setDeclaredSuperTypeNames(contentNodeSuperTypeNames);
			contentNodeTypeTemplate.setName("vfcr:content");
			contentNodeTypeTemplate.setMixin(true);
			contentNodeTypeTemplate.setOrderableChildNodes(true);
				
			nodeTypeManager.registerNodeType(contentNodeTypeTemplate, true);
			//Register vfcr:content end
				
			/*
			// Create a template for the property definition ...
			PropertyDefinitionTemplate propDefn = mgr.createPropertyDefinitionTemplate();
			propDefn.setName("ex:property");
			propDefn.setRequiredType(PropertyType.STRING);
			ValueFactory valueFactory = session.getValueFactory();
			Value[] defaultValues = {valueFactory.createValue("default1"),valueFactory.createValue("default2")};
			propDefn.setDefaultValues(defaultValues);
			propDefn.setMandatory(true);
			propDefn.setAutoCreated(true);
			propDefn.setProtected(true);
			propDefn.setMultiple(true);
			propDefn.setOnParentVersion(OnParentVersionAction.VERSION);
			propDefn.setValueConstraints(new String[]{"[.]*\\d","constraint2"});
			String[] queryOps = {QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
				QueryObjectModelConstants.JCR_OPERATOR_NOT_EQUAL_TO,
				QueryObjectModelConstants.JCR_OPERATOR_LESS_THAN,
				QueryObjectModelConstants.JCR_OPERATOR_LESS_THAN_OR_EQUAL_TO,
				QueryObjectModelConstants.JCR_OPERATOR_GREATER_THAN,
				QueryObjectModelConstants.JCR_OPERATOR_GREATER_THAN_OR_EQUAL_TO,
				QueryObjectModelConstants.JCR_OPERATOR_LIKE,
			};
			propDefn.setAvailableQueryOperators(queryOps);
			propDefn.setFullTextSearchable(false);
			propDefn.setQueryOrderable(false);
			type.getPropertyDefinitionTemplates().add(propDefn);
			*/
		}
	}
}