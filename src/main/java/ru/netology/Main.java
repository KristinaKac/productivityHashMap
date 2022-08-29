package ru.netology;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Map<String, Object> synchronizedHashMap = Collections.synchronizedMap(new HashMap<>());
        Map<String, Object> concurrentHashMap = new ConcurrentHashMap<>();

        long syncMapTime = getPutTime(synchronizedHashMap);
        long concurrentMapTime = getPutTime(concurrentHashMap);
        System.out.println("Производительность SynchronizedHashMap: " + syncMapTime);
        System.out.println("Производительность ConcurrentHashMap: " + concurrentMapTime);
    }

    private static long getPutTime(Map<String, Object> map) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        long startTime = System.nanoTime();
        for (int i = 0; i < 4; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 100_000; j++) {
                    int value = ThreadLocalRandom
                            .current()
                            .nextInt(10000);
                    String key = String.valueOf(value);
                    map.put(key, value);
                    map.get(key);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        return (System.nanoTime() - startTime) / 500_000;
    }
}
// Вывод: при увеличении добавляемых элементов, SynchronizedHashMap работает
// все более медленно по сравнению с ConcurrentHashMap.