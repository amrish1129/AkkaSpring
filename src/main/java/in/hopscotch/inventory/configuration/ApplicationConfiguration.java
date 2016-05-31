package in.hopscotch.inventory.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import in.hopscotch.inventory.actors.Supervisor;
import in.hopscotch.inventory.extention.SpringExtension;

/**
 * 
 * @author amrish
 *
 */
@Configuration
@Lazy
@ComponentScan(basePackages = { "in.hopscotch.inventory.beans", "in.hopscotch.inventory.extention" ,"in.hopscotch.inventory.service",
		"in.hopscotch.inventory.actors", "in.hopscotch.inventory.web"})
public class ApplicationConfiguration {
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SpringExtension springExtension;

	@Bean
	public ActorSystem actorSystem() {

		ActorSystem system = ActorSystem.create("AkkaTaskProcessing", akkaConfiguration());
		springExtension.initialize(applicationContext);
		return system;
	}
	
	@Bean
	@Qualifier("supervisor")
	public ActorRef getSupervisor() {
		ActorSystem system = actorSystem();
		ActorRef supervisor = system.actorOf(
				Supervisor.props(springExtension).withMailbox("akka.priority-mailbox"));
		return supervisor;
	}
	
	/*@Bean
	@Qualifier("deferredActor")
	public ActorRef getDeferredActor() {
		ActorSystem system = actorSystem();
		ActorRef deferredActor = system.actorOf(
				springExtension.props("deferredActor"));
		return deferredActor;
	}*/

	@Bean
	public Config akkaConfiguration() {
		return ConfigFactory.load();
	}

	/**
	 * Simple H2 based in memory backend using a connection pool. Creates th
	 * only table needed.
	 */
	@Bean
	public JdbcTemplate jdbcTemplate() throws Exception {

		// Disable c3p0 logging
		final Properties properties = new Properties(System.getProperties());
		properties.put("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
		properties.put("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "OFF");
		System.setProperties(properties);

		final ComboPooledDataSource source = new ComboPooledDataSource();
		source.setMaxPoolSize(100);
		source.setDriverClass("org.h2.Driver");
		source.setJdbcUrl("jdbc:h2:mem:taskdb");
		source.setUser("sa");
		source.setPassword("");

		JdbcTemplate template = new JdbcTemplate(source);
		template.update("CREATE TABLE tasks (id INT(11) AUTO_INCREMENT, " + "payload VARCHAR(255), updated DATETIME)");
		return template;
	}
}
