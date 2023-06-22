#! /bin/sh
# springboot的jar放同级目录下即可，只能有一个jar文件
CURRENT_PATH=$(cd "$(dirname "$0")"; pwd)
JAR=$(find $CURRENT_PATH -maxdepth 1 -name "*.jar")
PID=$(ps -ef | grep $JAR | grep -v grep | awk '{ print $2 }')
JVM_OPTS_BASE="-cp . -ea -Xms1024m -Xmx1024m -XX:MaxTenuringThreshold=7 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UnlockExperimentalVMOptions"
JAVA_OPTS="-XX:+UseG1GC -XX:G1LogLevel=finest -XX:+PrintHeapAtGC -XX:+UseGCLogFileRotation  -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=10240k -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -Xloggc:/home/logs/admin/gc.log"

case "$1" in
    start)
        if [ ! -z "$PID" ]; then
            echo "$JAR 已经启动，进程号: $PID"
        else
            echo -n -e "启动 $JAR ...  请查看日志确保成功\n"
            java $JVM_OPTS_BASE $JAVA_OPTS -Dserver.port=8080 -Duser.timezone=Asia/Shanghai -Dspring.profiles.active=test -jar $JAR
        fi
        ;;
    stop)
        if [ -z "$PID" ]; then
            echo "$JAR 没有在运行，无需关闭"
        else
            echo "关闭 $JAR ..."
              kill -9 $PID
            if [ "$?"="0" ]; then
                echo "服务已关闭"
            else
                echo "服务关闭失败"
            fi
        fi
        ;;
    restart)
        ${0} stop
        ${0} start
        ;;
    status)
        if [ ! -z "$PID" ]; then
            echo "$JAR 正在运行"
        else
            echo "$JAR 未在运行"
        fi
        ;;
  *)
    echo "Usage: ./springboot {start|stop|restart|status}" >&2
        exit 1
esac
