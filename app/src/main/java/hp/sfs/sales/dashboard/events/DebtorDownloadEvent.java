package hp.sfs.sales.dashboard.events;

import java.util.List;

import hp.sfs.sales.dashboard.model.Debtor;
import hp.sfs.sales.dashboard.model.Operator;

public class DebtorDownloadEvent {
    public boolean success = false;
    public List<Debtor> debtorList;

    public DebtorDownloadEvent(boolean success, List<Debtor> debtorList){
        this.success = success;
        this.debtorList = debtorList;
    }
}
