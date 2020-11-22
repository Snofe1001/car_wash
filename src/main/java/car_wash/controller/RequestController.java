package car_wash.controller;

import car_wash.dto.RequestDto;
import car_wash.service.interfaces.RequestService;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Request")
@RestController
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/addRequest")
    @ResponseBody
    public ResponseEntity<String> addRequest(@RequestBody RequestDto requestDto) {
        return requestService.addRequest(requestDto);
    }

    @GetMapping("/getQueueCountForAutoNumber")
    @ResponseBody
    public ResponseEntity<String> getQueueCountForAutoNumber(@RequestParam("autoNumber") String autoNumber) {
        return requestService.getQueueCountForAutoNumber(autoNumber);
    }

    @GetMapping("/getStartTimeForAutoNumber")
    @ResponseBody
    public ResponseEntity<String> getStartTimeForAutoNumber(@RequestParam("autoNumber") String autoNumber) {
        return requestService.getStartTimeForAutoNumber(autoNumber);
    }
}
