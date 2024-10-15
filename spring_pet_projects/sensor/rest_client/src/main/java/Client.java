import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class Client {
    public static void main(String[] args ) {
        final String sensorName = "SensorName";
        registerSensor(sensorName);
        for (int i = 0; i < 500; i++) {
            sendMeasurement(i % 100,
                    true, sensorName);
        }
    }

    private static void registerSensor(String name) {
        final String url = "http://localhost:8080/sensors/registration";
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", name);
        makePostRequestWithJSONData(url, jsonData);
    }

    private static void sendMeasurement(double value, boolean raining, String sensorName) {
        final String url = "http://localhost:8080/measurements/add";
        Map<String, Object> jsonData = new HashMap<>();
        Map<String, String> name = new HashMap<>();
        name.put("name", sensorName);
        jsonData.put("value", value);
        jsonData.put("raining", raining);
        jsonData.put("sensor", name);
        makePostRequestWithJSONData(url, jsonData);
    }

    private static void makePostRequestWithJSONData(String url, Map<String, Object> jsonData) {

        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);
        try {
            restTemplate.postForObject(url, request, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
        }
    }
}







