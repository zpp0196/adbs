package me.zpp0196.adbs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author zpp0196
 */
public class Utils {

    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat sft = new SimpleDateFormat("yyyy/MM/dd HH:dd:ss", Locale.getDefault());
    private static final DecimalFormat df = new DecimalFormat("####.00");

    public static String formatTime(long time) {
        return sft.format(new Date(time));
    }

    public static String formatApkSize(long size) {
        if (size < 1024) {
            return size + " bytes";
        } else if (size < 1024 * 1024) {
            float kbSize = size / 1024f;
            return df.format(kbSize) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbSize = size / 1024f / 1024f;
            return df.format(mbSize) + " MB";
        } else {
            float gbSize = size / 1024f / 1024f / 1024f;
            return df.format(gbSize) + " GB";
        }
    }

    public static String getMD5(String file) {
        try (InputStream is = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024 * 8];
            int read;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getApkSize(String file) {
        File f = new File(file);
        return Utils.formatApkSize(f.length());
    }

    @SuppressLint({"PrivateApi", "DiscouragedPrivateApi"})
    public static Context getSystemContext() throws Exception {
        Class<?> clazz = Class.forName("android.app.ActivityThread");
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        Looper.prepareMainLooper();
        Object activityThread = constructor.newInstance();

        Method method = clazz.getDeclaredMethod("getSystemContext");
        PrintStream err = System.err;
        // Prevent MIUI system error log output
        System.setErr(new PrintStream(new FileOutputStream(new File("/dev/null"))));
        Context context = (Context) method.invoke(activityThread);
        System.setErr(err);
        return context;
    }
}
