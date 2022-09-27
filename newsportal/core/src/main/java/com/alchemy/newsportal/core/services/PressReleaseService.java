package com.alchemy.newsportal.core.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
		enabled = true, 
		name = "Newsportal - Press release Service",
		immediate = true)
public class PressReleaseService {

	private static final Logger LOG = LoggerFactory.getLogger(PressReleaseService.class);
	
	@Reference
	ArticleService articleService;
	
	@Activate
	public void activate() {
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
	
}
