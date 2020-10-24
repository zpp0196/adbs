#!/usr/bin/env bash

adb=$(which adb)
path=$(pwd)
adbrc="$HOME/.adbrc"
xshrc="$HOME/.zshrc"

if [[ -n $1 ]]; then
    xshrc=$1
fi

if [[ ! -f $adb ]]; then
    echo "adb not found"
    exit 1
fi

if [[ ! -f $adbrc ]]; then
    cp .adbrc $adbrc
fi

sed -i "s/\${ADBS_AGENT}/${adb////\\/}/" $adbrc
sed -i "s/\${ADBS_PATH}/${path////\\/}/" $adbrc

if [[ ! -f $xshrc ]]; then
    touch $xshrc
fi

if [[ -z $(cat $xshrc | sed -n '/.*adbrc/p') ]]; then
    echo "[ -f $adbrc ] && source $adbrc" >>$xshrc
fi

echo "installed for $xshrc"
