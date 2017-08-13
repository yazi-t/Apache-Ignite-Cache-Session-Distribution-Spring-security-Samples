package test.prog.sample;

import org.apache.ignite.Ignite;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

/**
 * <p>This application demonstrates usages of apache ignite.</p>
 * <p>Sample service implementation</p>
 *
 * @author Yasitha Thilakaratne
 * @see <a href="https://ignite.apache.org/releases/2.1.0/javadoc/index.html">Apache Ignite</a>
 * @since v-1.0.0
 */
public class PersonServiceImpl implements PersonService, Service {

    private Ignite ignite;

    public PersonServiceImpl(Ignite ignite) {
        this.ignite = ignite;
    }

    @Override
    public boolean isKid(Person person) {
        return person.getAge() < 18;
    }

    @Override
    public boolean isElder(Person person) {
        return person.getAge() > 65;
    }

    @Override
    public void cancel(ServiceContext serviceContext) {
        System.out.println("Service was cancelled: " + serviceContext.name());
    }

    @Override
    public void init(ServiceContext serviceContext) throws Exception {
        System.out.println("Service was initialized: " + serviceContext.name());
    }

    @Override
    public void execute(ServiceContext serviceContext) throws Exception {

    }
}
