package hp.sfs.sales.dashboard.events;

import java.util.List;

import hp.sfs.sales.dashboard.model.Operator;

public class OperatorDownloadEvent {
    public boolean success = false;
    public List<Operator> operatorList;

    public OperatorDownloadEvent(boolean success, List<Operator> operatorList){
        this.success = success;
        this.operatorList = operatorList;
    }
}
