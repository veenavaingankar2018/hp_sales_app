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
import hp.sfs.sales.dashboard.model.OnlineDeposit;

public class OnlineDepositAdapter extends RecyclerView.Adapter<OnlineDepositAdapter.ViewHolder> {
    Context context;
    List<OnlineDeposit> onlineDepositList;
    public OnlineDepositAdapter(Context context, List<OnlineDeposit> onlineDepositList) {
        this.context = context;
        this.onlineDepositList = onlineDepositList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View onlineDepositView = layoutInflater.inflate(R.layout.online_deposit_list, parent, false);
        OnlineDepositAdapter.ViewHolder viewHolder = new OnlineDepositAdapter.ViewHolder(onlineDepositView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Optional.ofNullable(onlineDepositList).isPresent()) {
            holder.mode_textview.setText(onlineDepositList.get(position).mode);
            holder.amount_textview.setText(onlineDepositList.get(position).amount.toString());
            holder.remark_textview.setText(onlineDepositList.get(position).remark.toString());

            holder.content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer index = holder.getAdapterPosition();
                    showOnlineDepositDialog(onlineDepositList.get(index), index);
                }
            });
        }
    }

    private void showOnlineDepositDialog(OnlineDeposit onlineDeposit, Integer index) {
        LayoutInflater li = LayoutInflater.from(this.context);
        View promptsView = li.inflate(R.layout.online_deposit_card_view_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context);
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

        mode.setText(onlineDeposit.mode);
        remark.setText(onlineDeposit.remark);
        amount.setText(onlineDeposit.amount.toString());

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onlineDeposit.mode = mode.getText().toString();
                String amount_str = amount.getText().toString();
                onlineDeposit.amount = isStringNullOrEmpty(amount_str) ?
                        Double.parseDouble(amount_str) : 0;
                onlineDeposit.remark = remark.getText().toString();
                onlineDepositList.set(index, onlineDeposit);
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

    @Override
    public int getItemCount() {
        return onlineDepositList.size();
    }
    private boolean isStringNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mode_textview;
        TextView amount_textview;
        TextView remark_textview;
        LinearLayout content_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mode_textview = (TextView) itemView.findViewById(R.id.mode_textview);
            amount_textview = (TextView) itemView.findViewById(R.id.amount_textview);
            remark_textview = (TextView) itemView.findViewById(R.id.remark_textview);
            content_layout = (LinearLayout) itemView.findViewById(R.id.content_layout);
        }
    }
}
