package com.visionous.dms;
/**
 * @author delimeta
 *
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@EnableDiscoveryClient
@SpringBootApplication
public class DmsApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		final AnnotationConfigApplicationContext appContext =  new AnnotationConfigApplicationContext();
//
//		Map<String, Object> myProperties = new HashMap<>();
//		myProperties.put("spring.datasource.url", "jdbc:postgresql://localhost:0000/testalphadmsdb");
//
//		appContext.getEnvironment().getPropertySources().addFirst(new MapPropertySource("applicationConfig", myProperties));
//
//
//		appContext.getEnvironment().getPropertySources().stream().forEach(x -> {
//			if(x.containsProperty("spring.datasource.driver-class" +
//					"-name"))
//			{
//				System.out.println( " GOTS NAME = "+ x);
//			}else{
//				System.out.println("NO-NAME = "+x );
//			}
//		});

		new SpringApplicationBuilder(DmsApplication.class)
				.web(WebApplicationType.SERVLET).run(args);
//		appContext.refresh();

	}
//
//	@Override
//	public void setApplicationContext( ApplicationContext aApplicationContext )
//	{
//		Map<String, Object> myProperties = new HashMap<>();
//		myProperties.put("spring.datasource.driver-class-name", "my.own.db.engine");
//		myProperties.put("spring.datasource.url", "jdbc:postgresql://localhost:0000/testalphadmsdb");
//
//		System.out.println( " KLEVIN");
//
//		ConfigurableEnvironment env = (ConfigurableEnvironment) aApplicationContext.getEnvironment();
//		System.out.println( "GET PROEPRTY No1" + env.getProperty("spring.datasource.driver-class-name").toString());
//
//		env.getPropertySources().addLast(
//				new MapPropertySource("applicationConfig", myProperties));
//
//		env.getPropertySources().stream().forEach(x -> {
//			if(x.containsProperty("spring.datasource.driver-class-name"))
//			{
//				System.out.println( " GOTS NAME = "+ x);
//			}else{
//				System.out.println("NO-NAME = "+x );
//			}
//		});
//
//		System.out.println( "GET PROEPRTY No2" + env.getProperty("spring.datasource.driver-class-name").toString());
//
//		env.getPropertySources().addLast(
//				new MapPropertySource("applicationConfig", myProperties));
//		env.getPropertySources().stream().forEach(x-> System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + x.getName()));
//
//	}


}
