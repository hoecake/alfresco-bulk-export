package org.alfresco.extension.bulkexport.dao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.Path;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.service.namespace.NamespacePrefixResolver;
import org.alfresco.service.namespace.QName;

public class AlfrescoExportDaoImpl implements AlfrescoExportDao {
	private ServiceRegistry registry;
	
	private QName ignoreAspectQname[] = {
			ContentModel.ASPECT_TAGGABLE
	};
	
	private String ignoreAspectPrefix[] = {
			"app"
	};

	private QName ignorePropertyQname[] = { 
			ContentModel.PROP_NODE_DBID, 
			ContentModel.PROP_NODE_UUID, 
			ContentModel.PROP_CATEGORIES,
			ContentModel.PROP_CONTENT,
			ContentModel.ASPECT_TAGGABLE
	};
	
	private String[] ignorePropertyPrefix = {
			"app",
			"exif"
	};
	
	private QName[] ignoredType = {
			ContentModel.TYPE_SYSTEM_FOLDER,
			ContentModel.TYPE_LINK,
			QName.createQName("{http://www.alfresco.org/model/action/1.0}action")
	};
	
	public AlfrescoExportDaoImpl(ServiceRegistry registry) {
		this.registry = registry;
	}
	
	@Override
	public boolean isNodeIgnored(String nodeRef) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<QName, Serializable> getProperties(NodeRef nodeRef) throws Exception {
		NodeService nodeService = this.registry.getNodeService();
		
		Map<QName, Serializable> properties = nodeService.getProperties(nodeRef);
		
		return properties;
	}

	@Override
	public Map<String, String> getPropertiesAsString(NodeRef nodeRef) throws Exception {
		Map<QName, Serializable> properties = this.getProperties(nodeRef);
		
		Map<String, String> props = new HashMap<String, String>();
		Set<QName> qNameSet = properties.keySet();
		
		for (QName qName : qNameSet) {
			//case the qname is in ignored type do nothing will do.
			if(this.isPropertyIgnored(qName)){
				continue;
			}
					
			Serializable obj = properties.get(qName);
			String name = this.getQnameStringFormat(qName);
			String value = this.formatMetadata(obj);
		
			//put key value in the property list as <prefixOfProperty:nameOfProperty, valueOfProperty>
			props.put(name, value);
		}
		
		return props;
	}

	@Override
	public List<NodeRef> getChildren(NodeRef nodeRef) throws Exception {
		NodeService nodeService = this.registry.getNodeService();
		List<NodeRef> listChildren = new ArrayList<NodeRef>();
		
		List<ChildAssociationRef> children = nodeService.getChildAssocs(nodeRef);
		
		for (ChildAssociationRef childAssociationRef : children) {
			NodeRef child = childAssociationRef.getChildRef();
				
			if(this.isTypeIgnored(nodeService.getType(child))){
				continue;
			}
			
			listChildren.add(child);
		}
		
		return listChildren;
	}

	public List<NodeRef> getFolderChildren(NodeRef nodeRef) throws Exception {
		FileFolderService service = this.registry.getFileFolderService();
		
		List<FileInfo> folders = service.listFolders(nodeRef);
		
		List<NodeRef> listChildren = new ArrayList<NodeRef>();
		
		for (FileInfo fileInfo : folders) {
			listChildren.add(fileInfo.getNodeRef());
		}
		
		return listChildren;
	}

	public List<NodeRef> getFileChildren(NodeRef nodeRef) throws Exception {
		FileFolderService service = this.registry.getFileFolderService();
		
		List<FileInfo> files = service.listFiles(nodeRef);
		
		List<NodeRef> listChildren = new ArrayList<NodeRef>();
		
		for (FileInfo fileInfo : files) {
			listChildren.add(fileInfo.getNodeRef());
		}
		
		return listChildren;
	}
	
	@Override
	public String getPath(NodeRef nodeRef) throws Exception {
		NodeService nodeService = this.registry.getNodeService();
		PermissionService permissionService = this.registry.getPermissionService();
		
		//get element Path
		Path path = nodeService.getPath(nodeRef);
		
		//get element name 
		Serializable name = nodeService.getProperty(nodeRef, ContentModel.PROP_NAME);
		
		//get element Path as String
		String basePath = path.toDisplayPath(nodeService, permissionService);
		
		return (basePath + "/" + name);
	}

	@Override
	public ByteArrayOutputStream getContent(NodeRef nodeRef) throws Exception {
		ContentService contentService = this.registry.getContentService();
		
		ContentReader reader = contentService.getReader(nodeRef, ContentModel.PROP_CONTENT);
		
		InputStream in = reader.getContentInputStream();
		int size = in.available();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[ (size + 100) ];
		int sizeOut;
		
		while ((sizeOut=in.read(buf)) != -1 ) {
			out.write(buf, 0, sizeOut);
		}
		
		out.flush();
		out.close();
		
		in.close();
		
		
		return out;
	}

