package restapi.services;

import com.google.gson.Gson;
import comment.Comment;
import filter.FilterChain;
import filter.NumberFilter;
import filter.StringFilter;
import filter.filters.AuthorFilter;
import filter.filters.CommentFilter;
import filter.filters.VoteFilter;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import platform.youtube.Youtube;
import process.ProcessManager;
import restapi.model.YoutubeModel;
import spark.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class YoutubeService {
    private static final Gson gson = new Gson();

    public static Route find = (request, response) -> {
        YoutubeModel model = gson.fromJson(request.body(), YoutubeModel.class);

        FilterChain filterChain = new FilterChain();
        Arrays.stream(model.filter).forEach(s -> {
            switch (s.cls.toLowerCase()) {
                case "author" -> filterChain.addFilter(new AuthorFilter(StringFilter.FilterType.valueOf(s.type.toUpperCase()), s.pattern));
                case "comment" -> filterChain.addFilter(new CommentFilter(StringFilter.FilterType.valueOf(s.type.toUpperCase()), s.pattern));
                case "vote" -> filterChain.addFilter(new VoteFilter(NumberFilter.FilterType.valueOf(s.type.toUpperCase()), Integer.parseInt(s.pattern)));
                default -> log.error("{} is not valid.", s);
            }
        });

        ProcessManager<List<Comment>> manager = new ProcessManager<>();

        Youtube youtube = new Youtube(model.link);
        List<Comment> comments = new ArrayList<>();

        // Her istek 20 yorum çekiyor. Biz bunu 100 e çıkartıyoruz
        for (int i=0; i<5; i++) {
            List<Comment> c = manager.run(youtube);
            comments.addAll(c);
        }

        int totalSize = comments.size();

        filterChain.execute(comments);

        JSONObject resp = new JSONObject();
        resp.put("success", true);
        resp.put("total_size", totalSize);
        resp.put("data", comments);
        return resp;
    };
}
