#!/usr/bin/env bash

path=$(pwd)
adbrc="$HOME/.adbrc"
xshrc="$HOME/.zshrc"

if [[ -n $1 && $1 != "-a" ]]; then
    xshrc=$1
fi

sed -i '/.*.adbrc/d' $xshrc

if [[ "$@" =~ "-a" ]]; then
    if [[ -f $adbrc ]]; then
        serial=$(sed -n '/.*ADBS_SERIAL_FILE.*/p' $adbrc | cut -d '=' -f 2)
        if [[ -f $serial ]]; then
            rm $serial
        fi
        rm $adbrc
    fi
fi

echo "uninstalled from $xshrc"
