package split.playground.concurrency.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import split.playground.concurrency.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

    //@Lock(LockModeType.PESSIMISTIC_READ) //S-lock
    @Lock(LockModeType.PESSIMISTIC_WRITE) //X-lock
    @Query("SELECT s FROM Stock s WHERE s.id = :id")
    Stock findByIdWithPessimisticRead(@Param("id") final Long id);
}
