#!/usr/bin/env bash
OPTIMUS_HOME="/home/sbhowmick/tmp/optimus-server"
OPTIMUS_ENV="dev"
OPTIMUS_CFG_FILE="config.yml"
OPTIMUS_PORT=$2
OPTIMUS_LIBPATH="$OPTIMUS_HOME/lib"
OPTIMUS_CFGPATH="$OPTIMUS_HOME/conf"
OPTIMUS_PIDFILE="$OPTIMUS_HOME/optimus-server.pid"
KILL=/bin/kill

OPTIMUS_MAIN="com.optimuscode.thrift.server.OptimusPrimeRpcServer"

OPTIMUS_DAEMON_OUT="$OPTIMUS_HOME/optimus-deamon.log"

HEAP_OPTS="-Xmx128m -Xms128m -XX:NewSize=64m -XX:MaxPermSize=64m"

GC_OPTS="-XX:+UseConcMarkSweepGC -verbosegc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+UseParNewGC -Xloggc:gc.log"

JAVA_OPTS="-server $GC_OPTS $HEAP_OPTS"

### Add lib/jars to classpath
for i in ${OPTIMUS_LIBPATH}/*.jar
do
    CLASSPATH="$i:$CLASSPATH"
done

### Add conf file to classpath
for i in ${OPTIMUS_CFGPATH}/*.*
do
    CLASSPATH="$i:$CLASSPATH"
done

case $1 in
    start)
        echo -n "Starting Optimus Prime server..."
    if [ -f $OPTIMUS_PIDFILE ]; then
      if kill -0 `cat $OPTIMUS_PIDFILE` > /dev/null 2>&1; then
         echo $command already running as process `cat $OPTIMUS_PIDFILE`.
         exit 0
      fi
    fi
    nohup $JAVA_HOME/bin/java -cp "$CLASSPATH" $JAVA_OPTS \
        $OPTIMUS_MAIN --env="$OPTIMUS_ENV" --config="$OPTIMUS_CFG_FILE" --port="$OPTIMUS_PORT" \
                    > "$OPTIMUS_DAEMON_OUT" 2>&1 < /dev/null &
    if [ $? -eq 0 ]
    then
      if /bin/echo -n $! > "$OPTIMUS_PIDFILE"
      then
        sleep 1
        echo STARTED
      else
        echo FAILED TO WRITE PID
        exit 1
      fi
    else
      echo "OPTIMUS PRIME SERVER DID NOT START"
      exit 1
    fi
    ;;
    stop)
    echo -n "Stopping Optimus Prime server..."
    if [ ! -f "$OPTIMUS_PIDFILE" ]
    then
      echo "no zookeeper to stop (could not find file $OPTIMUS_PIDFILE)"
    else
      $KILL -9 $(cat "$OPTIMUS_PIDFILE")
      rm "$OPTIMUS_PIDFILE"
      echo STOPPED
    fi
    exit 0
    ;;
    restart)
    shift
    "$0" stop ${@}
    sleep 5
    "$0" start ${@}
    ;;

esac