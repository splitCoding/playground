package split.playground.concurrency.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import split.playground.concurrency.domain.Stock;
import split.playground.concurrency.repository.StockRepository;

@SpringBootTest
class PessimisticLockStockServiceTest {

    @Autowired
    private PessimisticLockStockService stockService;

    @Autowired
    private StockRepository stockRepository;

    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    void clean() {
        stock = null;
        stockRepository.deleteAll();
    }

    @DisplayName("")
    @Test
    void 재고감소() {
        // given
        // when
        stockService.decrease(stock.getId(), 1L);

        // then
        final Stock stock = stockRepository.findById(1L).orElseThrow();
        assertThat(stock.getQuantity()).isEqualTo(99L);
    }

    @DisplayName("")
    @Test
    void 동시에_100개_요청() throws InterruptedException {
        // given
        int threadCount = 10;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                stockService.decrease(stock.getId(), 1L);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // then
        final Stock stockResult = stockRepository.findById(stock.getId())
                .orElseThrow();
        assertThat(stockResult.getQuantity()).isEqualTo(90L);
    }
}
