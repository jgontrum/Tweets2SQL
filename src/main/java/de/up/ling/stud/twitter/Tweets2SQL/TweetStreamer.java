/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.up.ling.stud.twitter.Tweets2SQL;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Wrapper for all methods to connect to the Twitter streaming API.
 * Just call the startStreaming method with a function object and let 
 * the fun begin! 
 * Have a look at the JavaDocs for more details:
 * http://twitter4j.org/javadoc/twitter4j/
 * 
 * @author Johannes Gontrum <gontrum@uni-potsdam.de>
 */
public class TweetStreamer {
    public static void stream(String[] apiKeys, String[] stopwords, long[] users, double[][] coordinates, BiConsumer<Status, String> fn) throws InterruptedException {
        // Save the arguments
        String consumerKey = apiKeys[0];
        String consumerSecret = apiKeys[1];
        String token = apiKeys[2];
        String secret = apiKeys[3];

        // Store the configuration
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.setDebugEnabled(true);
        config.setOAuthConsumerKey(consumerKey);
        config.setOAuthConsumerSecret(consumerSecret);
        config.setOAuthAccessToken(token);
        config.setOAuthAccessTokenSecret(secret);
        
        config.setJSONStoreEnabled(true);
        
        // Create a status listener. When a new tweet arives, the function object will accept it.
        StatusListener listener = new TwitterStatusListener(fn);

        // Create the filter. Can filter:
        //  * by Users (follow[])
        //  * by Stopword (track[])
        //  * by Location (locations[][])
        //  * by Language (language[])
        FilterQuery filter = new FilterQuery();
        filter.track(stopwords);
        filter.follow(users);
        filter.locations(coordinates);
        
        // Create the stream-object
        // Documentation: http://twitter4j.org/javadoc/twitter4j/TwitterStream.html
        TwitterStream stream = new TwitterStreamFactory(config.build()).getInstance();

        stream.addListener(listener);
        stream.filter(filter);
    }
}
