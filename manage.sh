#!/bin/bash
# This script can push or pull a folder from/to github, depending on the inputs.
input=$1
if [ "$input" = "push" ]; then
	if [ ! -z "$2" ]; then
		echo "Uploading"
		git add -A
		timeOfDay=`date +"%m|%d|%y|%T"`
		git commit -m "update $timeOfDay $2"
		git push origin master
	else
		echo "I need a commit message!"
	fi
elif [ "$input" = "pull" ]; then
	git pull
else
	echo "You have to tell me what to do"
fi
