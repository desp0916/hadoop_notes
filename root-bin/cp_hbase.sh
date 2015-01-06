#!/bin/sh

source $HOME/bin/hadoop.sh

echo "start..."

for host in $HADOOP_HOSTS
do
	echo ${host}
#	ssh $USER@${host} 'adduser hbase'
#	ssh $USER@${host} 'passwd hbase' <<EOF
#1qaz@WSX
#1qaz@WSX
#EOF
#	ssh $USER@${host} 'usermod -G hadoop -a hbase'
#	ssh $USER@${host} 'rm -rf /home/hbase/\{bin\,/'
#	ssh $USER@${host} 'mkdir -p /home/hbase/bin /home/hbase/tmp /home/hbase/.ssh'
#       ssh $USER@${host} 'chown -R hbase:hbase /home/hbase; chmod 700 /home/hbase/.ssh'
#	ssh $USER@${host} 'tar xzvf /root/tmp/hbase-0.98.7-hadoop2-bin.tar.gz -C /usr/local'
#	ssh $USER@${host} 'mv /usr/local/hbase-0.98.7-hadoop2 /usr/local/hbase-0.98.7'
#	ssh $USER@${host} 'chown -R hbase:hadoop /usr/local/hbase-0.98.7'
#	ssh $USER@${host} 'chown -R hbase:hadoop /var/lib/hbase'
done

echo 'done.'
