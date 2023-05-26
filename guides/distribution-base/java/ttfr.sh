#!/usr/bin/env bash

set -e

TYPE="docker"
PORT=8080
DELAY=20

usage() {
  echo "$0: Time to first request for checkpoint, native, java or docker applications"
  echo ""
  echo "  $0 [-c|-d|-j|-n] [-p port] ARTIFACT"
  echo ""
  echo "    -c : ARTIFACT is a CRaC checkpoint"
  echo "    -d : ARTIFACT is a docker image (default)"
  echo "    -j : ARTIFACT is a fat jar"
  echo "    -n : ARTIFACT is a native executable"
  echo "    -p : port to check (default 8080)"
  echo ""
}

while getopts 'cdjnp:' flag; do
  case "${flag}" in
    c) TYPE="crac" ;;
    d) TYPE="docker" ;;
    j) TYPE="java" ;;
    n) TYPE="native" ;;
    p) PORT="${OPTARG}" ;;
    *) usage
       exit 1 ;;
  esac
done
shift $(($OPTIND - 1))
echo $1

if [ $# -eq 0 ]; then
  echo "Needs the docker image or Jar file to run"
  exit 1
fi

execute() {
  local END=$((SECONDS+DELAY))
  while ! curl -o /dev/null -s "http://localhost:${PORT}"; do
    if [ $SECONDS -gt $END ]; then
      echo "No response from the app in $DELAY seconds" >&2
      exit 1
    fi
    sleep 0.001;
  done
}

mytime() {
  exec 3>&1 4>&2
  mytime=$(TIMEFORMAT="%3R"; { time $1 1>&3 2>&4; } 2>&1)
  exec 3>&- 4>&-
  echo $mytime
}

if [[ "$TYPE" == "crac" ]]; then
  java -XX:CRaCRestoreFrom=$1 &
  PID=$!
  TTFR=$(mytime execute)
  kill -9 $PID
elif [[ "$TYPE" == "java" ]]; then
  java -jar $1 &
  PID=$!
  TTFR=$(mytime execute)
  kill -9 $PID
elif [[ "$TYPE" == "docker" ]]; then
  CONTAINER=$(docker run -d --rm -p $PORT:$PORT --privileged $1)
  TTFR=$(mytime execute)
  docker container kill $CONTAINER > /dev/null
else
  $1 &
  PID=$!
  TTFR=$(mytime execute)
  kill -9 $PID
fi

if [ "$TTFR" != "" ]; then
    echo "${TTFR} seconds"
else
    exit 1
fi

