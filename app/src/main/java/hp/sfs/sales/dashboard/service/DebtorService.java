package hp.sfs.sales.dashboard.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import hp.sfs.sales.dashboard.apis.Apis;
import hp.sfs.sales.dashboard.events.DebtorDownloadEvent;
import hp.sfs.sales.dashboard.events.MessageEvent;
import hp.sfs.sales.dashboard.model.Debtor;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DebtorService extends IntentService {
    private static final String ACTION_ADD_DEBTOR = "ACTION_ADD_DEBTOR";
    private static final String ACTION_DOWNLOAD_ALL_DEBTOR = "ACTION_DOWNLOAD_ALL_DEBTOR";
    private static final String ACTION_UPDATE_DEBTOR = "ACTION_UPDATE_DEBTOR";
    private static final String DEBTOR_NAME = "debtor_name";
    private static final String DEBTOR_ID = "debtor_id";
    private static final String DEBTOR_STATUS = "debtor_status";
    OkHttpClient client = null;
    public DebtorService() {
        super("DebtorService");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        client = builder.build();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ADD_DEBTOR.equals(action)) {
                String debtor_name = intent.getStringExtra(DEBTOR_NAME);
                handleSaveDebtor(debtor_name);
            } else if (ACTION_DOWNLOAD_ALL_DEBTOR.equals(action)) {
                handleRequestToDownloadDebtor();
            } else if (ACTION_UPDATE_DEBTOR.equals(action)) {
                String debtor_name = intent.getStringExtra(DEBTOR_NAME);
                String debtor_id = intent.getStringExtra(DEBTOR_ID);
                boolean dabtor_status = intent.getBooleanExtra(DEBTOR_STATUS, true);
                handleUpdateDebtor(debtor_name, debtor_id, dabtor_status);
            }
        }
    }

    private void handleRequestToDownloadDebtor() {
        Request request = new Request.Builder()
                .url(Apis.debtorApi + "/get")
                .get()
                .build();

        System.out.println("request:- " + request);
        Response response = null;
        String body = null;
        try {
            response = client.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Body in get operation:- " + body);
        if (Optional.ofNullable(body).isPresent() && !body.contains("error")) {
            Gson gson = new Gson();
            List<Debtor> list = Arrays.asList(gson.fromJson(body, Debtor[].class));
            EventBus.getDefault().post(new DebtorDownloadEvent(true, list));
        }
    }

    private void handleSaveDebtor(String debtor_name) {
        Debtor Debtor = new Debtor();
        Debtor.debtor_name = debtor_name;

        Gson gson = new Gson();
        String json = gson.toJson(Debtor);
        Request request = null;
        System.out.println("Debtor-Json =" + json);

        request = new Request.Builder()
                .url(Apis.debtorApi + "/save")
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

        System.out.println("Body in save Debtor:- " + body);
        if (Optional.ofNullable(body).isPresent() && !body.contains("error")) {
            EventBus.getDefault().post(new MessageEvent(true));
        }
    }

    private void handleUpdateDebtor(String debtor_name, String debtor_id, Boolean status) {
        Debtor Debtor = new Debtor();
        Debtor.debtor_name = debtor_name;
        Debtor.debtor_id = debtor_id;
        Debtor.isValid = status;

        Gson gson = new Gson();
        String json = gson.toJson(Debtor);
        Request request = null;
        System.out.println("Debtor-Json =" + json);

        HttpUrl url = HttpUrl.parse(Apis.debtorApi + "/update").newBuilder()
                .addQueryParameter("id", debtor_id)
                .build();

        request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(MediaType.parse("application/json"), json))
                .build();

        Response response = null;
        String body = null;
        try {
            response = client.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Body in update Debtor:- " + body);
        if (!body.contains("error")) {
            EventBus.getDefault().post(new MessageEvent(true));
        }
    }

    public static void saveDebtor(Context context, String debtorName) {
        if (TextUtils.isEmpty(debtorName)) {
            return;
        }

        Intent intent = new Intent(context, DebtorService.class);
        intent.setAction(ACTION_ADD_DEBTOR);
        intent.putExtra(DEBTOR_NAME, debtorName);
        context.startService(intent);
    }

    public static void loadDebtors(Context context) {
        Intent intent = new Intent(context, DebtorService.class);
        intent.setAction(ACTION_DOWNLOAD_ALL_DEBTOR);
        context.startService(intent);
    }

    public static void updateDebtor(Context context, Debtor Debtor) {
        if (!Optional.ofNullable(Debtor).isPresent()) {
            return;
        }

        Intent intent = new Intent(context, DebtorService.class);
        intent.setAction(ACTION_UPDATE_DEBTOR);
        intent.putExtra(DEBTOR_NAME, Debtor.debtor_name);
        intent.putExtra(DEBTOR_ID, Debtor.debtor_id);
        intent.putExtra(DEBTOR_STATUS, Debtor.isValid);
        context.startService(intent);
    }
}
