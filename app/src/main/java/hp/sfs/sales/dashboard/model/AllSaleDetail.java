package hp.sfs.sales.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllSaleDetail implements Serializable {
    @SerializedName("operatorId")
    @Expose
    public Long operatorId;
    @SerializedName("cashCollected")
    @Expose
    public Double cashCollected;
    @SerializedName("salesDetails")
    @Expose
    public List<SaleDetail> salesRecords;
    @SerializedName("oilSales")
    @Expose
    public List<OilSale> oilSaleList;
    @SerializedName("onlineDeposit")
    @Expose
    public List<OnlineDeposit> onlineDepositList;
    @SerializedName("creditDetails")
    @Expose
    public List<Credit> creditList;
    @SerializedName("expenses")
    @Expose
    public List<Expense> expenseList;

}
