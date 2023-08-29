package hp.sfs.sales.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SaleDetail implements Serializable {

    @SerializedName("startTime")
    @Expose
    public String start_time;
    @SerializedName("endTime")
    @Expose
    public String end_time;
    @SerializedName("product")
    @Expose
    public String product;
    @SerializedName("rate")
    @Expose
    public Double rate;
    @SerializedName("startReading")
    @Expose
    public Double start_reading;
    @SerializedName("endReading")
    @Expose
    public Double end_reading;
    @SerializedName("salesVolume")
    @Expose
    public Double sales_volume;
    @SerializedName("pumpTestVolume")
    @Expose
    public Double pump_test_volume;
    @SerializedName("amount")
    @Expose
    public Double amount;
}
