import comment.Comment;
import filter.FilterChain;
import filter.StringFilter;
import filter.filters.AuthorFilter;
import filter.filters.CommentFilter;
import platform.youtube.Youtube;
import process.CustomResponse;
import process.CustomRunnable;
import process.ProcessManager;
import restapi.Spark;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
        Spark.init();
    }

    public static void test() throws ExecutionException, InterruptedException {
        ProcessManager<List<Comment>> manager = new ProcessManager<>();

        String link = "https://www.youtube.com/watch?v=3YL4Btzcf2Q";
        Youtube youtube = new Youtube(link);

        List<Comment> run = manager.run(youtube);


        System.out.println("size: " + run.size());

        FilterChain filterChain = new FilterChain();
        filterChain.addFilter(new CommentFilter(StringFilter.FilterType.CONTAINS, "şarkı"));
        filterChain.addFilter(new AuthorFilter(StringFilter.FilterType.CONTAINS, "Elif"));
        filterChain.execute(run);

        run.forEach(s -> {
            System.out.println(s.getComment());
            System.out.println("-----------------------");
        });

        System.out.println("bitti");
    }
}
