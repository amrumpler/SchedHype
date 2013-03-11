#!/bin/sh

pwd=`pwd`
osascript -e "tell application \"Terminal\"" \
    -e "tell application \"System Events\" to keystroke \"t\" using {command down}" \
    -e "do script \"cd $pwd; clear\" in front window" \
    -e "do script \"echo hello\" in front window" \
    -e "do script \"cd $pwd/src/main/webapp\" in front window" \
    -e "do script \"coffee -cwo js coffee\" in front window" \
    -e "end tell"
    > /dev/null

osascript -e "tell application \"Terminal\"" \
    -e "tell application \"System Events\" to keystroke \"t\" using {command down}" \
    -e "do script \"cd $pwd; clear\" in front window" \
    -e "do script \"echo hello\" in front window" \
    -e "do script \"cd $pwd/src/main/webapp\" in front window" \
    -e "do script \"compass watch --poll\" in front window" \
    -e "end tell"
    > /dev/null