FROM  amazoncorretto:8-alpine-jre

MAINTAINER kang <johnhost@163.com>

ADD ./web-api-0.0.1-SNAPSHOT.jar web.jar

ADD ./web.sh start.sh

VOLUME /home/logs

EXPOSE 8080

ENTRYPOINT ["sh", "start.sh", "start"]