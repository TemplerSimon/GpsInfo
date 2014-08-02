package com.simon.gpsinfo;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.simon.csv.file.FileRW;
import com.simon.gpsoid.utils.ConverDDToDMS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GpsInfo extends Activity implements LocationListener {

    private final static String LOG = "GpsOid";

    private LocationManager locationManager;
    private String provider;
    private TextView speed;
    private TextView latitude;
    private TextView longitude;
    private TextView avgspeed;
    private TextView height;
    private TextView gpstime;
    private FileRW file;
    private final static String SEP = "; ";
    private List<Float> speedList = new ArrayList<Float>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_info);

        speed = (TextView) findViewById(R.id.speedValue);
        latitude = (TextView) findViewById(R.id.latitudeValue);
        longitude = (TextView) findViewById(R.id.longitudeValue);
        avgspeed = (TextView) findViewById(R.id.AvgSpeedValue);
        height = (TextView) findViewById(R.id.heightValue);
        gpstime = (TextView) findViewById(R.id.gpstimeValue);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Criteria criteria = new Criteria();
        // provider = locationManager.getBestProvider(criteria, false);
        provider = LocationManager.GPS_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gps_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void start(View v) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmss");
        file = new FileRW(ft.format(new Date()));
        locationManager.requestLocationUpdates(provider, 400, 1, this);

        speedList.clear();
    }

    public void stop(View v) {
        locationManager.removeUpdates(this);
        if (file != null) {
            file.closeFile();
        }
    }

    @Override
    protected void onDestroy() {
        if (file != null) {
            file.closeFile();
        }
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location loc) {
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");

        float sp;

        sp = (float) (loc.getSpeed() * 0.001 * 3660);

        speedList.add(sp);

        latitude.setText(ConverDDToDMS.getDMS(Double.toString(loc.getLatitude())));
        longitude.setText(ConverDDToDMS.getDMS(Double.toString(loc
                .getLongitude())));
        speed.setText(sp + "");
        avgspeed.setText(getAvgSpeed(speedList));
        height.setText(loc.getAltitude() + "");
        gpstime.setText(ft.format(new Date(loc.getTime())));

        StringBuilder sb = new StringBuilder();

        sb.append(latitude.getText()).append(SEP).append(longitude.getText())
                .append(SEP).append(speed.getText()).append(SEP)
                .append(avgspeed.getText()).append(SEP)
                .append(height.getText()).append(SEP).append(gpstime.getText())
                .append(System.getProperty("line.separator"));

        file.writeData(sb.toString());

    }

    private String getAvgSpeed(List<Float> list) {
        float avgSpeed;
        avgSpeed = 0;
        for (Float el : list) {
            avgSpeed = avgSpeed + el.floatValue();
        }
        try {
            return String.valueOf(avgSpeed / list.size());
        } catch (NullPointerException e) {
            Log.e(LOG, e.getMessage());
            return String.valueOf(0);
        }
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }
}
