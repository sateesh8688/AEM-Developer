package com.alchemy.newsportal.core.services;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
		service = ArticleService.class,
		enabled = true, 
		name = "Newsportal - Article Service",
		immediate = false)
public class ArticleService {

	private static final Logger LOG = LoggerFactory.getLogger(ArticleService.class);
	
	@Reference
	ResourceResolverFactory factory;
	
	@Activate
	public void activate() {
				
	
		try {
			
			Map<String, Object> props = new HashMap<>();
			props.put(factory.SUBSERVICE, "npsubservice");
			
			ResourceResolver resolver = factory.getServiceResourceResolver(props);
			Session session = resolver.adaptTo(Session.class);
			
		} catch (LoginException e) {
			LOG.error("Error while creating resolver and session object.");
		}
		
		
		LOG.info("Activate method is executed");
	}
	
	@Deactivate
	public void deactivate() {
		LOG.info("Deactivate method is executed");	
	}
	
	@Modified
	public void update() {
		LOG.info("Update method is executed");
	}
	
	public String getRelatedArticles() {
		return "logic need to implement";
	}
	
}
