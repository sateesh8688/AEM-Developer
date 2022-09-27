package com.alchemy.newsportal.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alchemy.newsportal.core.services.NewsportalUtilService;

@Component(
		immediate = true,
		service = NewsportalUtilService.class
		)
public class NewsportalUtilServiceImpl implements NewsportalUtilService{

	private static final Logger LOG = LoggerFactory.getLogger(NewsportalUtilServiceImpl.class);
	
	private static final String SUB_SERVICE_NAME = "npsubservice";
	
	@Reference
	ResourceResolverFactory factory;
	
	@Override
	public ResourceResolver getResourceResolver() {		
		ResourceResolver resolver = null;
		try {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put(ResourceResolverFactory.SUBSERVICE, SUB_SERVICE_NAME);	
			resolver = factory.getServiceResourceResolver(params);			
		} catch (LoginException e) {
			LOG.error("Error while creating resource resolver object from system user - {}",e.getMessage());
		}
		return resolver;		
	}

}
