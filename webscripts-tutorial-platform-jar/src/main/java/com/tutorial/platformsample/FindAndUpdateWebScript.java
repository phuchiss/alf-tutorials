package com.tutorial.platformsample;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.servlet.FormData;

import java.util.HashMap;
import java.util.Map;

public class FindAndUpdateWebScript extends DeclarativeWebScript {
    private static Log logger = LogFactory.getLog(FindAndUpdateWebScript.class);

    private ServiceRegistry serviceRegistry;

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
        Map<String, Object> model = new HashMap<String, Object>();

        String name = req.getParameter("name");

        FormData formData = (FormData) req.parseContent();
        FormData.FormField[] fields = formData.getFields();

        ResultSet resultSet = null;

        resultSet = serviceRegistry.getSearchService().query(
                new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore"), SearchService.LANGUAGE_FTS_ALFRESCO, "cm:name:" + name);

        for(NodeRef nodeRef : resultSet.getNodeRefs()) {
            for(FormData.FormField field : fields) {
                String fieldName = field.getName();
                String fieldValue = field.getValue();

                logger.debug("Set property :[" + fieldName + "]");
                serviceRegistry.getNodeService().setProperty(nodeRef, QName.resolveToQName(serviceRegistry.getNamespaceService(), fieldName), fieldValue );
            }
        }

        model.put("message", "success");

        return model;
    }
}
