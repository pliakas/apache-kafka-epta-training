# JDBC Source Connector 
The Kafka Connect JDBC Source connector allows you to import data from any relational database with a JDBC driver into an Apache Kafka® topic. This connector can support a wide variety of databases.

Data is loaded by periodically executing a SQL query and creating an output record for each row in the result set. By default, all tables in a database are copied, each to its own output topic. The database is monitored for new or deleted tables and adapts automatically. When copying data from a table, the connector can load only new or modified rows by specifying which columns should be used to detect new or modified data.

## Features
The source connector supports copying tables with a variety of JDBC data types, adding and removing tables from the database dynamically, whitelists and blacklists, varying polling intervals, and other settings. However, the most important features for most users are the settings controlling how data is incrementally copied from the database.

## Learning Objectives
This lab demonstrates how to receive events from MySQL database and stream them down to a PostgreSQL database and/or an Elasticsearch server using the kafka connect libaries from [Debezium](https://debezium.io). 
The JDBC Source connector includes the following features:


### JDBC Sink MySQL -> Kafka -> PostgreSQL

#### Topology
The following diagram depicts the overall architecture flow. 

```
                   +-------------+
                   |             |
                   |    MySQL    |
                   |             |
                   +------+------+
                          |
                          |
                          |
          +---------------v------------------+
          |                                  |
          |           Kafka                  |
          |                                  |
          |                                  |
          +---------------+------------------+
                          |
                          |
                          |
                          |
                  +-------v--------+
                  |                |
                  |   PostgreSQL   |
                  |                |
                  +----------------+


```

We are using Docker Compose to deploy following components
* MySQL
* Kafka
  * ZooKeeper
  * Kafka Broker
  * Kafka Connect with [Debezium](https://debezium.io/) and [JDBC](https://github.com/confluentinc/kafka-connect-jdbc) 
  
Connectors
* PostgreSQL

#### Usage

How to run:

```shell
# Start the application
docker-compose -f docker-compose-connect-jdbc.yml up

# Start PostgreSQL connector
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @jdbc-postgre-sink.json

# Start MySQL connector
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @jdbc-source-mysql.json
```

Check contents of the MySQL database:

```shell
docker-compose -f docker-compose-connect-jdbc.yml exec mysql bash -c 'mysql -u $MYSQL_USER  -p$MYSQL_PASSWORD inventory -e "select * from customers"'
+------+------------+-----------+-----------------------+
| id   | first_name | last_name | email                 |
+------+------------+-----------+-----------------------+
| 1001 | Sally      | Thomas    | sally.thomas@acme.com |
| 1002 | George     | Bailey    | gbailey@foobar.com    |
| 1003 | Edward     | Walker    | ed@walker.com         |
| 1004 | Anne       | Kretchmar | annek@noanswer.org    |
+------+------------+-----------+-----------------------+
```

Verify that the PostgreSQL database has the same content:

```shell
docker-compose -f docker-compose-connect-jdbc.yml exec postgres bash -c 'psql -U $POSTGRES_USER $POSTGRES_DB -c "select * from customers"'
 last_name |  id  | first_name |         email         
-----------+------+------------+-----------------------
 Thomas    | 1001 | Sally      | sally.thomas@acme.com
 Bailey    | 1002 | George     | gbailey@foobar.com
 Walker    | 1003 | Edward     | ed@walker.com
 Kretchmar | 1004 | Anne       | annek@noanswer.org
(4 rows)
```

##### Add a new record to mysql

Insert a new record into MySQL;
```shell
docker-compose -f docker-compose-connect-jdbc.yml exec mysql bash -c 'mysql -u $MYSQL_USER  -p$MYSQL_PASSWORD inventory'
mysql> insert into customers values(default, 'Thomas', 'Pliakas', 'tpliakas@gmail.com');
Query OK, 1 row affected (0.02 sec)
```

Verify that PostgreSQL contains the new record:

```shell
docker-compose -f docker-compose-connect-jdbc.yml exec postgres bash -c 'psql -U $POSTGRES_USER $POSTGRES_DB -c "select * from customers"'
 last_name |  id  | first_name |         email         
-----------+------+------------+-----------------------
...
Doe        | 1005 | Thomas       | tpliakas@gmail.com
(5 rows)
```

##### Record update

Update a record in MySQL:

```shell
mysql> update customers set first_name='Jane', last_name='Roe' where last_name='Doe';
Query OK, 1 row affected (0.02 sec)
Rows matched: 1  Changed: 1  Warnings: 0
```

Verify that record in PostgreSQL is updated:

```shell
docker-compose -f docker-compose-jdbc.yaml  exec postgres bash -c 'psql -U $POSTGRES_USER $POSTGRES_DB -c "select * from customers"'

 last_name |  id  | first_name |         email         
-----------+------+------------+-----------------------
 Thomas    | 1001 | Sally      | sally.thomas@acme.com
 Bailey    | 1002 | George     | gbailey@foobar.com
 Walker    | 1003 | Edward     | ed@walker.com
 Kretchmar | 1004 | Anne       | annek@noanswer.org
 Pliakas   | 1005 | Thomas     | tpliakas@gmail.com
(5 rows)
```

#### Record delete

Delete a record in MySQL:

```shell
mysql> delete from customers where email='tpliakas@gmail.com';
Query OK, 1 row affected (0.01 sec)
```

Verify that record in PostgreSQL is deleted:

```shell
docker-compose-f docker-compose-connect-jdbc.yml  exec postgres bash -c 'psql -U $POSTGRES_USER $POSTGRES_DB -c "select * from customers"'

-----------+------+------------+-----------------------
 Thomas    | 1001 | Sally      | sally.thomas@acme.com
 Bailey    | 1002 | George     | gbailey@foobar.com
 Walker    | 1003 | Edward     | ed@walker.com
 Kretchmar | 1004 | Anne       | annek@noanswer.org
(4 rows)
```

As you can see there is no longer a 'Jane Doe' as a customer.


Stop the  application:

```shell
# Shut down the cluster
docker-compose -f docker-compose-jdbc.yaml down
```

### Elasticsearch Sink ( MySQL -> Kafka -> Elastic Search)

#### Topology

The following diagram depicts the overall architecture flow. 

```
                   +-------------+
                   |             |
                   |    MySQL    |
                   |             |
                   +------+------+
                          |
                          |
                          |
          +---------------v------------------+
          |                                  |
          |           Kafka Connect          |
          |                                  |
          |                                  |
          +---------------+------------------+
                          |
                          |
                          |
                          |
                  +-------v--------+
                  |                |
                  | Elasticsearch  |
                  |                |
                  +----------------+


```
We are using Docker Compose to deploy the following components:

* MySQL
* Kafka
  * ZooKeeper
  * Kafka Broker
  * Kafka Connect with [Debezium](https://debezium.io/) and  [Elasticsearch](https://github.com/confluentinc/kafka-connect-elasticsearch) Connectors
* Elasticsearch

#### Usage

How to run:

```shell
# Start the application
docker-compose -f docker-compose-connect-elastic.yml up

# Start Elasticsearch connector
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @elastic-sink.json

# Start MySQL connector
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @jdbc-source-mysql.json
```

Check contents of the MySQL database:

```shell
docker-compose -f docker-compose-connect-elastic.yml exec mysql bash -c 'mysql -u $MYSQL_USER  -p$MYSQL_PASSWORD inventory -e "select * from customers"'
+------+------------+-----------+-----------------------+
| id   | first_name | last_name | email                 |
+------+------------+-----------+-----------------------+
| 1001 | Sally      | Thomas    | sally.thomas@acme.com |
| 1002 | George     | Bailey    | gbailey@foobar.com    |
| 1003 | Edward     | Walker    | ed@walker.com         |
| 1004 | Anne       | Kretchmar | annek@noanswer.org    |
+------+------------+-----------+-----------------------+
```

Verify that Elasticsearch has the same content:

```shell
curl 'http://localhost:9200/customers/_search?pretty'
{
  "took" : 42,
  "timed_out" : false,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "hits" : {
    "total" : 4,
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1001",
        "_score" : 1.0,
        "_source" : {
          "id" : 1001,
          "first_name" : "Sally",
          "last_name" : "Thomas",
          "email" : "sally.thomas@acme.com"
        }
      },
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1004",
        "_score" : 1.0,
        "_source" : {
          "id" : 1004,
          "first_name" : "Anne",
          "last_name" : "Kretchmar",
          "email" : "annek@noanswer.org"
        }
      },
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1002",
        "_score" : 1.0,
        "_source" : {
          "id" : 1002,
          "first_name" : "George",
          "last_name" : "Bailey",
          "email" : "gbailey@foobar.com"
        }
      },
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1003",
        "_score" : 1.0,
        "_source" : {
          "id" : 1003,
          "first_name" : "Edward",
          "last_name" : "Walker",
          "email" : "ed@walker.com"
        }
      }
    ]
  }
}

```
#### New record

Insert a new record into MySQL:

```shell
docker-compose -f docker-compose-connect-elastic.yml exec mysql bash -c 'mysql -u $MYSQL_USER  -p$MYSQL_PASSWORD inventory'
mysql> insert into customers values(default, 'Thomas', 'Pliakas', 'tpliakas@gmail.com');
Query OK, 1 row affected (0.02 sec)
```

Check that Elasticsearch contains the new record:

```shell
curl 'http://localhost:9200/customers/_search?pretty'
...
{
  "took" : 150,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 5,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1001",
        "_score" : 1.0,
        "_source" : {
          "id" : 1001,
          "first_name" : "Sally",
          "last_name" : "Thomas",
          "email" : "sally.thomas@acme.com"
        }
      },
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1004",
        "_score" : 1.0,
        "_source" : {
          "id" : 1004,
          "first_name" : "Anne",
          "last_name" : "Kretchmar",
          "email" : "annek@noanswer.org"
        }
      },
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1002",
        "_score" : 1.0,
        "_source" : {
          "id" : 1002,
          "first_name" : "George",
          "last_name" : "Bailey",
          "email" : "gbailey@foobar.com"
        }
      },
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1003",
        "_score" : 1.0,
        "_source" : {
          "id" : 1003,
          "first_name" : "Edward",
          "last_name" : "Walker",
          "email" : "ed@walker.com"
        }
      },
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1005",
        "_score" : 1.0,
        "_source" : {
          "id" : 1005,
          "first_name" : "Thomas",
          "last_name" : "Pliakas",
          "email" : "tpliakas@gmail.com"
        }
      }
    ]
  }
}
...
```

#### Record update

Update a record in MySQL:

```shell
mysql> update customers set first_name='Tom', last_name='Pli' where last_name='Pliakas';
Query OK, 1 row affected (0.02 sec)
Rows matched: 1  Changed: 1  Warnings: 0
```

Verify that record in Elasticsearch is updated:

```shell
curl 'http://localhost:9200/customers/_search?pretty'
...

     {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1005",
        "_score" : 1.0,
        "_source" : {
          "id" : 1005,
          "first_name" : "Tom",
          "last_name" : "Pli",
          "email" : "tpliakas@gmail.com"
        }
      }

...
```


#### Record delete

Delete a record in MySQL:

```shell
mysql> delete from customers where email='tpliakas@gmail.com';
Query OK, 1 row affected (0.01 sec)
```

Verify that record in Elasticsearch is deleted:

```shell
curl 'http://localhost:9200/customers/_search?pretty'

{
  "took" : 414,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 4,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1001",
        "_score" : 1.0,
        "_source" : {
          "id" : 1001,
          "first_name" : "Sally",
          "last_name" : "Thomas",
          "email" : "sally.thomas@acme.com"
        }
      },
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1004",
        "_score" : 1.0,
        "_source" : {
          "id" : 1004,
          "first_name" : "Anne",
          "last_name" : "Kretchmar",
          "email" : "annek@noanswer.org"
        }
      },
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1002",
        "_score" : 1.0,
        "_source" : {
          "id" : 1002,
          "first_name" : "George",
          "last_name" : "Bailey",
          "email" : "gbailey@foobar.com"
        }
      },
      {
        "_index" : "customers",
        "_type" : "customer",
        "_id" : "1003",
        "_score" : 1.0,
        "_source" : {
          "id" : 1003,
          "first_name" : "Edward",
          "last_name" : "Walker",
          "email" : "ed@walker.com"
        }
      }
    ]
  }
}

```



End the application:

```shell
# Shut down the cluster
docker-compose -f docker-compose-es.yaml down
```

## Two Parallel Sinks ( MySQL -> Kafka Connect -> (ElasticSearch,PostgreSQL))

### Topology
You need to implement the following architecture in docker compose file by combining the two docker-compose files `docker-compose-connect-jdbc-yml` and `docker-compose-connect-elastic.yml`

```
                   +-------------+
                   |             |
                   |    MySQL    |
                   |             |
                   +------+------+
                          |
                          |
                          |
          +---------------v------------------+
          |                                  |
          |           Kafka Connect          |
          |  (Debezium, JDBC, ES connectors) |
          |                                  |
          +---+-----------------------+------+
              |                       |
              |                       |
              |                       |
              |                       |
+-------------v--+                +---v---------------+
|                |                |                   |
|   PostgreSQL   |                |   ElasticSearch   |
|                |                |                   |
+----------------+                +-------------------+

```

### Actions

  * Implement the above architecture in docker, by creating the needed docker images. 
  * Configure the connectors using RESTful API (you can use `curl` command)
  * Execute commands as defined in previous sections for JDBC and Elasticsearch sinks and you can verify that inserts and updates are present in *both* sinks.

How to run:

```shell
# Start the application


# Start Elasticsearch connector


# Start MySQL connector


# Start PostgreSQL connector

```

Have fun :) 
