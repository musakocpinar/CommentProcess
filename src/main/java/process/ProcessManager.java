package process;

import lombok.extern.log4j.Log4j2;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class ProcessManager<T> implements Runnable {
    private final LinkedBlockingQueue<Map.Entry<CustomRunnable<T>, Callable<T>>> que;
    private final ExecutorService service;
    private final AtomicBoolean isRunning;

    public ProcessManager() {
        this.que = new LinkedBlockingQueue<>();
        this.service = Executors.newFixedThreadPool(100);
        this.isRunning = new AtomicBoolean(true);

        ExecutorService selfService = Executors.newSingleThreadExecutor();
        selfService.submit(this);
    }

    public void add(final Callable<T> callable, final CustomRunnable<T> runner) {
        this.que.add(new AbstractMap.SimpleEntry<>(runner, callable));
    }

    public boolean remove(final CustomRunnable<T> runner) {
        return this.que.removeIf(s -> s.getKey() == runner);
    }

    public LinkedBlockingQueue<Map.Entry<CustomRunnable<T>, Callable<T>>> getQue() {
        return que;
    }

    public T run(final Callable<T> callable) throws ExecutionException, InterruptedException {
        return service.submit(callable).get();
    }

    public CustomResponse<T> start(final Callable<T> callable) {
        CustomResponse<T> customResponse = new CustomResponse<>();

        this.service.submit(() -> {
            try {
                T response = ProcessManager.this.service.submit(callable).get();

                if (customResponse.getResponse() != null) {
                    customResponse.getResponse().run(response);
                }
            } catch (InterruptedException | ExecutionException e) {
                if (customResponse.getResponse() != null) {
                    customResponse.getResponse().error(e);
                }

                Thread.currentThread().interrupt();
            }
        });

        return customResponse;
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            Map.Entry<CustomRunnable<T>, Callable<T>> take;
            try {
                take = this.que.take();

                CustomRunnable<T> key = take.getKey();
                Callable<T> value = take.getValue();

                start(value).call(key);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }
}
