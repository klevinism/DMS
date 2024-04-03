package com.visionous.dms;
/**
 * @author delimeta
 *
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties
public class DmsApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringApplication().run(DmsApplication.class, args);
	}
}