	@Override
	public String getProperty(NodeRef nodeRef, QName propertyQName) throws Exception {
		NodeService nodeService = this.registry.getNodeService();
		
		Serializable value = nodeService.getProperty(nodeRef, propertyQName);
		
		return this.formatMetadata(value);
	}

	@Override
	public String getType(NodeRef nodeRef) throws Exception {
		NodeService nodeService = this.registry.getNodeService();
		
		QName value = nodeService.getType(nodeRef);
		
		String name = this.getQnameStringFormat(value);
		
		return name;
	}

	@Override
	public List<QName> getAspects(NodeRef nodeRef) throws Exception {
		NodeService nodeService = this.registry.getNodeService();
		
		Set<QName> aspectSet = nodeService.getAspects(nodeRef);
		List<QName> qn = new ArrayList<QName>(aspectSet);
		
		return qn;
	}

	@Override
	public List<String> getAspectsAsString(NodeRef nodeRef) throws Exception {
		List<QName> qn = this.getAspects(nodeRef);
		List<String> str = new ArrayList<String>();
		
		for (QName qName : qn) {			
			if(this.isAspectIgnored(qName)) {
				continue;
			}
			
			String name = this.getQnameStringFormat(qName);
			str.add(name); 
		}
		
		return str;
	}

	@Override
	public boolean isFolder(NodeRef nodeRef) throws Exception {
		FileFolderService service = this.registry.getFileFolderService();

		FileInfo info = service.getFileInfo(nodeRef);
		
		return info.isFolder();
	}

	@Override
	public NodeRef getNodeRef(String NodeRef) throws Exception {
		try{
			NodeRef nr = new NodeRef(NodeRef);
			return nr;
		} catch (Exception e) {
			return null;
		}
	}

	private boolean isPropertyIgnored(QName qName) {
		//verify if qname is in ignored
		for (QName qn : this.ignorePropertyQname) {
			if(qn.equals(qName)){
				return true;
			}
		}
		
		//verify if qname prefix is in ignored
		//String prefix = qName.getPrefixString();
		NamespacePrefixResolver nsR = this.registry.getNamespaceService();
		String prefix = qName.getPrefixedQName(nsR).getPrefixString();
		for (String str : this.ignorePropertyPrefix) {
			
			//str.equalsIgnoreCase(prefix)
			
			if(prefix.startsWith(str)){
				return true;
			}
		}
		
		return false;
	}

	
	/**
	 * Verify if the aspect qname is ignored 
	 * 
	 * @param qName
	 * @return {@link Boolean}
	 */
	private boolean isAspectIgnored(QName qName) {
		//verify if qname is in ignored
		for (QName qn : this.ignoreAspectQname) {
			if(qn.equals(qName)){
				return true;
			}
		}
		
		//verify if qname prefix is in ignored
		//String prefix = qName.getPrefixString();
		NamespacePrefixResolver nsR = this.registry.getNamespaceService();
		String prefix = qName.getPrefixedQName(nsR).getPrefixString();
		for (String str : this.ignoreAspectPrefix) {
			if(prefix.startsWith(str)){
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Verify if the tipe qname is ignored 
	 * 
	 * @param qName
	 * @return {@link Boolean}
	 */
	private boolean isTypeIgnored(QName qName) {
		//verify if qname is in ignored
		for (QName qn : this.ignoredType) {
			if(qn.equals(qName)){
				return true;
			}
		}
		
		return false;
	}
	

	/**
	 * Return Qname in String Format
	 * 
	 * @param qName
	 * @return {@link String}
	 */
	private String getQnameStringFormat(QName qName) throws Exception{
		NamespacePrefixResolver nsR = this.registry.getNamespaceService();
		return qName.getPrefixedQName(nsR).getPrefixString();
	}


	/**
	 * Format metadata guided by Bulk-Import format
	 * 
	 * @param obj
	 * @return {@link String}
	 */
	private String formatMetadata (Serializable obj){
		String returnValue = "";
		
		if(obj != null) {
			if(obj instanceof Date){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
				
				Date date = (Date) obj;
				returnValue = format.format(date);
				returnValue = returnValue.substring(0, 26) + ":" + returnValue.substring(26);
			} else {
				
				//
				// TODO: Format data to all bulk-import data format (list as example)
				//
				
				returnValue = obj.toString();
			}
		}
		
		return returnValue;
	}
}
