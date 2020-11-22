package car_wash.repository;

import car_wash.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestJpaRepository extends JpaRepository<Request, Integer> {

    @Query(value = "SELECT * FROM request " +
            "WHERE id = (SELECT min(id) FROM request) ", nativeQuery = true)
    Request selectFirstRequestInQueue();

    @Query(value = "SELECT count(*) FROM request " +
            "WHERE id < (SELECT id FROM request WHERE auto_number =:autoNumber)", nativeQuery = true)
    Integer countQueueForAutoNumber(String autoNumber);

    Request getByAutoNumber(String autoNumber);
}
