package hp.sfs.sales.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Optional;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.model.Debtor;
import hp.sfs.sales.dashboard.service.DebtorService;

public class DebtorAdapter extends RecyclerView.Adapter<DebtorAdapter.ViewHolder> {

    List<Debtor> debtorList;
    Context context;
    private AlertDialog alertDialog = null;

    public DebtorAdapter(Context context) {
        this.context = context;
    }

    public void setDebtorList(List<Debtor> debtorList) {
        this.debtorList = debtorList;
    }

    @NonNull
    @Override
    public DebtorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View debtorItems = layoutInflater.inflate(R.layout.debtor_list, parent, false);
        DebtorAdapter.ViewHolder viewHolder = new DebtorAdapter.ViewHolder(debtorItems);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DebtorAdapter.ViewHolder holder, int position) {
        if (Optional.ofNullable(debtorList).isPresent()) {
            holder.debtor_name_textview.setText(debtorList.get(position).debtor_name);
            holder.debtor_id_textview.setText(debtorList.get(position).debtor_id);
            boolean status = debtorList.get(position).isValid;
            if (status)
                holder.debtor_status_textview.setText("YES");
            else
                holder.debtor_status_textview.setText("NO");

            holder.content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateDebtor(debtorList.get(holder.getAdapterPosition()));
                }
            });
        }
    }

    private void updateDebtor(Debtor debtor) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.debtor_card_view_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        LinearLayout debtor_id_layout = (LinearLayout) promptsView.
                findViewById(R.id.debtor_id_layout);
        TextView debtor_id = (TextView) promptsView.
                findViewById(R.id.debtor_id);
        EditText debtor_name_edittext = (EditText) promptsView.
                findViewById(R.id.debtor_name_edittext);
        CheckBox debtor_status = (CheckBox) promptsView.findViewById(R.id.isvalid);

        debtor_id_layout.setVisibility(View.VISIBLE);
        debtor_status.setVisibility(View.VISIBLE);
        debtor_id.setText(debtor.debtor_id);
        debtor_name_edittext.setText(debtor.debtor_name);
        if (debtor.isValid)
            debtor_status.setChecked(true);
        else
            debtor_status.setChecked(false);

        Button add_button = (Button) promptsView.findViewById(R.id.add_btn);
        Button cancel_button = (Button) promptsView.findViewById(R.id.cancel_btn);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                debtor.debtor_name = debtor_name_edittext.getText().toString();
                debtor.isValid = debtor_status.isChecked();
                DebtorService.updateDebtor(context, debtor);
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
    public int getItemCount() {
        return Optional.ofNullable(debtorList).isPresent() ? debtorList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView debtor_id_textview;
        TextView debtor_name_textview;
        TextView debtor_status_textview;
        LinearLayout content_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            debtor_id_textview = (TextView) itemView.findViewById(R.id.debtor_id_textview);
            debtor_name_textview = (TextView) itemView.findViewById(R.id.debtor_name_textview);
            debtor_status_textview = (TextView) itemView.findViewById(R.id.debtor_status_textview);
            content_layout = (LinearLayout) itemView.findViewById(R.id.content_layout);
        }
    }
}

