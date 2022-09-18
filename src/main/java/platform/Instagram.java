package platform;

import comment.Comment;
import java.util.List;
import java.util.concurrent.Callable;

public class Instagram implements Callable<List<Comment>> {
    private String link;

    @Override
    public List<Comment> call() throws Exception {
        Comment comment = new Comment();
        comment.setAuthor(link);

        return List.of(comment);
    }
}
