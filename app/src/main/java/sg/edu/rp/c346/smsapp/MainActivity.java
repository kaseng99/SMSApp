package sg.edu.rp.c346.smsapp;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btn , btn2;
    EditText etTo, etContent;
    private SMSReceiver sr;
    ArrayList<String> al;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sr = new SMSReceiver();
        al = new ArrayList<>();

        btn = findViewById(R.id.buttonSend);
        btn2 = findViewById(R.id.buttonMsg);
        etTo = findViewById(R.id.editTextTo);
        etContent = findViewById(R.id.editTextContent);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String to = etTo.getText().toString().trim();
                String content = etContent.getText().toString();

                String [] split = to.split(",");
                for(int i=0 ; i<split.length;i++){

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(split[i], null, content, null, null);
                }
                Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_LONG).show();
                etContent.setText("");
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to = etTo.getText().toString().trim();
                String content = etContent.getText().toString();

                Uri smsUri = Uri.parse("tel:" + to);
                Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
                intent.putExtra("address", to);
                intent.putExtra("content", content);
                intent.setType("vnd.android-dir/mms-sms");

                    startActivity(intent);

            }
        });

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(sr, filter);

        checkPermission();
    }

    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }


}
