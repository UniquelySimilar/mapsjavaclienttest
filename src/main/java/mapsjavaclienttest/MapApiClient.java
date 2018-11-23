package mapsjavaclienttest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.google.maps.GeoApiContext;
//import com.google.maps.GeocodingApi;
//import com.google.maps.errors.ApiException;
//import com.google.maps.model.GeocodingResult;

public class MapApiClient {

	public static void main(String[] args) {
		// Call the 'mapapiwebservice'
		HttpURLConnection connection = null;
		String address = "101 E. 70th ave, Denver, CO 80221";
		
		try {
			String encodedAddress = URLEncoder.encode(address, "UTF-8");
			URL url = new URL("http://localhost:8080/mapapiwebservice/mapapi/geocode?address=" + encodedAddress);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setInstanceFollowRedirects(false);
			
			int status = connection.getResponseCode();
			System.out.println("HTTP request status: " + status);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			
			String response = content.toString();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jsonParser = new JsonParser();
			JsonElement jsonElement = jsonParser.parse(response);
			String prettyJsonString = gson.toJson(jsonElement);
			System.out.println(prettyJsonString);
			
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			String formattedAddress = jsonObject.get("formattedAddress").toString();
			System.out.println(formattedAddress);
			
			JsonObject locationObject = jsonObject.getAsJsonObject("geometry").getAsJsonObject("location");
			String latitude = locationObject.get("lat").getAsString();
			System.out.println(latitude);
		}
		catch (Exception e) {
			System.err.println(e);
		}
		finally {
			if (null != connection) {
				connection.disconnect();
			}
		}
		
//		String apiKeyArg = args[0];
//		GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKeyArg).build();
//		GeocodingResult[] results = null;
//		try {
//			results = GeocodingApi.geocode(context, "1600 Amphitheatre Parkway Mountain View, CA 94043")
//					.await();
//		} catch (ApiException ae) {
//			System.err.println(ae);
//			System.exit(-1);
//		} catch (InterruptedException ie) {
//			System.err.println(ie);
//			System.exit(-1);
//		} catch (IOException ioe) {
//			System.err.println(ioe);
//			System.exit(-1);
//		}
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		System.out.println(gson.toJson(results[0].addressComponents));
//		System.out.println(gson.toJson(results[0].geometry));
//		System.out.println(gson.toJson(results[0].geometry.location));
	}

}
