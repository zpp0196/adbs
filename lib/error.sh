ERR_NO_AGENT=1
ERR_NO_DEVICE=2
ERR_CANCELLED=4

function err::msg() {
    local code=$1
    case $code in
        $ERR_NO_AGENT) echo "\$ADBS_AGENT is not in your \$PATH" ;;
        $ERR_NO_DEVICE) echo "no devices/emulators found";;
        $ERR_CANCELLED) echo "cancelled" ;;
        *) echo "unknown error code: $code"
    esac
}
