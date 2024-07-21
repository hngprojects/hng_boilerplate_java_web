package hng_java_boilerplate.integrationTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.region.dto.RegionUpdateDto;
import hng_java_boilerplate.region.dto.UserRegionDto;
import hng_java_boilerplate.region.entity.RegionEntity;
import hng_java_boilerplate.region.entity.UserRegionEntity;
import hng_java_boilerplate.region.service.RegionService;
import hng_java_boilerplate.utils.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class regionControllerIntegrationTest {

    @Autowired
    private  MockMvc mockMvc;

    @Autowired
    private RegionService regionService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void test_that_getAllRegion_returns_list_of_region_with_status_200() throws Exception {
        RegionEntity regionEntity = TestDataUtils.createRegionEntity();
        regionService.createRegion(regionEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/regions")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].region_code").value(regionEntity.getRegionCode())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].region_name").value(regionEntity.getRegionName())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void test_that_getAllRegion_returns_error_when_no_available_region_with_status_404() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/regions")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value("Not found")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status_code").value(404)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void test_that_getAllRegion_returns_error_when_an_exception_is_caught_with_status_400() throws Exception {

        RegionEntity regionEntity = TestDataUtils.createRegionEntity();
        regionService.createRegion(regionEntity);

        Connection connection = dataSource.getConnection();
        connection.createStatement().execute("SHUTDOWN");

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/regions")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value("Bad request")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status_code").value(400)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }


    @Test
    public void test_that_assignRegion_assigns_region_with_status_200() throws Exception {
        UserRegionDto userRegionDto =  TestDataUtils.createUserRegionDto();
        RegionEntity regionEntity = TestDataUtils.createRegionEntity();
        regionService.createRegion(regionEntity);

        String userRegionJson = objectMapper.writeValueAsString(userRegionDto);


        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegionJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.region_name").value("North America")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.country_code").value("NA")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void test_that_assignRegion_returns_error_when_region_is_available_with_status_400() throws Exception {
        UserRegionDto userRegionDto =  TestDataUtils.createUserRegionDto();

        String userRegionJson = objectMapper.writeValueAsString(userRegionDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegionJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value("Bad request")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status_code").value(400)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void test_that_assignRegion_returns_error_when_user_region_already_exist_with_status_409() throws Exception {
        UserRegionDto userRegionDto =  TestDataUtils.createUserRegionDto();
        UserRegionEntity userRegionEntity = TestDataUtils.createUserRegionEntity();
        RegionEntity regionEntity = TestDataUtils.createRegionEntity();

        regionService.createRegion(regionEntity);
        regionService.saveUserRegion(userRegionEntity);

        String userRegionJson = objectMapper.writeValueAsString(userRegionDto);


        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegionJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value("Conflict")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status_code").value(409)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        );
    }


    @Test
    public void test_that_getUserRegion_returns_user_region_with_302() throws Exception {
        UserRegionEntity userRegionEntity = TestDataUtils.createUserRegionEntity();
        RegionEntity regionEntity = TestDataUtils.createRegionEntity();

        regionService.createRegion(regionEntity);
        regionService.saveUserRegion(userRegionEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/regions/{user_id}", userRegionEntity.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.user_id").value(userRegionEntity.getUserId().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.region_name").value(userRegionEntity.getRegionName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.country_code").value(userRegionEntity.getRegionCode())
        ).andExpect(
                MockMvcResultMatchers.status().isFound()
        );
    }


    @Test
    public void test_that_getUserRegion_returns_error_when_no_assigned_region_with_404() throws Exception {
        UserRegionEntity userRegionEntity = TestDataUtils.createUserRegionEntity();
        RegionEntity regionEntity = TestDataUtils.createRegionEntity();

        regionService.createRegion(regionEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/regions/{user_id}", userRegionEntity.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value("Not found")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status_code").value(404)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }


    @Test
    public void test_that_getUserRegion_returns_error_when_any_exception_is_caught_with_status_400() throws Exception {
        UserRegionEntity userRegionEntity = TestDataUtils.createUserRegionEntity();
        RegionEntity regionEntity = TestDataUtils.createRegionEntity();

        regionService.createRegion(regionEntity);
        regionService.saveUserRegion(userRegionEntity);

        Connection connection = dataSource.getConnection();
        connection.createStatement().execute("SHUTDOWN");

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/regions/{user_id}", userRegionEntity.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }


    @Test
    public void test_that_updateUserRegion_updates_and_return_user_region_with_status_200() throws Exception {
        RegionUpdateDto update = RegionUpdateDto.builder()
                .regionName("Africa")
                .countryCode("AF")
                .build();

        UserRegionEntity userRegionEntity = TestDataUtils.createUserRegionEntity();
        RegionEntity regionEntity = TestDataUtils.createRegionEntity();
        RegionEntity regionEntity2 = TestDataUtils.createRegionEntity2();

        regionService.createRegion(regionEntity);
        regionService.createRegion(regionEntity2);
        regionService.saveUserRegion(userRegionEntity);

        String userRegionJson = objectMapper.writeValueAsString(update);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/regions/{user_id}", userRegionEntity.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegionJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.region_name").value(update.getRegionName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.country_code").value(update.getCountryCode())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }


    @Test
    public void test_that_updateUserRegion_returns_error_when_chosen_region_is_not_predefined_with_status_400() throws Exception {
        RegionUpdateDto update = RegionUpdateDto.builder()
                .regionName("Africa")
                .countryCode("AF")
                .build();

        UserRegionEntity userRegionEntity = TestDataUtils.createUserRegionEntity();
        RegionEntity regionEntity = TestDataUtils.createRegionEntity();

        regionService.createRegion(regionEntity);
        regionService.saveUserRegion(userRegionEntity);

        String userRegionJson = objectMapper.writeValueAsString(update);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/regions/{user_id}", userRegionEntity.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegionJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value("Bad request")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status_code").value(400)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }


    @Test
    public void test_that_updateUserRegion_returns_error_when_any_exception_caught_with_status_400() throws Exception {
        RegionUpdateDto update = RegionUpdateDto.builder()
                .regionName("Africa")
                .countryCode("AF")
                .build();

        UserRegionEntity userRegionEntity = TestDataUtils.createUserRegionEntity();
        RegionEntity regionEntity = TestDataUtils.createRegionEntity();
        RegionEntity regionEntity2 = TestDataUtils.createRegionEntity2();

        regionService.createRegion(regionEntity);
        regionService.createRegion(regionEntity2);
        regionService.saveUserRegion(userRegionEntity);

        String userRegionJson = objectMapper.writeValueAsString(update);

        Connection connection = dataSource.getConnection();
        connection.createStatement().execute("SHUTDOWN");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/regions/{user_id}", userRegionEntity.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegionJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value("Bad request")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status_code").value(400)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }


}

