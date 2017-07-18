package murray;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/*
start the server using mvn org.apache.tomcat.maven:tomcat7-maven-plugin:2.2:run-war and then run this.
 */
public class SampleClientTest {

    private final Gson gson = new Gson();

    @Test
    public void testStory() throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            reset(httpClient);

            Outcome johnGBP1 = getAccountBalance(httpClient, "johnGBP");
            assertEquals(1000, johnGBP1.getAccountBalance().getAmount(), 0.1);
            Outcome johnUSD1 = getAccountBalance(httpClient, "johnUSD");
            assertEquals(1000, johnUSD1.getAccountBalance().getAmount(), 0.1);
            Outcome transfer1 = makeTransfer(httpClient, 100);
            assertEquals(900, transfer1.getAccountBalance().getAmount(), 0.1);
            Outcome johnGBP2 = getAccountBalance(httpClient, "johnGBP");
            assertEquals(900, johnGBP2.getAccountBalance().getAmount(), 0.1);
            Outcome johnUSD2 = getAccountBalance(httpClient, "johnUSD");
            assertEquals(1130.29, johnUSD2.getAccountBalance().getAmount(), 0.1);
            Outcome transfer2 = makeTransfer(httpClient, 100);
            assertEquals(800, transfer2.getAccountBalance().getAmount(), 0.1);
            Outcome johnGBP3 = getAccountBalance(httpClient, "johnGBP");
            assertEquals(800, johnGBP3.getAccountBalance().getAmount(), 0.1);
            Outcome johnUSD3 = getAccountBalance(httpClient, "johnUSD");
            assertEquals(1260.58, johnUSD3.getAccountBalance().getAmount(), 0.1);

            Outcome transfer3 = makeTransfer(httpClient, 900);
            assertFalse(transfer3.isSuccess());

            Outcome anotherAccount1 = getAccountBalance(httpClient, "anotherAccount");
            assertFalse(anotherAccount1.isSuccess());

        } finally {
            try {
                httpClient.getConnectionManager().shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void reset(DefaultHttpClient httpClient) throws IOException {
        HttpRequestBase get = new HttpGet(
                "http://localhost:9090/reset");
        get.addHeader("accept", "application/json");

        HttpResponse response = httpClient.execute(get);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));

        String output;
        System.out.println("Reset: Output from Server .... ");
        while ((output = br.readLine()) != null) {
            System.out.println(output);

        }

    }


    private Outcome getAccountBalance(DefaultHttpClient httpClient, String accountName) throws IOException {
        HttpRequestBase get = new HttpGet(
                "http://localhost:9090/getBalance/" + accountName);
        get.addHeader("accept", "application/json");

        HttpResponse response = httpClient.execute(get);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));
        StringBuilder stringBuilder = new StringBuilder();
        String output;
        System.out.println("GetBalance: Output from Server .... ");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            stringBuilder.append(output);
        }
        return gson.fromJson(stringBuilder.toString(), Outcome.class);
    }


    private Outcome makeTransfer(DefaultHttpClient httpClient, int amount) throws IOException {
        final HttpPost request = new HttpPost(
                "http://localhost:9090/transfer");

        TransferRequest transferRequest = new TransferRequest("johnGBP", "johnUSD", amount, true);
        JSONObject jsonObject = new JSONObject(transferRequest);

        String s = jsonObject.toString();

        StringEntity input = new StringEntity(s);
        input.setContentType("application/json");
        request.setEntity(input);
        request.addHeader("accept", "application/json");
        HttpResponse response = httpClient.execute(request);


        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));

        String output;
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("Transfer: Output from Server .... ");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            stringBuilder.append(output);
        }
        return gson.fromJson(stringBuilder.toString(), Outcome.class);
    }
}
