package in.hopscotch.inventory.actors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import in.hopscotch.inventory.beans.AkkaResult;
import in.hopscotch.inventory.beans.AkkaTask;
import in.hopscotch.inventory.beans.Response;
import in.hopscotch.inventory.beans.Task;
import in.hopscotch.inventory.service.TaskDAO;

/**
 * 
 * @author amrish
 *
 */
@Component
//@Scope("prototype")
public class TaskActor extends UntypedActor {
	private final LoggingAdapter log = Logging.getLogger(getContext().system(), "TaskProcessor");

	@Autowired
	private TaskDAO taskDAO;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Task) {
			log.info("Inside TaskActor:");
			System.out.println(getSender());
			Long result = taskDAO.createTask((Task) message);
			log.info("created task:{}", result);
			getSender().tell(new Response(), getSelf());
		} else if (message instanceof AkkaTask) {
			log.info("Inside TaskActor:AKKA TASK");
			//System.out.println(getSender());
			getSender().tell(new AkkaResult(), getSelf());
		}
	}

}
