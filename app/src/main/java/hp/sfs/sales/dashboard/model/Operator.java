package hp.sfs.sales.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Operator implements Serializable {
    @SerializedName("id")
    @Expose(serialize = false)
    public Long operator_id;
    @SerializedName("name")
    @Expose
    public String operator_name;
    @SerializedName("isValid")
    @Expose(serialize = false)
    public boolean isValid = true;
}
