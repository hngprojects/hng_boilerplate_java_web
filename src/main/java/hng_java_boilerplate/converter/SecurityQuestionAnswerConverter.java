//package hng_java_boilerplate.converter;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import hng_java_boilerplate.dtos.requests.SecurityQuestionAnswer;
//import jakarta.persistence.AttributeConverter;
//
//import java.util.List;
//
//public class SecurityQuestionAnswerConverter implements AttributeConverter<List<SecurityQuestionAnswer>, String> {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public String convertToDatabaseColumn(List<SecurityQuestionAnswer> answers) {
//        try {
//            return objectMapper.writeValueAsString(answers);
//        } catch (Exception e) {
//            throw new RuntimeException("Error converting list of security questions to JSON", e);
//        }
//    }
//
//    @Override
//    public List<SecurityQuestionAnswer> convertToEntityAttribute(String dbData) {
//        try {
//            return objectMapper.readValue(dbData, new TypeReference<List<SecurityQuestionAnswer>>() {});
//        } catch (Exception e) {
//            throw new RuntimeException("Error converting JSON to list of security questions", e);
//        }
//    }
//
//}



package hng_java_boilerplate.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.dtos.requests.SecurityQuestionAnswer;
import jakarta.persistence.AttributeConverter;

import java.util.List;

public class SecurityQuestionAnswerConverter implements AttributeConverter<List<SecurityQuestionAnswer>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<SecurityQuestionAnswer> answers) {
        try {
            return objectMapper.writeValueAsString(answers);
        } catch (Exception e) {
            throw new RuntimeException("Error converting list of security questions to JSON", e);
        }
    }

    @Override
    public List<SecurityQuestionAnswer> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return null;
            }
            return objectMapper.readValue(dbData, new TypeReference<List<SecurityQuestionAnswer>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to list of security questions", e);
        }
    }
}
