package hp.sfs.sales.dashboard.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import hp.sfs.sales.dashboard.apis.Apis;
import hp.sfs.sales.dashboard.events.MessageEvent;
import hp.sfs.sales.dashboard.model.AllSaleDetail;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SaleDetailService extends IntentService {
    /**
     * @param name
     * @deprecated
     */
    private OkHttpClient client;
    private static final String SALE_DETAIL = "sale_detail";
    private static final String ACTION_ADD_OPERATOR = "ACTION_ADD_OPERATOR";

    public SaleDetailService() {
        super(SaleDetailService.class.getSimpleName());
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        client = builder.build();
    }

    public SaleDetailService(String name) {
        super(name);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        client = builder.build();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ADD_OPERATOR.equals(action)) {
                AllSaleDetail allSaleDetail = (AllSaleDetail) intent.getSerializableExtra(SALE_DETAIL);
                handleSaveSaleDetail(allSaleDetail);
            }
        }
    }

    private void handleSaveSaleDetail(AllSaleDetail allSaleDetail) {
        Gson gson = new Gson();
        String json = gson.toJson(allSaleDetail);
        Request request = null;
        System.out.println("SaleDetailService =" + json);

        request = new Request.Builder()
                .url(Apis.salesApi + "/save")
                .post(RequestBody.create(MediaType.parse("application/json"), json))
                .build();

        Response response = null;
        String body = null;
        try {
            response = client.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Body in save sale detail:- " + body);

        System.out.println("Body in save operator:- " + body);
        if (Optional.ofNullable(body).isPresent() && !body.contains("Failed")) {
            EventBus.getDefault().post(new MessageEvent(true));
        }
    }

    public static void saveSaleDetail(Context context, AllSaleDetail allSaleDetail) {
        if (!Optional.ofNullable(allSaleDetail).isPresent()) {
            return;
        }
        Intent intent = new Intent(context, SaleDetailService.class);
        intent.setAction(ACTION_ADD_OPERATOR);
        intent.putExtra(SALE_DETAIL, allSaleDetail);
        context.startService(intent);
    }
}
