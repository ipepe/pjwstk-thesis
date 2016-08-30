package pl.ipepe.android.geowifi;

import android.location.Location;
import android.net.wifi.ScanResult;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;

@Table(name = "wifi_observations")
public class WifiObservation extends Model {

    @Column(name = "ssid", index=true)
    public String ssid;

    @Column(name = "bssid", index=true)
    public String bssid;

    @Column(name = "signal_level")
    public int signal_level;

    @Column(name = "capabilities")
    public String capabilities;

    @Column(name = "seen_at")
    public Date seen_at;

    @Column(name = "channel_frequency")
    public int channel_frequency;

    @Column(name = "latitude")
    public double latitude;

    @Column(name = "longitude")
    public double longitude;

    @Column(name = "exported")
    public boolean exported;

    public WifiObservation() {
        super();
    }

    public WifiObservation(ScanResult scanResult, Location location){
        super();
        this.ssid = scanResult.SSID;
        this.bssid = scanResult.BSSID;
        this.signal_level = scanResult.level;
        this.capabilities = scanResult.capabilities;
        this.seen_at = new Date();
        this.channel_frequency = scanResult.frequency;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.exported = false;
    }

    @Override
    public String toString(){
        return String.format("WO: %d %s %s", this.signal_level, this.ssid, this.bssid);
    }

    public static int count(){
        return new Select().from(WifiObservation.class).execute().size();
    }
}