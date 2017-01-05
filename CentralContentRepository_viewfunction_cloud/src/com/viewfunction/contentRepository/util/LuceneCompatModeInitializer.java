package com.viewfunction.contentRepository.util;

import java.util.Set;

import org.apache.jackrabbit.oak.plugins.index.lucene.util.LuceneInitializerHelper;
import org.apache.jackrabbit.oak.plugins.index.lucene.LuceneIndexConstants;
import org.apache.jackrabbit.oak.spi.state.NodeBuilder;

import static org.apache.jackrabbit.JcrConstants.JCR_PRIMARYTYPE;
import static org.apache.jackrabbit.oak.api.Type.NAME;
import static org.apache.jackrabbit.oak.plugins.index.IndexConstants.*;
import static org.apache.jackrabbit.oak.plugins.index.lucene.LuceneIndexConstants.TYPE_LUCENE;

public class LuceneCompatModeInitializer extends LuceneInitializerHelper{
	 private final String name;

     public LuceneCompatModeInitializer(String name, Set<String> propertyTypes) {
         super(name, propertyTypes);
         this.name = name;
     }

     @Override
     public void initialize(NodeBuilder builder) {
         if (builder.hasChildNode(INDEX_DEFINITIONS_NAME)
                 && builder.getChildNode(INDEX_DEFINITIONS_NAME).hasChildNode(name)) {
             // do nothing
         } else {
             NodeBuilder index = builder.child(INDEX_DEFINITIONS_NAME).child(name);
             index.setProperty(JCR_PRIMARYTYPE, INDEX_DEFINITIONS_NODE_TYPE, NAME)
                     .setProperty(TYPE_PROPERTY_NAME, TYPE_LUCENE)
                     .setProperty(REINDEX_PROPERTY_NAME, true);
                     //.setProperty(LuceneIndexConstants.TEST_MODE, true)
                     //.setProperty(LuceneIndexConstants.EVALUATE_PATH_RESTRICTION, true);             
             index.child(LuceneIndexConstants.SUGGESTION_CONFIG)
                     .setProperty(JCR_PRIMARYTYPE, "nt:unstructured", NAME)
                     .setProperty(LuceneIndexConstants.SUGGEST_UPDATE_FREQUENCY_MINUTES, 10);             
             NodeBuilder rules = index.child(LuceneIndexConstants.INDEX_RULES);
             rules.setProperty(JCR_PRIMARYTYPE, "nt:unstructured", NAME);
             NodeBuilder ntBase = rules.child("nt:base");
             ntBase.setProperty(JCR_PRIMARYTYPE, "nt:unstructured", NAME);

             //Enable nodeName index support
             ntBase.setProperty(LuceneIndexConstants.INDEX_NODE_NAME, true);
             
             NodeBuilder props = ntBase.child(LuceneIndexConstants.PROP_NODE);
             props.setProperty(JCR_PRIMARYTYPE, "nt:unstructured", NAME);
             enableFulltextIndex(props.child("allProps"));
         }
     }

     private void enableFulltextIndex(NodeBuilder propNode){
         propNode.setProperty(JCR_PRIMARYTYPE, "nt:unstructured", NAME)
                 .setProperty(LuceneIndexConstants.PROP_ANALYZED, true)
                 .setProperty(LuceneIndexConstants.PROP_NODE_SCOPE_INDEX, true)
                 .setProperty(LuceneIndexConstants.PROP_USE_IN_EXCERPT, true)
                 .setProperty(LuceneIndexConstants.PROP_PROPERTY_INDEX, true)
                 .setProperty(LuceneIndexConstants.PROP_USE_IN_SPELLCHECK, true)
                 .setProperty(LuceneIndexConstants.PROP_USE_IN_SUGGEST, true)
                 .setProperty(LuceneIndexConstants.PROP_NAME, LuceneIndexConstants.REGEX_ALL_PROPS)
                 .setProperty(LuceneIndexConstants.PROP_IS_REGEX, true);
     }
}
