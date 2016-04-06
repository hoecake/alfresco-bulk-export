package org.alfresco.extension.bulkexport.webscripts;

import java.io.IOException;

import org.alfresco.extension.bulkexport.controller.Engine;
import org.alfresco.extension.bulkexport.dao.AlfrescoExportDao;
import org.alfresco.extension.bulkexport.model.FileFolder;
import org.alfresco.service.ServiceRegistry;
import org.apache.chemistry.opencmis.server.support.query.CmisQlExtParser_CmisBaseGrammar.boolean_factor_return;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;



public class BulkExportWebscript extends AbstractWebScript{
	
	protected ServiceRegistry serviceRegistry;
	protected AlfrescoExportDao dao;
	protected FileFolder fileFolder;
	protected Engine engine;

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
		
		dao = 
		
	}

}
