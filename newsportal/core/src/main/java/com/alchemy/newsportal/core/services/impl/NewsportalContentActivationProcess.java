package com.alchemy.newsportal.core.services.impl;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.alchemy.newsportal.core.services.NewsportalUtilService;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;

@Component(
		immediate = true,
		property = {
			"process.label=Newsportal Content Activation Process"	
		}
		)
public class NewsportalContentActivationProcess implements WorkflowProcess {
	
	@Reference
	Replicator replicator;
	
	@Reference
	NewsportalUtilService newsportalUtilService;

	@Override
	public void execute(WorkItem item, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {
		
		ResourceResolver resolver = newsportalUtilService.getResourceResolver();
		Session session = resolver.adaptTo(Session.class);
		
		String payload = item.getWorkflowData().getPayload().toString();
		
		try {
			replicator.replicate(session, ReplicationActionType.ACTIVATE, payload);
		} catch (ReplicationException e) {
			
		}
	}

}
