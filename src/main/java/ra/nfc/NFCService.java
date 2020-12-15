package ra.nfc;

import ra.common.Client;
import ra.common.Envelope;
import ra.common.messaging.MessageProducer;
import ra.common.network.*;
import ra.common.route.Route;
import ra.common.service.ServiceStatus;
import ra.common.service.ServiceStatusObserver;
import ra.util.Config;
import ra.util.Wait;
import ra.util.tasks.TaskRunner;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * Provides an API for NFC as a Service.
 */
public final class NFCService extends NetworkService {

    private static final Logger LOG = Logger.getLogger(NFCService.class.getName());

    public static final String OPERATION_SEND = "SEND";
    public static final String OPERATION_CHECK_NFC_STATUS = "CHECK_NFC_STATUS";
    public static final String OPERATION_ACTIVE_PEERS_COUNT = "ACTIVE_PEERS_COUNT";

    private File nfcDir;

    private Thread taskRunnerThread;
    private TaskRunner taskRunner;

    public NFCService() {
        super(Network.NFC);
    }

    public NFCService(MessageProducer messageProducer, ServiceStatusObserver observer) {
        super(Network.NFC, messageProducer, observer);
    }

    @Override
    public void handleDocument(Envelope e) {
        super.handleDocument(e);
        Route r = e.getRoute();
        switch(r.getOperation()) {
            case OPERATION_SEND: {
                sendOut(e);
                break;
            }
            case OPERATION_CHECK_NFC_STATUS: {
                checkNFCStatus();
                break;
            }
            case OPERATION_ACTIVE_PEERS_COUNT: {
                Integer count = activePeersCount();
                e.addNVP("activePeersCount", count);
                break;
            }
            default: {
                LOG.warning("Operation ("+r.getOperation()+") not supported. Sending to Dead Letter queue.");
                deadLetter(e);
            }
        }
    }

    /**
     * Sends UTF-8 content to a Destination using I2P.
     * @param envelope Envelope containing Envelope as data.
     *                 To DID must contain base64 encoded I2P destination key.
     * @return boolean was successful
     */
    public Boolean sendOut(Envelope envelope) {
        LOG.fine("Send out Envelope over I2P...");

        return true;
    }

    public File getDirectory() {
        return nfcDir;
    }

    public boolean start(Properties p) {
        LOG.info("Starting NFC Service...");
        updateStatus(ServiceStatus.INITIALIZING);
        LOG.info("Loading NFC properties...");
        try {
            // Load environment variables first
            config = Config.loadAll(p, "nfc.config");
        } catch (Exception e) {
            LOG.severe(e.getLocalizedMessage());
            return false;
        }

        // Config Directory
        nfcDir = getServiceDirectory();
        File nfcConfigDir = new File(nfcDir, "config");
        if(!nfcConfigDir.exists())
            if(!nfcConfigDir.mkdir())
                LOG.warning("Unable to create NFC config directory: " +nfcConfigDir);
        if(nfcConfigDir.exists()) {
            System.setProperty("nfc.dir.config",nfcConfigDir.getAbsolutePath());
            config.setProperty("nfc.dir.config",nfcConfigDir.getAbsolutePath());
        }

        updateStatus(ServiceStatus.STARTING);
        // Start NFC Client


//        if(taskRunner==null) {
//            taskRunner = new TaskRunner(2, 2);
//            taskRunner.setPeriodicity(1000L); // Default check every second
//            CheckNFCStatus statusChecker = new CheckNFCStatus(this, taskRunner);
//            statusChecker.setPeriodicity(30 * 1000L); // Check status every 30 seconds
//            taskRunner.addTask(statusChecker);
//            NFCDiscovery discovery = new NFCDiscovery(this, taskRunner);
//            discovery.setPeriodicity(120 * 1000L); // Set periodicity to 30 seconds longer than I2P network timeout (90 seconds).
//            taskRunner.addTask(discovery);
//        }
//
//        taskRunnerThread = new Thread(taskRunner);
//        taskRunnerThread.setDaemon(true);
//        taskRunnerThread.setName("NFCService-TaskRunnerThread");
//        taskRunnerThread.start();

        updateStatus(ServiceStatus.RUNNING);

        return true;
    }

    @Override
    public boolean pause() {
        return false;
    }

    @Override
    public boolean unpause() {
        return false;
    }

    @Override
    public boolean restart() {

        return true;
    }

    @Override
    public boolean shutdown() {
        updateStatus(ServiceStatus.SHUTTING_DOWN);
        super.shutdown();
        LOG.info("I2P router stopping...");
//        taskRunner.shutdown();
//        if(taskRunnerThread!=null) {
//            taskRunnerThread.interrupt();
//        }
//        taskRunner = null;
//        taskRunnerThread = null;
//        for(NetworkClientSession s : sessions.values()) {
//            s.disconnect();
//            s.close();
//        }
//        sessions.clear();

        updateStatus(ServiceStatus.SHUTDOWN);
        LOG.info("I2P router stopped.");
        return true;
    }

    @Override
    public boolean gracefulShutdown() {
        updateStatus(ServiceStatus.GRACEFULLY_SHUTTING_DOWN);
        LOG.info("I2P router gracefully stopping...");
//        taskRunner.shutdown();
//        if(taskRunnerThread!=null) {
//            taskRunnerThread.interrupt();
//        }
//        taskRunner = null;
//        taskRunnerThread = null;
//        for(NetworkClientSession s : sessions.values()) {
//            s.disconnect();
//            s.close();
//        }
//        sessions.clear();

        updateStatus(ServiceStatus.GRACEFULLY_SHUTDOWN);
        LOG.info("I2P router gracefully stopped.");
        return true;
    }

    public void reportNFCStatus() {
        LOG.info("NFCService status: "+getNetworkState().networkStatus.name());
    }

    public void checkNFCStatus() {

        reportNFCStatus();
    }

    private Integer activePeersCount() {
        return 0;
    }

    public static void main(String[] args) {
        MessageProducer messageProducer = new MessageProducer() {
            @Override
            public boolean send(Envelope envelope) {
                LOG.info(envelope.toJSON());
                return true;
            }

            @Override
            public boolean send(Envelope envelope, Client client) {
                LOG.info(envelope.toJSON());
                return true;
            }

            @Override
            public boolean deadLetter(Envelope envelope) {
                LOG.warning("Dead letter: \n\t"+envelope.toJSON());
                return false;
            }
        };
        NFCService service = new NFCService(messageProducer, null);
        service.start(Config.loadFromMainArgs(args));
        while(true) {
            Wait.aSec(1);
        }
    }

}
