FROM ubuntu:18.04
EXPOSE 3000
RUN apt-get update
RUN echo "mysql-server mysql-server/root_password password Lillo1996" | debconf-set-selections
RUN echo "mysql-server mysql-server/root_password_again password Lillo1996" | debconf-set-selections
RUN apt-get install -y mysql-server
RUN apt-get install -y git
RUN apt-get install -y maven
RUN apt install -y openjdk-11-jdk
RUN git clone https://github.com/LeonardoMazzoni/HttpServer
RUN cd HttpServer && mvn clean compile assembly:single
CMD service mysql start && mysql -uroot -pLillo1996 < target/Database.sql && java -jar target/MyApplication-jar-with-dependencies.jar