package steam.com.stteam.http;

import android.app.Application;
import android.content.res.Resources;
import android.os.Environment;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;

import steam.com.stteam.R;

/**
 * Created by mave on 15/8/30.
 */
public class STApplication extends Application {
    public static Resources resources;
    public static String sessionId = "";
    public static String appHome;
public  static  final String userIconName= "user.jpg";
    @Override
    public void onCreate() {
        super.onCreate();
        resources = getResources();
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        appHome = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + resources.getString(R.string.app_name);
        File file = new File(appHome);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


}
