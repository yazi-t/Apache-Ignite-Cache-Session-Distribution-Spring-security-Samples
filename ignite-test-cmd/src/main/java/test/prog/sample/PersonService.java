package test.prog.sample;

/**
 * <p>This application demonstrates usages of apache ignite.</p>
 * <p>Sample interface to service</p>
 *
 * @author Yasitha Thilakaratne
 * @see <a href="https://ignite.apache.org/releases/2.1.0/javadoc/index.html">Apache Ignite</a>
 * @since v-1.0.0
 */
public interface PersonService {

    boolean isKid(Person person);

    boolean isElder(Person person);
}
