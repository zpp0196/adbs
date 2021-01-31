if [[ -z ${ADBS_ROOT:-} ]]; then
    echo -e "\033[031m\$ADBS_ROOT is not in your \$PATH\033[0m"
    exit -1
fi

if [[ -z ${ADBS_AGENT:-} ]]; then
    echo -e "\033[031m\$ADBS_AGENT is not in your \$PATH\033[0m"
    exit -1
fi

SERVER_FILE=$ADBS_ROOT/server.apk

function server::exec() {
    local serial=$1
    shift 1
    local args=$@

    local path="/data/local/tmp/adbs_server"
    local main="me.zpp0196.adbs.Server"
    $ADBS_AGENT -s $serial push $SERVER_FILE $path &>/dev/null &&
        $ADBS_AGENT -s $serial shell CLASSPATH=$path app_process / $main $args
}
