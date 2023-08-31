package hp.sfs.sales.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Expense implements Serializable {
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("amount")
    @Expose
    public Double amount;
}
