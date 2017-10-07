package com.ammarkureja.locationtest;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MeetingActivity extends AppCompatActivity implements GetMatrixJsonData.onDataAvailable{


    EditText edttxt_from,edttxt_to;
    Button btn_get;
    String str_from,str_to;
    TextView tv_result1,tv_result2;
    final String LOCATION_KEY = "locationKey";
    private static final String TAG = "MeetingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        initialize();

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_from=edttxt_from.getText().toString();
                str_to=edttxt_to.getText().toString();
                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
                        + str_from + "&destinations="
                        + str_to + "&mode=walking&language=fr-FR&avoid=tolls&key=AIzaSyBbTU0m3Sa3NfAeLdp-aM8feaNzywVK3s8";
                new GetMatrixJsonData(MeetingActivity.this).execute(url);

            }
        });
    }

    @Override
    public void onDataAvailable(String result, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: "+status);
        if (status == DownloadStatus.OK) {
            String res[] = result.split(",");
            Double min = Double.parseDouble(res[0]) / 60;
            int dist = Integer.parseInt(res[1]) / 1000;
            tv_result1.setText("Duration= " + (int) (min / 60) + " hr " + (int) (min % 60) + " mins");
            tv_result2.setText("Distance= " + dist + " kilometers");
        }
    }

    public static Location midPoint(double lat1,double lon1,double lat2,double lon2){

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);
        Location midLocation = new Location("ammar");
        midLocation.setLatitude(lat3);
        midLocation.setLongitude(lon3);
        //print out in degrees
        System.out.println(Math.toDegrees(lat3) + " " + Math.toDegrees(lon3));
        return midLocation;
    }

    public void initialize()
    {
        edttxt_from= (EditText) findViewById(R.id.editText_from);
        edttxt_to= (EditText) findViewById(R.id.editText_to);
        btn_get= (Button) findViewById(R.id.button_get);
        tv_result1= (TextView) findViewById(R.id.textView_result1);
        tv_result2=(TextView) findViewById(R.id.textView_result2);

    }
}
