package filter;

import comment.Comment;
import java.util.ArrayList;
import java.util.List;

public class FilterChain {
    private final List<Filter> filters = new ArrayList<>();

    public void addFilter(Filter filter){
        filters.add(filter);
    }

    public void execute(List<Comment> comments){
        comments.removeIf(s -> {
            for (Filter f: filters){
                boolean process = f.execute(s);
                if (!process) {
                    return true;
                }
            }
            return false;
        });
    }
}
