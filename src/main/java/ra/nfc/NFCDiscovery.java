package ra.nfc;

import ra.util.tasks.BaseTask;
import ra.util.tasks.TaskRunner;

import java.util.logging.Logger;

public class NFCDiscovery extends BaseTask {

    private static final Logger LOG = Logger.getLogger(NFCDiscovery.class.getName());

    private NFCService service;

    public NFCDiscovery(NFCService service, TaskRunner taskRunner) {
        super(NFCDiscovery.class.getSimpleName(), taskRunner);
        this.service = service;
    }

    @Override
    public Boolean execute() {

        return true;
    }


}
