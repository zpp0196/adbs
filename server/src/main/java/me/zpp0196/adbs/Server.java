package me.zpp0196.adbs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
        DUMP_PKG("dumppkg", "dp") {
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

                echo("UID", ai.uid, 3);
                echo("名称", ai.loadLabel(pm), 2);
                echo("包名 ", packageName, 2);
                echo("版本名", pi.versionName, 2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    echo("版本号", pi.getLongVersionCode(), 2);
                } else {
                    echo("版本号", pi.versionCode, 2);
                }
                echo("数据路径", ai.dataDir, 2);
                String installer = pm.getInstallerPackageName(packageName);
                if (installer != null) {
                    try {
                        CharSequence appName = pm.getApplicationInfo(installer, 0).loadLabel(pm);
                        installer = String.format("%s(%s)", appName, installer);
                    } catch (PackageManager.NameNotFoundException ignored) {
                    }
                }
                echo("安装来源", installer, 2);
                String activityName = Utils.getLaunchActivityName(pm, packageName);
                if (activityName != null) {
                    echo("启动界面", activityName, 2);
                }
                echo("安装包路径", ai.sourceDir, 2);
                echo("安装包 MD5", Utils.getMD5(ai.sourceDir), 2);
                echo("安装包大小", Utils.getApkSize(ai.sourceDir), 2);
                echo("首次安装时间", Utils.formatTime(pi.firstInstallTime), 1);
                echo("最后更新时间", Utils.formatTime(pi.lastUpdateTime), 1);
            }
        },
        PKG_INFO("pkginfo", "pi") {
            @Override
            protected void main(String[] args) throws Exception {
                if (args.length == 0) {
                    throw new IllegalArgumentException("No package name specified!");
                }
                if (args.length == 1) {
                    Handler.DUMP_PKG.main(args);
                    return;
                }
                String packageName = args[0];
                String opt = args[1];
                Context context = Utils.getSystemContext();
                PackageManager pm = context.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                ApplicationInfo ai = pi.applicationInfo;

                switch (opt) {
                    case "-u":
                    case "-uid":
                        System.out.println(ai.uid);
                        return;
                    case "-l":
                    case "--label":
                        System.out.println(ai.loadLabel(pm));
                        return;
                    case "-v":
                    case "--version-name":
                        System.out.println(pi.versionName);
                        return;
                    case "-c":
                    case "--version-code":
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            System.out.println(pi.getLongVersionCode());
                        } else {
                            System.out.println(pi.versionCode);
                        }
                        return;
                    case "-d":
                    case "--data":
                        System.out.println(ai.dataDir);
                        return;
                    case "-i":
                    case "--installer":
                        System.out.println(pm.getInstallerPackageName(packageName));
                        return;
                    case "-m":
                    case "-splash":
                        String activityName = Utils.getLaunchActivityName(pm, packageName);
                        if (activityName != null) {
                            System.out.println(activityName);
                        } else {
                            System.exit(1);
                        }
                        return;
                    case "-p":
                    case "--path":
                        System.out.println(ai.sourceDir);
                        return;
                    case "-5":
                    case "--md5":
                        System.out.println(Utils.getMD5(ai.sourceDir));
                        return;
                    case "--apk-size":
                        System.out.println(Utils.getApkSize(ai.sourceDir));
                        return;
                    default:
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

        private final List<String> opts;

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
