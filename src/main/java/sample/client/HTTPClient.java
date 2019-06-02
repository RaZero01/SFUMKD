package sample.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

        return null;
    }
}
