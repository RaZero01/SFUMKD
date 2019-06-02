package sample.client;

import sample.client.util.ParameterStringBuilder;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HTTPClient {

    public final String stringUrl = "http://94.177.189.97:11040";


    public boolean authtorize(String login, String password) {

        try {
            URL url = new URL(stringUrl + "/eps/user/auth");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            Map<String, String> parameters = new HashMap<>();
            parameters.put("login", login);
            parameters.put("password", password);
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
