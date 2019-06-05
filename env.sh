#!/bin/sh
#
# Usage: . env.sh
#
encode() {
  local string="\${1}"
  local strlen=\${#string}
  local encoded=""

  for (( pos=0 ; pos<strlen ; pos++ )); do
    c=\${string:\$pos:1}
    case "\$c" in
      [-_.~a-zA-Z0-9] ) o="\${c}" ;;
      * )               printf -v o '%%%02x' "'\$c"
    esac
    encoded+="\${o}"
  done
  ENCODED="\${encoded}"
}

if [[ \$_ != \$0 ]]; then
	echo -n euid:
	read    euid
	echo -n password:
	read -s password
	echo

        encode "\$password"
	export HTTP_PROXY=http://\$euid:\$ENCODED@proxy.kroger.com:3128
	export HTTPS_PROXY=\$HTTP_PROXY
	export NO_PROXY=127.0.0.1,localhost,kroger.com
else
	echo usage: source \$0
fi
