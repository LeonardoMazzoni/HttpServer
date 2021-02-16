FROM ubuntu:18.04 
RUN apt-get update
RUN apt-get install -y default-jre
RUN echo "mysql-server mysql-server/root_password password Lillo1996" | debconf-set-selections
RUN echo "mysql-server mysql-server/root_password_again password Lillo1996" | debconf-set-selections
RUN apt-get install -y mysql-server
COPY MyApplication-jar-with-dependencies.jar /root/
COPY Database.sql /root/
CMD service mysql start && mysql -uroot -pLillo1996 < /root/Database.sql && java -jar /root/MyApplication-jar-with-dependencies.jar