package in.hopscotch.inventory.extention;

import org.springframework.context.ApplicationContext;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;

/**
 * Akka needs an extension to provide a Java API for actors created in the
 * Spring context. A SpringActorProducer defines how to instantiate an actor in
 * the dependency injection framework.
 * An actor producer that lets Spring create the Actor instances.
 * 
 * @author amrish
 *
 */
public class SpringActorProducer implements IndirectActorProducer {

	private final ApplicationContext applicationContext;
	private final String actorBeanName;

	public SpringActorProducer(ApplicationContext applicationContext, String actorBeanName) {
		this.applicationContext = applicationContext;
		this.actorBeanName = actorBeanName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Actor> actorClass() {
		// TODO Auto-generated method stub
		return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
	}

	@Override
	public Actor produce() {
		// TODO Auto-generated method stub
		return (Actor) applicationContext.getBean(actorBeanName);
	}

}
