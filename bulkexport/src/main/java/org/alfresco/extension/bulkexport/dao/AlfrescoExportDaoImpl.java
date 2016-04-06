package org.alfresco.extension.bulkexport.dao;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;

public class AlfrescoExportDaoImpl implements AlfrescoExportDao {

	@Override
	public boolean isNodeIgnored(String nodeRef) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<QName, Serializable> getProperties(NodeRef nodeRef)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getPropertiesAsString(NodeRef nodeRef)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NodeRef> getChildren(NodeRef nodeRef) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath(NodeRef nodeRef) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteArrayOutputStream getContent(NodeRef nodeRef) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProperty(NodeRef nodeRef, QName propertyQName)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(NodeRef nodeRef) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QName> getAspects(NodeRef nodeRef) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAspectsAsString(NodeRef nodeRef) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFolder(NodeRef nodeRef) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public NodeRef getNodeRef(String NodeRef) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
