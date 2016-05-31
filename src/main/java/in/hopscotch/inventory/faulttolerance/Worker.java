package in.hopscotch.inventory.faulttolerance;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

/**
 * Worker performs some work when it receives the Start message. It will
 * continuously notify the sender of the Start message of current Progress.
 * The Worker supervise the CounterService.
 */
public class Worker extends UntypedActor {
	
	final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	final Timeout askTimeout = new Timeout(Duration.create(5, "seconds"));

	@Override
	public void onReceive(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		//return resume();
	}

}
