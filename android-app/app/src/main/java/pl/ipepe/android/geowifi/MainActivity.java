package pl.ipepe.android.geowifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

public class MainActivity extends AppCompatActivity {

    ListView list_view;
    TextView last_scan_time_text_view;
    TextView last_gps_position_text_view;
    WifiManager wifi_manager;
    ArrayList<String> wifis;
    WifiScanReceiver wifi_scan_reciever;
    GpsLocationListener gps_location_listener;
    Location last_location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        list_view = (ListView) findViewById(R.id.listView);
        last_scan_time_text_view = (TextView) findViewById(R.id.lastScanTimeTextView);
        last_gps_position_text_view = (TextView) findViewById(R.id.lastGpsPositionTextView);

        wifi_manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifi_scan_reciever = new WifiScanReceiver();
        gps_location_listener = new GpsLocationListener();
        startWifiScan();
        startGpsListener();
    }

    public void startWifiScan() {
        wifi_manager.startScan();
    }

    public void startGpsListener(){
        SmartLocation.with(this).location().config(LocationParams.NAVIGATION).continuous().start(gps_location_listener);
    }

    protected void onPause() {
        unregisterReceiver(wifi_scan_reciever);
        SmartLocation.with(this).location().stop();
        super.onPause();
    }

    protected void onResume() {
        startGpsListener();
        registerReceiver(wifi_scan_reciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    private class GpsLocationListener implements OnLocationUpdatedListener {
        @Override
        public void onLocationUpdated(Location location) {
            last_location = location;
            last_gps_position_text_view.setText(
                    String.format("%s:\n%4.3f %4.3f\n%s",
                            getString(R.string.gps_scan_text),
                            location.getLatitude(),
                            location.getLongitude(),
                            new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime())));
        }
    }

    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            if(last_location != null) {
                List<ScanResult> wifiScanList = wifi_manager.getScanResults();
                last_scan_time_text_view.setText(String.format("%s:\n%s",
                        getString(R.string.wifi_scan_text),
                        new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime())));

                wifis = new ArrayList<String>();
                if (wifiScanList.size() == 0) {
                    wifis.add("Brak sieci : (");
                } else {
                    ActiveAndroid.beginTransaction();
                    try {
                        for (ScanResult wifi : wifiScanList) {
                            WifiObservation wifiObservation = new WifiObservation(wifi, last_location);
                            wifis.add(wifiObservation.toString());
                            wifiObservation.save();
                        }
                        ActiveAndroid.setTransactionSuccessful();
                    } finally {
                        ActiveAndroid.endTransaction();
                    }
                }
                list_view.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.row, wifis));
                wifi_manager.startScan();
            }
        }
    }
}