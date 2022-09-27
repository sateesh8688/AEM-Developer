package com.alchemy.newsportal.core.listeners;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
		service = EventHandler.class,
		immediate = true,
		property = {
				EventConstants.EVENT_TOPIC+"=com/day/cq/replication",
				EventConstants.EVENT_FILTER+"=(| (& (type=ACTIVATE) (paths=/content/newsportal/us/en/articles/*)) (& (type=DEACTIVATE) (paths=/content/wknd/language-masters/en/magazine/*)))"
		}
		)
public class PromolPageActivationListener implements EventHandler {

	private static final Logger log = LoggerFactory.getLogger(PromolPageActivationListener.class);
	
	
	@Override
	public void handleEvent(Event event) {
		
		log.info("Inside event handler .. ");	
		String[] properties = event.getPropertyNames();
		for(String prop : properties) {
			log.info("Property name {}, property value {}",prop,event.getProperty(prop));
		}
	}

}
