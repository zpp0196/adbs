if [[ -z ${ADBS_ROOT:-} ]]; then
    echo -e "\033[031m\$ADBS_ROOT is not in your \$PATH\033[0m"
    exit -1
fi

SERVER_FILE=$ADBS_ROOT/server.apk

function server::exec() {
    local args=$@

    local path="/data/local/tmp/adbs_server"
    local main="me.zpp0196.adbs.Server"
    $ADB push $SERVER_FILE $path &>/dev/null &&
        $ADB shell CLASSPATH=$path app_process / $main $args
}
