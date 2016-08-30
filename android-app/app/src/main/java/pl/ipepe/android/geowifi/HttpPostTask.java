package pl.ipepe.android.geowifi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.File;

/**
 * Created by Patryk on 31.08.2016.
 */
public class HttpPostTask extends AsyncTask<Object, Void, Integer> {
    private Context context;
    private String url;
    private String json;
    public HttpPostTask(String url, String json, Context context){
        super();
        this.json = json;
        this.url = url;
        this.context = context;
    }
    protected Integer doInBackground(Object... params) {
        try {
            return HttpRequest.post(this.url).send(this.json).code();
        } catch (HttpRequest.HttpRequestException exception) {
            return 0;
        }
    }

    protected void onPostExecute(Integer http_code) {
        Toast.makeText(context, "HttpPostCode: "+http_code.toString(), Toast.LENGTH_SHORT).show();
        Log.i("HttpPostResult",http_code.toString());
    }
}