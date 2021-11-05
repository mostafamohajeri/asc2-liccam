
FROM hseeberger/scala-sbt:graalvm-ce-21.3.0-java17_1.5.5_2.13.7
EXPOSE 3030
#
WORKDIR /Project

COPY . /Project

CMD sbt clean & sbt compile & sbt run