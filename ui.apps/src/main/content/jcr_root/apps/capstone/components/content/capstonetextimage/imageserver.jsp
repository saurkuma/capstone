<%@include file="/libs/foundation/global.jsp"%><%
%><%@ page import="com.day.text.Text,
                   com.day.cq.wcm.foundation.Image,
com.day.cq.commons.Doctype" %><% 
Resource res = null;
    if (properties.get("fileReference")!=null){
                      res  =  resourceResolver.getResource((String)properties.get("fileReference"));
    }
else{
 res  =  resourceResolver.getResource(resource.getPath()+"/file");
}
Image img = null;
try{
		img = new Image(res);
        img.setItemName(Image.NN_FILE, "file");
        img.setItemName(Image.PN_REFERENCE, "imageReference");
        img.setSelector("img");
        img.setDoctype(Doctype.fromRequest(request));
        img.setAlt("Xumak Logo");

    // img.draw(out);
}catch(Exception e){}

    %>
<% if(img==null) { %> Please select an image <%} %>