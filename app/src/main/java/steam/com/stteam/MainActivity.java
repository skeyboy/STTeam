package steam.com.stteam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText nickName, password, confirmPassword;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        nickName = (EditText) findViewById(R.id.nick_name);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);


    }

    public void userRegister(View view) {
        String nickNameStr = nickName.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String confirmPasswordStr = confirmPassword.getText().toString().trim();
        if (nickNameStr.isEmpty() || passwordStr.isEmpty() || confirmPasswordStr.isEmpty()) {
            Toast.makeText(this, "请检查用户名或者密码", Toast.LENGTH_SHORT).show();
        } else {
            if (passwordStr.equals(confirmPasswordStr)) {
                String url = "http://192.168.1.116:8080/index.php?module=app&c=user&m=reg&userName=" + nickNameStr + "&pwd=" + passwordStr + "&confirmPwd=" + confirmPasswordStr;
//                url = "http://192.168.1.116:8080/index.php";
                Map<String, String> params = new HashMap<>();
                params.put("module", "app");
                params.put("c", "user");
                params.put("userName", nickNameStr);
                params.put("pwd", passwordStr);
                params.put("confirmPwd", confirmPasswordStr);
                JSONObject object = new JSONObject(params);

                Toast.makeText(this, url, Toast.LENGTH_LONG).show();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(MainActivity.this, "成功：" + jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity.this, "失败" + volleyError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(request);
            } else {
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
