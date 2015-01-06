export HBASE_HOME="/usr/local/hbase-0.98.7"
export HADOOP_CLASSPATH=$(${HBASE_HOME}/bin/hbase mapredcp):${HBASE_HOME}/conf:${HADOOP_CLASSPATH}
export PHOENIX_HOME=/usr/local/phoenix-4.1.0
export PATH=${PHOENIX_HOME}/hadoop2/bin:$PATH
