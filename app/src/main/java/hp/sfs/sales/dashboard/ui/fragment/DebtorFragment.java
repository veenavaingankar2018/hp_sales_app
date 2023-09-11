package hp.sfs.sales.dashboard.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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

import java.util.List;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.adapter.DebtorAdapter;
import hp.sfs.sales.dashboard.events.DebtorDownloadEvent;
import hp.sfs.sales.dashboard.events.MessageEvent;
import hp.sfs.sales.dashboard.events.OperatorDownloadEvent;
import hp.sfs.sales.dashboard.model.Debtor;
import hp.sfs.sales.dashboard.service.DebtorService;
import hp.sfs.sales.dashboard.service.OperatorService;
import hp.sfs.sales.dashboard.ui.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DebtorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebtorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private RecyclerView recyclerView;
    private DebtorAdapter debtorAdapter;
    private List<Debtor> debtorList;
    private Button add_button;
    private AlertDialog alertDialog = null;
    private ProgressDialog progressDialog = null;

    public DebtorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DebterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DebtorFragment newInstance(String param1, String param2) {
        DebtorFragment fragment = new DebtorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Debtor");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        DebtorService.loadDebtors(getContext());
        showProgressDialog();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.fragment_debtor, container, false);
            add_button = (Button) view.findViewById(R.id.add_button);
            recyclerView = (RecyclerView) view.findViewById(R.id.debtor_list);
            setAddButtonListener(add_button);
            debtorAdapter = new DebtorAdapter(getContext());;
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.setAdapter(debtorAdapter);
        }
        return view;
    }

    private void setAddButtonListener(Button add_button) {
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.debtor_card_view_layout, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setView(promptsView);
                // create alert dialog
                alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);

                EditText debtor_name = (EditText) promptsView.findViewById(R.id.debtor_name_edittext);
                Button add_button = (Button) promptsView.findViewById(R.id.add_btn);
                Button cancel_button = (Button) promptsView.findViewById(R.id.cancel_btn);
                add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("debtor :" + debtor_name.getText().toString());
                        DebtorService.saveDebtor(getContext(), debtor_name.getText().toString());
                        alertDialog.dismiss();
                        showProgressDialog();
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
        });
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(getActivity().getString(R.string.loading));
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDebtorSaveEvent(MessageEvent messageEvent) {
        progressDialog.cancel();
        System.out.println("Called MessageEvent");
        if (messageEvent.success) {
            showAlertDialog(false, "Debtor saved successfully");
            DebtorService.loadDebtors(getContext());
            showProgressDialog();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDebtorDownloadEvent(DebtorDownloadEvent debtorDownloadEvent) {
        progressDialog.cancel();
        System.out.println("Called debtorDownloadEvent");
        if (debtorDownloadEvent.success) {
            debtorList = debtorDownloadEvent.debtorList;
            debtorAdapter.setDebtorList(debtorList);
            debtorAdapter.notifyDataSetChanged();
//            showAlertDialog(false, "Debtor downloaded successfully");
        }
    }

    private void showAlertDialog(final Boolean removeFragment, String response) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);

        // Setting Dialog Title
        //alertDialog.setTitle("Feedback Response...");
//        // Setting Dialog Message
        alertDialog.setMessage(response);
//
//        // Setting Icon to Dialog
//        alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (removeFragment) {
                    //removeSelf();
                }
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
}