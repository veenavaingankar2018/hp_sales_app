package hp.sfs.sales.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OilSale implements Serializable {
    @SerializedName("product")
    @Expose
    public String product;
    @SerializedName("quantity")
    @Expose
    public Double quantity;
    @SerializedName("amount")
    @Expose
    public Double amount;
}
