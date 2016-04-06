package org.alfresco.extension.bulkexport.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.alfresco.extension.bulkexport.dao.AlfrescoExportDao;
import org.alfresco.extension.bulkexport.model.FileFolder;
import org.alfresco.service.cmr.repository.NodeRef;

public class Engine {
	
	private AlfrescoExportDao dao;
	private FileFolder fileFolder;
	
	public Engine(AlfrescoExportDao dao,FileFolder fileFolder){
		this.dao = dao;
		this.fileFolder = fileFolder;
	}
	
	public void execute(NodeRef nodeRef) throws Exception{
		if(!this.dao.isNodeIgnored(nodeRef.toString())){
			if(this.dao.isFolder(nodeRef)){
				this.createFolder(nodeRef);
				
				List<NodeRef> children = this.dao.getChildren(nodeRef);
				for(NodeRef child : children){
					this.execute(child);
				}
			}
			else{
				this.createFile(nodeRef);
			}
		}
	}
	
	private void createFolder(NodeRef folder) throws Exception{
		String path = this.dao.getPath(folder);
		String type = this.dao.getType(folder);
		
		List<String> aspects = this.dao.getAspectsAsString(folder);
		Map<String, String> properties = this.dao.getPropertiesAsString(folder);
		
		//Create Folder and XML Metadata
		this.fileFolder.createFolder(path);
		this.fileFolder.insertFileProperties(type, aspects, properties, path);
	}
	
	private void createFile(NodeRef file) throws Exception{
		ByteArrayOutputStream out = this.dao.getContent(file);
		String type = this.dao.getType(file);
		List<String> aspects = this.dao.getAspectsAsString(file);
		Map<String, String> properties = this.dao.getPropertiesAsString(file);
		String path = this.dao.getPath(file);
		
		//Create File and XML Metadata
		this.fileFolder.insertFileContent(out, path);
		this.fileFolder.insertFileProperties(type, aspects, properties, path);
	}
}
