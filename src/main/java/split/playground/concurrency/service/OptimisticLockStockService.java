package split.playground.concurrency.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import split.playground.concurrency.domain.OptimisticLockStock;
import split.playground.concurrency.repository.OptimisticStockRepository;

@Service
@Transactional
public class OptimisticLockStockService {

    private final OptimisticStockRepository stockRepository;

    public OptimisticLockStockService(final OptimisticStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void decrease(Long id, Long quantity) {
        final OptimisticLockStock stock = stockRepository.findByIdWithOptimisticLock(id);
        stock.decrease(quantity);
    }
}
