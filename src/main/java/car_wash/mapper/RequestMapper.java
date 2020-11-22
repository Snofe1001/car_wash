package car_wash.mapper;

import car_wash.dto.RequestDto;
import car_wash.model.Request;
import car_wash.repository.ServiceJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RequestMapper {

    private final ModelMapper mapper;

    private final ServiceJpaRepository serviceJpaRepository;

    @Autowired
    public RequestMapper(ModelMapper mapper,
                         ServiceJpaRepository serviceJpaRepository) {
        this.mapper = mapper;

        this.serviceJpaRepository = serviceJpaRepository;
    }

    @PostConstruct
    public void setup() {
        mapper.createTypeMap(Request.class, RequestDto.class)
                .setPostConverter(ctx -> {
                    mapCustom(ctx.getSource(), ctx.getDestination());
                    return ctx.getDestination();
                });
        mapper.createTypeMap(RequestDto.class, Request.class)
                .setPostConverter(ctx -> {
                    mapCustom(ctx.getSource(), ctx.getDestination());
                    return ctx.getDestination();
                });
    }

    void mapCustom(Request source, RequestDto destination) {
        destination.setServiceId(source.getService().getId());
    }

    void mapCustom(RequestDto source, Request destination) {
        destination.setService(serviceJpaRepository.getById(source.getServiceId()));
    }

    public <D> D convert(Object source, Class<D> destinationType) {
        return mapper.map(source, destinationType);
    }
}
