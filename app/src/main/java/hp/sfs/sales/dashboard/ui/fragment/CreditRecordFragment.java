package hp.sfs.sales.dashboard.ui.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.adapter.CreditAdapter;
import hp.sfs.sales.dashboard.enums.CreditProduct;
import hp.sfs.sales.dashboard.enums.Product;
import hp.sfs.sales.dashboard.enums.TransactionType;
import hp.sfs.sales.dashboard.events.MessageEvent;
import hp.sfs.sales.dashboard.model.Credit;

public class CreditRecordFragment extends Fragment implements SaleFragment.OnSaveClickListener {
    View view;
    private RecyclerView recyclerView;
    private CardView credit_add_btn;
    private List<Credit> creditList = new ArrayList<>();
    private CreditAdapter creditAdapter;
    private String selected_transaction_type;
    private String selected_product;

    public CreditRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.fragment_credit_record, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.credit_list);
            credit_add_btn = (CardView) view.findViewById(R.id.credit_add_btn);

            creditAdapter = new CreditAdapter(getContext(), creditList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.setAdapter(creditAdapter);

            credit_add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCreditDialog();
                }
            });
        }
        return view;
    }

    private void showCreditDialog() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.credit_card_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        Spinner transaction_spinner = (Spinner) promptsView.findViewById(R.id.transaction_spinner);
        EditText driver_edit_text = (EditText) promptsView.findViewById(R.id.driver_edit_text);
        EditText vehicle_edit_text = (EditText) promptsView.findViewById(R.id.vehicle_edit_text);
        Spinner product_spinner = (Spinner) promptsView.findViewById(R.id.product_spinner);
        EditText amount = (EditText) promptsView.findViewById(R.id.amount_edit_text);
        Button add_button = (Button) promptsView.findViewById(R.id.add_btn);
        Button cancel_button = (Button) promptsView.findViewById(R.id.cancel_btn);
        //set listener and adapter for transaction type
        setListenerForTransactionType(transaction_spinner);
        List<String> transactionList = Stream.of(TransactionType.values()).map(TransactionType::name).collect(Collectors.toList());
        setSpinnerAdapter(transaction_spinner, transactionList);

        //set listener and adapter for product
        setListenerForProductSpinner(product_spinner);
        List<String> productList = Stream.of(CreditProduct.values()).map(CreditProduct::name).collect(Collectors.toList());
        setSpinnerAdapter(product_spinner, productList);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isError = false;
                String driverName = driver_edit_text.getText() != null ? driver_edit_text.getText().toString() : null;
                if (isStringNullOrEmpty(driverName)) {
                    isError = true;
                    driver_edit_text.setError(getResources().getString(R.string.driver_error));
                }
                String vehicle = vehicle_edit_text.getText() != null ? vehicle_edit_text.getText().toString() : null;
                if (isStringNullOrEmpty(vehicle)) {
                    isError = true;
                    vehicle_edit_text.setError(getResources().getString(R.string.vehicle_error));
                }
                String amount_txt = amount.getText() != null ? amount.getText().toString() : null;
                if (isStringNullOrEmpty(amount_txt)) {
                    isError = true;
                    amount.setError(getResources().getString(R.string.amount_error));
                }
                if (!isError) {
                    Credit credit = new Credit();
                    credit.transactionType = selected_transaction_type;
                    credit.driverName = driverName;
                    credit.vehicleNumber = vehicle;
                    credit.product = selected_product;
                    credit.amount = Double.parseDouble(amount_txt);
                    creditList.add(credit);
                    creditAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void setListenerForTransactionType(Spinner transaction_spinner) {
        transaction_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_transaction_type = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void setListenerForProductSpinner(Spinner product_spinner) {
        product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_product = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void setSpinnerAdapter(Spinner spinner, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, list);
        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
    }

    private boolean isStringNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    @Override
    public List<Credit> getCreditList() {
        return creditList;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreditSaveEvent(MessageEvent messageEvent) {
        System.out.println("Called MessageEvent");
        if (messageEvent.success) {
            creditList.clear();
            creditAdapter.notifyDataSetChanged();
        }
    }
}