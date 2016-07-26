# Geospatial indexing, MongoDB versus Elasticsearch

Contains tools for performance testing geospatial indexing and querying using both MongoDB and Elasticsearch.


## Setting up

First, we need instances of both MongoDB and Elasticsearch running locally. We do this using docker. So make sure docker is installed on the machine. Then execute the following commands:

```shell
docker run --detach --publish 27017:27017 --name performance-test-mongodb mongo
docker run --detach --publish 9200:9200 --name performance-test-elasticsearch elasticsearch
```
