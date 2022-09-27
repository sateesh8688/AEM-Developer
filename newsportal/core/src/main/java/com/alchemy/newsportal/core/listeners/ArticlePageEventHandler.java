package com.alchemy.newsportal.core.listeners;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alchemy.newsportal.core.services.NewsportalUtilService;

@Component(	
		service = EventHandler.class,
		property = {
			EventConstants.EVENT_TOPIC+"=com/day/cq/wcm/core/page"
		}
		)
public class ArticlePageEventHandler implements EventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ArticlePageEventHandler.class);
	
	@Reference
	NewsportalUtilService newsportalUtilService;
		
	@Override
	public void handleEvent(Event event) {
		LOG.info("inside event handler {}"+event.getTopic());
				
		ResourceResolver resolver = newsportalUtilService.getResourceResolver();
		if(resolver == null) {
			LOG.error("Resource Resolver is null");
			return;
		}
		
		List<Map<String,String>> data = (ArrayList<Map<String,String>>) event.getProperty("modifications");
		for(Map<String,String> pageInfo : data) {
			if(pageInfo.get("type").equals("PageCreated")) {
				
				String[] tags = {"newsportal:category/entertainment"};
				
				LOG.info("Page Path :: {}",pageInfo.get("path"));				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 15);				
				
				try {
					String path = pageInfo.get("path");
					Resource pageResource = resolver.getResource(path+"/jcr:content");
					ModifiableValueMap props = pageResource.adaptTo(ModifiableValueMap.class);
					props.put("articleExpiry", cal);					
					props.put("cq:tags", tags);
					resolver.commit();
					resolver.close();
				} catch (PersistenceException e) {
					LOG.error("Error while adding article expiry date {}",e.getMessage());
				}				
			}	       
		}	
		
	}

}
