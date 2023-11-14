package split.playground.concurrency.service;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class OptimisticLockeStockServiceFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    public OptimisticLockeStockServiceFacade(final OptimisticLockStockService optimisticLockStockService) {
        this.optimisticLockStockService = optimisticLockStockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);
                break;
            } catch (OptimisticLockingFailureException e) {
                Thread.sleep(30);
            }
        }
    }
}
