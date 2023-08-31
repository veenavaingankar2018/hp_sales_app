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
import hp.sfs.sales.dashboard.model.Credit;
import hp.sfs.sales.dashboard.model.OilSale;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.ViewHolder> {
    Context context;
    List<Credit> creditList;

    public CreditAdapter(Context context, List<Credit> creditList) {
        this.context = context;
        this.creditList = creditList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View creditView = layoutInflater.inflate(R.layout.credit_list, parent, false);
        CreditAdapter.ViewHolder viewHolder = new CreditAdapter.ViewHolder(creditView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Optional.ofNullable(creditList).isPresent()) {
            Credit credit = creditList.get(position);
            holder.transaction_textview.setText(credit.transactionType);
            holder.driver_textview.setText(credit.driverName);
            holder.vehicle_number_textview.setText(credit.vehicleNumber);
            holder.product_textview.setText(credit.product);
            holder.amount_textview.setText(credit.amount.toString());

            holder.content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer index = holder.getAdapterPosition();
                    showCreditDialog(creditList.get(index), index);
                }
            });

        }
    }

    private void showCreditDialog(Credit credit, Integer index) {
        LayoutInflater li = LayoutInflater.from(this.context);
        View promptsView = li.inflate(R.layout.credit_card_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context);
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

        transaction_edit_text.setText(credit.transactionType);
        driver_edit_text.setText(credit.driverName);
        vehicle_edit_text.setText(credit.vehicleNumber);
        product.setText(credit.product);
        amount.setText(credit.amount.toString());

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
                creditList.set(index, credit);
                notifyItemChanged(index);
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
    public int getItemCount() {
        return creditList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView transaction_textview;
        TextView driver_textview;
        TextView vehicle_number_textview;
        TextView product_textview;
        TextView amount_textview;
        LinearLayout content_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            transaction_textview = (TextView) itemView.findViewById(R.id.transaction_textview);
            driver_textview = (TextView) itemView.findViewById(R.id.driver_textview);
            vehicle_number_textview = (TextView) itemView.findViewById(R.id.vehicle_number_textview);
            product_textview = (TextView) itemView.findViewById(R.id.product_textview);
            amount_textview = (TextView) itemView.findViewById(R.id.amount_textview);
            content_layout = (LinearLayout) itemView.findViewById(R.id.content_layout);
        }
    }
}
