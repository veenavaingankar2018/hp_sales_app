package hp.sfs.sales.dashboard.adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.enums.Product;
import hp.sfs.sales.dashboard.model.SaleDetail;

public class SaleDetailAdapter extends RecyclerView.Adapter<SaleDetailAdapter.ViewHolder> {
    private Context context;
    private List<SaleDetail> saleDetailList;
    private String selected_product;

    public SaleDetailAdapter(Context context, List<SaleDetail> saleDetailList) {
        this.context = context;
        this.saleDetailList = saleDetailList;
    }

    @NonNull
    @Override
    public SaleDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View saleDetailView = layoutInflater.inflate(R.layout.sale_detail_list, parent, false);
        SaleDetailAdapter.ViewHolder viewHolder = new SaleDetailAdapter.ViewHolder(saleDetailView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SaleDetailAdapter.ViewHolder holder, int position) {
        if (Optional.ofNullable(saleDetailList).isPresent()) {
            holder.start_time_textview.setText(saleDetailList.get(position).start_time);
            holder.end_time_textview.setText(saleDetailList.get(position).end_time);
            holder.product.setText(saleDetailList.get(position).product);
            holder.rate.setText(saleDetailList.get(position).rate.toString());
            holder.start_reading.setText(saleDetailList.get(position).start_reading.toString());
            holder.end_reading.setText(saleDetailList.get(position).end_reading.toString());
            holder.sale_volume.setText(saleDetailList.get(position).sales_volume.toString());
            holder.pump_test_volume.setText(saleDetailList.get(position).pump_test_volume.toString());
            holder.amount.setText(saleDetailList.get(position).amount.toString());

            holder.content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer index = holder.getAdapterPosition();
                    showSaleDetailDialog(saleDetailList.get(index), index);
                }
            });
        }
    }

    private void showSaleDetailDialog(SaleDetail saleDetail, Integer index) {
        LayoutInflater li = LayoutInflater.from(this.context);
        View promptsView = li.inflate(R.layout.sale_detail_card_view_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context);
        alertDialogBuilder.setView(promptsView);
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        EditText startTime = (EditText) promptsView.findViewById(R.id.start_time_edit_text);
        EditText endTime = (EditText) promptsView.findViewById(R.id.end_time_edit_text);
        Spinner product_spinner = (Spinner) promptsView.findViewById(R.id.product_spinner);
        EditText rate = (EditText) promptsView.findViewById(R.id.rate_edit_text);
        EditText startReading = (EditText) promptsView.findViewById(R.id.start_reading_edit_text);
        EditText endReading = (EditText) promptsView.findViewById(R.id.end_reading_edit_text);
        EditText salesVolume = (EditText) promptsView.findViewById(R.id.sales_volume_edit_text);
        EditText pumpTestVolume = (EditText) promptsView.findViewById(R.id.pump_test_volume_edit_text);
        EditText amount = (EditText) promptsView.findViewById(R.id.amount_edit_text);
        Button add_button = (Button) promptsView.findViewById(R.id.add_btn);
        Button cancel_button = (Button) promptsView.findViewById(R.id.cancel_btn);
        //sales volume non-editable
        salesVolume.setKeyListener(null);
        salesVolume.setEnabled(false);
        //make amount non-editable
        amount.setKeyListener(null);
        amount.setEnabled(false);

        product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_product = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        List<String> productList = Stream.of(Product.values()).map(Product::name).collect(Collectors.toList());
        ArrayAdapter<String> productAdapter = new ArrayAdapter<String>(context, R.layout.spinner_text, productList);
        // Drop down layout style - list view with radio button
        productAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        product_spinner.setAdapter(productAdapter);
        int selectedProductPosition = productAdapter.getPosition(saleDetail.product);
        product_spinner.setSelection(selectedProductPosition);

        startTime.setText(saleDetail.start_time);
        endTime.setText(saleDetail.end_time);
        rate.setText(saleDetail.rate.toString());
        startReading.setText(saleDetail.start_reading.toString());
        endReading.setText(saleDetail.end_reading.toString());
        salesVolume.setText(saleDetail.sales_volume.toString());
        pumpTestVolume.setText(saleDetail.pump_test_volume.toString());
        amount.setText(saleDetail.amount.toString());

        final Calendar calendarStartTime = Calendar.getInstance();
        setDatePicker(calendarStartTime, startTime);
        final Calendar calendarEndTime = Calendar.getInstance();
        setDatePicker(calendarEndTime, endTime);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isError = false;
                Double startReadingValue = 0.0, endReadingValue = 0.0;
                String startTimeValue = startTime.getText() != null ? startTime.getText().toString() : null;
                if (isStringNullOrEmpty(startTimeValue)) {
                    isError = true;
                    startTime.setError(context.getResources().getString(R.string.start_time_error));
                }
                String endTimeValue = endTime.getText() != null ? endTime.getText().toString() : null;
                if (isStringNullOrEmpty(endTimeValue)) {
                    isError = true;
                    endTime.setError(context.getResources().getString(R.string.end_time_error));
                }

                String rate_str = rate.getText() != null ? rate.getText().toString() : null;
                if (isStringNullOrEmpty(rate_str)) {
                    isError = true;
                    rate.setError(context.getResources().getString(R.string.rate_error));
                }
                String start_reading = startReading.getText() != null ?
                        startReading.getText().toString() : null;
                if (isStringNullOrEmpty(start_reading)) {
                    isError = true;
                    startReading.setError(context.getResources().getString(R.string.start_reading_error));
                } else
                    startReadingValue = Double.parseDouble(start_reading);

                String end_reading = endReading.getText() != null ?
                        endReading.getText().toString() : null;
                if (isStringNullOrEmpty(end_reading)) {
                    isError = true;
                    endReading.setError(context.getResources().getString(R.string.end_reading_error));
                } else
                    endReadingValue = Double.parseDouble(end_reading);

                String pump_test_volume = pumpTestVolume.getText() != null ?
                        pumpTestVolume.getText().toString() : null;
                if (isStringNullOrEmpty(pump_test_volume)) {
                    isError = true;
                    pumpTestVolume.setError(context.getResources().getString(R.string.pump_test_volume_error));
                }
                if (startReadingValue > endReadingValue) {
                    isError = true;
                    startReading.setError(context.getResources().getString(R.string.start_reading_greater_than_end_reading_error));
                }
                if (compareStartTimeAndEndTime(startTimeValue, endTimeValue)) {
                    isError = true;
                    startTime.setError(context.getResources().getString(R.string.start_time_greater_than_end_time_error));
                }

                if (!isError) {
                    saleDetail.start_time = startTimeValue;
                    saleDetail.end_time = endTimeValue;
                    saleDetail.product = selected_product;
                    Double rateValue = Double.parseDouble(rate_str);
                    saleDetail.rate = rateValue;
                    saleDetail.start_reading = startReadingValue;
                    saleDetail.end_reading = endReadingValue;
                    Double pumpTestVolume = Double.parseDouble(pump_test_volume);
                    saleDetail.pump_test_volume = pumpTestVolume;

                    Double salesVolume = endReadingValue - startReadingValue - pumpTestVolume;
                    saleDetail.sales_volume = salesVolume;
                    Double salesAmount = salesVolume * rateValue;
                    saleDetail.amount = salesAmount;

                    saleDetailList.set(index, saleDetail);
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

    private boolean compareStartTimeAndEndTime(String startTime, String endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            if (startDate.compareTo(endDate) > 0) {
                return true;
            } else
                return false;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDatePicker(Calendar calendar, EditText dateTimeEditText) {
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

    @Override
    public int getItemCount() {
        return Optional.ofNullable(saleDetailList).isPresent() ? saleDetailList.size() : 0;
    }

    private boolean isStringNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView start_time_textview;
        TextView end_time_textview;
        TextView product;
        TextView rate;
        TextView start_reading;
        TextView end_reading;
        TextView sale_volume;
        TextView pump_test_volume;
        TextView amount;
        LinearLayout content_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            start_time_textview = (TextView) itemView.findViewById(R.id.start_time_textview);
            end_time_textview = (TextView) itemView.findViewById(R.id.end_time_textview);
            product = (TextView) itemView.findViewById(R.id.product);
            rate = (TextView) itemView.findViewById(R.id.rate);
            start_reading = (TextView) itemView.findViewById(R.id.start_reading);
            end_reading = (TextView) itemView.findViewById(R.id.end_reading);
            sale_volume = (TextView) itemView.findViewById(R.id.sale_volume);
            pump_test_volume = (TextView) itemView.findViewById(R.id.pump_test_volume);
            amount = (TextView) itemView.findViewById(R.id.amount);
            content_layout = (LinearLayout) itemView.findViewById(R.id.content_layout);
        }
    }
}
