package com.ecobee.ml;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;

public class WeatherData {
    public void main() {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {

            url = new URL("https://api.darksky.net/forecast/API_TOKEN_REMOVED_FOR_PRIVACY");
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
               stringBuilder.append(line);
            }

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONObject main = jsonObject.getJSONObject("currently");
            String weat = main.getString("temperature");

            Double weatherTemp = Double.parseDouble(weat);


            System.out.println(weatherTemp);
            FileWriter fileWriter = new FileWriter("WeatherTemp.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(weatherTemp + "\n");
            printWriter.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
    }
}
