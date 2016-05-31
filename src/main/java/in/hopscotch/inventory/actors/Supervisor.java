package in.hopscotch.inventory.actors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import akka.routing.ActorRefRoutee;
import akka.routing.Routee;
import akka.routing.Router;
import akka.routing.SmallestMailboxRoutingLogic;
import in.hopscotch.inventory.beans.AkkaResult;
import in.hopscotch.inventory.beans.AkkaTask;
import in.hopscotch.inventory.beans.Response;
import in.hopscotch.inventory.beans.Task;
import in.hopscotch.inventory.extention.SpringExtension;

//@Component
//@Scope("prototype")
public class Supervisor extends UntypedActor {
	
	private final LoggingAdapter log = Logging
	        .getLogger(getContext().system(), "Supervisor");
	
	//@Autowired
    private SpringExtension springExtension;
	
	private Router router;
	
	public Supervisor(SpringExtension springExtension) {
		this.springExtension = springExtension;
	}

	public static Props props(SpringExtension springExtension) {
		return Props.create(new Creator<Supervisor>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Supervisor create() throws Exception {
				// TODO Auto-generated method stub
				return new Supervisor(springExtension);
			}
		});
	}
	
	@Override
	public void preStart() throws Exception {
		log.info("Starting Up!");
		List<Routee> routees = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			ActorRef actor = getContext().actorOf(springExtension.props("taskActor"));
			getContext().watch(actor);
			routees.add(new ActorRefRoutee(actor));
		}
		
		router = new Router(new SmallestMailboxRoutingLogic(), routees);
		super.preStart();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Task) {
			router.route(message, getSelf());
		} else if (message instanceof Terminated) {
			// If one of those Routees fails with a Terminated message, a new
			// instance is added to the router resources.
			router = router.removeRoutee(((Terminated) message).actor());
			ActorRef actor = getContext().actorOf(springExtension.props("taskActor"));
			getContext().watch(actor);
			router = router.addRoutee(new ActorRefRoutee(actor));
		} else if (message instanceof Response) {
			log.info("Got response from:" + getSender());
		} else if (message instanceof AkkaTask) {
			router.route(message, getSender());
		} 
		else {
			log.error("Unable to handle message:{}", message);
		}
	}
	
	@Override
	public void postStop() throws Exception {
		log.info("Shutting Down");
		super.postStop();
	}

}
