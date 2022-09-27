package com.alchemy.newsportal.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ArticleJsonSchedulerAPI.class, immediate = true)
@Designate(ocd=ArticleJsonSchedulerAPI.Config.class)
public class ArticleJsonSchedulerAPI implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(ArticleJsonSchedulerAPI.class);
	
	@Reference
	Scheduler scheduler;
	
	@ObjectClassDefinition(name="Article json scheduler",
            description = "Article Json scheduler implementation")
	public static @interface Config {
	
		@AttributeDefinition(name = "Cron-job expression")
		String schedulerexpression() default "*/5 * * * * ?";
		
		@AttributeDefinition(name = "Concurrent task",
		              description = "Whether or not to schedule this task concurrently")
		boolean scheduler_concurrent() default false;
		
		@AttributeDefinition(name = "Schdeduler Name")
		String schedulername() default "articlescheduler";
		
		@AttributeDefinition(name = "Enable/Disable Scheduler")
		boolean enable() default false;
	}
	

	@Override
	public void run() {
		log.info("Article Json scheduler run method is executed ..");
	}
	
	@Activate
    protected void activate(final Config config) {
		scheduleJob(config);
    }
	
	@Modified
    protected void update(final Config config) {
		scheduleJob(config);
    }
	
	@Deactivate
    protected void deactivate(final Config config) {
		scheduleJob(config);
    }
	
	 private void scheduleJob(Config config) {
		    if(config.enable()) {
		    	ScheduleOptions  options = scheduler.EXPR(config.schedulerexpression());
		    	options.canRunConcurrently(false);
		    	options.name(config.schedulername());    	
		    	scheduler.schedule(this, options);
		    	log.info("End of the add schedule job method");
		    } else {
		    	scheduler.unschedule(config.schedulername());
		    }
		    
	}

}
