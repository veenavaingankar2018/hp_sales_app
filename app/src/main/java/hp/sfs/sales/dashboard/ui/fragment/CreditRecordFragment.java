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
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.adapter.CreditAdapter;
import hp.sfs.sales.dashboard.model.Credit;

public class CreditRecordFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private CardView credit_add_btn;
    private List<Credit> creditList = new ArrayList<>();
    private CreditAdapter creditAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState == null){
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
    private void showCreditDialog(){
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.credit_card_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        EditText transaction_edit_text = (EditText) promptsView.findViewById(R.id.transaction_edit_text);
        EditText driver_edit_text = (EditText) promptsView.findViewById(R.id.driver_edit_text);
        EditText vehicle_edit_text = (EditText) promptsView.findViewById(R.id.vehicle_edit_text);
        EditText product = (EditText) promptsView.findViewById(R.id.product_edit_text);
        EditText amount = (EditText) promptsView.findViewById(R.id.amount_edit_text);
        Button add_button = (Button) promptsView.findViewById(R.id.add_btn);
        Button cancel_button = (Button) promptsView.findViewById(R.id.cancel_btn);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Credit credit = new Credit();
                credit.transactionType = transaction_edit_text.getText().toString();
                credit.driverName = driver_edit_text.getText().toString();
                credit.vehicleNumber = vehicle_edit_text.getText().toString();
                credit.product = product.getText().toString();
                String amount_txt = amount.getText().toString();
                credit.amount = isStringNullOrEmpty(amount_txt) ?
                        Double.parseDouble(amount_txt) : 0;
                creditList.add(credit);
                creditAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
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
    private boolean isStringNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}