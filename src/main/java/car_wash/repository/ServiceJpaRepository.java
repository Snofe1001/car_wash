package car_wash.repository;

import car_wash.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceJpaRepository extends JpaRepository<Service, Integer> {

    Service getById(Integer id);
}
