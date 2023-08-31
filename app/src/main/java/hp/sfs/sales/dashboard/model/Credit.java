package hp.sfs.sales.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Credit implements Serializable {
    @SerializedName("transactionType")
    @Expose
    public String transactionType;
    @SerializedName("driverName")
    @Expose
    public String driverName;
    @SerializedName("vehicleNumber")
    @Expose
    public String vehicleNumber;

    @SerializedName("product")
    @Expose
    public String product;

    @SerializedName("amount")
    @Expose
    public Double amount;
}
