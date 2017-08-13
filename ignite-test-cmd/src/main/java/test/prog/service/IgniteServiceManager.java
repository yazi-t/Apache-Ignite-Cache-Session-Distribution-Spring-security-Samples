package test.prog.service;

import org.apache.ignite.IgniteServices;
import test.prog.IgniteManger;
import test.prog.sample.Person;
import test.prog.sample.PersonService;
import test.prog.sample.PersonServiceImpl;

/**
 * <p>This application demonstrates usages of apache ignite.</p>
 * <p>Implements sample usages of ignite distributed service.</p>
 *
 * @author Yasitha Thilakaratne
 * @see <a href="https://ignite.apache.org/releases/2.1.0/javadoc/index.html">Apache Ignite</a>
 * @since v-1.0.0
 */
public class IgniteServiceManager extends IgniteManger {

    /**
     * singleton eager loaded instance
     */
    private static IgniteServiceManager INSTANCE = new IgniteServiceManager();

    private IgniteServiceManager() {
        init();
    }

    public static IgniteServiceManager getInstance() {
        return INSTANCE;
    }

    private void init() {
        // Get an instance of IgniteServices for the cluster group.
        IgniteServices svcs = ignite.services();

        // Deploy per-node singleton. An instance of the service
        // will be deployed on every node within the cluster group.
        svcs.deployNodeSingleton("personService", new PersonServiceImpl(ignite));
    }

    /**
     * calls method on personService
     * @param person object to check
     */
    public void execIsKid(Person person) {
        PersonService service = ignite.services().serviceProxy("personService", PersonService.class, /*not-sticky*/false);
        System.out.println("Is " + person.getName() + " (" + person.getAge() + ") kid? " + service.isKid(person));
    }

    /**
     * calls method on personService
     * @param person object to check
     */
    public void execIsElder(Person person) {
        PersonService service = ignite.services().serviceProxy("personService", PersonService.class, /*not-sticky*/false);
        System.out.println("Is " + person.getName() + " (" + person.getAge() + ") elder? " + service.isElder(person));
    }
}
