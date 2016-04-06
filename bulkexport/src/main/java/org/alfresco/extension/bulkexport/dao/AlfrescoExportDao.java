package org.alfresco.extension.bulkexport.dao;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;

public interface AlfrescoExportDao {
	
	public boolean isNodeIgnored(String nodeRef);
	
	public Map<QName, Serializable> getProperties(NodeRef nodeRef) throws Exception;
	
	public Map<String,String> getPropertiesAsString(NodeRef nodeRef) throws Exception;
	
	public List<NodeRef> getChildren(NodeRef nodeRef) throws Exception;
	
	public String getPath(NodeRef nodeRef) throws Exception;
	
	public ByteArrayOutputStream getContent(NodeRef nodeRef) throws Exception;
	
	public String getProperty(NodeRef nodeRef,QName propertyQName) throws Exception;
	
	public String getType(NodeRef nodeRef) throws Exception;
	
	public List<QName> getAspects(NodeRef nodeRef) throws Exception;
	
	public List<String> getAspectsAsString(NodeRef nodeRef) throws Exception;
	
	public boolean isFolder(NodeRef nodeRef) throws Exception;
	
	public NodeRef getNodeRef(String NodeRef) throws Exception;
	
}
