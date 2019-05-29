package com.tutorial.platformsample;

import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderUtil;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.surf.util.Content;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.servlet.FormData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UploadWebScript extends DeclarativeWebScript {
    private static Log logger = LogFactory.getLog(UploadWebScript.class);

    private ServiceRegistry serviceRegistry;

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {

        Map<String, Object> model = new HashMap<String, Object>();

        String path = req.getParameter("path");

        FormData formData = (FormData) req.parseContent();
        FormData.FormField[] fields = formData.getFields();

        NodeRef folder = createPath("path");

        for(FormData.FormField field : fields) {
            String fieldName = field.getName();

            if(fieldName.equalsIgnoreCase("filedata")
                    && field.getIsFile()) {
                String fileName = field.getFilename();
                Content fileContent = field.getContent();
                String fileMimetype = field.getMimetype();

               String nodeRef = writeContent(fileName, fileContent, fileMimetype, folder);

               model.put("message", "success");
               model.put("nodeRef", nodeRef);
            }
        }

        return model;
    }

    private NodeRef createPath(String path) {
        NodeRef companyHome = serviceRegistry.getNodeLocatorService().getNode("companyhome", null, null);

        FileInfo newFolder = FileFolderUtil.makeFolders(serviceRegistry.getFileFolderService(), companyHome, Arrays.asList(path.split("/")), ContentModel.TYPE_FOLDER);

        return newFolder.getNodeRef();
    }


    private String writeContent(String fileName, Content fileContent, String fileMimetype, NodeRef parentNodeRef) {
        try {
            NodeRef fileNodeRef = createFileNode(parentNodeRef,
                    fileName);

            ContentWriter contentWriter = serviceRegistry.getContentService().getWriter(fileNodeRef,
                    ContentModel.PROP_CONTENT, true);
            contentWriter.setMimetype(fileMimetype);
            contentWriter.putContent(fileContent.getInputStream());

            return fileNodeRef.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    private NodeRef createFileNode(NodeRef parentNode, String fileName) {
        try {
            FileInfo fileInfo = serviceRegistry.getFileFolderService().create(parentNode,
                    fileName, ContentModel.TYPE_CONTENT);

            return fileInfo.getNodeRef();

        } catch (Exception e) {
            throw e;
        }
    }
}
