import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProxyList {

    public static void main(String[] args) {



         String countrycode = "US";  ////////////////////////////// Proxy country and also can use all


        String url = "https://api.proxyscrape.com/v2/?request=displayproxies&protocol=http&timeout=10000&country=" + countrycode + "&ssl=all&anonymity=all";

        Response resp = RestAssured.get(url);
        String responsebody = resp.getBody().asString();

        String[] lines = responsebody.split("\n");
        // List<String[]> ipslist = new ArrayList<>();

        Map<String, Object> postpayload = new HashMap<>();

        for (String line : lines) {

            String postresponse = given()
                    .contentType(ContentType.URLENC)
                    .formParam("ip_addr[]", line)
                    .when().post("https://api.proxyscrape.com/v4/online_check")
                    .then()
                    .extract().body().asString();



            JsonPath jpath = new JsonPath(postresponse);
            String truefalse = jpath.getString("working").replaceAll("[\\[\\]]", "");
            String ipvalue = jpath.getString("ip").replaceAll("[\\[\\]]", "");;



            if (truefalse.equalsIgnoreCase("true")){

                System.out.println(ipvalue+" This IP is working");
            } else if (truefalse.equalsIgnoreCase("false")) {

                System.out.println(ipvalue+" This IP is blocked");

            }


        }
    }


}



