package com.ecobee.ml;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
public class EcobeeTempData {

    private static String ACCESS_TOKEN = getAccessToken();
    private static Integer responseCode;
    private static String REFRESH_TOKEN = getRefreshToken();
    public static Double pollData() {
        Double actualTemp = 1.0;

        try {
            URL url = new URL("https://api.ecobee.com/1/thermostat?json" +
                    "={\"selection\":{\"includeAlerts\":\"true\",\"selectionType\":\"registered\",\"selectionMatch\":\"\",\"includeEvents\":\"true\",\"includeSettings\":\"true\",\"includeRuntime\":\"true\"}}");
            //get connection to the url
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //set header values
            httpURLConnection.setRequestProperty("Content-Type","text/json");
            //set auth token
            httpURLConnection.setRequestProperty("Authorization","Bearer " + ACCESS_TOKEN);
            httpURLConnection.setRequestMethod("GET");
            responseCode = httpURLConnection.getResponseCode();


            if (responseCode == 500){
                autoRefressh(REFRESH_TOKEN);
            }else {
                FileWriter fileWriter = new FileWriter("Tokens.txt", false);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.println(ACCESS_TOKEN);
                printWriter.println(REFRESH_TOKEN);
                printWriter.close();
                fileWriter.close();
            }

            //get data from website
            InputStream ip = new BufferedInputStream(httpURLConnection.getInputStream());

            //read the data
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ip));


            StringBuilder response = new StringBuilder();
            int responseSingle = 0;
            //read each line of JSON
            while ((responseSingle = bufferedReader.read()) != -1) {
                response.append((char) responseSingle);
            }

            //Parse JSON to get current in house temperature
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray thermostatList =jsonObject.getJSONArray("thermostatList");
            JSONObject first = thermostatList.getJSONObject(0);
            JSONObject temp = first.getJSONObject("runtime");
            String id = temp.getString("actualTemperature");
            actualTemp =  (Double.parseDouble(id)/10);

            getRefreshToken();
            FileWriter fileWriter = new FileWriter("EcobeeTemp.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(actualTemp);
            printWriter.close();
            fileWriter.close();
            WeatherData weatherData = new WeatherData();
            weatherData.main();

            return actualTemp;


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return actualTemp;
    }
    private static void autoRefressh(String refreshToken){
        try{
            if(responseCode == 500){
                URL url = new URL("https://api.ecobee.com/token?grant_type=refresh_token&refresh_token=" + refreshToken +"API_TOKEN_REMOVED_FOR_PRIVACY");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                int number = 0;
                StringBuilder response = new StringBuilder();
                while ((number = bufferedReader.read()) != -1){
                    response.append((char) number);
                }
                JSONObject jsonObject = new JSONObject(response.toString());
                String updatedAccessToken = jsonObject.getString("access_token");
                String updatedRefreshToken = jsonObject.getString("refresh_token");

                File deleteToken = new File("Tokens.txt");
                deleteToken.delete();
                FileWriter fileWriter = new FileWriter("Tokens.txt");
                fileWriter.write(updatedAccessToken+"\n");
                fileWriter.write(updatedRefreshToken);
                fileWriter.close();
                readFile();
//                pollData();

            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private static void readFile(){
        try {
            FileReader fileReader = new FileReader("Tokens.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ACCESS_TOKEN =bufferedReader.readLine();
            REFRESH_TOKEN = bufferedReader.readLine();
            pollData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getAccessToken(){
        String fileAccessToken ="";
        try {
            FileReader fileReader = new FileReader("Tokens.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            fileAccessToken= bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileAccessToken;
    }
    private static String getRefreshToken(){
        String fileRefreshToken ="";
        try {
            FileReader fileReader = new FileReader("Tokens.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            fileRefreshToken = bufferedReader.readLine();
            fileRefreshToken = bufferedReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileRefreshToken;
    }
    public static void main(String[] args) throws IOException {
        pollData();


    }
}
