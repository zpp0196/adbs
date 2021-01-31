if [[ -z ${ADBS_ROOT:-} ]]; then
    echo -e "\033[031m\$ADBS_ROOT is not in your \$PATH\033[0m"
    exit -1
fi

CONFIG=$ADBS_ROOT/.properties

function cfg::list() {
    [[ -f $CONFIG ]] && cat $CONFIG | grep "^[^#]"
}

function cfg::get() {
    local key=$1

    if [[ -z $key ]]; then
        log::err "no key specified"
        exit -1
    fi

    local def=$2
    if [[ ! -f "$CONFIG" ]]; then
        echo $2
        return
    fi

    local val=$(grep -P "^\s*[^#]?${key}=.*$" $CONFIG | cut -d '=' -f 1 --complement)
    if [[ -z $val ]]; then
        val=$2
    fi
    echo $val
}

function cfg::set() {
    local key=$1
    local val=$2

    if [[ -z $key ]]; then
        log::err "no key specified"
        exit -1
    fi

    if [[ -z $val ]]; then
        log::err "no value specified"
        exit -1
    fi

    if [[ ! -f $CONFIG ]]; then
        touch $CONFIG
    fi

    if [[ $(grep -P "^\s*[^#]?${key}=.*$" $CONFIG) ]]; then
        val=${val////\\/}
        sed -i "s/$key=.*/$key=${val}/g" $CONFIG
    else
        echo $key=$val >>$CONFIG
    fi
}

function cfg::remove() {
    local key=$1

    if [[ ! -f $CONFIG ]]; then
        return
    fi

    sed -i "/$key=.*/d" $CONFIG
}

function cfg::localhost() {
    cfg::get 'global.localhost' 'localhost'
}

function cfg::fzf_serial() {
    cfg::get 'fzf.serial' 'fzf --layout=reverse --inline-info --height=5'
}

function cfg::fzf_package() {
    cfg::get 'fzf.package' 'fzf --layout=reverse --inline-info --height=12'
}

function cfg::fzf_file() {
    cfg::get 'fzf.file' 'fzf --layout=reverse --inline-info --height=12'
}

function cfg::serial_file() {
    cfg::get 'global.serial.file' '/tmp/.adbs_serial'
}

function cfg::install_paths() {
    cfg::get 'install.paths' '.'
}

function cfg::pullapk_output() {
    cfg::get 'pullapk.output' './$(adb -s ${serial} pi ${pkg} --label)_${pkg}_v$(adb -s ${serial} pi ${pkg} --version-name)_$(adb -s ${serial} pi ${pkg} --md5).apk'
}

function cfg::screencap_output() {
    cfg::get 'screencap.output' './screenshot_$(date +%Y%m%d%H%M%S).png'
}

function cfg::screenrecord_output() {
    cfg::get 'screenrecord.output' './screenrecord_$(date +%Y%m%d%H%M%S).mp4'
}

function cfg::screenrecord_touches() {
    cfg::get 'screenrecord.touches' '1'
}

function cfg::screenrecord_cache() {
    cfg::get 'screenrecord.cache' '/sdcard/$(date +%Y%m%d%H%M%S).mp4'
}

SERIAL_FILE=$(cfg::serial_file)
