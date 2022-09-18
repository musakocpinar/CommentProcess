package filter;

import comment.Comment;
import filter.filters.AuthorFilter;
import filter.filters.VoteFilter;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
public class Main {
    public static void main(String[] args) {
        List<Comment> comments = IntStream.range(0, 15).mapToObj(x -> {
            Comment comment = new Comment();
            comment.setComment("comment" + x);
            comment.setVoteCount(x);
            comment.setAuthor("author" + x);
            return comment;
        }).collect(Collectors.toList());

        FilterChain chain = new FilterChain();
        chain.addFilter(new AuthorFilter(StringFilter.FilterType.EQUALS, "author5"));
        chain.addFilter(new VoteFilter(NumberFilter.FilterType.GREATER, 5));
        chain.execute(comments);

        comments.forEach(System.out::println);

    }
}
