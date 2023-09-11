package hp.sfs.sales.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.enums.CreditProduct;
import hp.sfs.sales.dashboard.enums.TransactionType;
import hp.sfs.sales.dashboard.model.Credit;
import hp.sfs.sales.dashboard.model.OilSale;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.ViewHolder> {
    Context context;
    List<Credit> creditList;
    private String selected_transaction_type;
    private String selected_product;

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
        setSpinnerAdapter(transaction_spinner, transactionList, credit.transactionType);

        //set listener and adapter for product
        setListenerForProductSpinner(product_spinner);
        List<String> productList = Stream.of(CreditProduct.values()).map(CreditProduct::name).collect(Collectors.toList());
        setSpinnerAdapter(product_spinner, productList, credit.product);

        //transaction_edit_text.setText(credit.transactionType);
        driver_edit_text.setText(credit.driverName);
        vehicle_edit_text.setText(credit.vehicleNumber);
        //product.setText(credit.product);
        amount.setText(credit.amount.toString());

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isError = false;
                String driverName = driver_edit_text.getText() != null ? driver_edit_text.getText().toString() : null;
                if (isStringNullOrEmpty(driverName)) {
                    isError = true;
                    driver_edit_text.setError(context.getResources().getString(R.string.driver_error));
                }
                String vehicle = vehicle_edit_text.getText() != null ? vehicle_edit_text.getText().toString() : null;
                if (isStringNullOrEmpty(vehicle)) {
                    isError = true;
                    vehicle_edit_text.setError(context.getResources().getString(R.string.vehicle_error));
                }
                String amount_txt = amount.getText() != null ? amount.getText().toString() : null;
                if (isStringNullOrEmpty(amount_txt)) {
                    isError = true;
                    amount.setError(context.getResources().getString(R.string.amount_error));
                }

                if (!isError) {
                    Credit credit = new Credit();
                    credit.transactionType = selected_transaction_type;
                    credit.driverName = driverName;
                    credit.vehicleNumber = vehicle;
                    credit.product = selected_product;
                    credit.amount = Double.parseDouble(amount_txt);
                    creditList.set(index, credit);
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
    private void setSpinnerAdapter(Spinner spinner, List<String> list, String item) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_text, list);
        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
        int selectedProductPosition = adapter.getPosition(item);
        spinner.setSelection(selectedProductPosition);
    }
    private boolean isStringNullOrEmpty(String str) {
        return str == null || str.isEmpty();
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
