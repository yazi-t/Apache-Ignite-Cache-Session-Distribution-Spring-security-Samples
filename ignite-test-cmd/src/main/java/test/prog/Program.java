package test.prog;

import test.prog.cache.IgniteCacheManager;
import test.prog.compute.IgniteComputeManager;
import test.prog.filesystem.IgniteFSManager;
import test.prog.messaging.IgniteMsgManager;
import test.prog.sample.Person;
import test.prog.service.IgniteServiceManager;

import java.util.List;
import java.util.Random;

/**
 * <p>This application demonstrates usages of apache ignite.</p>
 * <p>Implements execution flow</p>
 *
 * @author Yasitha Thilakaratne
 * @see <a href="https://ignite.apache.org/releases/2.1.0/javadoc/index.html">Apache Ignite</a>
 * @since v-1.0.0
 */
public class Program {

    public static void main(String[] args) {

        try (IgniteManger igniteManger = IgniteManger.getInstance()) {

            IgniteCacheManager cacheManager = IgniteCacheManager.getInstance();
            initiateCache(cacheManager);
            igniteManger.printClusterNodes();
            igniteManger.printMemoryMetrics();

            //cache based examples
            List<Person> personScan = cacheManager.getScanQueryResult(25);
            System.out.println("Scan query result for age > 25:");
            personScan.forEach(p -> System.out.println("Person found -> " + p.getName() + " age: " + p.getAge()));

            List<Person> personsSQL = cacheManager.getSQLQueryResult("John");
            System.out.println("SQL query result for name = 'John':");
            personsSQL.forEach(p -> System.out.println("Person found -> " + p.getName()));

            List<Person> personsAll = cacheManager.getAllFromCache();

            IgniteComputeManager computeManager = IgniteComputeManager.getInstance();

            //compute based examples
            print_ln_br();
            System.out.println("Executor compute job::");
            int totalCost = computeManager.totalSalaryCostWithAllowance_executorService(personsAll);
            System.out.println("Calculated cost: " + totalCost);

            print_ln_br();
            System.out.println("Map reduce split compute job::");
            int totalCost2 = computeManager.totalSalaryCostWithAllowance_mapReduce_Split(personsAll);
            System.out.println("Calculated cost: " + totalCost2);

            print_ln_br();
            System.out.println("Map reduce compute job::");
            int totalCost3 = computeManager.totalSalaryCostWithAllowance_mapReduce_Split(personsAll);
            System.out.println("Calculated cost: " + totalCost3);

            //print_ln_br();
            //computeManager.startMetricScheduler();

            //service based examples
            print_ln_br();
            System.out.println("Ignite services::");
            IgniteServiceManager serviceManager = IgniteServiceManager.getInstance();
            serviceManager.execIsKid(cacheManager.getFromCache(1));
            serviceManager.execIsElder(cacheManager.getFromCache(2));

            //file system based examples
            print_ln_br();
            System.out.println("Ignite file system::");
            IgniteFSManager fsManager = IgniteFSManager.getInstance();
            fsManager.createDir();
            fsManager.createFile();
            fsManager.readFile();
            fsManager.deleteDir();

            //messaging based examples
            print_ln_br();
            System.out.println("Ignite messaging::");
            IgniteMsgManager msgManager = IgniteMsgManager.getInstance();
            msgManager.registerListeners();
            msgManager.sendOrderedMsgs("message 1 ", "message 2 ", "message 3 ");
            msgManager.sendUnorderedMsgs("message 1 ", "message 2 ", "message 3 ");
        }
    }

    private static void initiateCache(IgniteCacheManager manger) {
        Random random = new Random();
        for (int i = 1; i <= 20; i++) {
            char c = 100;
            Person p = new Person(i, "name_" + c + '_' + i, 10 + i, "address_" + i, random.nextInt(100000) + 100000);
            manger.putToCache(i, p);
        }
        Person p1 = new Person(21, "John", 30, "NewYork", random.nextInt(100000) + 100000);
        manger.putToCache(21, p1);
        Person p2 = new Person(21, "Sarah", 30, "Boston", random.nextInt(100000) + 100000);
        manger.putToCache(22, p2);
    }

    private static void print_ln_br() {
        System.out.println("");
    }
}
