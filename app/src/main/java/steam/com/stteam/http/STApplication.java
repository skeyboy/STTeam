package steam.com.stteam.http;

import android.app.Application;
import android.content.res.Resources;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Created by mave on 15/8/30.
 */
public class STApplication extends Application {
    public static Resources resources;
public  static String sessionId="";
    @Override
    public void onCreate() {
        super.onCreate();
        resources = getResources();
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

    }


}
