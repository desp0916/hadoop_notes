#!/bin/sh
# Import tsv files into HBase
# hbase org.apache.hadoop.hbase.mapreduce.ImportTsv -Dimporttsv.columns=HBASE_ROW_KEY,attr:Title,attr:Genres movies /user/root/movielens

COLUMNS="${1}"
TABLE="${2}"
HDFSINPUTDIR="${3}"

export HADOOP_CLASS=`hbase classpath`

hbase org.apache.hadoop.hbase.mapreduce.ImportTsv -Dimporttsv.columns=${COLUMNS} ${TABLE} ${HDFSINPUTDIR}
