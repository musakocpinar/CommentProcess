package filter.filters;

import comment.Comment;
import filter.Filter;
import filter.StringFilter;

public class CommentFilter extends StringFilter implements Filter {

    public CommentFilter(FilterType type, String val) {
        super(type, val);
    }

    @Override
    public boolean execute(Comment request) {
        return process(request.getComment());
    }
}
