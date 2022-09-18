package filter.filters;

import comment.Comment;
import filter.Filter;
import filter.NumberFilter;

public class VoteFilter extends NumberFilter implements Filter {

    public VoteFilter(FilterType type, int value) {
        super(type, value);
    }

    @Override
    public boolean execute(Comment request) {
        return process(request.getVoteCount());
    }
}
