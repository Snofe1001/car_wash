package car_wash.task_scheduler;

import car_wash.model.Garage;
import car_wash.model.Request;
import car_wash.repository.GarageJpaRepository;
import car_wash.repository.RequestJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class RequestTaskScheduler {

    private final ThreadPoolTaskScheduler taskScheduler;

    private final GarageJpaRepository garageJpaRepository;
    private final RequestJpaRepository requestJpaRepository;

    @Autowired
    public RequestTaskScheduler (ThreadPoolTaskScheduler taskScheduler,
                                 GarageJpaRepository garageJpaRepository, RequestJpaRepository requestJpaRepository) {
        this.taskScheduler = taskScheduler;

        this.garageJpaRepository = garageJpaRepository;
        this.requestJpaRepository = requestJpaRepository;
    }

    public void scheduleGarageFree(Garage garage, Integer time) {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(time);
        taskScheduler.schedule(new RunnableTusk(garage), Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    @AllArgsConstructor
    class RunnableTusk implements Runnable {

        private Garage garage;

        @Override
        public void run() {

            Request firstRequest = requestJpaRepository.selectFirstRequestInQueue();
            if (firstRequest == null) {
                garage.setOccupied(false);
                garage.setWillBeFreeAt(null);
                garage.setAutoNumber(null);
            } else {
                garage.setAutoNumber(firstRequest.getAutoNumber());
                requestJpaRepository.delete(firstRequest);
            }
            garageJpaRepository.save(garage);
        }
    }
}
