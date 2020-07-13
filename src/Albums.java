import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Albums {

    public static void httpRequest() throws IOException {
        //jave.net.http.httpClient JDK 11 and up built in java API ** thread safe
        HttpClient client = HttpClient.newHttpClient(); // create client
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://jsonplaceholder.typicode.com/albums")).build(); //Built request with url
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()) //set request using client sending Async an want responce as a string // returns a CompletableFuture<HttpResponse<String>> type
                .thenApply(HttpResponse::body) // once sendAsync is done apply this method to previous result. :: is lambda expression use body from HTTP responce
                //.thenAccept(System.out::println) // prints out body
                .thenApply(Albums::parse)
                .join(); //return result of CompleteableFuture need join to print to console

    }

    public static String parse(String responseBody){
        JSONArray albums = new JSONArray(responseBody);

        for (int i = 0; i < albums.length(); i++){
            JSONObject albumObj = albums.getJSONObject(i);
            int id = albumObj.getInt("id");
            int userId = albumObj.getInt("userId");
            String title = albumObj.getString("title");
            System.out.println(id + "  " + title + "  " + userId);

        }
        return null;
    }


}
