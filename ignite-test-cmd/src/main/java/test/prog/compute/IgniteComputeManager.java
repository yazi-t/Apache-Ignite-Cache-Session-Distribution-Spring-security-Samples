package test.prog.compute;

import org.apache.ignite.IgniteCompute;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.*;
import org.apache.ignite.lang.IgniteCallable;
import test.prog.IgniteManger;
import test.prog.sample.Person;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * <p>This application demonstrates usages of apache ignite.</p>
 * <p>Implements sample usages of ignite computing</p>
 *
 * @author Yasitha Thilakaratne
 * @see <a href="https://ignite.apache.org/releases/2.1.0/javadoc/index.html">Apache Ignite</a>
 * @since v-1.0.0
 */
public class IgniteComputeManager extends IgniteManger {

    /**
     * singleton eager loaded instance
     */
    private static final IgniteComputeManager INSTANCE = new IgniteComputeManager();

    public static IgniteComputeManager getInstance() {
        return INSTANCE;
    }

    /**
     * counts letters of provided string in distributed manner.
     * Breaks string in to words and asign nodes to process each word.
     * This method extracted from official ignite documentation.
     * @param s Phrase to count letters
     * @return number of letters in phrase
     */
    public int letterCount(String s) {
        Collection<IgniteCallable<Integer>> calls = new ArrayList<>();

        // Iterate through all words in the sentence and create callable jobs.
        for (String word : s.split(" "))
            calls.add(word::length);

        // Execute collection of callables on the cluster.
        Collection<Integer> res = ignite.compute().call(calls);

        // Add all the word lengths received from cluster nodes.
        return res.stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Using ignite implementation of distributed ExecutorService for
     * execute simple salary calculating function.
     * @param persons list of persons
     * @return total cost
     */
    public int totalSalaryCostWithAllowance_executorService(List<Person> persons) {
        ExecutorService executor = ignite.executorService();

        List<Integer> calculatedSalaries = new ArrayList<>(persons.size());
        persons.forEach(person -> {
            Future<Integer> future = executor.submit(new IgniteCallable<Integer>() {
                @Override
                public Integer call() {
                    try {
                        int salary = person.getSalary();
                        int finalSalary = salary + salary * 10 / 100;
                        System.out.println("Calculated salary for " + salary + " is: " + finalSalary);
                        return finalSalary;
                    } catch (Exception e) {
                        return 0;
                    }
                }
            });
            try {
                calculatedSalaries.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                calculatedSalaries.add(0);
            }
        });
        return calculatedSalaries.stream().mapToInt(value -> value).sum();
    }

    /**
     * Using ignite implementation of map reduce split abstraction for
     * distributed execution of simple salary calculating function.
     * @param persons list of persons
     * @return total cost
     */
    public int totalSalaryCostWithAllowance_mapReduce_Split(List<Person> persons) {
        IgniteCompute compute = ignite.compute();

        // Execute task on the cluster and wait for its completion.
        return compute.execute(SalaryCountTask_Split.class, persons);
    }

    /**
     * Using ignite implementation of map reduce algorithm to
     * distributed execution of simple salary calculating function.
     * @param persons list of persons
     * @return total cost
     */
    public int totalSalaryCostWithAllowance_mapReduce(List<Person> persons) {
        IgniteCompute compute = ignite.compute();

        // Execute task on the cluster and wait for its completion.
        return compute.execute(SalaryCountTask.class, persons);
    }

    /**
     * Job class for split
     */
    private static class SalaryCountTask_Split extends ComputeTaskSplitAdapter<List<Person>, Integer> {
        // 1. Splits the received string into to words
        // 2. Creates a child job for each word
        // 3. Sends created jobs to other nodes for processing.
        @Override
        public List<ComputeJob> split(int gridSize, List<Person> arg) {
            List<ComputeJob> jobs = new ArrayList<>(arg.size());

            for (final Person person: arg) {
                jobs.add(new ComputeJobAdapter() {
                    @Override
                    public Object execute() {
                        int salary = person.getSalary();
                        int finalSalary = salary + salary * 10 / 100;
                        System.out.println("Calculated salary for " + salary + " is: " + finalSalary);
                        return finalSalary;
                    }
                });
            }

            return jobs;
        }

        @Override
        public Integer reduce(List<ComputeJobResult> results) {
            int sum = 0;

            for (ComputeJobResult res : results)
                sum += res.<Integer>getData();

            return sum;
        }
    }

    /**
     * Job class for map reduce
     */
    private static class SalaryCountTask extends ComputeTaskAdapter<List<Person>, Integer> {
        // 1. Splits the received string into to words
        // 2. Creates a child job for each word
        // 3. Sends created jobs to other nodes for processing.
        @Override
        public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> subgrid, List<Person> arg) {
            Map<ComputeJob, ClusterNode> map = new HashMap<>(arg.size());

            Iterator<ClusterNode> it = subgrid.iterator();

            for (final Person person: arg) {
                // If we used all nodes, restart the iterator.
                if (!it.hasNext())
                    it = subgrid.iterator();

                ClusterNode node = it.next();

                map.put(new ComputeJobAdapter() {
                    @Override
                    public Object execute() {
                        int salary = person.getSalary();
                        int finalSalary = salary + salary * 10 / 100;
                        System.out.println("Calculated salary for " + salary + " is: " + finalSalary);
                        return finalSalary;
                    }
                }, node);
            }

            return map;
        }

        @Override
        public Integer reduce(List<ComputeJobResult> results) {
            int sum = 0;

            for (ComputeJobResult res : results)
                sum += res.<Integer>getData();

            return sum;
        }
    }

    public void startMetricScheduler() {
        ignite.compute().broadcast(new IgniteCallable<Object>() {
            @Override
            public Object call() throws Exception {
                ignite.scheduler().scheduleLocal(new Runnable() {
                    @Override public void run() {
                        printMemoryMetrics();
                    }
                }, "0 0   *");
                return null;
            }
        });
    }
}
