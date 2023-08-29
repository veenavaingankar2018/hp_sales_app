package hp.sfs.sales.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OnlineDeposit implements Serializable {
    @SerializedName("mode")
    @Expose
    public String mode;

    @SerializedName("amount")
    @Expose
    public Double amount;
    @SerializedName("remark")
    @Expose
    public String remark;
}
