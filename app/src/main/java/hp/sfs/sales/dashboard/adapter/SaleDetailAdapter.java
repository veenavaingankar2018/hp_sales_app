package hp.sfs.sales.dashboard.adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.model.SaleDetail;

public class SaleDetailAdapter extends RecyclerView.Adapter<SaleDetailAdapter.ViewHolder> {
    Context context;
    List<SaleDetail> saleDetailList;

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
        EditText product = (EditText) promptsView.findViewById(R.id.product_edit_text);
        EditText rate = (EditText) promptsView.findViewById(R.id.rate_edit_text);
        EditText startReading = (EditText) promptsView.findViewById(R.id.start_reading_edit_text);
        EditText endReading = (EditText) promptsView.findViewById(R.id.end_reading_edit_text);
        EditText salesVolume = (EditText) promptsView.findViewById(R.id.sales_volume_edit_text);
        EditText pumpTestVolume = (EditText) promptsView.findViewById(R.id.pump_test_volume_edit_text);
        EditText amount = (EditText) promptsView.findViewById(R.id.amount_edit_text);
        Button add_button = (Button) promptsView.findViewById(R.id.add_btn);
        Button cancel_button = (Button) promptsView.findViewById(R.id.cancel_btn);
        startTime.setText(saleDetail.start_time);
        endTime.setText(saleDetail.end_time);
        product.setText(saleDetail.product);
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
                saleDetailList.set(index, saleDetail);
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
        return str != null && !str.isEmpty();
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