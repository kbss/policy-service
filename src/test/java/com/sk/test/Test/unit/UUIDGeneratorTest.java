package com.sk.test.Test.unit;

import com.sk.test.service.UUIDGenerator;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

class UUIDGeneratorTest {

    @Test
    void concurrentIdGenerationTest() throws InterruptedException {
        Executor executor = Executors.newFixedThreadPool(5);
        Set<String> ids = ConcurrentHashMap.newKeySet();
        int testSize = 100;
        CountDownLatch latch = new CountDownLatch(testSize);
        for (int i = 0; i < testSize; i++) {
            executor.execute(() -> {
                ids.add(UUIDGenerator.generate());
                latch.countDown();
            });
        }
        latch.await();
        assertThat(ids, not(empty()));
        assertThat(ids, hasSize(testSize));
    }
}
