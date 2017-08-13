package test.prog.datastructure;

import org.apache.ignite.*;
import org.apache.ignite.configuration.CollectionConfiguration;
import test.prog.IgniteManger;
import test.prog.sample.Person;

/**
 * <p>This application demonstrates usages of apache ignite.</p>
 * <p>Implements sample usages of ignite distributed data structures</p>
 * <ul>
 *     <li>IgniteQueue</li>
 *     <li>IgniteSet</li>
 *     <li>IgniteAtomicLong</li>
 *     <li>IgniteAtomicReference</li>
 *     <li>IgniteCountDownLatch</li>
 *     <li>IgniteSemaphore</li>
 * </ul>
 *
 * @author Yasitha Thilakaratne
 * @see <a href="https://ignite.apache.org/releases/2.1.0/javadoc/index.html">Apache Ignite</a>
 * @see java.util.concurrent.BlockingQueue
 * @see java.util.Set
 * @see java.util.concurrent.atomic.AtomicLong
 * @see java.util.concurrent.atomic.AtomicReference
 * @see java.util.concurrent.Semaphore
 * @since v-1.0.0
 */
public class IgniteDataStructureManager extends IgniteManger {

    /**
     * singleton eager loaded instance
     */
    private static final IgniteDataStructureManager INSTANCE = new IgniteDataStructureManager();

    /**
     *  implementation of java.util.concurrent.BlockingQueue
     */
    private IgniteQueue<Person> queue;

    /**
     * implementation of @see java.util.Set
     */
    private IgniteSet<Person> set;

    /**
     * implementation of java.util.concurrent.atomic.AtomicLong
     */
    private IgniteAtomicLong atomicLong;

    /**
     * implementation of java.util.concurrent.atomic.AtomicReference
     */
    private IgniteAtomicReference<Person> atomicReference;

    /**
     * implementation of java.util.concurrent.CountDownLatch
     */
    private IgniteCountDownLatch latch;

    /**
     * atomic sequence id generator with #atomicSequenceReserveSize
     */
    private IgniteAtomicSequence seq;

    /**
     * similar implementation of java.util.concurrent.Semaphore
     */
    private IgniteSemaphore semaphore;

    public IgniteDataStructureManager() {
        initQueue();
        initSet();
        initAtomicLong();
        initCountDownLatch();
        initSequence();
        initSemaphore();
    }

    public static IgniteDataStructureManager getInstance() {
        return INSTANCE;
    }

    private void initQueue() {
        queue = ignite.queue(
                "personQueue", // Queue name.
                0,          // Queue capacity. 0 for unbounded queue.
                null         // Collection configuration.
        );
    }

    private void initSet() {
        CollectionConfiguration colCfg = new CollectionConfiguration();
        colCfg.setCollocated(true);
        // Create collocated set.
        set = ignite.set("personSet", colCfg);
    }

    private void initAtomicLong() {
        atomicLong = ignite.atomicLong(
                "atomicLong", // Atomic long name.
                0,                // Initial value.
                false            // Create if it does not exist.
        );
    }

    public void initAtomicReference(Person person) {
        atomicReference = ignite.atomicReference("atomicPerson",
                person,
                false);
    }

    private void initCountDownLatch() {
        latch = ignite.countDownLatch("latchName", 10, false, true);
    }

    private void initSequence() {
        seq = ignite.atomicSequence("seqName", 0, true);
    }

    private void initSemaphore() {
        semaphore = ignite.semaphore(
                "semaphoreName", // Distributed semaphore name.
                20,        // Number of permits.
                true,      // Release acquired permits if node, that owned them, left topology.
                true       // Create if it doesn't exist.
        );
    }

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately.
     *
     * @param person object to add
     */
    public void addToQueue(Person person) {
        queue.add(person);
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary
     * until an element becomes available.
     *
     * @return top object of queue
     */
    public Person takeFromQueue() {
        return queue.take();
    }

    /**
     * Adds the specified element to this set if it is not already present
     * (optional operation).  More formally, adds the specified element
     * <tt>e</tt> to this set if the set contains no element <tt>e2</tt>
     * such that
     * <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>.
     * If this set already contains the element, the call leaves the set
     * unchanged and returns <tt>false</tt>.  In combination with the
     * restriction on constructors, this ensures that sets never contain
     * duplicate elements.
     *
     * @param person object to add
     */
    public void addToSet(Person person) {
        set.add(person);
    }

    /**
     * Returns <tt>true</tt> if this set contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this set
     * contains an element <tt>e</tt> such that.
     *
     * @param person
     * @return
     */
    public boolean containsInSet(Person person) {
        return set.contains(person);
    }

    /**
     * Decrements the count of the latch, releasing all waiting threads till
     * the count reaches zero.
     */
    public void countDown() {
        // Execute jobs.
        for (int i = 0; i < 10; i++)
            // Execute a job on some remote cluster node.
            ignite.compute().run(() -> {
                int newCnt = latch.countDown();

                System.out.println("Counted down: newCnt=" + newCnt);
            });

        // Wait for all jobs to complete.
        latch.await();
    }

    /**
     * @return distributed sequence next value
     */
    public long getNewSequenceValue() {
        return seq.incrementAndGet();
    }

    /**
     * Locks code block using semaphore
     */
    public void executeSynchronizedPrintTask() {
        // Acquires a permit, blocking until it's available.
        semaphore.acquire();

        try {
            // Semaphore permit is acquired. Execute a distributed task.
            ignite.compute().run(() -> {
                System.out.println("Executed on:" + ignite.cluster().localNode().id());

                // Additional logic.
            });
        }
        finally {
            // Releases a permit, returning it to the semaphore.
            semaphore.release();
        }
    }
}
