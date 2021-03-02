ERR_NO_DEVICE=2
ERR_CANCELLED=4

function err::msg() {
    local code=$1
    case $code in
        $ERR_NO_DEVICE) echo "no devices/emulators found";;
        $ERR_CANCELLED) echo "cancelled" ;;
        *) echo "unknown error code: $code"
    esac
}
