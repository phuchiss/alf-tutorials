[
<#list resultSet as node>
    {
    "name" : "${node.name}",
    "download" : "${url.serviceContext}/api/node/content/${node.nodeRef.storeRef.protocol}/${node.nodeRef.storeRef.identifier}/${node.nodeRef.id}/${node.name?url}",
    "title" : "${node.properties["cm:title"]}"
    }
</#list>
]