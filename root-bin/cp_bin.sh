#!/bin/sh

source ${HOME}/bin/hadoop.sh

for host in $HADOOP_HOSTS
do
  test "$(hostname -s )" != ${host} && \
  echo "Copying ${HOME}/bin/* to ${host} ....." && \
  scp ${HOME}/bin/* $USER@${host}:${HOME}/bin/
done
