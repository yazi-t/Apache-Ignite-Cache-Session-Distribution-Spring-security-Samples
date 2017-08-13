package test.prog;

import org.apache.ignite.*;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.binary.BinaryObjectBuilder;
import org.apache.ignite.cache.CacheEntryProcessor;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.spi.discovery.tcp.internal.TcpDiscoveryNode;
import test.prog.sample.Person;

import javax.cache.Cache;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import javax.enterprise.inject.Default;
import java.util.*;

/**
 * <p>This application demonstrates usages of apache ignite.</p>
 * <p>Implements core ignite instance and start up</p>
 *
 * @author Yasitha Thilakaratne
 * @see <a href="https://ignite.apache.org/releases/2.1.0/javadoc/index.html">Apache Ignite</a>
 * @since v-1.0.0
 */
@Default
public class IgniteManger implements AutoCloseable {

    protected static final String CONF = "example-ignite.xml";

    protected static final String CACHE_NAME = "myCache";

    private static final IgniteManger INSTANCE = new IgniteManger();

    protected static Ignite ignite = Ignition.start(CONF);

    public static IgniteManger getInstance() {
        return INSTANCE;
    }

    public void printClusterNodes() {
        IgniteCluster cluster = ignite.cluster();
        Collection<ClusterNode> clusterNodes = cluster.topology(cluster.topologyVersion());

        if (clusterNodes.isEmpty())
            System.out.println("Cluster nodes are not available.");

        clusterNodes.forEach(clusterNode -> {
            System.out.println(clusterNode.id() + " on server -> " + clusterNode.addresses());
            if (clusterNode instanceof TcpDiscoveryNode)
                System.out.println("+ port -> " + ((TcpDiscoveryNode) clusterNode).discoveryPort());
        });
    }

    public void printMemoryMetrics() {
        // Get the metrics of all the memory regions defined on the node.
        Collection<MemoryMetrics> regionsMetrics = ignite.memoryMetrics();

        // Print some of the metrics' probes for all the regions.
        for (MemoryMetrics metrics : regionsMetrics) {
            System.out.println(">>> Memory Region Name: " + metrics.getName());
            System.out.println(">>> Allocation Rate: " + metrics.getAllocationRate());
            System.out.println(">>> Fill Factor: " + metrics.getPagesFillFactor());
        }
    }

    @Override
    public void close() {
        try {
            ignite.close();
        } catch (Exception e) {
            System.out.println(e.getMessage() + " Exception occurred.");
        }
    }
}
