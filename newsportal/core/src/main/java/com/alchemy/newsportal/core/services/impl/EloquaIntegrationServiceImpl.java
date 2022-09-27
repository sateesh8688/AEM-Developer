package com.alchemy.newsportal.core.services.impl;

import java.io.IOException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alchemy.newsportal.core.services.EloquaConfiguration;
import com.alchemy.newsportal.core.services.EloquaIntegrationService;

@Component(service = EloquaIntegrationService.class, enabled = true, immediate = true)
@Designate(ocd = EloquaConfiguration.class)
@ServiceDescription("Eloqua Integration Service")
@ServiceVendor("Alchemy")
@ServiceRanking(100)
public class EloquaIntegrationServiceImpl implements EloquaIntegrationService {

	private static final Logger LOG = LoggerFactory.getLogger(EloquaIntegrationServiceImpl.class);

	EloquaConfiguration config;

	public void postCustomerInfo(String data) {

		String restUrl = config.restUrl(); 

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(restUrl);

		HttpResponse httpresponse;
		try {
			httpresponse = httpclient.execute(httpget);
			Scanner sc = new Scanner(httpresponse.getEntity().getContent());
			System.out.println(httpresponse.getStatusLine());
			LOG.info("Response {}", httpresponse.getStatusLine());
			while (sc.hasNext()) {
				LOG.info("Response Line {}", sc.nextLine());
			}
		} catch (IOException e) {
			LOG.error("Error while fetching user info {}", e.getMessage());
		}
	}

	public String getCustomerInfoById(String id) {
		return null;
	}

	@Activate
	public void activate(EloquaConfiguration config) {
		LOG.info("Activate method is executed");
		this.config = config;
		// printConfig(config);
		postCustomerInfo(null);
	}

	@Deactivate
	public void deactivate(EloquaConfiguration config) {
		LOG.info("Deactivate method is executed");
		printConfig(config);
	}

	@Modified
	public void update(EloquaConfiguration config) {
		LOG.info("Update method is executed");
		this.config = config;
		printConfig(config);
	}

	public void printConfig(EloquaConfiguration config) {
		LOG.info("Client Id {}", config.clientId());
		LOG.info("Client Secret {}", config.clientSecret());
		LOG.info("Rest URL {}", config.restUrl());
		LOG.info("Status {}", config.status());
		LOG.info("Protocol {}", config.protocol());
	}

}
