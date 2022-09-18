package filter;

import comment.Comment;

import java.util.List;

public interface Filter {
    boolean execute(Comment request);
}
