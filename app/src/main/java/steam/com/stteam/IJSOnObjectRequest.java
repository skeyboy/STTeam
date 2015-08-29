package steam.com.stteam;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mave on 15/8/30.
 */
public class IJSOnObjectRequest extends JsonObjectRequest {

    public IJSOnObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public IJSOnObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

//        Map<String, String> superMap = super.getHeaders();
        Map<String, String> iHeader = new HashMap<>();
        iHeader.put("Content-Type", "application/*");
        iHeader.put("Accept - Encoding", "gzip, deflate ");
        ;
        Log.d("请求头", iHeader.toString());
        return iHeader;
    }
}
