package split.playground.concurrency.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import split.playground.concurrency.domain.Stock;
import split.playground.concurrency.repository.StockRepository;

@Service
@Transactional
public class PessimisticLockStockService {

    private final StockRepository stockRepository;

    public PessimisticLockStockService(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void decreaseWithXLock(Long id, Long quantity) {
        /*
        Pessimistic.READ
        - 조회시 S-lock 을 걸기 때문에 모든 트랜잭션에서 같은 재고에 접근이 가능하다.
        - 수량 감소를 위해 X-lock 을 결려고 할 때 다른 트랜잭션이 건 S-lock 이 존재해서 걸 수 없다.
        - 트랜잭션 들끼리 서로의 S-lock 이 풀릴 때까지 기다리는 상황이 발생하면서 DeadLock 이 발생한다.
         */
        final Stock stock = stockRepository.findByIdWithPessimisticWrite(id);
        stock.decrease(quantity);
    }

    public void decreaseWithSlock(Long id, Long quantity) {
        /*
        Pessimistic.WRITE
        - 조회시 X-lock 을 걸기 때문에 애초에 다른 트랜잭션에서 접근이 불가하여 순차적으로 락을 획득하면서 동시성 문제가 발생하지 않는다.
         */
        final Stock stock = stockRepository.findByIdWithPessimisticRead(id);
        stock.decrease(quantity);
    }
}
