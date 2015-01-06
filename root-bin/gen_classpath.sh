#!/bin/sh

find /usr/local/hadoop-2.5.1/etc/hadoop -print -name *.jar
find /usr/local/hadoop-2.5.1/share/hadoop/common/lib/ -print -name *.jar
find /usr/local/hadoop-2.5.1/share/hadoop/common/ -print -name *.jar
find /usr/local/hadoop-2.5.1/share/hadoop/hdfs -print -name *.jar
find /usr/local/hadoop-2.5.1/share/hadoop/hdfs/lib/ -print -name *.jar
find /usr/local/hadoop-2.5.1/share/hadoop/hdfs/ -print -name *.jar
find /usr/local/hadoop-2.5.1/share/hadoop/yarn/lib/ -print -name *.jar
find /usr/local/hadoop-2.5.1/share/hadoop/yarn/ -print -name *.jar
find /usr/local/hadoop-2.5.1/share/hadoop/mapreduce/lib/ -print -name *.jar
find /usr/local/hadoop-2.5.1/share/hadoop/mapreduce/ -print -name *.jar
find /usr/local/hadoop-2.5.1/contrib/capacity-scheduler/
