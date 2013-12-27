package com.github.linsolas.go2space;

import com.github.amsacode.predict4java.TLE;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeBuilder;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ElasticSearchInterface {

    private static final String ES_INDEX = "norad";
    private static final boolean CLEAR_INDEX = false;

    public static final Logger logger = LogManager.getLogger(ElasticSearchInterface.class);

    private static void loadTLEInElasticSearch(Client client, List<TLE> tles, String source) {
        int count = 0;
        for (TLE tle : tles) {
            String json = JsonUtils.toJson(tle, source);
            IndexResponse response = client.prepareIndex(ES_INDEX, "TLE", String.valueOf(tle.getCatnum()))
                    .setSource(json)
                    .execute()
                    .actionGet();
            count++;
        }
        logger.info("Source '{}' loaded {} TLEs.", source, count);
    }

    private static void clearIndex(Client client) {
        logger.info("Clearing {} index", ES_INDEX);
        // Delete index NORA
        client.admin().indices().delete(new DeleteIndexRequest(ES_INDEX)).actionGet();
        logger.info("{} index deleted", ES_INDEX);
        logger.info("Recreating {} index", ES_INDEX);
        // Recreate it, with 1 shard and 0 replicas
        CreateIndexRequest create = new CreateIndexRequest(ES_INDEX);
        create.settings(new HashMap() {{
            put("index.number_of_shards", "1");
            put("index.number_of_replicas", "0");
        }});
        client.admin().indices().create(create).actionGet();
        logger.info("{} index recreated", ES_INDEX);
    }


    public static void main(String[] args) throws Exception {
        logger.info("Hello World");

        System.exit(0);


        Client client = NodeBuilder.nodeBuilder().node().client();
        if (CLEAR_INDEX) {
            clearIndex(client);
        }
        URL resource = ElasticSearchInterface.class.getResource("/tle");

        Collection<File> files = FileUtils.listFiles(new File(resource.getPath()), new String[]{"txt"}, false);
        for (File file : files) {
            List<TLE> tles = TLE.importSat(FileUtils.openInputStream(file));
            loadTLEInElasticSearch(client, tles, StringUtils.remove(file.getName(), ".txt"));
        }

        client.close();
    }

}
