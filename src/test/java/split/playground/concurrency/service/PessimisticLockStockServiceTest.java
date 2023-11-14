package split.playground.concurrency.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import split.playground.concurrency.domain.Stock;
import split.playground.concurrency.repository.StockRepository;

@SpringBootTest
class PessimisticLockStockServiceTest {

    private Logger logger = LoggerFactory.getLogger(PessimisticLockStockServiceTest.class);

    @Autowired
    private PessimisticLockStockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private DataSource dataSource;

    private Long stockId;

    @BeforeEach
    void setUp() {
        stockId = stockRepository.saveAndFlush(new Stock(1L, 100L)).getId();
    }

    @AfterEach
    void clean() {
        stockRepository.deleteAll();
    }

    @Test
    void 재고감소() {
        // given
        // when
        stockService.decreaseWithXLock(stockId, 1L);

        // then
        final Stock stock = stockRepository.findById(stockId).orElseThrow();
        assertThat(stock.getQuantity()).isEqualTo(99L);
    }

    @Test
    void X_lock을_통해_동시성_처리를_한다() throws InterruptedException {
        // given
        int threadCount = 10;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                stockService.decreaseWithXLock(stockId, 1L);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // then
        final Stock stockResult = stockRepository.findById(stockId).orElseThrow();
        assertThat(stockResult.getQuantity()).isEqualTo(90L);
    }

    @Test
    void S_lock_사용시_deadlock_detection을_통해_1개의_트랜잭션만_처리된다() throws InterruptedException {
        // given
        deadlockDetectionOn();

        int threadCount = 10;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            final int order = i;
            executorService.execute(() -> {
                try {
                    stockService.decreaseWithSlock(stockId, 1L);
                } catch (Exception e) {
                    logger.info("{}-Thread : [{}]", order, e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // then
        final Stock stockResult = stockRepository.findById(stockId).orElseThrow();
        assertThat(stockResult.getQuantity()).isEqualTo(99L);
    }

    @Test
    void S_Lock_사용시_deadlock_detection을_끄면_lock_wait_timeout까지_대기후_모든_트랜잭션이_롤백된다() throws InterruptedException {
        // given
        deadlockDetectionOff();

        int threadCount = 10;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            final int order = i;
            executorService.execute(() -> {
                try {
                    stockService.decreaseWithSlock(stockId, 1L);
                } catch (Exception e) {
                    logger.error("{}-Thread : [{}]", order, e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        deadlockDetectionOn();

        // then
        final Stock stockResult = stockRepository.findById(stockId).orElseThrow();
        assertThat(stockResult.getQuantity()).isEqualTo(100L);
    }

    private void deadlockDetectionOff() {
        try (
                final Connection connection = dataSource.getConnection();
                final Statement statement = connection.createStatement()
        ) {
            statement.execute("SET GLOBAL innodb_deadlock_detect = OFF");
            statement.execute("SET GLOBAL innodb_lock_wait_timeout = 3");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deadlockDetectionOn() {
        try (
                final Connection connection = dataSource.getConnection();
                final Statement statement = connection.createStatement()
        ) {
            statement.execute("SET GLOBAL innodb_deadlock_detect = ON");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
