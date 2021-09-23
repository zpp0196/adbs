#!/usr/bin/env bash

./gradlew assembleDebug

cp build/outputs/apk/debug/server-debug.apk ../server.apk
