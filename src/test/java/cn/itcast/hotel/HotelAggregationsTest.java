package cn.itcast.hotel;


import cn.itcast.hotel.service.IHotelService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class HotelAggregationsTest {
    private RestHighLevelClient client;

    @Test
    void testBrandAgg() throws IOException{
        String terms = "brand_agg";
        SearchRequest request = new SearchRequest("hotel");
        request.source().size(0);
        request.source().aggregation(
                AggregationBuilders
                        .terms(terms)
                        .field("brand")
                        .size(10)
        );
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        Terms brandTerms = aggregations.get(terms);
        List<? extends Terms.Bucket> buckets = brandTerms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            String brand = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            System.out.println(brand+": "+docCount);
        }
    }

    @BeforeEach
    void setUp() {
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.101.65:9200")
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }
}
