#!/bin/sh

export HBASE_HOME=/usr/local/hbase-0.98.7
runuser -l hbase -g hbase $HBASE_HOME/bin/stop-hbase.sh
