package platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import platform.entity.Code;

import java.util.List;
import java.util.UUID;

public interface CodeRepo extends JpaRepository<Code, UUID> {
    @Query(value = "SELECT * FROM SNIPPET WHERE time = 0 AND views = 0 ORDER BY date DESC LIMIT 10", nativeQuery = true)
    List<Code> findAllByTimeAndViewsOrderByDate();

}
