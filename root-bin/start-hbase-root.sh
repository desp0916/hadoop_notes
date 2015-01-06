#!/bin/sh

source ${HOME}/bin/hadoop.sh

export HBASE_HOME=/usr/local/hbase-0.98.7

for host in ${HADOOP_HOSTS}; do
  ssh ${USER}@${host} "rm -rf ${HBASE_HOME}/logs/*"
  echo "Deleting ${HBASE_HOME}/logs/* on ${host} ..."
done

runuser -l hbase -g hbase ${HBASE_HOME}/bin/start-hbase.sh

tail -f ${HBASE_HOME}/logs/hbase-hbase-master-stack01.localdomain.log
