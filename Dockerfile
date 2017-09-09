FROM openjdk:8-jdk

EXPOSE 2001

WORKDIR /app

ADD . /app

RUN ./build.sh

RUN rmiregistry -J-Djava.rmi.server.hostname="$(hostname)" -J-Djava.rmi.server.codebase="file:///app/build/" 2001 &

CMD java -classpath "/app/build" br.usp.ep1.dsid.Server 1 $(hostname)
