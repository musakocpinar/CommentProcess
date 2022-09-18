package restapi;

import org.json.JSONObject;
import restapi.services.UserApi;
import lombok.extern.log4j.Log4j2;
import restapi.services.YoutubeService;

import static spark.Spark.*;

@Log4j2
public class Spark {
    public static void init() {
        port(8080);

        path("/api", () -> {
            path("/user", () -> {
                post("/add", UserApi.addUser);
            });

            path("/youtube", () -> {
                post("/filter", YoutubeService.find);
            });
        });

        exception(Exception.class, (exception, request, response) -> {
            log.error("", exception);

            JSONObject resp = new JSONObject();
            resp.put("success", false);
            resp.put("message", exception.getMessage());

            response.body(resp.toString());
        });
    }
}
