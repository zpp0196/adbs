#/bin/sh

set -e

# Default settings
ADBS=${ADBS:-~/.adbs}
REPO=${REPO:-zpp0196/adbs}
REMOTE=${REMOTE:-https://github.com/${REPO}.git}
BRANCH=${BRANCH:-main}

command_exists() {
	command -v "$@" >/dev/null 2>&1
}

fmt_warn() {
  printf '%sWarn: %s%s\n' "$BOLD$YELLOW" "$*" "$RESET" >&2
}

fmt_error() {
  printf '%sError: %s%s\n' "$BOLD$RED" "$*" "$RESET" >&2
}

fmt_underline() {
  printf '\033[4m%s\033[24m\n' "$*"
}

fmt_code() {
  # shellcheck disable=SC2016 # backtic in single-quote
  printf '`\033[38;5;247m%s%s`\n' "$*" "$RESET"
}

setup_color() {
	# Only use colors if connected to a terminal
	if [ -t 1 ]; then
		RED=$(printf '\033[31m')
		GREEN=$(printf '\033[32m')
		YELLOW=$(printf '\033[33m')
		BLUE=$(printf '\033[34m')
		BOLD=$(printf '\033[1m')
		RESET=$(printf '\033[m')
	else
		RED=""
		GREEN=""
		YELLOW=""
		BLUE=""
		BOLD=""
		RESET=""
	fi
}

setup_adbs() {
  # Prevent the cloned repository from having insecure permissions. Failing to do
  # so causes compinit() calls to fail with "command not found: compdef" errors
  # for users with insecure umasks (e.g., "002", allowing group writability). Note
  # that this will be ignored under Cygwin by default, as Windows ACLs take
  # precedence over umasks except for filesystems mounted with option "noacl".
  umask g-w,o-w

  echo "${BLUE}Cloning adbs...${RESET}"

  command_exists git || {
    fmt_error "git is not installed"
    exit 1
  }

  ostype=$(uname)
  if [ -z "${ostype%CYGWIN*}" ] && git --version | grep -q msysgit; then
    fmt_error "Windows/MSYS Git is not supported on Cygwin"
    fmt_error "Make sure the Cygwin git package is installed and is first on the \$PATH"
    exit 1
  fi

  git clone -c core.eol=lf -c core.autocrlf=false \
    -c fsck.zeroPaddedFilemode=ignore \
    -c fetch.fsck.zeroPaddedFilemode=ignore \
    -c receive.fsck.zeroPaddedFilemode=ignore \
    --depth=1 --branch "$BRANCH" "$REMOTE" "$ADBS" || {
    fmt_error "git clone of adbs repo failed"
    exit 1
  }
  echo
}

setup_env() {
  sudo ln -snf $ADBS/bin/adbs /usr/local/bin/adbs
}

setup_completions() {
  if [ -n $ZSH ]; then
    sudo ln -snf $ADBS/zsh-completions/adbs $ZSH/custom/plugins/adbs
  fi
}

main() {
    setup_color

    if ! command_exists adb; then
        fmt_warn "adb is not installed."
    fi

    setup_adbs
    setup_env
    setup_completions
    adbs version && echo "${GREEN}adbs installed.${RESET}"
}

main "$@"