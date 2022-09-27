package com.alchemy.newsportal.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition
public @interface EloquaConfiguration {

	@AttributeDefinition(name = "Client Id", 
			required = true,
			type = AttributeType.STRING,
			description = "Eloqua Client Id")
	String clientId() default "890890";
	
	@AttributeDefinition(name = "Client Secret", 
			required = true,
			type = AttributeType.STRING,
			description = "Eloqua Client Secret")
	String clientSecret() default "8767890876";
	
	@AttributeDefinition(name = "Status (enable/disable)", 
			required = true,
			type = AttributeType.BOOLEAN,
			description = "Eloqua Integration Status")
	boolean status() default true;
	
	@AttributeDefinition(name = "Eloqua Rest URL", 
			required = true,
			type = AttributeType.STRING)
	String restUrl() default "https://api.eloqua.com/v2/services";
	
	@AttributeDefinition(name = "API Protocol", 
			required = true,
			options = {
					@Option(label = "HTTPS",value = "https"),
					@Option(label = "HTTP",value = "http")
			},
			type = AttributeType.STRING)
	String protocol() default "https";
	
}
