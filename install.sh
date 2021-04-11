#!/usr/bin/env bash

path=$(dirname $(readlink -f "$0"))
xshrc="$HOME/.zshrc"

if [[ -n $1 ]]; then
    xshrc=$1
fi

if [[ ! -f $xshrc ]]; then
    touch $xshrc
fi

cat >> $xshrc <<EOF
export ADBS_ROOT=$path
export PATH=\$ADBS_ROOT/bin:\$PATH
EOF

echo "installed for $xshrc"
