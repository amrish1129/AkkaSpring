package in.hopscotch.inventory.mailbox;

import com.typesafe.config.Config;

import akka.actor.ActorSystem;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedPriorityMailbox;
import in.hopscotch.inventory.beans.Task;

public class PriorityMailBox extends UnboundedPriorityMailbox {

	public PriorityMailBox(ActorSystem.Settings settings, Config config) {
		// Create a new PriorityGenerator, lower priority means more important
        super(new PriorityGenerator() {

            @Override
            public int gen(Object message) {
                if (message instanceof Task) {
                    return ((Task) message).getPriority();
                } else {
                    // default
                    return 100;
                }
            }
        });
	}

}
