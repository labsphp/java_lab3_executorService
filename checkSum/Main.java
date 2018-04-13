package lab3.executorService.checkSum;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static AtomicInteger atomicCheckSum = new AtomicInteger();

    private static void checkSum(int[] arr, int start, int end) {
        for (int i = start; i < end; i++) {
            int oldValue, newValue;
            do {
                oldValue = atomicCheckSum.get();
                newValue = oldValue ^ arr[i];
            } while (!atomicCheckSum.compareAndSet(oldValue, newValue));
        }
    }

    public static void main(String[] argc) throws Exception {
        int countElem = 20;
        int[] arr = new int[countElem];
        Random random = new Random();
        for (int i = 0; i < countElem; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<?> future1 = executorService.submit(() -> checkSum(arr, 0, countElem / 2));
        Future<?> future2 = executorService.submit(() -> checkSum(arr, countElem / 2, arr.length));

        executorService.awaitTermination(300, TimeUnit.MILLISECONDS);
        executorService.shutdown();
        System.out.println("Контрольная сумма = " + atomicCheckSum);
        System.out.println("Контрольная сумма в двоичной форме= " + Integer.toBinaryString(atomicCheckSum.get()));

    }
}
