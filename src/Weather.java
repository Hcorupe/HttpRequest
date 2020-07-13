import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Weather {
    private static HttpURLConnection connection; // http connection

    public static void doHttpRequest() throws IOException {
       //jave.net.http.httpClient JDK 11 and up built in java API ** thread safe
        HttpClient client = HttpClient.newHttpClient(); // create client
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(    )).build(); //Built request with url
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()) //set request using client sending Async an want responce as a string // returns a CompletableFuture<HttpResponse<String>> type
                .thenApply(HttpResponse::body) // once sendAsync is done apply this method to previous result. :: is lambda expression use body from HTTP responce
                //.thenAccept(System.out::println) // prints out body
                .thenApply(Weather::parse)
                .join(); //return result of CompleteableFuture need join to print to console
    }

    public static String parse(String responseBody){

        JSONObject response = new JSONObject(responseBody);
        JSONObject coord = response.getJSONObject("coord");
        double lon = response.getJSONObject("coord").getInt("lon");
        double lat = response.getJSONObject("coord").getInt("lat");
        JSONArray weather = response.getJSONArray("weather");

        double temp = response.getJSONObject("main").getInt("temp");
        double feelsLike = response.getJSONObject("main").getInt("feels_like");
        double tempMin = response.getJSONObject("main").getInt("temp_min");
        double tempMax = response.getJSONObject("main").getInt("temp_max");
        double humidity = response.getJSONObject("main").getInt("humidity");

        String description = null;
        for(int i = 0; i < weather.length(); i++){
            description = weather.getJSONObject(i).getString("description");
        }

        String city = response.getString("name");

        System.out.println(
                "City = " + city + "\n"+ "lon = " + lon + "\n" +"lat = " + lat + "\n" +
                "Temp = " + temp + "\n" + "temp min = " + tempMin + "\n" + "temp max = "+ tempMax + "\n"+
                "Description = " + description + "\n" + "Humidity = " + humidity + "\n" + "feels like = " + feelsLike );

        return null;
    }

    public static void otherHttpRequest(){
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        try {
            URL url = new URL ("https://api.openweathermap.org/data/2.5/weather?q=Phoenix&appid=2da6df75a5baed4d6f00669489483f5d&units=imperial");
            connection = (HttpURLConnection) url.openConnection(); // open connection to URL
            // request set up
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // if connection not succesfull times out after 5 seconds
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode(); // returns success or fail code  200 success
            System.out.println("Response code = "+status);

            if(status > 299){
                reader = new BufferedReader( new InputStreamReader( connection.getErrorStream()));
                while((line = reader.readLine()) != null){ // null = not finished reading
                    responseContent.append(line);
                }
                reader.close();
            }else{
                reader = new BufferedReader( new InputStreamReader( connection.getInputStream()));
                while((line = reader.readLine()) != null){
                    responseContent.append(line);
                }
                reader.close();
            }
            //Albums.parse(responseContent.toString()); // pass to parse method
            //System.out.println(responseContent.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            connection.disconnect(); // close connection
        }

    }


}
