package com.eblog.demo.chatgpt.service;

import com.eblog.demo.chatgpt.model.response.PlagReport;
import com.fasterxml.jackson.databind.JsonNode;
import com.eblog.demo.chatgpt.model.response.TurnitinResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;

@Service
public class Turnitin {

	
	@SuppressWarnings("deprecation")
	public TurnitinResponse checkPlagiarism(String text) {
	    OkHttpClient client = new OkHttpClient().newBuilder()
	        .readTimeout(50000, TimeUnit.MILLISECONDS)
	        .callTimeout(50000, TimeUnit.MILLISECONDS)
	        .build();

	    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
	    RequestBody body = new FormBody.Builder()
	        .add("key", "68de9c4b705d6c07a4e367296d10c773") // Replace with your actual API key
	        .add("data", text)
	        .build();
//poojaKey="139029a3d9757e319f9f42d887bb5c6ce27d041a"
	    Request request = new Request.Builder()
	        .url("https://www.prepostseo.com/apis/checkPlag")
	        .post(body)
	        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
	        .build();

	    try (Response response = client.newCall(request).execute()) {
	        if (response.isSuccessful()) {
	            String jsonResponse = getResponseBodyAsString(response); // Use decompression if needed
	            System.out.println("Raw JSON Response: " + jsonResponse);

	            ObjectMapper objectMapper = new ObjectMapper();
	            try {
	            	
	            	 JsonNode rootNode = objectMapper.readTree(jsonResponse);

	                 TurnitinResponse turnitinResponse = new TurnitinResponse("success");
	                 Map<String, Double> urlPercentMap = new HashMap<>();
	                 double maxPercentage = 0.0;
	            	
	                 
	                 JsonNode sourcesNode = rootNode.path("sources");
	                 if (sourcesNode.isArray()) {
	                     for (JsonNode source : sourcesNode) {
	                         String link = source.path("link").asText();
	                         double percent = source.path("percent").asDouble();

	                         if (!link.isEmpty()) {
	                             urlPercentMap.put(link, percent);
	                             if (percent > maxPercentage) {
	                                 maxPercentage = percent;
	                             }
	                         }
	                     }
	                 }

	                 // Set the overall plagiarism percentage
	                 DecimalFormat decimalFormat = new DecimalFormat("#.##");
	                 String formattedOverallPercentage = decimalFormat.format(maxPercentage);
	                 turnitinResponse.setPlagiarismPercentage(formattedOverallPercentage);

	                 // Set the map of URLs and percentages
	                 System.out.println("url percentage map = "+urlPercentMap);
	                 turnitinResponse.setUrlPercentMap(urlPercentMap);
	                 return turnitinResponse;

	             } catch (Exception e) {
	                 e.printStackTrace();
	                 return new TurnitinResponse("error");
	             }
	         } else {
	             System.out.println("Request was not successful: " + response.code());
	         }
	     } catch (IOException e) {
	         e.printStackTrace();
	     }
	     return new TurnitinResponse("error");
	}
  private static String getResponseBodyAsString(Response response) throws IOException {
	    try (ResponseBody responseBody = response.body()) {
	        if (responseBody != null) {
	            if (isResponseCompressed(response)) {
	                String encoding = response.headers().get("Content-Encoding");
	                if ("gzip".equalsIgnoreCase(encoding)) {
	                    return decompressGzip(responseBody.byteStream());
	                } else if ("deflate".equalsIgnoreCase(encoding)) {
	                    return decompressDeflate(responseBody.byteStream());
	                }
	            }
	            return responseBody.string();
	        }
	    }
	    return "";
	}


  private static boolean isResponseCompressed(Response response) {
    String encoding = response.headers().get("Content-Encoding");
    return encoding != null && (encoding.equalsIgnoreCase("gzip") || encoding.equalsIgnoreCase("deflate"));
  }

  private static String decompressGzip(InputStream inputStream) throws IOException {
    GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
    return readInputStreamAsString(gzipInputStream);
  }

  private static String decompressDeflate(InputStream inputStream) throws IOException {
    InflaterInputStream inflaterInputStream = new InflaterInputStream(inputStream);
    return readInputStreamAsString(inflaterInputStream);
  }

  private static String readInputStreamAsString(InputStream inputStream) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
      stringBuilder.append(new String(buffer, 0, bytesRead));
    }
    return stringBuilder.toString();
  }
}



