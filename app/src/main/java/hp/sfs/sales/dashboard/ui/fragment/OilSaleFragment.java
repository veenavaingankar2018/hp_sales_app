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
import hp.sfs.sales.dashboard.adapter.SaleDetailAdapter;
import hp.sfs.sales.dashboard.model.OilSale;
import hp.sfs.sales.dashboard.model.SaleDetail;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OilSaleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OilSaleFragment extends Fragment implements SaleFragment.OnSaveClickListener {
    private View view;
    private RecyclerView recyclerView;
    private CardView oil_sale_add_btn;
    private List<OilSale> oilSaleList = new ArrayList<>();
    private OilSaleAdapter oilSaleAdapter;

    public OilSaleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OilSaleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OilSaleFragment newInstance(String param1, String param2) {
        OilSaleFragment fragment = new OilSaleFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.fragment_oil_sale, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.oil_sale_list);
            oil_sale_add_btn = (CardView) view.findViewById(R.id.oil_sale_add_btn);

            oilSaleAdapter = new OilSaleAdapter(getContext(), oilSaleList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.setAdapter(oilSaleAdapter);

            oil_sale_add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSaleDetailDialog();
                }
            });
        }
        return view;
    }
    private void showSaleDetailDialog() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.oil_sale_card_view_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        EditText product = (EditText) promptsView.findViewById(R.id.product_edit_text);
        EditText quantity = (EditText) promptsView.findViewById(R.id.quantity_edit_text);
        EditText amount = (EditText) promptsView.findViewById(R.id.amount_edit_text);
        Button add_button = (Button) promptsView.findViewById(R.id.add_btn);
        Button cancel_button = (Button) promptsView.findViewById(R.id.cancel_btn);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OilSale oilSale = new OilSale();
                oilSale.product = product.getText().toString();
                String quantity_str = quantity.getText().toString();
                oilSale.quantity = isStringNullOrEmpty(quantity_str) ?
                        Double.parseDouble(quantity_str) : 0;
                String amount_txt = amount.getText().toString();
                oilSale.amount = isStringNullOrEmpty(amount_txt) ?
                        Double.parseDouble(amount_txt) : 0;
                oilSaleList.add(oilSale);
                oilSaleAdapter.notifyDataSetChanged();
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

    @Override
    public List<OilSale> getOilSaleList() {
        return oilSaleList;
    }
}