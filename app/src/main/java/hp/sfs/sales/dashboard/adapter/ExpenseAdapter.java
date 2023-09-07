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
import hp.sfs.sales.dashboard.model.Expense;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    Context context;
    List<Expense> expenseList;

    public ExpenseAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View expenseView = layoutInflater.inflate(R.layout.expense_list, parent, false);
        ExpenseAdapter.ViewHolder viewHolder = new ExpenseAdapter.ViewHolder(expenseView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Optional.ofNullable(expenseList).isPresent()) {
            Expense expense = expenseList.get(position);
            holder.description_textview.setText(expense.description);
            holder.amount_textview.setText(expense.amount.toString());
            holder.content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer index = holder.getAdapterPosition();
                    showExpenseDialog(expenseList.get(index), index);
                }
            });
        }
    }

    private void showExpenseDialog(Expense expense, Integer index) {
        LayoutInflater li = LayoutInflater.from(this.context);
        View promptsView = li.inflate(R.layout.expense_card_view_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context);
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        EditText description_edit_text = (EditText) promptsView.findViewById(R.id.description_edit_text);
        EditText amount = (EditText) promptsView.findViewById(R.id.amount_edit_text);
        Button add_button = (Button) promptsView.findViewById(R.id.add_btn);
        Button cancel_button = (Button) promptsView.findViewById(R.id.cancel_btn);

        description_edit_text.setText(expense.description);
        amount.setText(expense.amount.toString());
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isError = false;
                String description = description_edit_text.getText() != null ? description_edit_text.getText().toString() : null;
                if (isStringNullOrEmpty(description)) {
                    isError = true;
                    description_edit_text.setError(context.getResources().getString(R.string.description_error));
                }
                String amount_str = amount.getText() != null ? amount.getText().toString() : null;
                if (isStringNullOrEmpty(amount_str)) {
                    isError = true;
                    amount.setError(context.getResources().getString(R.string.amount_error));
                }

                if (!isError) {
                    Expense expense = new Expense();
                    expense.description = description;
                    expense.amount = Double.parseDouble(amount_str);
                    expenseList.set(index, expense);
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

    private boolean isStringNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView description_textview;
        TextView amount_textview;
        LinearLayout content_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description_textview = (TextView) itemView.findViewById(R.id.description_textview);
            amount_textview = (TextView) itemView.findViewById(R.id.amount_textview);
            content_layout = (LinearLayout) itemView.findViewById(R.id.content_layout);
        }
    }
}
