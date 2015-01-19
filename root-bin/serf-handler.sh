#!/bin/sh

echo
echo "New event: ${SERF_EVENT}. Data folllows..."
while read line; do
	printf "${line}\n"
done
