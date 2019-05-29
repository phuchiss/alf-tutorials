package com.tutorial.platformsample;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SearchWebScript extends DeclarativeWebScript {

    private static Log logger = LogFactory.getLog(SearchWebScript.class);

    private ServiceRegistry serviceRegistry;

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    protected Map<String, Object> executeImpl(
            WebScriptRequest req, Status status, Cache cache) {
        Map<String, Object> model = new HashMap<String, Object>();

        String name = req.getParameter("name");

        ResultSet resultSet = null;

        resultSet = serviceRegistry.getSearchService().query(
                new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore"), SearchService.LANGUAGE_FTS_ALFRESCO, "cm:name:" + name);


        model.put("resultSet", resultSet.getNodeRefs());

        logger.debug("Your 'Hello World' Web Script was called!");

        return model;
    }


}
