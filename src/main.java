import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class main {


    public static void main(String[] args)  {


        try {
            Weather.doHttpRequest();
            //Albums.httpRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
