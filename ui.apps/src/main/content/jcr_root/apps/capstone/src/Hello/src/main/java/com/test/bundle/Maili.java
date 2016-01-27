package com.test.bundle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.*;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.mail.Header;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import com.day.cq.workflow.exec.WorkflowProcess;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.mail.HtmlEmail;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.tika.io.CountingInputStream;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.w3c.dom.Document;
 
/**
 * The <code>SendDownloadAssetEmailProcess</code> will send download asset email.
 */
@Component(metatype = false)
@Service
@Properties({
        @Property(name = Constants.SERVICE_DESCRIPTION, value = "Send Maile"),
        @Property(name = "process.label", value = "Send Maile")
})
 
public class Maili implements WorkflowProcess {
 
    private static final Logger log = LoggerFactory.getLogger(Maili.class);
    private static final String DEFAULT_CHARSET = "utf-8";
 
    /**
     * resource resolver factory.
     */
    @Reference
    private ResourceResolverFactory resourceResolverFactory;
 
    @Reference(policy = ReferencePolicy.STATIC)
    private MessageGatewayService messageGatewayService;
 
    @Reference(policy = ReferencePolicy.STATIC)
    private ResourceResolverFactory resolverFactory;
 
    public void execute(WorkItem workItem, WorkflowSession session, MetaDataMap metaData)
            throws WorkflowException {
        MetaDataMap workflowMetaDataMap = workItem.getWorkflowData().getMetaDataMap();
        Node submitNode = null;
        ResourceResolver resolver;
        try {
            resolver =
                    resourceResolverFactory.getResourceResolver(Collections.singletonMap("user.jcr.session",
                            (Object) session.getSession()));
        } catch (final Exception e) {
            throw new WorkflowException("could not get resource resolver", e);
        }
 
        // do not execute upon missing mail service. osgi component still needs to be available for UI
        // selection in workflow step configuration.
 
 
        if (messageGatewayService != null) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                if (factory != null) {
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = null;
                    String payloadPath = workItem.getWorkflowData().getPayload().toString();
                    submitNode = resolver.getResource(payloadPath).adaptTo(Node.class);
                    if (submitNode != null) {
                        InputStream is = submitNode.getProperty("jcr:data").getBinary().getStream();
                        byte[] fileBytes = IOUtils.toByteArray(is);
                        is.close();
                        String username = null, emailAdd = null;
                        doc = builder.parse(new ByteArrayInputStream(fileBytes));
                        XPath xPath = XPathFactory.newInstance().newXPath();
                        org.w3c.dom.Node domNode = (org.w3c.dom.Node) xPath.evaluate("//emailid", doc, XPathConstants.NODE);
                        if(domNode !=null) {
                            emailAdd = domNode.getFirstChild().getNodeValue();
                        }
                        org.w3c.dom.Node domNodeForName = (org.w3c.dom.Node) xPath.evaluate("//username", doc, XPathConstants.NODE);
                        if(domNodeForName != null) {
                            username = domNodeForName.getFirstChild().getNodeValue();
                        }
 
                        if(emailAdd !=null && username != null ) {
                            ArrayList<InternetAddress> emailRecipients = new ArrayList<InternetAddress>(10);
                            emailRecipients.add(new InternetAddress(emailAdd));
                            if (workflowMetaDataMap != null) {
 
                                if (emailRecipients != null && emailRecipients.size() != 0) {
                                    String template = getEmailTemplate(metaData, session);
                                    if (template != null) {
                                        // get the string substitutes
                                        Map<String, String> valuesMap = new HashMap<String, String>();
                                        valuesMap.put("event.TimeStamp", Calendar.getInstance().getTime().toString());
                                        valuesMap.put("username", username);
                                        StrSubstitutor substitutor = new StrSubstitutor(valuesMap);
 
                                        final HtmlEmail email = createEmail(template, substitutor);
                                        email.setTo(emailRecipients);
                                        messageGatewayService.getGateway(HtmlEmail.class).send(email);
 
                                        log.info("Email was sent.");
                                    } else {
                                        log.warn("Did not send email. No email template defined");
                                    }
                                } else {
                                    log.warn("Did not send email. No recipient addresses or download URL available.");
                                }
                            } else {
                                log.warn("Did not send email. No workflow metadata is null.");
                            }
                        }
 
                    }
                }
 
 
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Fatal error while sending email in workflow", e);
            }
        } else {
            log.warn("cannot send email, mail service unavailable. Please configure Gateway in OSGi Console");
        }
    }
 
    private String getEmailTemplate(MetaDataMap workflowMetaDataMap, WorkflowSession session) {
        String template = workflowMetaDataMap.get("template", String.class);
        if (template == null) {
            // load mail template
            String templatePath = workflowMetaDataMap.get("templatePath", String.class);
            template = loadTemplate(session.getSession(), templatePath);
        }
        log.debug("Loaded template: {}", template);
        return template;
    }
 
    private HtmlEmail createEmail(final String template, final StrSubstitutor substitutor) {
        final HtmlEmail email = new HtmlEmail();
        try {
            // Note that substitutions must be called, because they are expected to be
            // US-ASCII only or specially encoded using MimeUtils class, but substitutions might introduce other chars,
            // like e.g. Japanese characters.
            // Further the CountingInputStream class does not seem to properly count when reading Japanese characters.
            final CountingInputStream in = new CountingInputStream(new ByteArrayInputStream(template.getBytes(DEFAULT_CHARSET)));
            final InternetHeaders iHdrs = new InternetHeaders(in);
            final Map<String, String[]> hdrs = new HashMap<String, String[]>();
            final Enumeration e = iHdrs.getAllHeaders();
            while (e.hasMoreElements()) {
                final Header hdr = (Header) e.nextElement();
                final String name = hdr.getName();
                log.debug("Header: {} = {}", name, hdr.getValue());
                hdrs.put(name, iHdrs.getHeader(name));
            }
 
            // use the counting stream reader to read the mail body
            String templateBody = template.substring(in.getCount());
 
            // create email
            email.setCharset(DEFAULT_CHARSET);
 
            // set subject
            final String[] ret = hdrs.remove("subject");
            final String subject = (ret == null ? "" : ret[0]);
            log.info("Email subject: " + subject);
            if (!StringUtils.isEmpty(subject)) {
                email.setSubject(substitutor.replace(subject));
            }
 
            // set message body
            templateBody = substitutor.replace(templateBody);
            log.debug("Substituted mail body: {}", templateBody);
            email.setMsg(templateBody);
 
            IOUtils.closeQuietly(in);
        } catch (Exception e) {
            log.error("Create email: ", e.getMessage());
        }
        return email;
    }
 
    /**
     * Loads the mail templates from the repository.
     *
     * @param path    mail templates root path
     * @param session session
     * @param charSet The character set
     * @return a reader to the template or <code>null</code> if not valid.
     */
    public String loadTemplate(final Session session, final String path) {
        InputStream is = null;
        try {
            final Node content = session.getNode(path + "/" + JcrConstants.JCR_CONTENT);
            is = content.getProperty(JcrConstants.JCR_DATA).getBinary().getStream();
            final InputStreamReader r = new InputStreamReader(is, DEFAULT_CHARSET);
            final StringWriter w = new StringWriter();
            IOUtils.copy(r, w);
            return w.toString();
        } catch (final Exception e) {
            log.error("Error while loading mail template {}:{}", path, e.toString());
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }
 
}