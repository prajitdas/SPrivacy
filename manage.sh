#!/bin/bash
# This script can Push and Pull from github depending on the inputs.
input=$1
if [ "$input" = "push" ]; then
	echo "Pushing"
	git add -A
	timeOfDay=`date +"%m-%d-%Y-%T"`
	git commit -m "updated: $timeOfDay $2"
	git push origin master
elif [ "$input" = "pull" ]; then
	echo "Pulling"
	git pull
else
	echo "You have to tell me what to do"
fi
