package hp.sfs.sales.dashboard.ui.fragment;

import static hp.sfs.sales.dashboard.ui.fragment.DashboardFragment.OPERATOR_LIST;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.adapter.SaleViewPagerAdapter;
import hp.sfs.sales.dashboard.events.MessageEvent;
import hp.sfs.sales.dashboard.events.OperatorDownloadEvent;
import hp.sfs.sales.dashboard.model.AllSaleDetail;
import hp.sfs.sales.dashboard.model.Credit;
import hp.sfs.sales.dashboard.model.Expense;
import hp.sfs.sales.dashboard.model.OilSale;
import hp.sfs.sales.dashboard.model.OnlineDeposit;
import hp.sfs.sales.dashboard.model.Operator;
import hp.sfs.sales.dashboard.model.SaleDetail;
import hp.sfs.sales.dashboard.service.OperatorService;
import hp.sfs.sales.dashboard.service.SaleDetailService;
import hp.sfs.sales.dashboard.ui.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleFragment extends Fragment {
    private View view;
    private Spinner operator_spinner;
    private EditText cash_collected_edit_text;
    ArrayAdapter<String> operatorAdapter;
    private List<String> operatorList = new ArrayList<>();
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Button saveButton;
    List<String> mFragmentTitleList = new ArrayList<>();
    OnSaveClickListener onSaveClickListener;
    SaleDetailFragment saleDetailFragment;
    OilSaleFragment oilSaleFragment;
    OnlineDepositFragment onlineDepositFragment;
    CreditRecordFragment creditRecordFragment;
    ExpenseFragment expenseFragment;
    List<Operator> operators = new ArrayList<>();
    String operator_name;

    public SaleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SaleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SaleFragment newInstance(String param1, String param2) {
        SaleFragment fragment = new SaleFragment();
        return fragment;
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
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Sale Detail");
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            System.out.println("***bundle in Salefragment");
            operators = (List<Operator>) bundle.getSerializable(OPERATOR_LIST);
            operatorList = operators.stream().map(x -> x.operator_name).collect(Collectors.toList());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.fragment_sale, container, false);
            operator_spinner = (Spinner) view.findViewById(R.id.operator_spinner);
            cash_collected_edit_text = (EditText) view.findViewById(R.id.cash_collected_edit_text);
            viewPager = (ViewPager2) view.findViewById(R.id.pager);
            saveButton = (Button) view.findViewById(R.id.save_btn);
            addTabs(viewPager);
            viewPager.setOffscreenPageLimit(3);

            tabLayout = (TabLayout) view.findViewById(R.id.tabs);
            new TabLayoutMediator(tabLayout, viewPager,
                    new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                            tab.setText(mFragmentTitleList.get(position));
                        }
                    }).attach();

            operator_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    operator_name = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            operatorAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text, operatorList);
            // Drop down layout style - list view with radio button
            operatorAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
            operator_spinner.setAdapter(operatorAdapter);
            setSaveButtonListener(saveButton);
        }
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOperatorDownloadEvent(OperatorDownloadEvent operatorDownloadEvent) {
        System.out.println("Called onOperatorDownloadEvent in Sale Fragment");
        if (operatorDownloadEvent.success) {
            operators = operatorDownloadEvent.operatorList;
            operatorList.clear();
            operatorList = operators.stream().map(x -> x.operator_name).collect(Collectors.toList());
            operatorAdapter.addAll(operatorList);
            operatorAdapter.notifyDataSetChanged();
        }
    }

    private void addTabs(ViewPager2 viewPager) {
        SaleViewPagerAdapter adapter = new SaleViewPagerAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        saleDetailFragment = new SaleDetailFragment();
        adapter.addFrag(saleDetailFragment, "Sale Detail");
        oilSaleFragment = new OilSaleFragment();
        adapter.addFrag(oilSaleFragment, "Oil Sale");
        onlineDepositFragment = new OnlineDepositFragment();
        adapter.addFrag(onlineDepositFragment, "Online Deposit");
        creditRecordFragment = new CreditRecordFragment();
        adapter.addFrag(creditRecordFragment, "Credit Record");
        expenseFragment = new ExpenseFragment();
        adapter.addFrag(expenseFragment, "Expense");
        viewPager.setAdapter(adapter);
        mFragmentTitleList = adapter.getmFragmentTitleList();
    }
    private void setSaveButtonListener(Button saveButton) {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveClickListener = (OnSaveClickListener) saleDetailFragment;
                List<SaleDetail> saleDetailList = onSaveClickListener.getSaleDetailList();
                onSaveClickListener = (OnSaveClickListener) oilSaleFragment;
                List<OilSale> oilSaleList = onSaveClickListener.getOilSaleList();
                onSaveClickListener = (OnSaveClickListener) onlineDepositFragment;
                List<OnlineDeposit> onlineDepositList = onSaveClickListener.getOnlineDepositList();
                onSaveClickListener = (OnSaveClickListener) creditRecordFragment;
                List<Credit> creditList = onSaveClickListener.getCreditList();
                onSaveClickListener = (OnSaveClickListener) expenseFragment;
                List<Expense> expenseList = onSaveClickListener.getExpenseList();
                Operator operator = operators.stream().filter(x -> x.operator_name.equals(operator_name)).findFirst().get();

                AllSaleDetail allSaleDetail = new AllSaleDetail();
                allSaleDetail.operatorId = operator.operator_id;
                allSaleDetail.salesRecords = saleDetailList;
                allSaleDetail.oilSaleList = oilSaleList;
                allSaleDetail.onlineDepositList = onlineDepositList;
                allSaleDetail.creditList = creditList;
                allSaleDetail.expenseList = expenseList;
                String cash = cash_collected_edit_text.getText().toString();
                allSaleDetail.cashCollected = isStringNullOrEmpty(cash) ?
                        Double.parseDouble(cash) : 0;
                SaleDetailService.saveSaleDetail(getContext(), allSaleDetail);
            }
        });
    }
    private boolean isStringNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }
    public interface OnSaveClickListener {
        default List<SaleDetail> getSaleDetailList() {
            return null;
        }

        default List<OilSale> getOilSaleList() {
            return null;
        }

        default List<OnlineDeposit> getOnlineDepositList() {
            return null;
        }

        default List<Credit> getCreditList() {
            return null;
        }

        default List<Expense> getExpenseList() {
            return null;
        }
    }
}

