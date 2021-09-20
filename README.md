# adbs

一个基于 [fzf](https://github.com/junegunn/fzf) 实现的 `adb` 扩展命令。

## Install

```bash
sh -c "$(curl -fsSL https://raw.githubusercontent.com/zpp0196/adbs/main/install.sh)"
```

> 命令补全请看这里：[zsh-completions](https://github.com/zpp0196/adbs/tree/main/zsh-completions/adbs)

## Usage

下面是一些常用命令的使用示例：

### serial

```bash
adbs serial
```

仅连接一台设备时，会直接输出该设备的序列号，否则选择一个设备并输出序列号。

可配合其它脚本或命令一起使用，例如 adb 或 [scrcpy](https://github.com/Genymobile/scrcpy):

```bash
adb -s $(adbs serial) shell

export ANDROID_SERIAL=$(adbs serial)
adb shell
```

```bash
scrcpy --serial $(adbs serial)
```

### disconnect

```bash
adbs disconnect
```

该命令约等于 `adb disconnect $(adbs serial)`，区别是该命令会过滤不包含 `:` 的设备。

### pkg

```bash
# 在已安装的应用包名中选择一个并输出
#   -3: 仅显示第三方应用
#   -s: 仅显示系统应用
adbs pkg [-3|-s]
# 获取当前焦点应用的包名
adbs pkg -c
```

例如：

```bash
# 停止当前应用
adb shell am force-stop $(adbs pkg -c)
# 禁用系统应用
adb shell pm disable $(adbs pkg -s)
# 卸载第三方应用
adb uninstall $(adbs pkg -3)
```

### install

```bash
adbs install [apk]
```

如果不指定安装包的路径，则默认在当前目录下选择一个 apk 进行安装。

### pullapk

```bash
adbs pullapk [pkg]
```

将设备上安装的应用安装包提取到当前目录，并自动重命名，文件名包括：应用名称、应用包名、应用版本、MD5 等信息。

### dumppkg

```bash
adbs dumppkg [pkg]
```

获取指定应用的安装信息，例如：

```bash
$ adbs dumppkg android
    UID			1000
    名称		Android 系统
    包名 		android
    版本名		11
    版本号		30
    数据路径		/data/system
    安装来源		null
    安装包路径		/system/framework/framework-res.apk
    安装包 MD5		9e326abe674d5e826136dae97f1a907f
    安装包大小		9.14 MB
    首次安装时间	2009/01/01 08:01:00
    最后更新时间	2009/01/01 08:01:00
```

### pkginfo

```bash
adbs pkginfo $(adbs pkg) <opt>
```

获取指定应用的部分安装信息，例如：

```bash
# 获取 UID
$ adbs pkginfo android -u
1000
# 获取安装包路径
$ adbs pkginfo android -p
/system/framework/framework-res.apk
```

### screencap

```bash
adbs screencap [path]
```

屏幕截图并保存到当前目录。

### screenrecord

```bash
adbs screenrecord [-o <path>] [recordoptions]
```

录制屏幕并保存到当前目录，按 `Ctrl+C` 停止。例如：

```bash
$ adbs screenrecord -o tmp.mp4 --size 1080x1920 --bit-rate 8M --verbose
Display is 2340x1080 @60.00fps (orientation=ROTATION_90), layerStack=0
Configuring recorder for 1080x1920 video/avc at 8.00Mbps
Content area is 1080x498 at offset x=0 y=711
^C
tmp.mp4
```

### other

除了以上命令外，其他的 adb 命令也可以直接使用。例如：

```bash
adbs devices
adbs kill-server
adbs root
```

## License

```
Copyright 2020-2021 zpp0196

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
