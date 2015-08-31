package steam.com.stteam.http;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import steam.com.stteam.Util;

/**
 * Created by mave on 15/8/30.
 */
public class IStringRequest extends Request<JSONObject> {
    protected Map<String, String> params;
    protected Response.Listener<JSONObject> listener;

    public IStringRequest(int method, String url, Map<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.params = params;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> aHeader = super.getHeaders();
        Util.userLog(JSON.toJSONString(aHeader));

        return aHeader;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (!STApplication.sessionId.isEmpty()) {
            Map<String, String> newParams = new HashMap<>(this.params);
            newParams.put("sessId", STApplication.sessionId);
            Util.userLog(newParams);

            return newParams;
        }
        Util.userLog(params);

        return this.params == null ? null : this.params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String je = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Util.userLog(je);
            return Response.success(new JSONObject(je), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException var3) {
            return Response.error(new ParseError(var3));
        } catch (JSONException var4) {
            return Response.error(new ParseError(var4));
        }
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        this.listener.onResponse(jsonObject);
    }


}
