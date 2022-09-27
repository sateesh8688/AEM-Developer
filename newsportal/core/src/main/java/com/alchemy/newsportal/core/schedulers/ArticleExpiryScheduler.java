package com.alchemy.newsportal.core.schedulers;

import java.util.Date;
import java.util.Iterator;
import javax.jcr.Session;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alchemy.newsportal.core.services.NewsportalUtilService;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(service = Runnable.class,immediate = true)
@Designate(ocd = ArticleExpiryScheduler.Config.class)
public class ArticleExpiryScheduler implements Runnable {

	@Reference
	NewsportalUtilService newsportalUtilService;

	@Reference
	Replicator replicator;

	private static final Logger LOG = LoggerFactory.getLogger(ArticleExpiryScheduler.class);

	@ObjectClassDefinition(name = "Article Expiry Scheduler Configuration", description = "Article Expiry Scheduler Configuration")
	public static @interface Config {

		@AttributeDefinition(name = "Cron-job expression")
		String scheduler_expression() default ""; // */5 * * * * ?

		@AttributeDefinition(name = "Concurrent task", description = "Whether or not to schedule this task concurrently")
		boolean scheduler_concurrent() default false;
	}

	@Override
	public void run() {
		LOG.info("Inside Article Expiry Scheduler");
		ResourceResolver resolver = newsportalUtilService.getResourceResolver();
		PageManager pageManager = resolver.adaptTo(PageManager.class);
		Page articlePage = pageManager.getPage("/content/newsportal/us/en/articles");
		Iterator<Page> articleChildPages = articlePage.listChildren();

		while (articleChildPages.hasNext()) {
			try {
				Page page = (Page) articleChildPages.next();
				Date expiryDate = page.getProperties().get("articleExpiry", Date.class);
				if(expiryDate != null) {
					if ((new Date()).compareTo(expiryDate) > 0) {
						ModifiableValueMap props = page.getContentResource().adaptTo(ModifiableValueMap.class);
						props.put("expired", true);
						resolver.commit();
						replicator.replicate(resolver.adaptTo(Session.class), ReplicationActionType.DEACTIVATE,
								page.getPath());
					}
				}				
			} catch (PersistenceException | ReplicationException e) {
				LOG.error("Error while checking expiry article pages");
			}
		}
		resolver.close();
	}

	@Activate
	public void init(Config config) {
		LOG.info("Cron expression {}, run concurrent {}", config.scheduler_expression(), config.scheduler_concurrent());
	}

}
