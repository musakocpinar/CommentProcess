package utils.web;

import okhttp3.*;
import org.json.JSONObject;
import java.util.Map;
import java.util.Objects;

public class WebRequest {
    private final OkHttpClient client;
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public WebRequest(){
        client = new OkHttpClient();
    }

    public Response get(String url) throws Exception {
        return get(url, null, null);
    }

    public Response get(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();

        if (params != null){
            params.forEach(httpBuilder::addQueryParameter);
        }

        Request.Builder builder = new Request.Builder();
        builder.url(httpBuilder.build());

        if (headers != null){
            headers.forEach(builder::addHeader);
        }

        Request request = builder.build();

        Call call = client.newCall(request);
        try (okhttp3.Response response = call.execute()){
            Response resp = new Response();
            resp.setCode(response.code());
            resp.setBody(Objects.requireNonNull(response.body()).string());
            return resp;
        }
    }

    public Response post(String url, JSONObject body, Map<String, String> headers) throws Exception {
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        if (headers != null){
            headers.forEach(builder::addHeader);
        }

        RequestBody b = RequestBody.create(body.toString(), JSON);

        builder.post(b);
        Request request = builder.build();

        Call call = client.newCall(request);
        try (okhttp3.Response response = call.execute()){
            Response resp = new Response();
            resp.setCode(response.code());
            resp.setBody(Objects.requireNonNull(response.body()).string());
            return resp;
        }
    }
}
