package car_wash.service.interfaces;

import car_wash.dto.RequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface RequestService {

    ResponseEntity<String> addRequest(RequestDto requestDto);

    ResponseEntity<String> getQueueCountForAutoNumber(String autoNumber);

    ResponseEntity<String> getStartTimeForAutoNumber(String autoNumber);
}
