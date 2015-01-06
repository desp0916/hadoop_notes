#!/bin/sh

source ${HOME}/bin/hadoop.sh

for host in $HADOOP_HOSTS
do
  test "$(hostname -s )" != ${host} && \
  echo "Copying ${1} to ${host} ...." && \
  scp ${1} ${USER}@${host}:${1}
done
