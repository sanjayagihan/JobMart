package com.app.JobMart.repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.app.JobMart.model.Post;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

@Component
public class SearchRepositoryImpl implements SearchRepository{

    @Autowired
    MongoClient client;

    @Autowired
    MongoConverter converter;

    @Override
    public List<Post> findByText(String text) {
        final List<Post> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("JobMart");
        MongoCollection<Document> collection = database.getCollection("Jobs");

        // Use regex for searching
        Document regexQuery = new Document("$or", Arrays.asList(
                new Document("techs", new Document("$regex", text).append("$options", "i")),
                new Document("desc", new Document("$regex", text).append("$options", "i")),
                new Document("profile", new Document("$regex", text).append("$options", "i"))
        ));

        // Perform the query and sort by experience
        collection.find(regexQuery)
                .sort(new Document("exp", 1))
                .limit(5)
                .forEach(doc -> posts.add(converter.read(Post.class, doc)));

        return posts;
    }

}