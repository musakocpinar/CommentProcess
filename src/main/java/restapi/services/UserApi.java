package restapi.services;

import restapi.model.User;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import spark.Route;

@Log4j2
public class UserApi{
    private static final Gson gson = new Gson();
    public static Route addUser = (request, response) -> {
        User user = gson.fromJson(request.body(), User.class);
        System.out.println("user: " + user);
        return null;
    };


}
