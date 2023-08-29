package hp.sfs.sales.dashboard.ui.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.adapter.OperatorAdapter;
import hp.sfs.sales.dashboard.adapter.SaleDetailAdapter;
import hp.sfs.sales.dashboard.events.MessageEvent;
import hp.sfs.sales.dashboard.model.SaleDetail;
import hp.sfs.sales.dashboard.service.OperatorService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaleDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleDetailFragment extends Fragment implements SaleFragment.OnSaveClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private RecyclerView recyclerView;
    private SaleDetailAdapter saleDetailAdapter;
    private CardView sale_detail_add_btn;
    private List<SaleDetail> saleDetailList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context context;

    public SaleDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SaleDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SaleDetailFragment newInstance(String param1, String param2) {
        SaleDetailFragment fragment = new SaleDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.fragment_sale_detail, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.sale_detail_list);
            saleDetailAdapter = new SaleDetailAdapter(getContext(), saleDetailList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.setAdapter(saleDetailAdapter);

            sale_detail_add_btn = (CardView) view.findViewById(R.id.sale_detail_add_btn);

            sale_detail_add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSaleDetailDialog();
                }
            });
        }
        return view;
    }

    private void showSaleDetailDialog() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.sale_detail_card_view_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        EditText startTime = (EditText) promptsView.findViewById(R.id.start_time_edit_text);
        EditText endTime = (EditText) promptsView.findViewById(R.id.end_time_edit_text);
        EditText product = (EditText) promptsView.findViewById(R.id.product_edit_text);
        EditText rate = (EditText) promptsView.findViewById(R.id.rate_edit_text);
        EditText startReading = (EditText) promptsView.findViewById(R.id.start_reading_edit_text);
        EditText endReading = (EditText) promptsView.findViewById(R.id.end_reading_edit_text);
        EditText salesVolume = (EditText) promptsView.findViewById(R.id.sales_volume_edit_text);
        EditText pumpTestVolume = (EditText) promptsView.findViewById(R.id.pump_test_volume_edit_text);
        EditText amount = (EditText) promptsView.findViewById(R.id.amount_edit_text);
        Button add_button = (Button) promptsView.findViewById(R.id.add_btn);
        Button cancel_button = (Button) promptsView.findViewById(R.id.cancel_btn);
        final Calendar calendarStartTime = Calendar.getInstance();
        setDatePicker(calendarStartTime, startTime);
        final Calendar calendarEndTime = Calendar.getInstance();
        setDatePicker(calendarEndTime, endTime);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaleDetail saleDetail = new SaleDetail();
                saleDetail.start_time = startTime.getText().toString();
                saleDetail.end_time = endTime.getText().toString();
                saleDetail.product = product.getText().toString();
                String rate_str = rate.getText().toString();
                saleDetail.rate = isStringNullOrEmpty(rate_str) ?
                        Double.parseDouble(rate_str) : 0;
                String start_reading = startReading.getText().toString();
                saleDetail.start_reading = isStringNullOrEmpty(start_reading) ?
                        Double.parseDouble(start_reading) : 0;
                String end_reading = endReading.getText().toString();
                saleDetail.end_reading = isStringNullOrEmpty(end_reading) ?
                        Double.parseDouble(end_reading) : 0;
                String sales_volume = salesVolume.getText().toString();
                saleDetail.sales_volume = isStringNullOrEmpty(sales_volume) ?
                        Double.parseDouble(sales_volume) : 0;
                String pump_test_volume = pumpTestVolume.getText().toString();
                saleDetail.pump_test_volume = isStringNullOrEmpty(pump_test_volume) ?
                        Double.parseDouble(pump_test_volume) : 0;
                String amount_str = amount.getText().toString();
                saleDetail.amount = isStringNullOrEmpty(amount_str) ? Double.parseDouble(amount_str) : 0;

                saleDetailList.add(saleDetail);
                saleDetailAdapter.notifyDataSetChanged();
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

    private void setDatePicker(Calendar calendar, EditText dateTimeEditText) {
        Context context = getContext();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                setTimePicker(calendar, dateTimeEditText);
            }
        };

        dateTimeEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        new DatePickerDialog(context, R.style.DatePickerDialogTheme, date, calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                        break;
                }

                return false;
            }
        });
    }

    private void setTimePicker(Calendar calendar, EditText dateTimeEditText) {
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                setDate(dateTimeEditText, calendar);
            }
        };
        new TimePickerDialog(context, R.style.DatePickerDialogTheme, time, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false).show();
    }

    private void setDate(EditText date, Calendar calendar) {
        String myFormat = "dd-MM-yyyy hh:mm:ss aa";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(dateFormat.format(calendar.getTime()));
    }

    private boolean isStringNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    @Override
    public List<SaleDetail> getSaleDetailList() {
        return saleDetailList;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaleDetailSaveEvent(MessageEvent messageEvent) {
        System.out.println("Called MessageEvent");
        if (messageEvent.success) {
            saleDetailList.clear();
            saleDetailAdapter.notifyDataSetChanged();
        }
    }
}

