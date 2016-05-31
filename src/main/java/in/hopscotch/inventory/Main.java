package in.hopscotch.inventory;

import java.util.Random;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import in.hopscotch.inventory.beans.Task;
import in.hopscotch.inventory.extention.SpringExtension;

/**
 *  
 * 
 * @author amrish
 *
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("in.hopscotch.inventory.configuration")
public class Main {
	
	public static void main(String ... args) {
		
		try {
			// Spring Boot manages the startup and context including the ActorSystem
			// as an Akka interface.
			ApplicationContext context = SpringApplication.run(Main.class, args);
			ActorSystem system = context.getBean(ActorSystem.class);
			
			final LoggingAdapter log = Logging.getLogger(system, "Application------");
			log.info("--- Starting Up-----");
			
			//An instance of the Supervisor actor is created with the
			// SpringActorProducer and the custom PriorityMailbox mailbox
		/*	SpringExtension ext = context.getBean(SpringExtension.class);
			// Use the Spring Extension to create props for a named actor bean
			ActorRef supervisor = system.actorOf(
			    ext.props("supervisor").withMailbox("akka.priority-mailbox"));
			//Tasks with random priorities are created and
			// sent to the supervisor actor
			for (int i = 0; i <50 ; i ++) {
				Task task = new Task();
				task.setPayload("Payload:" + i);
				task.setPriority(new Random().nextInt(99));
				supervisor.tell(task, null);
			}*/
			
			// Poison pill will be queued with a priority of 100 as the last
			// message
			
			//Thread.sleep(10);
			//supervisor.tell(PoisonPill.getInstance(), null);
			
			/*while (!supervisor.isTerminated()) {
			    Thread.sleep(1000);
			    
			}

			log.info("Created {} tasks", context.getBean(JdbcTemplate.class)
			    .queryForObject("SELECT COUNT(*) FROM tasks", Integer.class));

			log.info("Shutting down");

			system.shutdown();
			system.awaitTermination();*/
		} catch (BeansException | DataAccessException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
