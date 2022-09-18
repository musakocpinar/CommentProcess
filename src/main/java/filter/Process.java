package filter;

public interface Process<T> {
    boolean process(T val);
}
