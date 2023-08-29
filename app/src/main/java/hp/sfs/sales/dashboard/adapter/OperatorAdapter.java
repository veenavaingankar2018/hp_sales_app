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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.model.Operator;
import hp.sfs.sales.dashboard.service.OperatorService;

public class OperatorAdapter extends RecyclerView.Adapter<OperatorAdapter.ViewHolder> {

    List<Operator> operatorList;
    Context context;
    private AlertDialog alertDialog = null;

    public OperatorAdapter(Context context) {
        this.context = context;
    }

    public void setOperatorList(List<Operator> operatorList) {
        this.operatorList = operatorList;
    }

    @NonNull
    @Override
    public OperatorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View operatorItems = layoutInflater.inflate(R.layout.operator_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(operatorItems);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OperatorAdapter.ViewHolder holder, int position) {
        if (Optional.ofNullable(operatorList).isPresent()) {
            holder.operator_name_textview.setText(operatorList.get(position).operator_name);
            holder.operator_id_textview.setText(operatorList.get(position).operator_id.toString());
            boolean status = operatorList.get(position).isValid;
            if (status)
                holder.operator_status_textview.setText("YES");
            else
                holder.operator_status_textview.setText("NO");

            holder.content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateOperator(operatorList.get(holder.getAdapterPosition()));
                }
            });
        }
    }

    private void updateOperator(Operator operator) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.operator_card_view_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        LinearLayout operator_id_layout = (LinearLayout) promptsView.
                findViewById(R.id.operator_id_layout);
        TextView operator_id = (TextView) promptsView.
                findViewById(R.id.operator_id);
        EditText operator_name_edittext = (EditText) promptsView.
                findViewById(R.id.operator_name_edittext);
        CheckBox operator_status = (CheckBox) promptsView.findViewById(R.id.isvalid);

        operator_id_layout.setVisibility(View.VISIBLE);
        operator_status.setVisibility(View.VISIBLE);
        operator_id.setText(operator.operator_id.toString());
        operator_name_edittext.setText(operator.operator_name);
        if (operator.isValid)
            operator_status.setChecked(true);
        else
            operator_status.setChecked(false);

        Button add_button = (Button) promptsView.findViewById(R.id.add_btn);
        Button cancel_button = (Button) promptsView.findViewById(R.id.cancel_btn);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operator.operator_name = operator_name_edittext.getText().toString();
                operator.isValid = operator_status.isChecked();
                OperatorService.updateOperator(context, operator);
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
        return Optional.ofNullable(operatorList).isPresent() ? operatorList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView operator_id_textview;
        TextView operator_name_textview;
        TextView operator_status_textview;
        LinearLayout content_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            operator_id_textview = (TextView) itemView.findViewById(R.id.operator_id_textview);
            operator_name_textview = (TextView) itemView.findViewById(R.id.operator_name_textview);
            operator_status_textview = (TextView) itemView.findViewById(R.id.operator_status_textview);
            content_layout = (LinearLayout) itemView.findViewById(R.id.content_layout);
        }
    }
}
