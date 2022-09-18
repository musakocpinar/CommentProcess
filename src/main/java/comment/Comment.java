package comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Comment {
    private String author;
    private String comment;
    private long voteCount = -1;
    private int replyCount = -1;
}
