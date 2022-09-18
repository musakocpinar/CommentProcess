package platform.youtube;

import comment.Comment;
import exceptions.YoutubeCommentsError;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.SystemUtils;
import utils.web.Response;
import utils.web.WebRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
public class Youtube implements Callable<List<Comment>> {
    private final String link;
    private String api;
    private String token;
    private static final String COMMENT_LINK = "https://www.youtube.com/youtubei/v1/next?key=";
    private final WebRequest client;
    private final AtomicBoolean initted = new AtomicBoolean(false);
    private boolean isReachedLastIndex = false;

    public Youtube(String link) {
        this.link = link;
        this.client = new WebRequest();
    }

    @Override
    public List<Comment> call() throws Exception {
        if (!initted.get()){
            Response resp = this.client.get(link);

            api = YoutubeRegex.getApi(resp.getBody());
            token = YoutubeRegex.getToken(resp.getBody());

            initted.set(true);
        }

        if (isReachedLastIndex) {
            return new ArrayList<>();
        }else {
            return next();
        }
    }

    public List<Comment> next() throws Exception {
        JSONObject data = new JSONObject();

        JSONObject client = new JSONObject();
        client.put("clientName", "WEB");
        client.put("clientVersion", "2.20220411.01.00");

        JSONObject context = new JSONObject();
        context.put("client", client);

        data.put("continuation", token);
        data.put("context", context);

        Response post = this.client.post(COMMENT_LINK + api, data, null);
        JSONObject resp = new JSONObject(post.getBody());

        JSONArray arr = resp.getJSONArray("onResponseReceivedEndpoints");
        JSONObject obj = arr.getJSONObject(arr.length() - 1);

        JSONObject continuationItemsCommand;
        if (obj.has("reloadContinuationItemsCommand")) {
            continuationItemsCommand = obj.getJSONObject("reloadContinuationItemsCommand");
        }else if (obj.has("appendContinuationItemsAction")){
            continuationItemsCommand = obj.getJSONObject("appendContinuationItemsAction");
        }else{
            throw new YoutubeCommentsError("reloadContinuationItemsCommand or appendContinuationItemsAction is not found in list.");
        }

        if (!continuationItemsCommand.has("continuationItems")) {
            return new ArrayList<>();
        }

        JSONArray jsonArray = continuationItemsCommand.getJSONArray("continuationItems");

        JSONObject last = jsonArray.getJSONObject(jsonArray.length() - 1);
        jsonArray.remove(jsonArray.length() - 1);

        List<Comment> comments = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject commentRenderer = jsonArray.getJSONObject(i).getJSONObject("commentThreadRenderer").getJSONObject("comment").getJSONObject("commentRenderer");
            JSONArray commentArray = commentRenderer.getJSONObject("contentText").getJSONArray("runs");

            Comment c = new Comment();

            String comment = IntStream.range(0, commentArray.length()).mapToObj((k) -> commentArray.getJSONObject(k).getString("text")).collect(Collectors.joining());
            c.setComment(comment);

            String author = commentRenderer.getJSONObject("authorText").getString("simpleText");
            c.setAuthor(author);

            // TODO: Sanırım 1k gibi sayısal olmayan değerler kullanılıyor
            if (commentRenderer.has("voteCount")){
                String voteCount = commentRenderer.getJSONObject("voteCount").getString("simpleText");
                c.setVoteCount((Long) SystemUtils.compactNumberParse(voteCount));
            }

            if (commentRenderer.has("replyCount")){
                int replyCount = commentRenderer.getInt("replyCount");
                c.setReplyCount(replyCount);
            }

            comments.add(c);
        }

        if (last.has("continuationItemRenderer")){
            token =  last.getJSONObject("continuationItemRenderer").getJSONObject("continuationEndpoint").getJSONObject("continuationCommand").getString("token");
        }else{
            token = null;
            isReachedLastIndex = true;
        }

        return comments;
    }
}
