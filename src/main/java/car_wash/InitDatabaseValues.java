package car_wash;

import car_wash.model.Garage;
import car_wash.model.Service;
import car_wash.repository.GarageJpaRepository;
import car_wash.repository.ServiceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitDatabaseValues {

    private final GarageJpaRepository garageJpaRepository;
    private final ServiceJpaRepository serviceJpaRepository;

    @Autowired
    public InitDatabaseValues(GarageJpaRepository garageJpaRepository,
                              ServiceJpaRepository serviceJpaRepository) {
        this.garageJpaRepository = garageJpaRepository;
        this.serviceJpaRepository = serviceJpaRepository;
    }

    @PostConstruct
    private void insertDefaultValues() {
         if (garageJpaRepository.findAll().size() == 0) {
            List<Garage> garageList = new ArrayList<>();
            garageList.add(new Garage(null, 1, false, null, null));
            garageList.add(new Garage(null, 2, false, null, null));
            garageList.add(new Garage(null, 3, false, null, null));
            garageList.add(new Garage(null, 4, false, null, null));


            garageJpaRepository.saveAll(garageList);
        }

        if (serviceJpaRepository.findAll().size() == 0) {
            List<Service> serviceList = new ArrayList<>();
            serviceList.add(new Service(null, "Кузов", 15));
            serviceList.add(new Service(null, "Кузов + коврики", 20));
            serviceList.add(new Service(null, "Кузов + коврики + пылесос", 25));
            serviceList.add(new Service(null, "Люкс", 30));

            serviceJpaRepository.saveAll(serviceList);
        }
    }
}
