package hp.sfs.sales.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Debtor {
    @SerializedName("id")
    @Expose(serialize = false)
    public String debtor_id;
    @SerializedName("name")
    @Expose
    public String debtor_name;
    @SerializedName("isValid")
    @Expose(serialize = false)
    public boolean isValid = true;
}
