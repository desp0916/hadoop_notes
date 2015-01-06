#!/bin/sh

runuser -l hadoop -g hadoop -c /usr/local/hadoop-2.5.1/sbin/start-dfs.sh
runuser -l hadoop -g hadoop -c /usr/local/hadoop-2.5.1/sbin/start-yarn.sh
