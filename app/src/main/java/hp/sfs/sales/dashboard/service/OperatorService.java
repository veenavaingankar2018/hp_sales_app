package hp.sfs.sales.dashboard.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import hp.sfs.sales.dashboard.apis.Apis;
import hp.sfs.sales.dashboard.events.MessageEvent;
import hp.sfs.sales.dashboard.events.OperatorDownloadEvent;
import hp.sfs.sales.dashboard.model.Operator;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class OperatorService extends IntentService {
    private static final String ACTION_ADD_OPERATOR = "ACTION_ADD_OPERATOR";
    private static final String ACTION_DOWNLOAD_ALL_OPERATOR = "ACTION_DOWNLOAD_ALL_OPERATOR";
    private static final String ACTION_UPDATE_OPERATOR = "ACTION_UPDATE_OPERATOR";
    private static final String OPERATOR_NAME = "operator_name";
    private static final String OPERATOR_ID = "operator_id";
    private static final String OPERATOR_STATUS = "operator_status";
    OkHttpClient client = null;

    public OperatorService() {
        super("OperatorService");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        client = builder.build();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ADD_OPERATOR.equals(action)) {
                String operator_name = intent.getStringExtra(OPERATOR_NAME);
                handleSaveOperator(operator_name);
            } else if (ACTION_DOWNLOAD_ALL_OPERATOR.equals(action)) {
                handleRequestToDownloadOperator();
            } else if (ACTION_UPDATE_OPERATOR.equals(action)) {
                String operator_name = intent.getStringExtra(OPERATOR_NAME);
                Long operator_id = intent.getLongExtra(OPERATOR_ID, 0L);
                boolean operator_status = intent.getBooleanExtra(OPERATOR_STATUS, true);
                handleUpdateOperator(operator_name, operator_id, operator_status);
            }
        }
    }

    private void handleRequestToDownloadOperator() {
        Request request = new Request.Builder()
                .url(Apis.operatorApi + "/get")
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
            List<Operator> list = Arrays.asList(gson.fromJson(body, Operator[].class));
            EventBus.getDefault().post(new OperatorDownloadEvent(true, list));
        }
    }

    private void handleSaveOperator(String operator_name) {
        Operator operator = new Operator();
        operator.operator_name = operator_name;

        Gson gson = new Gson();
        String json = gson.toJson(operator);
        Request request = null;
        System.out.println("Operator-Json =" + json);

        request = new Request.Builder()
                .url(Apis.operatorApi + "/save")
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

        System.out.println("Body in save operator:- " + body);
        if (Optional.ofNullable(body).isPresent() && !body.contains("error")) {
            EventBus.getDefault().post(new MessageEvent(true));
        }
    }

    private void handleUpdateOperator(String operator_name, Long operator_id, Boolean status) {
        Operator operator = new Operator();
        operator.operator_name = operator_name;
        operator.operator_id = operator_id;
        operator.isValid = status;

        Gson gson = new Gson();
        String json = gson.toJson(operator);
        Request request = null;
        System.out.println("Operator-Json =" + json);

        HttpUrl url = HttpUrl.parse(Apis.operatorApi + "/update").newBuilder()
                .addQueryParameter("id", operator_id.toString())
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

        System.out.println("Body in update operator:- " + body);
        if (Optional.ofNullable(body).isPresent() && !body.contains("error")) {
            EventBus.getDefault().post(new MessageEvent(true));
        }
    }

    public static void saveOperator(Context context, String operatorName) {
        if (TextUtils.isEmpty(operatorName)) {
            return;
        }

        Intent intent = new Intent(context, OperatorService.class);
        intent.setAction(ACTION_ADD_OPERATOR);
        intent.putExtra(OPERATOR_NAME, operatorName);
        context.startService(intent);
    }

    public static void loadOperators(Context context) {
        Intent intent = new Intent(context, OperatorService.class);
        intent.setAction(ACTION_DOWNLOAD_ALL_OPERATOR);
        context.startService(intent);
    }

    public static void updateOperator(Context context, Operator operator) {
        if (!Optional.ofNullable(operator).isPresent()) {
            return;
        }

        Intent intent = new Intent(context, OperatorService.class);
        intent.setAction(ACTION_UPDATE_OPERATOR);
        intent.putExtra(OPERATOR_NAME, operator.operator_name);
        intent.putExtra(OPERATOR_ID, operator.operator_id);
        intent.putExtra(OPERATOR_STATUS, operator.isValid);
        context.startService(intent);
    }
}