package car_wash.repository;

import car_wash.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarageJpaRepository extends JpaRepository<Garage, Integer> {

    List<Garage> getAllByOccupied(Boolean occupied);

    @Query(value = "SELECT * FROM garage " +
            "WHERE will_be_free_at = (SELECT min(will_be_free_at) FROM garage)", nativeQuery = true)
    Garage getFirstReleasedGarage();
}
