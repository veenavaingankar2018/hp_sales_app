package hp.sfs.sales.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Optional;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.model.OilSale;
import hp.sfs.sales.dashboard.model.SaleDetail;

public class OilSaleAdapter extends RecyclerView.Adapter<OilSaleAdapter.ViewHolder> {
    Context context;
    List<OilSale> oilSaleList;

    public OilSaleAdapter(Context context, List<OilSale> oilSaleList) {
        this.context = context;
        this.oilSaleList = oilSaleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View oilSaleView = layoutInflater.inflate(R.layout.oil_sale_list, parent, false);
        OilSaleAdapter.ViewHolder viewHolder = new OilSaleAdapter.ViewHolder(oilSaleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Optional.ofNullable(oilSaleList).isPresent()) {
            holder.product_textview.setText(oilSaleList.get(position).product);
            holder.quantity_textview.setText(oilSaleList.get(position).quantity.toString());
            holder.amount_textview.setText(oilSaleList.get(position).amount.toString());

            holder.content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer index = holder.getAdapterPosition();
                    showOilSaleDialog(oilSaleList.get(index), index);
                }
            });

        }
    }

    private void showOilSaleDialog(OilSale oilSale, Integer index) {
        LayoutInflater li = LayoutInflater.from(this.context);
        View promptsView = li.inflate(R.layout.oil_sale_card_view_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context);
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

        product.setText(oilSale.product);
        quantity.setText(oilSale.quantity.toString());
        amount.setText(oilSale.amount.toString());

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isError = false;
                String product_value = product.getText() != null ? product.getText().toString() : null;
                if (isStringNullOrEmpty(product_value)) {
                    isError = true;
                    product.setError(context.getResources().getString(R.string.product_error));
                }
                String quantity_value = quantity.getText() != null ? quantity.getText().toString() : null;
                if (isStringNullOrEmpty(quantity_value)) {
                    isError = true;
                    quantity.setError(context.getResources().getString(R.string.quantity_error));
                }
                String amount_str = amount.getText() != null ? amount.getText().toString() : null;
                if (isStringNullOrEmpty(amount_str)) {
                    isError = true;
                    amount.setError(context.getResources().getString(R.string.amount_error));
                }
                if (!isError) {
                    OilSale oilSale = new OilSale();
                    oilSale.product = product_value;
                    oilSale.quantity = Double.parseDouble(quantity_value);
                    oilSale.amount = Double.parseDouble(amount_str);
                    oilSaleList.set(index, oilSale);
                    notifyItemChanged(index);
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
    @Override
    public int getItemCount() {
        return oilSaleList.size();
    }
    private boolean isStringNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_textview;
        TextView quantity_textview;
        TextView amount_textview;
        LinearLayout content_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_textview = (TextView) itemView.findViewById(R.id.product_textview);
            quantity_textview = (TextView) itemView.findViewById(R.id.quantity_textview);
            amount_textview = (TextView) itemView.findViewById(R.id.amount_textview);
            content_layout = (LinearLayout) itemView.findViewById(R.id.content_layout);
        }
    }
}
