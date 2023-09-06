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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.adapter.CreditAdapter;
import hp.sfs.sales.dashboard.adapter.ExpenseAdapter;
import hp.sfs.sales.dashboard.events.MessageEvent;
import hp.sfs.sales.dashboard.model.Credit;
import hp.sfs.sales.dashboard.model.Expense;

public class ExpenseFragment extends Fragment implements SaleFragment.OnSaveClickListener {
    View view;
    private RecyclerView recyclerView;
    private CardView expense_add_btn;
    private List<Expense> expense_list = new ArrayList<>();
    private ExpenseAdapter expenseAdapter;

    public ExpenseFragment() {
        // Required empty public constructor
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.fragment_expense, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.expense_list);
            expense_add_btn = (CardView) view.findViewById(R.id.expense_add_btn);

            expenseAdapter = new ExpenseAdapter(getContext(), expense_list);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.setAdapter(expenseAdapter);
            expense_add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showExpenseDialog();
                }
            });
        }
        return view;
    }

    private void showExpenseDialog() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.expense_card_view_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        EditText description_edit_text = (EditText) promptsView.findViewById(R.id.description_edit_text);
        EditText amount = (EditText) promptsView.findViewById(R.id.amount_edit_text);
        Button add_button = (Button) promptsView.findViewById(R.id.add_btn);
        Button cancel_button = (Button) promptsView.findViewById(R.id.cancel_btn);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Expense expense = new Expense();
                expense.description = description_edit_text.getText().toString();
                String amount_txt = amount.getText().toString();
                expense.amount = isStringNullOrEmpty(amount_txt) ?
                        Double.parseDouble(amount_txt) : 0;
                expense_list.add(expense);
                expenseAdapter.notifyDataSetChanged();
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
    public List<Expense> getExpenseList() {
        return expense_list;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExpenseSaveEvent(MessageEvent messageEvent) {
        System.out.println("Called MessageEvent in expense");
        if (messageEvent.success) {
            expense_list.clear();
            expenseAdapter.notifyDataSetChanged();
        }
    }
}