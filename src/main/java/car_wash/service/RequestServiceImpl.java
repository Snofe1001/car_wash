package car_wash.service;

import car_wash.dto.RequestDto;
import car_wash.mapper.RequestMapper;
import car_wash.model.Garage;
import car_wash.model.Request;
import car_wash.repository.GarageJpaRepository;
import car_wash.repository.RequestJpaRepository;
import car_wash.repository.ServiceJpaRepository;
import car_wash.service.interfaces.RequestService;
import car_wash.task_scheduler.RequestTaskScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    private final GarageJpaRepository garageJpaRepository;
    private final RequestJpaRepository requestJpaRepository;

    private final RequestTaskScheduler taskScheduler;

    private final RequestMapper requestMapper;

    @Autowired
    public RequestServiceImpl(GarageJpaRepository garageJpaRepository, RequestJpaRepository requestJpaRepository,
                              RequestTaskScheduler taskScheduler,
                              RequestMapper requestMapper) {
        this.garageJpaRepository = garageJpaRepository;
        this.requestJpaRepository = requestJpaRepository;

        this.taskScheduler = taskScheduler;

        this.requestMapper = requestMapper;
    }

    @Override
    public ResponseEntity<String> addRequest(RequestDto requestDto) {
        List<Garage> freeGarageList = garageJpaRepository.getAllByOccupied(false);

        if (freeGarageList.isEmpty()) {
            return addRequestInQueue(requestDto);
        } else {
            return washCar(requestDto, freeGarageList.get(0));
        }
    }

    private ResponseEntity<String> addRequestInQueue(RequestDto requestDto) {
        Request request = requestMapper.convert(requestDto, Request.class);
        Garage firstReleasedGarage = garageJpaRepository.getFirstReleasedGarage();

        firstReleasedGarage.setWillBeFreeAt(firstReleasedGarage.getWillBeFreeAt().plusMinutes(request.getService().getDuration()));
        garageJpaRepository.save(firstReleasedGarage);

        request.setStartTime(firstReleasedGarage.getWillBeFreeAt().minusMinutes(request.getService().getDuration()));
        requestJpaRepository.save(request);

        return new ResponseEntity<>("К сожалению, все места заняты, вы поставлены в очередь", HttpStatus.OK);
    }

    private ResponseEntity<String> washCar(RequestDto requestDto, Garage freeGarage) {
        Request request = requestMapper.convert(requestDto, Request.class);
        freeGarage.setOccupied(true);
        freeGarage.setWillBeFreeAt(LocalDateTime.now().plusMinutes(request.getService().getDuration()));
        freeGarage.setAutoNumber(request.getAutoNumber());
        garageJpaRepository.save(freeGarage);
        taskScheduler.scheduleGarageFree(freeGarage, request.getService().getDuration());

        return new ResponseEntity<>("Проезжайте в бокс №" + freeGarage.getNumber(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> getQueueCountForAutoNumber(String autoNumber) {
        return new ResponseEntity<>("Ваш номер в очереди: " +
                (requestJpaRepository.countQueueForAutoNumber(autoNumber) + 1),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> getStartTimeForAutoNumber(String autoNumber) {
        return new ResponseEntity<>("Вы записаны на время: " +
                requestJpaRepository.getByAutoNumber(autoNumber).getStartTime(),
                HttpStatus.OK);
    }
}
