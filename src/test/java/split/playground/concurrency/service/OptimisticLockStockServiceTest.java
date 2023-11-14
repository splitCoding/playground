package split.playground.concurrency.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import split.playground.concurrency.domain.OptimisticLockStock;
import split.playground.concurrency.repository.OptimisticStockRepository;

@SpringBootTest
class OptimisticLockStockServiceTest {

    private Logger logger = LoggerFactory.getLogger(OptimisticLockStockServiceTest.class);

    @Autowired
    private OptimisticLockStockService stockService;

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
    void 재고감소() {
        // given
        // when
        stockService.decrease(stockId, 1L);

        // then
        final OptimisticLockStock stock = stockRepository.findById(stockId).orElseThrow();
        assertThat(stock.getQuantity()).isEqualTo(99L);
    }

    @Test
    void Optimistic_Lock을_사용하면_첫번째_트랜잭션에서_버전을_올리면_다른_모든_트랜잭션이_롤백된다() throws InterruptedException {
        // given
        int threadCount = 10;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            final int order = i;
            executorService.execute(() -> {
                try {
                    stockService.decrease(stockId, 1L);
                } catch (ObjectOptimisticLockingFailureException e) {
                    logger.info("{}-Thread : [{}]", order, e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // then
        final OptimisticLockStock stockResult = stockRepository.findById(stockId).orElseThrow();
        assertThat(stockResult.getQuantity()).isEqualTo(99L);
    }
}
