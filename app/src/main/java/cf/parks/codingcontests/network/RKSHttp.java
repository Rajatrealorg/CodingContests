package cf.parks.codingcontests.network;

import android.os.StrictMode;

import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by RKS.
 */

public class RKSHttp {
    public static String getOkHttp(String url) throws Exception {
        String data;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();
        URL website = new URL(url);
        Request request = new Request.Builder().url(website).build();

        try (Response response = client.newCall(request).execute()) {
            data =  response.body().string();
        }
        return data;
    }

}
