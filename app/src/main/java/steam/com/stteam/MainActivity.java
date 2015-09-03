package steam.com.stteam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.platformtools.BackwardSupportUtil;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import steam.com.stteam.http.IStringRequest;
import steam.com.stteam.http.STApplication;
import steam.com.stteam.widget.NavigationFragment;


public class MainActivity extends AppCompatActivity {
    ImageView appIcon;
    TextView logShow;
    EditText nickName, password, confirmPassword;
    RequestQueue queue;
    String url = "http://192.168.1.116:8080/index.php";
    RelativeLayout mainLayout;
    ScrollView uiContainer;
    ResideMenu resideMenu;
    NavigationFragment navigationFragment;

    IUiListener tencntUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            if (o != null) {
                JSONObject qqResult = (JSONObject) o;
                MainActivity.this.logShow.setText("QQ授权:" + JSON.toJSONString(qqResult));
                Toast.makeText(MainActivity.this, o.toString(), Toast.LENGTH_LONG).show();
                int ret;
                String pay_token;
                long expires_in;
                String pfkey, access_token, openid;
                int query_authority_cost;
                try {
                    ret = qqResult.getInt("ret");
                    pay_token = qqResult.getString("pay_token");
                    expires_in = qqResult.getLong("expires_in");
                    pfkey = qqResult.getString("pfkey");
                    access_token = qqResult.getString("access_token");
                    openid = qqResult.getString("openid");
                    query_authority_cost = qqResult.getInt("query_authority_cost");
                    Log.d("授权信息", qqResult.toString());

                    String paramsStr = "?oauth_consumer_key=1104843090&access_token=" + access_token + "&openid=" + openid;
                    IStringRequest request = new IStringRequest(Request.Method.GET, "https://graph.qq.com/user/get_user_info" + paramsStr, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("授权", jsonObject.toString());
                        }
                    }, new ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
                    MainActivity.this.queue.add(request);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.main_navigation);
        navigationFragment.addLeftBarItem("发反反复复", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "HHHHHHH", Toast.LENGTH_LONG).show();
            }
        });
        navigationFragment.setOnLeftBackItemListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "开关", Toast.LENGTH_LONG).show();

                if (resideMenu.isOpened()) {
                    resideMenu.closeMenu();
                } else {
                    resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }

        resideMenu = new ResideMenu(this);
        resideMenu.attachToActivity(this);
        /*
        * 方向禁用*/
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        String titles[] = {"Home", "Profile", "Calendar", "Settings"};
        int icon[] = {R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};

        for (int i = 0; i < titles.length; i++) {
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (resideMenu.isOpened()) {
                        resideMenu.closeMenu();
                    }
                }
            });
            resideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }

        resideMenu.setMenuListener(new ResideMenu.OnMenuListener() {
            @Override
            public void openMenu() {
                Toast.makeText(MainActivity.this, "Menu is opened!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void closeMenu() {
                Toast.makeText(MainActivity.this, "Menu is closed!", Toast.LENGTH_SHORT).show();
            }
        });


        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        uiContainer = (ScrollView) findViewById(R.id.ui_container);
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        };

        uiContainer.setOnTouchListener(onTouchListener);
        mainLayout.setOnTouchListener(onTouchListener);

        queue = Volley.newRequestQueue(this);
        nickName = (EditText) findViewById(R.id.nick_name);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        logShow = (TextView) findViewById(R.id.logShow);
        appIcon = (ImageView) findViewById(R.id.app_icon);
        File userIcon = new File(STApplication.appHome + "/" + STApplication.userIconName);
        if (userIcon.exists()) {
            BitmapDrawable drawable = new BitmapDrawable(userIcon.getAbsolutePath());
            appIcon.setImageDrawable(drawable);

        }
        appIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BitmapDrawable qrBitmapDra = (BitmapDrawable) appIcon.getDrawable();
                Bitmap qrBitmap = qrBitmapDra.getBitmap();
                String appHome = STApplication.appHome;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                qrBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(appHome + "/" + STApplication.userIconName);
                    byteArrayOutputStream.writeTo(fileOutputStream);
                    byteArrayOutputStream.close();
                    fileOutputStream.flush();
                    Uri uri;
                    uri = Uri.fromFile(new File(appHome + "/" + STApplication.userIconName));
                    fileOutputStream.close();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.this.startActivity(Intent.createChooser(intent, "分享我的二维码"));
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    /*
    * QQ授权
    * */
    public void qqAuthor(View view) {
        if (!STApplication.tencentApi.isSessionValid()) {
            STApplication.tencentApi.login(this, "get_simple_userinfo", tencntUiListener);
        } else {
            Toast.makeText(this, "QQ不可用,需要授权", Toast.LENGTH_LONG).show();
        }
    }

    /*
    * 微信授权登陆*/
    public void wxAuthor(View view) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "steam_wx_login";
        STApplication.wxApi.sendReq(req);
    }

    /*
    * 关于
    * */
    public void aboutUs(View view) {
        Intent intent = new Intent(this, AboutUsActivity.class);
        startActivity(intent);
    }

    /*
    * 微信图片分享
    * */
    public void wxShareWithPhoto(View view) {
        WXImageObject imageObject = new WXImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        imageObject.imageData = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imageObject;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        STApplication.wxApi.sendReq(req);
    }

    /*
    * 微信分享文本*/
    public void wxShare(View view) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = "微信分享文本";

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = textObject.text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        STApplication.wxApi.sendReq(req);
    }

    /*
    * @param 在logo位置生成二维码
    * */
    public void renderMyQR(View view) {
        appIcon.setImageBitmap(STApplication.Util.renderMyQR("http://www.baidu.com", BarcodeFormat.QR_CODE, appIcon.getWidth(), appIcon.getHeight()));
    }

    public void toast(String tag, String msg) {
        Toast.makeText(this, tag + ":" + msg.trim().toString(), Toast.LENGTH_SHORT).show();
    }

    /*
    * @param 根据toId添加好友
    * */
    public void addFriend(View view) {
        addFriendById("5");
    }

    public void addFriendById(String toId) {
        Map<String, String> params = new HashMap<>();
        params.put("module", "app");
        params.put("c", "user");
        params.put("m", "addFriend");
        params.put("toId", toId);

        IStringRequest request = new IStringRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                toast("添加", url + jsonObject.toString());
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toast("添加失败", volleyError.getMessage());

            }
        });
        queue.add(request);

    }

    /*
    * @param 用户登录
    *
    * */
    public void userLogin(View view) {
        doIt("login");
    }

    public void doIt(String m) {
        String nickNameStr = nickName.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String confirmPasswordStr = confirmPassword.getText().toString().trim();
        if (nickNameStr.isEmpty() || passwordStr.isEmpty() || confirmPasswordStr.isEmpty()) {
            Toast.makeText(this, "请检查用户名或者密码", Toast.LENGTH_SHORT).show();
        } else {
            if (passwordStr.equals(confirmPasswordStr) && STApplication.Util.isAvailable(passwordStr, "^([0-9a-zA-Z]{6,12})$")) {
                Map<String, String> params = new HashMap<>();
                params.put("module", "app");
                params.put("c", "user");
                params.put("m", m);
                params.put("userName", nickNameStr);
                params.put("pwd", passwordStr);
                params.put("confirmPwd", confirmPasswordStr);

                Toast.makeText(this, url + JSON.toJSONString(params), Toast.LENGTH_LONG).show();


                IStringRequest strRequest = new IStringRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        Log.d("返回", json.toString());
                        JSONObject data = null;
                        try {
                            data = json.getJSONObject("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (STApplication.sessionId.isEmpty()) {
                            try {
                                String sessionId = data.getString("sessId");
                                if (!sessionId.isEmpty()) {
                                    STApplication.sessionId = sessionId;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

//

                    }
                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("返回", volleyError.getLocalizedMessage());
                    }
                });
                strRequest.setShouldCache(true);
                queue.add(strRequest);


            } else {
            }
        }
    }

    /*
    * @param 扫码添加好友
    * @param
    * */
    public void addFriendByQR(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();

    }

    /*
    * @param 用户注册
    * */
    public void userRegister(View view) {
        doIt("reg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (STApplication.tencentApi.onActivityResultData(requestCode, resultCode, data, tencntUiListener)) {
            return;
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        String content = result.getContents().trim();
        if (content == null
                || content.isEmpty()) {
            return;
        }
        Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();

        if (!content.isEmpty()) {
            if (content.contains("toId")) {
                String[] paras = content.split("=");
                logShow.setText(paras[0] + paras[1]);
                addFriendById(paras[1]);
            } else {
                logShow.setText(content);
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
