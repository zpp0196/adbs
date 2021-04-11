#!/usr/bin/env bash

path=$(dirname $(readlink -f "$0"))
xshrc="$HOME/.zshrc"

if [[ -n $1 ]]; then
    xshrc=$1
fi

sed -i -e '/.*.ADBS_ROOT/d' $xshrc
path=${path////\\/}
sed -i -e '/.*.'$path'\/bin/d' $xshrc

echo "uninstalled from $xshrc"
