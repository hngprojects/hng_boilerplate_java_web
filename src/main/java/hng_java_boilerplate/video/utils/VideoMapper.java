package hng_java_boilerplate.video.utils;

import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VideoMapper {

    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);


    //To VideoStatusDTO
    @Mapping(source = "jobId", target = "jobId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "jobType", target = "jobType")
    @Mapping(source = "filename", target = "filename")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "progress", target = "progress")
    @Mapping(source = "currentProcess", target = "current_process")
    @Mapping(source = "expectedFormat", target = "expected_format")
    @Mapping(source = "mediaFormat", target = "media_format")
    VideoStatusDTO toDTO(VideoSuite videoSuite);


    //To Video suite Entity
    @Mapping(source = "jobId", target = "jobId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "jobType", target = "jobType")
    @Mapping(source = "filename", target = "filename")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "progress", target = "progress")
    @Mapping(source = "current_process", target = "currentProcess")
    @Mapping(source = "expected_format", target = "expectedFormat")
    @Mapping(source = "media_format", target = "mediaFormat")
    VideoSuite toDTO(VideoStatusDTO statusDTO);
}
