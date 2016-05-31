package in.hopscotch.inventory.web;

import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import in.hopscotch.inventory.actors.DeferredActor;
import in.hopscotch.inventory.beans.AkkaTask;
import in.hopscotch.inventory.beans.AsyncTask;
import in.hopscotch.inventory.configuration.ApplicationContextProvider;
import in.hopscotch.inventory.extention.SpringExtension;

@RestController
public class NonBlockingCallbackController {
	
	@Autowired
	SpringExtension extension;
	
	@Autowired
	ApplicationContextProvider contextProvider;
	
	@Autowired
	ActorRef supervisor;
	
	@Autowired
	ActorSystem system;
	
	@RequestMapping("/processnonblocking")
	public DeferredResult<String> getResult() {
		DeferredResult<String> result = new DeferredResult<>();
		AsyncTask task = new AsyncTask(result);
		Timer timer = new Timer();
		timer.schedule(task, 1);
		return result;
	}
	
	@RequestMapping("/processAkkaNonblocking")
	public DeferredResult<String> testAkka() {
		DeferredResult<String> result = new DeferredResult<String>();
		//ActorSystem system = contextProvider.getApplicationContext().getBean(ActorSystem.class);
		
		//final LoggingAdapter log = Logging.getLogger(system, "Application");
		//log.info("Starting Up");
		
		//An instance of the Supervisor actor is created with the
		// SpringActorProducer and the custom PriorityMailbox mailbox
		//SpringExtension ext = context.getBean(SpringExtension.class);
		// Use the Spring Extension to create props for a named actor bean
		ActorRef defActor = system.actorOf(
		    DeferredActor.props(result, supervisor));
		defActor.tell(new AkkaTask(), defActor);
		
		return result;
	}
	
}
