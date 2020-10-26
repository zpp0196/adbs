# adbs

基于 [fzf](https://github.com/junegunn/fzf) 实现的 `adb` 扩展命令。

## install

* [fzf](https://github.com/junegunn/fzf)

```bash
$ git clone https://github.com/zpp0196/adbs.git && cd adbs
$ git clone git@github.com:zpp0196/adbs.git && cd adbs
# zsh
$ ./install.sh && source ~/.zshrc
# bash
$ ./install.sh ~/.bashrc && source ~/.bashrc
```

## help

### devices

```bash
$ adb ds [-l]
```

该命令等价于 `adb devices [-l]`。

### serial

```bash
$ adb serial select [<serial>]
$ adb sl [<serial>]
```

如果指定的设备在线则直接输出序列号，否则选择一个设备并输出序列号。

```bash
$ adb -sl <cmd>
```

该命令等价于 `adb -s $(adb sl) <cmd>`。

```bash
$ adb serial set [<serial>]
$ adb ss [<serial>]
```

设置默认的设备序列号，从此告别 `adb -s <serial>`。

如果需要在不修改默认序列号的情况下选择其它设备，请使用 `adb -sl`。

> 序列号默认保存到 `/tmp/.adbs_serial`，该路径可在 `~/.adbrc` 中修改。

```bash
$ adb serial get [-n]
$ adb gs [-n]
```

查看设置的默认序列号。

> 没有 `-n`: 输出颜色为绿色表示该设备在线，红色则表示该设备不在线。
>
> 有 `-n`: 如果该设备在线则直接输出序列号，否则选择一个设备并输出序列号。

```bash
$ adb serial clear
$ adb cs
```

清除设置的默认设备序列号。

### connect-localhost

```bash
$ adb (connect-localhost | cl) <port>
```

该命令等价于 `adb connect localhost:<port>`。

### disconnect

```bash
$ adb (disconnect | dc) [<serial> | -a]
```

断开连接指定的设备。

如果未指定设备序列号，则会列出所有的设备以供选择。

> `-a`: 断开所有设备。

> 使用 `adb disconnect` 时需要关闭兼容模式！

### disconnect-ssh

```bash
$ adb (disconnect-ssh | dcs) [<port>]
```

断开本地 ssh 连接的设备，并结束对应的 `ssh` 进程。

如果未指定本地端口号，则会列出所有的 `localhost` 设备以供选择。

### install

```bash
$ adb (install | is) [<apkfile>] [<options>]
```

安装指定路径的 apk。

如果未指定安装包路径，则会列出该目录及子目录下所有的 `.apk` 文件以供选择。

eg.

```bash
$ adb is -r
$ adb install foo.apk -r -t
```

> 使用 `adb install` 时需要关闭兼容模式！

### pkg

```bash
$ adb pkg [-c | -s | -3]
```

列出该设备中已安装的应用包名列表以供选择。

> `-c`: 仅显示当前应用。
>
> `-s`: 仅列出系统应用。
>
> `-3`: 仅列出第三方应用。
>
> 下面凡是参数为 `[<pkg>]` 的都可以替换为 `[<pkg> | -c | -s | -3]`。

eg.

```bash
$ adb serial set
$ adb shell am force-stop $(adb pkg -c)
$ adb shell pm clear $(adb pkg -3)
$ adb shell pm disable $(adb pkg -s)
```

### uninstall

```bash
$ adb (uninstall | us) [<pkg>] [-k]
```

卸载指定包名的应用。

如果未指定包名，则会列出已安装的应用包名以供选择。

> 使用 `adb uninstall` 时需要关闭兼容模式！

### pullapk

```bash
$ adb (pullapk | pp) [<pkg>]
```

从设备中提取安装包。

如果未指定包名，则会列出已安装的应用包名以供选择。

> 提取后的 apk 文件名为 `${pkg}_v${versionName}.apk`。

### appinfo

```bash
$ adb (appinfo | ai) [<pkg>]
```

查看指定包名的应用信息。

如果未指定包名，则会列出已安装的应用包名以供选择。

### pkginfo

```bash
$ adb (pkginfo | pi) [<pkg>]
```

查看指定包名的应用安装包信息。

如果未指定包名，则会列出已安装的应用包名以供选择。

### startapp

```bash
$ adb (statrtapp | start) [<pkg>] [<options>]
```

启动指定包名的应用。

如果未指定包名，则会列出已安装的应用包名以供选择。

> `options`: see `adb shell cmd activity help`。

eg.

```bash
# 重启应用
$ adb start -S
```

### appsetting

```bash
$ adb (appsetting | as) [<pkg>]
```

打开指定包名的设置详情页。

如果未指定包名，则会列出已安装的应用包名以供选择。

### devopt

```bash
$ adb (devopt | dev)
```

打开开发者选项界面。

> 需要在设置中连点版本号并进入开发者模式！

### openurl

```bash
$ adb (openurl | url) [<url>]
```

启动默认浏览器并打开指定的链接。

> 链接地址需要以 `http://` 或 `https://` 开头，默认以 `http` 协议打开。

### setclip

```bash
$ adb (setclip | scp) [text] [-p] [-c]
```

设置剪切板中的内容。

> `-p`: 发送粘贴事件。
>
> `-i`: 发送粘贴事件并清除剪切板内容。

### screencap

```bash
$ adb (screencap | sc) [<dateformat>]
```

屏幕截图。

> `dateformat`: 截图文件名时间戳部分的格式，默认为 `+%Y%m%d%H%M%S`，可在 `~/.adbrc` 中修改。

### winrecord

```bash
$ adb (winrcord | wr)
```

启动 Window 记录服务。

> Ctrl + C 停止记录。

### screenrecord

```bash
$ adb (screenrecord | sr) [<dateformat>] [<recordoptions>]
$ adb (screenrecord | sr) [--help | -h]
```

屏幕录制。

> `dateformat`: 视频文件名时间戳部分的格式，默认为 `+%Y%m%d%H%M%S`，可在 `~/.adbrc` 中修改。
>
> `recordoptions`: see `adb sr -h`。

eg.

```bash
$ adb sr
$ adb sr --bit-rate 8M
$ adb screenrecord +%Y%m%d%H%M%S --size 1080x1920 --bit-rate 8M --verbose
```

## uninstall

```bash
# zsh
$ $ADBS_PATH/uninstall.sh [-a]
# bash
$ $ADBS_PATH/uninstall.sh ~/.bashrc [-a]
```

卸载后需要重启 shell！

> `-a`: 删除 `~/.adbrc` 文件。
