function log::info() {
    [[ -n $1 ]] && echo -e "\033[032m$1\033[0m"
}

function log::err() {
    [[ -n $1 ]] && echo -e "\033[031merror: $1\033[0m"
}

function adb::is_device_online() {
    $ADB -s $1 shell exit 2>/dev/null
}
