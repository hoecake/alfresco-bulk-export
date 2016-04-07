package org.alfresco.extension.bulkexport.webscripts;

import java.io.IOException;

import org.alfresco.extension.bulkexport.controller.Engine;
import org.alfresco.extension.bulkexport.dao.AlfrescoExportDao;
import org.alfresco.extension.bulkexport.dao.AlfrescoExportDaoImpl;
import org.alfresco.extension.bulkexport.model.FileFolder;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;



public class BulkExportWebscript extends AbstractWebScript{
	
	protected ServiceRegistry serviceRegistry;
	protected AlfrescoExportDao dao;
	protected FileFolder fileFolder;
	protected Engine engine;

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}


	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
	
	@Override
	public void execute(WebScriptRequest req, WebScriptResponse res) throws IOException {
		String nodeRef = req.getParameter("nodeRef");
		String base = req.getParameter("base");
		boolean escapeExported = false;
		
		if(req.getParameter("ignoreExported") != null){
			if(req.getParameter("ignoreExported").equals("true")){
				escapeExported = true;
			}
		}
		
		dao = new AlfrescoExportDaoImpl(this.serviceRegistry);
		fileFolder = new FileFolder(base, escapeExported);
		engine = new Engine(dao, fileFolder);
		
		try{
			NodeRef nf = dao.getNodeRef(nodeRef);
			engine.execute(nf);
			res.getWriter().write("Process finished Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			res.getWriter().write(e.toString());
		}
	}

}
