package car_wash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestDto {

    private Integer id;
    private Integer serviceId;
    private String autoNumber;

}
