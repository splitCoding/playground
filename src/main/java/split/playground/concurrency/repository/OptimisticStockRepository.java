package split.playground.concurrency.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import split.playground.concurrency.domain.OptimisticLockStock;

public interface OptimisticStockRepository extends JpaRepository<OptimisticLockStock, Long> {

    @Lock(LockModeType.OPTIMISTIC) //X-lock
    @Query("SELECT s FROM OptimisticLockStock s WHERE s.id = :id")
    OptimisticLockStock findByIdWithOptimisticLock(@Param("id") final Long id);
}
