package com.neo.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class JerseyClientGet {

    public static void main(String[] args) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://localhost:1000/neo-rest/rest/json/neo/get/count");
            HttpResponse response = client.execute(request);

            HttpEntity entity = response.getEntity();
            System.out.println("Returned " + EntityUtils.toString(entity));

            System.out.println("Output from Server .... \n");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
