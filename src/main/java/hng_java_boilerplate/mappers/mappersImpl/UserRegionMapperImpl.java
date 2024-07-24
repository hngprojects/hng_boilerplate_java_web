package hng_java_boilerplate.mappers.mappersImpl;
import hng_java_boilerplate.mappers.Mapper;
import hng_java_boilerplate.region.dto.UserRegionDto;
import hng_java_boilerplate.region.entity.UserRegionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRegionMapperImpl implements Mapper<UserRegionEntity, UserRegionDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public UserRegionMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserRegionDto mapTo(UserRegionEntity dtoClass) {
        return modelMapper.map(dtoClass, UserRegionDto.class);
    }

    @Override
    public UserRegionEntity mapFrom(UserRegionDto entityClass) {
        return modelMapper.map(entityClass, UserRegionEntity.class);
    }
}
