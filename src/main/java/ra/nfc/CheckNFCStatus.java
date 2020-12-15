package ra.nfc;

import ra.util.tasks.BaseTask;
import ra.util.tasks.TaskRunner;

class CheckNFCStatus extends BaseTask {

    private NFCService service;

    public CheckNFCStatus(NFCService service, TaskRunner taskRunner) {
        super(CheckNFCStatus.class.getSimpleName(), taskRunner);
        this.service = service;
    }

    @Override
    public Boolean execute() {
        service.checkNFCStatus();
        return true;
    }
}
