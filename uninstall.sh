#/bin/sh

sudo rm -rf $(which adbs)
if [ -n $ZSH ]; then
    sudo rm -rf $ZSH/custom/plugins/adbs
fi