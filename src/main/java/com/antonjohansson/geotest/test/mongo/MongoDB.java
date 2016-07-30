package com.antonjohansson.geotest.test.mongo;

import static com.antonjohansson.geotest.utils.RandomGenerator.COORDINATE_SCALE;
import static java.lang.String.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

import com.antonjohansson.geotest.model.GeospatialDocument;
import com.antonjohansson.geotest.model.QueryInfo;
import com.antonjohansson.geotest.model.QueryResult;
import com.antonjohansson.geotest.test.framework.ITestable;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Describes how to performance test MongoDB.
 */
public class MongoDB implements ITestable
{
    private static final int FIVE_KM = 5000000;
    private static final int PORT = 27017;

    private MongoClient client;
    private MongoCollection<BasicDBObject> collection;

    @Override
    public String getLabel()
    {
        return "MongoDB";
    }

    @Override
    public String getDockerImageName()
    {
        return "mongo";
    }

    @Override
    public int getDockerExposedPort()
    {
        return PORT;
    }

    @Override
    public void prepare(int port)
    {
        MongoCollection<BasicDBObject> collection = getCollection(port);
        BasicDBObject index = new BasicDBObject();
        index.put("location", "2dsphere");
        collection.createIndex(index);
    }

    @Override
    public void addDocumentToDatabase(GeospatialDocument document, int port)
    {
        MongoCollection<BasicDBObject> collection = getCollection(port);

        double[] location = new double[] {document.getLongitude().doubleValue(), document.getLatitude().doubleValue()};
        Date creationDate = new Date(document.getCreationDate().toInstant().toEpochMilli());

        BasicDBObject mongoDoc = new BasicDBObject();
        mongoDoc.put("_id", valueOf(document.getId()));
        mongoDoc.put("value", document.getValue());
        mongoDoc.put("creationDate", creationDate);
        mongoDoc.put("location", location);
        collection.insertOne(mongoDoc);
    }

    @Override
    public QueryResult query(QueryInfo info, int port)
    {
        MongoCollection<BasicDBObject> collection = getCollection(port);

        double[] coordinates = new double[] {info.getLongitude().doubleValue(), info.getLatitude().doubleValue()};

        BasicDBObject geometry = new BasicDBObject();
        geometry.put("type", "Point");
        geometry.put("coordinates", coordinates);

        BasicDBObject near = new BasicDBObject();
        near.put("$geometry", geometry);
        near.put("$maxDistance", FIVE_KM);

        BasicDBObject location = new BasicDBObject();
        location.put("$near", near);

        BasicDBObject filter = new BasicDBObject();
        filter.put("location", location);

        Spliterator<BasicDBObject> spliterator = collection.find(filter).spliterator();
        List<GeospatialDocument> documents = StreamSupport.stream(spliterator, false)
                .map(this::getDocument)
                .collect(toList());

        QueryResult result = new QueryResult();
        result.setDocuments(documents);
        return result;
    }

    private GeospatialDocument getDocument(BasicDBObject object)
    {
        String id = (String) object.get("_id");
        Date creationDate = (Date) object.get("creationDate");
        String value = (String) object.get("value");
        BasicDBList location = (BasicDBList) object.get("location");
        Double[] coordinates = location.toArray(new Double[2]);
        BigDecimal longitude = new BigDecimal(coordinates[0]).setScale(COORDINATE_SCALE, HALF_UP);
        BigDecimal latitude = new BigDecimal(coordinates[1]).setScale(COORDINATE_SCALE, HALF_UP);

        GeospatialDocument document = new GeospatialDocument();
        document.setId(Integer.valueOf(id));
        document.setValue(value);
        document.setCreationDate(ZonedDateTime.ofInstant(Instant.ofEpochMilli(creationDate.getTime()), ZoneOffset.UTC));
        document.setLongitude(longitude);
        document.setLatitude(latitude);
        return document;
    }

    private MongoCollection<BasicDBObject> getCollection(int port)
    {
        if (collection == null)
        {
            ServerAddress address = new ServerAddress("127.0.0.1", port);
            client = new MongoClient(address);
            MongoDatabase database = client.getDatabase("mydatabase");
            collection = database.getCollection("mycollection", BasicDBObject.class);
        }
        return collection;
    }
}
