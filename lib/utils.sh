if [[ -z ${ADBS_AGENT:-} ]]; then
    echo -e "\033[031m\$ADBS_AGENT is not in your \$PATH\033[0m"
    exit -1
fi

function log::info() {
    [[ -n $1 ]] && echo -e "\033[032m$1\033[0m"
}

function log::err() {
    [[ -n $1 ]] && echo -e "\033[031merror: $1\033[0m"
}

function adb::is_device_online() {
    local serial=$1
    $ADBS_AGENT -s $serial shell "echo TEST" 2>&1 >/dev/null
}
