package split.playground.concurrency.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import split.playground.concurrency.domain.OptimisticLockStock;
import split.playground.concurrency.repository.OptimisticStockRepository;

@SpringBootTest
class OptimisticLockeStockServiceFacadeTest {

    @Autowired
    private OptimisticLockeStockServiceFacade optimisticLockeStockServiceFacade;

    @Autowired
    private OptimisticStockRepository stockRepository;

    private Long stockId;

    @BeforeEach
    void setUp() {
        stockId = stockRepository.saveAndFlush(new OptimisticLockStock(1L, 100L)).getId();
    }

    @AfterEach
    void clean() {
        stockRepository.deleteAll();
    }

    @Test
    void Optimistic_Lock을_이용해서_버전이_최신일떄까지_반복하여_재고감소_요청을_보낸다() throws InterruptedException {
        // given
        int threadCount = 10;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    optimisticLockeStockServiceFacade.decrease(stockId, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // then
        final OptimisticLockStock stockResult = stockRepository.findById(stockId).orElseThrow();
        assertThat(stockResult.getQuantity()).isEqualTo(90L);
    }
}
