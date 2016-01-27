<%@include file="/libs/fd/af/components/guidesglobal.jsp" %>
<%@page import="com.day.cq.wcm.foundation.forms.FormsHelper,
             org.apache.sling.api.resource.ResourceUtil,
             org.apache.sling.api.resource.ValueMap,
                com.day.cq.mailer.MessageGatewayService,
        com.day.cq.mailer.MessageGateway,
        org.apache.commons.mail.Email,
                org.apache.commons.mail.SimpleEmail" %>
<%@taglib prefix="sling"
                uri="http://sling.apache.org/taglibs/sling/1.0" %>
<%@taglib prefix="cq"
                uri="http://www.day.com/taglibs/cq/1.0"
%>
<cq:defineObjects/>
<sling:defineObjects/>
<%
        String storeContent = 
                    "/libs/fd/af/components/guidesubmittype/store";
        FormsHelper.runAction(storeContent, "post", resource, 
                                slingRequest, slingResponse);
    ValueMap props = ResourceUtil.getValueMap(resource);
    Email email = new SimpleEmail();
    String[] mailTo = props.get("mailto", new String[0]);
    email.setFrom((String)props.get("from"));
        for (String toAddr : mailTo) {
            email.addTo(toAddr);
      }
    email.setMsg((String)props.get("template"));
    email.setSubject((String)props.get("subject"));

log.info ("About to send EMAIL from:"+props.get("from"));
    MessageGatewayService messageGatewayService = 
                    sling.getService(MessageGatewayService.class);
    MessageGateway messageGateway = 
                messageGatewayService.getGateway(SimpleEmail.class);
//messageGateway.send(email);
%>