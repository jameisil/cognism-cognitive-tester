package com.cognism.webservices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;

@ManagedBean(name = "restClientBean")
@SessionScoped
public class RestClientBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String content = null;
    private List<Cognitive> resultList = new ArrayList<Cognitive>();

    public RestClientBean() {

    }
    public void clear(){
        resultList.clear();
    }

    public void sendContent() {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("http://localhost:1000/cognism-nlp-process/rest/nlp/post/content");
            JSONObject json = new JSONObject();
            json.put("content", content);

            StringEntity params = new StringEntity(json.toString());

            request.addHeader("content-type", "application/json");
            request.addHeader("charset", "utf8");
            request.setEntity(params);
            HttpResponse response = (HttpResponse) httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            String jsonOutput = EntityUtils.toString(entity);
            resultList = new ArrayList<Cognitive>();
            Cognitive userData = null;
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = mapper.getJsonFactory();
            JsonParser jp;
            jp = factory.createJsonParser(jsonOutput);
            JsonNode input = mapper.readTree(jp);
            Iterator<JsonNode> retList = input.getElements();
            while (retList.hasNext()) {
                userData = new Cognitive();
                JsonNode node = retList.next();
                String phrase = node.get("phrase").getTextValue();
                String sentiment = node.get("sentiment").getTextValue();
                String sentimentScore = node.get("sentimentScore").getTextValue();

                userData.setPhrase(phrase);
                userData.setSentiment(sentiment);
                userData.setSentimentScore(sentimentScore);
                resultList.add(userData);

            }
            System.out.println("Final list size : " + resultList.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Cognitive> getResultList() {
        return resultList;
    }

    public void setResultList(List<Cognitive> resultList) {
        this.resultList = resultList;
    }

    public static void main(String args[]) {
        RestClientBean test = new RestClientBean();
        test.sendContent();

    }
}
