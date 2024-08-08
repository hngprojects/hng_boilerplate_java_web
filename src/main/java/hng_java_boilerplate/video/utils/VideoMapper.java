package hng_java_boilerplate.video.utils;

import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VideoMapper {

    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);

    //To Video suite Entity
    @Mapping(source = "jobId", target = "jobId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "jobType", target = "jobType")
    @Mapping(source = "filename", target = "filename")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "progress", target = "progress")
    @Mapping(source = "currentProcess", target = "currentProcess")
    VideoStatusDTO toDTO(VideoSuite videoSuite);


    //To VideoStatusDTO
    @Mapping(source = "jobId", target = "jobId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "jobType", target = "jobType")
    @Mapping(source = "filename", target = "filename")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "progress", target = "progress")
    @Mapping(source = "currentProcess", target = "currentProcess")
    VideoSuite toDTO(VideoStatusDTO statusDTO);
}
