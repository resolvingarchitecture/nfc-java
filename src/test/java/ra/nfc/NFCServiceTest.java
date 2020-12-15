package ra.nfc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import ra.common.network.NetworkPeer;

import java.util.Properties;
import java.util.logging.Logger;

public class NFCServiceTest {

    private static final Logger LOG = Logger.getLogger(NFCServiceTest.class.getName());

    private static NetworkPeer orig;
    private static NetworkPeer dest;
    private static MockProducerClient mockProducerClient;
    private static NFCService service;
    private static Properties props;
    private static boolean serviceRunning = false;

    @BeforeClass
    public static void init() {
        LOG.info("Init...");
        props = new Properties();
        props.put("ra.tor.privkey.destroy", "true");
        mockProducerClient = new MockProducerClient();
        service = new NFCService(mockProducerClient, null);
//        service.start(props);
//        Wait.aMin(2); // Wait 2 minutes for NFC network to warm up
    }

    @AfterClass
    public static void tearDown() {
        LOG.info("Teardown...");
        service.gracefulShutdown();
    }

//    @Test
//    public void initializedTest() {
//        Assert.assertTrue(serviceRunning);
//    }

    /**
     * Send an op message to the nfc peer and verify op reply.
     */
//    @Test
//    public void peer2Peer() {
//        Envelope e = Envelope.documentFactory();
//        e.addExternalRoute(NFCService.class, NFCService.OPERATION_SEND, orig, dest);
//        e.mark("op");
//        // Ratchet route for testing
////        e.ratchet();
//        service.handleDocument(e);
//        Assert.assertTrue("{op=200}".equals(e.getContent()));
//    }
}
