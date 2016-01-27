package com.adobe.aemtraining.capstone.core;



import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
 
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
    	
    }
 
 
 
}