package sample.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sample.client.dto.UploadEducationalPlanRequest;
import sample.dto.EducationalPlan;
import sample.dto.User;
import wiremock.org.apache.http.client.methods.CloseableHttpResponse;
import wiremock.org.apache.http.client.methods.HttpPost;
import wiremock.org.apache.http.entity.ContentType;
import wiremock.org.apache.http.entity.StringEntity;
import wiremock.org.apache.http.impl.client.CloseableHttpClient;
import wiremock.org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HTTPClient {

    public final String stringUrl = "http://94.177.189.97:11040";


    public boolean authtorize(String login, String password) {

        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

            HttpPost request = new HttpPost(stringUrl + "/eps/user/auth");
            StringEntity params = new StringEntity("{\n" +
                    "  \"login\":\"" + login + "\",\n" +
                    "  \"password\":\"" + password + "\"\n" +
                    "} ",
                    ContentType.APPLICATION_JSON);
            request.addHeader("Accept", "*/*");
            request.setEntity(params);
            CloseableHttpResponse execute = httpClient.execute(request);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(execute.getEntity().getContent()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonObject = JSON.parseObject(response.toString());
            if (jsonObject.getString("status").equals("SUCCESS")) {
                JSONObject user = jsonObject.getJSONObject("user");
                User.user.setLogin(user.getString("login"));
                User.user.setName(user.getString("name"));
                User.user.setSurname(user.getString("surname"));
                User.user.setPatronymic(user.getString("patronymic"));
                User.user.setDegree(user.getString("degree"));
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<EducationalPlan> getAllEducationalPlans() {
        List<EducationalPlan> plans = new ArrayList<>();
        try {

            URL obj = new URL(stringUrl + "/eps/file/get-plans");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            ObjectMapper objectMapper = new ObjectMapper();
            plans = objectMapper.readValue(response.toString(), new TypeReference<List<EducationalPlan>>(){});
        } catch (Exception e) {

        }
        return plans;
    }

    public void sendEducationalPlan(EducationalPlan educationalPlan) {

        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead
            HttpPost request = new HttpPost(stringUrl + "/eps/file/upload-plan");
            ObjectMapper obj = new ObjectMapper();
            UploadEducationalPlanRequest uploadRequest = new UploadEducationalPlanRequest(educationalPlan);
            uploadRequest.setCustomName(User.user.getName() + " " + System.currentTimeMillis());
            String content = obj.writeValueAsString(uploadRequest);
            StringEntity params = new StringEntity(content,
                    ContentType.APPLICATION_JSON);
            request.setEntity(params);
            CloseableHttpResponse execute = httpClient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
