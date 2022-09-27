package com.alchemy.newsportal.core.listeners;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true,service = EventListener.class)
public class ArticleEventListener implements EventListener {

	private static final Logger log = LoggerFactory.getLogger(ArticleEventListener.class);
	
	@Reference
	SlingRepository repo;
	
	@Override
	public void onEvent(EventIterator events) {
		log.info("On event method is executed ..");
		while (events.hasNext()) {
			Event event = (Event) events.next();
			try {
				log.info("Path {}",event.getPath());
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Activate
	public void activate() {
		
		try {
			String[] nodeType = {"cq:PageContent","cq:Page"};
			
			Session session = repo.loginService("npsubservice", null);
			ObservationManager observationManager = session.getWorkspace().getObservationManager();
			observationManager.addEventListener(this, 
						Event.NODE_ADDED | Event.PROPERTY_ADDED | Event.NODE_MOVED, 
						"/content/newsportal/us/en/articles", 
						true, 
						null, 
						nodeType, 
						true);
			
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
