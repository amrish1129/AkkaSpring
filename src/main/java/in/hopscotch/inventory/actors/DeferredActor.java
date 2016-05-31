package in.hopscotch.inventory.actors;

import org.springframework.web.context.request.async.DeferredResult;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import in.hopscotch.inventory.beans.AkkaResult;
import in.hopscotch.inventory.beans.AkkaTask;

//@Component
//@Scope("prototype")
public class DeferredActor extends UntypedActor {
	private final LoggingAdapter log = Logging.getLogger(getContext().system(), "DeferredActor");
	
	
	private ActorRef supervisor;
	private DeferredResult<String> deferredResult;
	
	public static Props props(DeferredResult<String> deferredResult, ActorRef supervisor) {
		return Props.create(new Creator<DeferredActor>() {

			private static final long serialVersionUID = 1L;

			@Override
			public DeferredActor create() throws Exception {
				// TODO Auto-generated method stub
				return new DeferredActor(deferredResult, supervisor);
			}
		});
	}
	
	public DeferredActor() {
		
	}
	
	public DeferredActor(DeferredResult<String> deferredResult, ActorRef supervisor) {
		this.deferredResult = deferredResult;
		this.supervisor = supervisor;
	}
	

	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof AkkaTask) {
			log.info("Task Recevied");
			log.info(getSender().toString());
			supervisor.tell(message, getSelf());
		} else if (message instanceof AkkaResult) {
			log.info(getSender().toString());
			deferredResult.setResult("YoHoooooo!!!!");
		}
	}

	public void setDeferredResult(DeferredResult<String> deferredResult) {
		this.deferredResult = deferredResult;
	}

}
