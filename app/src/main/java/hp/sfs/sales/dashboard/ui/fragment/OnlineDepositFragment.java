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
import hp.sfs.sales.dashboard.adapter.OilSaleAdapter;
import hp.sfs.sales.dashboard.adapter.OnlineDepositAdapter;
import hp.sfs.sales.dashboard.model.OilSale;
import hp.sfs.sales.dashboard.model.OnlineDeposit;
public class OnlineDepositFragment extends Fragment implements SaleFragment.OnSaveClickListener {
    private View view;
    private RecyclerView recyclerView;
    private CardView online_deposit_add_btn;
    private List<OnlineDeposit> onlineDepositList = new ArrayList<>();
    private OnlineDepositAdapter onlineDepositAdapter;
    public OnlineDepositFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState == null){
            view = inflater.inflate(R.layout.fragment_online_deposit, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.online_deposit_list);
            online_deposit_add_btn = (CardView) view.findViewById(R.id.online_deposit_add_btn);

            onlineDepositAdapter = new OnlineDepositAdapter(getContext(), onlineDepositList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.setAdapter(onlineDepositAdapter);

            online_deposit_add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showOnlineDepositDialog();
                }
            });
        }
        return view;
    }

    private void showOnlineDepositDialog() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.online_deposit_card_view_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        EditText mode = (EditText) promptsView.findViewById(R.id.mode_edit_text);
        EditText amount = (EditText) promptsView.findViewById(R.id.amount_edit_text);
        EditText remark = (EditText) promptsView.findViewById(R.id.remark_edit_text);
        Button add_button = (Button) promptsView.findViewById(R.id.add_btn);
        Button cancel_button = (Button) promptsView.findViewById(R.id.cancel_btn);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnlineDeposit onlineDeposit = new OnlineDeposit();
                onlineDeposit.mode = mode.getText().toString();
                String amount_str = amount.getText().toString();
                onlineDeposit.amount = isStringNullOrEmpty(amount_str) ?
                        Double.parseDouble(amount_str) : 0;
                onlineDeposit.remark = remark.getText().toString();
                onlineDepositList.add(onlineDeposit);
                onlineDepositAdapter.notifyDataSetChanged();
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

    @Override
    public List<OnlineDeposit> getOnlineDepositList() {
        return onlineDepositList;
    }

    private boolean isStringNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}