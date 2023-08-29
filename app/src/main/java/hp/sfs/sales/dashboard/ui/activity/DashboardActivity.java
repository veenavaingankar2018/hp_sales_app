package hp.sfs.sales.dashboard.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import hp.sfs.sales.dashboard.R;
import hp.sfs.sales.dashboard.ui.fragment.OperatorFragment;

public class DashboardActivity extends AppCompatActivity {

    Button add_sale_data;
    Button add_operator;
    Button add_debtor;
    Button add_deposit;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        add_sale_data = (Button) findViewById(R.id.sale_id);
        add_operator = (Button) findViewById(R.id.operator_id);
        add_debtor = (Button) findViewById(R.id.debtor_id);
        add_deposit = (Button) findViewById(R.id.deposit_id);

        add_operator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_frame_layout, new OperatorFragment(), null);
                fragmentTransaction.commit();
            }
        });
    }
}