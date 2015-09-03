package steam.com.stteam.http;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Hashtable;
import java.util.regex.Pattern;

import steam.com.stteam.R;

/**
 * Created by mave on 15/8/30.
 */
public class STApplication extends Application {
    public static Resources resources;
    public static String sessionId = "";
    public static String appHome;
    public static final String userIconName = "user.jpg";
    public static final String WX_APP_ID = "wx479cda8a45e3c667";
    public static final String TENCENT_APP_ID = "1104843090";

    /**
     * WX发布签名 b026c310d75822aa0631c40332bac875
     * WX测试签名 7fa8ee4a888213580be8d25b16e072f9
     */
    public static final String WX_APP_SECRET = "739f829103cbbd62ffd0f1c84473d791";
    public static IWXAPI wxApi;
    public static Tencent tencentApi;
    @Override
    public void onCreate() {
        super.onCreate();
        wxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        wxApi.registerApp(WX_APP_ID);

        tencentApi = Tencent.createInstance(TENCENT_APP_ID, this);


        resources = getResources();
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        appHome = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + resources.getString(R.string.app_name);
        File file = new File(appHome);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    /**
     * Created by mave on 15/8/30.
     */
    public static class Util {
        public static boolean isAvailable(String msg, String reg) {
            Pattern pattern = Pattern.compile(reg);
            return pattern.matcher(msg).matches();

        }

        public static void userLog(Object paras) {
            Log.d("参数:", paras.toString());
        }

        /*
        * @param 二维码信息
        * @param 生成的图形码格式
        * @param 图形码的宽
        * @param 图形码的高
        * */
        public static Bitmap renderMyQR(String info, BarcodeFormat barcodeFormat, int width, int height) {
            String tmp;
            Bitmap bitmap = null;
            try {
                if (info == null || info.isEmpty()) {
                    tmp = "";
                } else {
                    tmp = info;
                }

                QRCodeWriter writer = new QRCodeWriter();

                BitMatrix matrix = writer.encode(tmp, barcodeFormat, width, height);

                Hashtable<EncodeHintType, String> hints = new Hashtable<>();
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

                BitMatrix bitMatrix = new QRCodeWriter().encode(tmp, barcodeFormat, width, height, hints);
                int[] pixels = new int[width * height];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * width + x] = 0xff000000;
                        } else {
                            pixels[y * width + x] = 0xffffffff;
                        }
                    }
                }
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }
}
