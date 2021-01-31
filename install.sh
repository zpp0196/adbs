#!/usr/bin/env bash

adb=$(which adb)
path=$(pwd)
xshrc="$HOME/.zshrc"

if [[ -n $1 ]]; then
    xshrc=$1
fi

if [[ ! -f $adb ]]; then
    echo "adb not found"
    exit 1
fi

if [[ ! -f $xshrc ]]; then
    touch $xshrc
fi

echo "export ADBS_AGENT=$adb" >>$xshrc
echo "export ADBS_ROOT=$path" >>$xshrc
echo 'export PATH='\$ADBS_ROOT'/bin:$PATH' >>$xshrc

echo "installed for $xshrc"
