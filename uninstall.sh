#!/usr/bin/env bash

path=$(pwd)
xshrc="$HOME/.zshrc"

if [[ -n $1 ]]; then
    xshrc=$1
fi

sed -i '/.*.ADBS_AGENT/d' $xshrc
path=${path////\\/}
sed -i '/.*.'$path'\/bin/d' $xshrc

echo "uninstalled from $xshrc"
