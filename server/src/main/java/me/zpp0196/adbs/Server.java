package me.zpp0196.adbs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author zpp0196
 */
public class Server {

    private enum Handler {
        PKG_INFO("pkginfo", "pi") {
            @Override
            protected void main(String[] args) throws Exception {
                if (args.length == 0) {
                    throw new IllegalArgumentException("No package name specified!");
                }
                String packageName = args[0];
                Context context = Utils.getSystemContext();
                PackageManager pm = context.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                ApplicationInfo ai = pi.applicationInfo;

                echo("应用名称", ai.loadLabel(pm), 2);
                echo("应用包名 ", packageName, 2);
                echo("应用版本", pi.versionName, 2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    echo("应用版本号", pi.getLongVersionCode(), 2);
                } else {
                    echo("应用版本号", pi.versionCode, 2);
                }
                echo("安装包路径", ai.sourceDir, 2);
                echo("安装包 MD5", Utils.getMD5(ai.sourceDir), 2);
                echo("安装包大小", Utils.getApkSize(ai.sourceDir), 2);
                echo("首次安装时间", Utils.formatTime(pi.firstInstallTime), 1);
                echo("最后更新时间", Utils.formatTime(pi.lastUpdateTime), 1);
                String installer = pm.getInstallerPackageName(packageName);
                if (installer != null) {
                    CharSequence appName = pm.getApplicationInfo(installer, 0).loadLabel(pm);
                    installer = String.format("%s(%s)", appName, installer);
                }
                echo("应用安装来源", installer, 1);
            }
        },
        APP_INFO("appinfo", "ai") {
            @Override
            protected void main(String[] args) throws Exception {
                if (args.length == 0) {
                    throw new IllegalArgumentException("No package name specified!");
                }
                String packageName = args[0];
                Context context = Utils.getSystemContext();
                PackageManager pm = context.getPackageManager();

                ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                echo("应用名称", ai.loadLabel(pm), 2);
                echo("应用包名 ", packageName, 2);
                echo("应用 UID", ai.uid, 2);
                echo("应用数据目录", ai.dataDir, 1);

                Intent launcherIntent = pm.getLaunchIntentForPackage(packageName);
                if (launcherIntent != null) {
                    ComponentName component = launcherIntent.getComponent();
                    if (component != null) {
                        echo("应用启动界面", component.getClassName(), 1);
                    }
                }
            }
        },
        GET_LAUNCHER_ACTIVITY("gla") {
            @Override
            protected void main(String[] args) throws Exception {
                if (args.length == 0) {
                    throw new IllegalArgumentException("No package name specified!");
                }
                String packageName = args[0];
                Context context = Utils.getSystemContext();
                PackageManager pm = context.getPackageManager();

                Intent launcherIntent = pm.getLaunchIntentForPackage(packageName);
                if (launcherIntent != null) {
                    ComponentName component = launcherIntent.getComponent();
                    if (component != null) {
                        System.out.println(component.getClassName());
                    }
                }
            }
        },
        SET_CLIPBOARD_DATA("scp") {
            @Override
            protected void main(String[] args) throws Exception {
                String text = "";
                if (args.length > 0) {
                    text = args[0];
                }
                Context context = Utils.getSystemContext();
                ClipboardManager cm = (ClipboardManager) context.getSystemService(android.content.Context.CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText(null, text);
                if (cm != null) {
                    cm.setPrimaryClip(data);
                }
            }
        };

        private List<String> opts;

        Handler(String... opts) {
            this.opts = Arrays.asList(opts);
        }

        protected void main(String[] args) throws Exception {
            throw new AbstractMethodError();
        }

        public static Handler of(String opt) {
            for (Handler handler : Handler.values()) {
                if (handler.opts.contains(opt)) {
                    return handler;
                }
            }
            throw new IllegalArgumentException("unknown option: " + opt);
        }

        protected void echo(String key, Object val, int tabCount) {
            System.out.print("    ");
            System.out.print(key);
            for (int i = 0; i < tabCount; i++) {
                System.out.print("\t");
            }
            System.out.println(val);
        }
    }

    public static void main(String[] args) {
        clear();
        if (args.length == 0) {
            return;
        }
        try {
            String opt = args[0];
            Handler handler = Handler.of(opt);
            String[] newArgs = new String[args.length - 1];
            System.arraycopy(args, 1, newArgs, 0, newArgs.length);
            handler.main(newArgs);
        } catch (Throwable th) {
            th.printStackTrace(System.err);
        }
    }

    private static void clear() {
        try {
            //noinspection ResultOfMethodCallIgnored
            new File("/data/local/tmp/adbs_server").delete();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
