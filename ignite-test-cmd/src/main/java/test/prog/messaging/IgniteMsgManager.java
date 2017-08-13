package test.prog.messaging;

import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.cluster.ClusterGroup;
import test.prog.IgniteManger;

/**
 * <p>This application demonstrates usages of apache ignite.</p>
 * <p>Implements sample usages of ignite messaging</p>
 *
 * @author Yasitha Thilakaratne
 * @see <a href="https://ignite.apache.org/releases/2.1.0/javadoc/index.html">Apache Ignite</a>
 * @since v-1.0.0
 */
public class IgniteMsgManager extends IgniteManger {

    /**
     * singleton eager loaded instance
     */
    private static final IgniteMsgManager INSTANCE = new IgniteMsgManager();

    /**
     * Messaging instance over this cluster.
     */
    private IgniteMessaging msg;

    /**
     * Messaging instance over given cluster group (in this case, remote nodes).
     */
    private IgniteMessaging rmtMsg;

    /**
     * remote group
     */
    private ClusterGroup remoteClusterGroup;

    public IgniteMsgManager() {
        initMsgs();
    }

    public static IgniteMsgManager getInstance() {
        return INSTANCE;
    }

    private void initMsgs() {
        msg = ignite.message();
        remoteClusterGroup = ignite.cluster().forRemotes();
        rmtMsg = ignite.message(remoteClusterGroup);
    }

    /**
     * sends messages in unordered way.
     * Does not guarantee sent order in receiving end point.
     *
     * @param msgs array of messages to be sent
     */
    public void sendUnorderedMsgs(String... msgs) {

        for (int i = 0; i < msgs.length; i++) {
            msg.send("MyLocalTopic", msgs[i] + "_msg_no_" + i);
        }

        if (!remoteClusterGroup.nodes().isEmpty()) {
            for (int i = 0; i < msgs.length; i++)
                rmtMsg.send("MyRemoteTopic", msgs[i] + "_msg_no_" + i);
        } else {
            System.out.println("Remote cluster group is empty! Remote messages were not sent.");
        }
    }

    /**
     * sends messages in ordered way.
     * Messages are received at receiver end point in sent order.
     *
     * @param msgs array of messages to be sent
     */
    public void sendOrderedMsgs(String... msgs) {
        for (int i = 0; i < msgs.length; i++) {
            msg.sendOrdered("MyLocalTopic", msgs[i] + "_msg_no_" + i, 10000);
        }

        if (!remoteClusterGroup.nodes().isEmpty()) {
            for (int i = 0; i < msgs.length; i++)
                rmtMsg.sendOrdered("MyRemoteTopic", msgs[i] + "_msg_no_" + i, 10000);
        } else {
            System.out.println("Remote cluster group is empty! Remote messages were not sent.");
        }
    }

    /**
     * Register listeners
     */
    public void registerListeners() {
        msg.localListen("MyLocalTopic", (nodeId, msg) -> {
            System.out.println("Received local message [msg=" + msg + ", from=" + nodeId + ']');

            return true;
        });

        // Add listener for messages on all remote nodes.
        rmtMsg.remoteListen("MyRemoteTopic", (nodeId, msg) -> {
            System.out.println("Received remote message [msg=" + msg + ", from=" + nodeId + ']');

            return true; // Return true to continue listening.
        });
    }
}
