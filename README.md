Lagom Framework + Play Framework

This is a Scala Sbt activator project that demonstrates the legacy word count example using the Lagom Framework integrated with play where we are hitting our lagom rest services via Play.
This is a microservice based Kafka Producer/Consumer application where one is the producer which produces data in Kafka and persist events in Cassandra Db. The other service consumes data from Kafka and gives the word count of the words.
The play integration provides us a way to call our lagom services via a user interface which was not provided before.


##############################################################

Prerequisites

    Scala 2.11.8
    Sbt 0.13.13


Clone Project

    1. git clone github-url

    2. cd app-name

    3. sbt clean compile


Steps to Install and Run Zookeeper and Kafka on your system:

    1. Download kafka_2.11-0.10.0.1 From https://kafka.apache.org/downloads

    2. Extract downloaded file

tar -xzvf kafka_2.11-0.10.0.1.tgz

cd kafka_2.11-0.10.0.1

    3. Start Servers
     Start Zookeeper:

    bin/zookeeper-server-start.sh config/zookeeper.properties

     Start kafka server:

    bin/kafka-server-start.sh config/server.properties

Steps to Install and Run Cassandra on your system:

    1. * [Download Cassandra from](http://cassandra.apache.org/download/)
    
    2: Extract downloaded file

        tar -xzvf apache-cassandra-3.0.10-bin.tar.gz
        cd apache-cassandra-3.0.10/bin


    3. Start Cassandra server:

        ./cassandra

       Command to start all services:

    sbt runAll

The play routes which are used to hit the lagom rest services are :

|play routes:                      |                 lagom routes                                                    |
-----------------------------------|---------------------------------------------------------------------------------|
|localhost:9000/			       |    localhost:9000/api/hello/:id                                                 | 
|localhost:9000/change		       |    localhost:9000/api/hello/:id with body as {"message":"new-welcome-message"}  |
|localhost:9000/wordCount	       |    localhost:9000/api/wordcount                                                 |
|localhost:9000/latest		       |    localhost:9000/api/latest                                                    |     
