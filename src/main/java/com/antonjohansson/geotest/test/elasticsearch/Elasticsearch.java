package com.antonjohansson.geotest.test.elasticsearch;

import static com.antonjohansson.geotest.utils.RandomGenerator.COORDINATE_SCALE;
import static java.math.RoundingMode.HALF_UP;
import static java.util.stream.Collectors.toList;
import static org.apache.http.util.EntityUtils.consumeQuietly;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.antonjohansson.geotest.model.GeospatialDocument;
import com.antonjohansson.geotest.model.QueryInfo;
import com.antonjohansson.geotest.model.QueryResult;
import com.antonjohansson.geotest.test.elasticsearch.model.ElasticsearchSearchHit;
import com.antonjohansson.geotest.test.elasticsearch.model.ElasticsearchSearchResult;
import com.antonjohansson.geotest.test.elasticsearch.model.Mapping;
import com.antonjohansson.geotest.test.elasticsearch.model.MappingProperty;
import com.antonjohansson.geotest.test.elasticsearch.model.NewIndex;
import com.antonjohansson.geotest.test.framework.ITestable;
import com.antonjohansson.geotest.utils.GsonHelper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Describes how to performance test Elasticsearch.
 */
public class Elasticsearch implements ITestable
{
    private static final int PORT = 9200;
    private static final String QUERY = getQuery();
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, GsonHelper.ZDT_DESERIALIZER)
            .create();

    private CloseableHttpClient client;

    @Override
    public String getLabel()
    {
        return "Elasticsearch";
    }

    @Override
    public String getDockerImageName()
    {
        return "elasticsearch";
    }

    @Override
    public int getDockerExposedPort()
    {
        return PORT;
    }

    @Override
    public void prepare(int port)
    {
        HttpClient client = getClient();
        HttpPut request = new HttpPut("http://localhost:" + port + "/myindex");

        Map<String, MappingProperty> properties = new HashMap<>();
        properties.put("value", new MappingProperty("string"));
        properties.put("creationDate", new MappingProperty("date"));
        properties.put("location", new MappingProperty("geo_point"));

        Mapping mytype = new Mapping();
        mytype.setProperties(properties);

        Map<String, Mapping> mappings = new HashMap<>();
        mappings.put("mytype", mytype);

        NewIndex index = new NewIndex();
        index.setMappings(mappings);

        try
        {
            String json = GSON.toJson(index);
            request.setEntity(new StringEntity(json));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }

        HttpResponse response = null;
        try
        {
            response = client.execute(request);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            if (response != null)
            {
                consumeQuietly(response.getEntity());
            }
        }
    }

    @Override
    public void addDocumentToDatabase(GeospatialDocument document, int port)
    {
        HttpClient client = getClient();
        HttpPut request = new HttpPut("http://localhost:" + port + "/myindex/mytype/" + document.getId());

        String json = "{\"location\":{\"lon\":" + document.getLongitude() + ",\"lat\":" + document.getLatitude() + "},\"value\": \"" + document.getValue() + "\",\"creationDate\":"
            + document.getCreationDate().toInstant().toEpochMilli() + "}";

        try
        {
            request.setEntity(new StringEntity(json));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }

        HttpResponse response = null;
        try
        {
            response = client.execute(request);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            if (response != null)
            {
                consumeQuietly(response.getEntity());
            }
        }
    }

    @Override
    public QueryResult query(QueryInfo info, int port)
    {
        HttpClient client = getClient();
        HttpPost request = new HttpPost("http://localhost:" + port + "/myindex/mytype/_search");

        try
        {
            request.setEntity(new StringEntity(QUERY));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }

        HttpResponse response = null;
        try
        {
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String string = EntityUtils.toString(entity);
            ElasticsearchSearchResult<Map<String, Object>> result = GSON.fromJson(string, new TypeToken<ElasticsearchSearchResult<Map<String, Object>>>()
            {
            }.getType());

            List<GeospatialDocument> documents = result.getHits()
                    .getHits()
                    .stream()
                    .map(this::getDocumentFromResult)
                    .collect(toList());

            QueryResult queryResult = new QueryResult();
            queryResult.setDocuments(documents);
            return queryResult;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            if (response != null)
            {
                consumeQuietly(response.getEntity());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private GeospatialDocument getDocumentFromResult(ElasticsearchSearchHit<Map<String, Object>> result)
    {
        Map<String, Object> source = result.getSource();
        String value = (String) source.get("value");
        double creationDateDouble = (double) source.get("creationDate");
        long creationDateLong = (long) creationDateDouble;
        ZonedDateTime creationDate = ZonedDateTime.ofInstant(Instant.ofEpochMilli(creationDateLong), ZoneOffset.UTC);
        Map<String, Object> location = (Map<String, Object>) source.get("location");
        BigDecimal longitude = new BigDecimal((double) location.get("lon")).setScale(COORDINATE_SCALE, HALF_UP);
        BigDecimal latitude = new BigDecimal((double) location.get("lat")).setScale(COORDINATE_SCALE, HALF_UP);

        GeospatialDocument document = new GeospatialDocument();
        document.setId(Integer.valueOf(result.getId()));
        document.setValue(value);
        document.setCreationDate(creationDate);
        document.setLongitude(longitude);
        document.setLatitude(latitude);
        return document;
    }

    private CloseableHttpClient getClient()
    {
        if (this.client == null)
        {
            this.client = HttpClientBuilder.create()
                    .build();
        }
        return this.client;
    }

    private static String getQuery()
    {
        try (InputStream stream = Elasticsearch.class.getResourceAsStream("/ElasticsearchQuery.json"))
        {
            return IOUtils.toString(stream, "UTF-8");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
