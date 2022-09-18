package process;

public class CustomResponse<T> {
    private CustomRunnable<T> response;

    public void call(CustomRunnable<T> r){
        this.response = r;
    }

    protected CustomRunnable<T> getResponse() {
        return response;
    }
}
