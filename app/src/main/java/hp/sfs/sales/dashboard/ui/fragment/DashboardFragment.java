package hp.sfs.sales.dashboard.ui.fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.events.OperatorDownloadEvent;
import hp.sfs.sales.dashboard.model.Operator;
import hp.sfs.sales.dashboard.service.OperatorService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CardView sale_detail_card_view;
    CardView operator_card_view;
    CardView debtor_card_view;
    CardView bank_deposit_card_view;
    private View view = null;
    private FragmentTransaction fragmentTransaction;
    List<Operator> operators = new ArrayList<>();
    public static final String OPERATOR_LIST = "operator_list";

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        OperatorService.loadOperators(getContext());
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOperatorDownloadEvent(OperatorDownloadEvent operatorDownloadEvent) {
        System.out.println("Called onOperatorDownloadEvent in Sale Fragment");
        if (operatorDownloadEvent.success) {
            operators = operatorDownloadEvent.operatorList;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//return inflater.inflate(R.layout.fragment_dashboard, container, false)
        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.fragment_dashboard, container, false);
            sale_detail_card_view = (CardView) view.findViewById(R.id.sale_detail_card_view);
            operator_card_view = (CardView) view.findViewById(R.id.operator_card_view);
            debtor_card_view = (CardView) view.findViewById(R.id.debtor_card_view);
            bank_deposit_card_view = (CardView) view.findViewById(R.id.bank_deposit_card_view);

            operator_card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_activity_frame_layout, new OperatorFragment(),
                            OperatorFragment.class.getSimpleName());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            debtor_card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_activity_frame_layout, new DebtorFragment(),
                            DebtorFragment.class.getSimpleName());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            sale_detail_card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(OPERATOR_LIST, (Serializable) operators);
                    Fragment fragment = new SaleFragment();
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.main_activity_frame_layout, fragment,
                            SaleFragment.class.getSimpleName());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
        return view;
    }
}

