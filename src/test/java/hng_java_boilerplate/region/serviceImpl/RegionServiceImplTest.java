package hng_java_boilerplate.region.serviceImpl;

import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.region.dto.request.CreateRegion;
import hng_java_boilerplate.region.dto.request.UpdateRequest;
import hng_java_boilerplate.region.dto.response.GetAllRegion;
import hng_java_boilerplate.region.dto.response.GetResponse;
import hng_java_boilerplate.region.entity.Region;
import hng_java_boilerplate.region.repository.RegionRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RegionServiceImplTest {
    @Mock
    private RegionRepository regionRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private RegionServiceImpl underTest;
    @Mock
    private Region region;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setId("user-id");

        region = new Region();
        region.setRegion("Abuja");
        region.setLanguage("Tib");
        region.setTimezone("@AT");
        region.setUser(user);
        region.setId("region-id");
    }

    @Test
    void shouldGetRegionByUserId() {
        when(regionRepository.findByUserId("user-id")).thenReturn(Optional.of(region));

        GetResponse response = underTest.getRegionByUserId("user-id");

        verify(regionRepository, times(1)).findByUserId("user-id");
        assertThat(response).hasFieldOrPropertyWithValue("id", region.getId());
        assertThat(response).hasFieldOrPropertyWithValue("user_id", region.getUser().getId());
        assertThat(response).hasFieldOrPropertyWithValue("region", region.getRegion());
    }

    @Test
    void shouldThrowNotFoundWhenNoRegionWithUserId() {
        when(regionRepository.findByUserId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getRegionByUserId("userId"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("region not found for user");
    }

    @Test
    void shouldCreateRegion() {
        when(userService.getLoggedInUser()).thenReturn(region.getUser());

        CreateRegion request = CreateRegion.builder()
                .region("lagos")
                .language("las")
                .timezone("WAT")
                .build();

        GetResponse response = underTest.createRegion(request);
        ArgumentCaptor<Region> regionArgumentCaptor = ArgumentCaptor.forClass(Region.class);
        verify(regionRepository).saveAndFlush(regionArgumentCaptor.capture());
        Region capturedRegion = regionArgumentCaptor.getValue();

        assertThat(capturedRegion.getRegion()).isEqualTo(request.getRegion());
        assertThat(capturedRegion.getLanguage()).isEqualTo(request.getLanguage());
        assertThat(capturedRegion.getTimezone()).isEqualTo(request.getTimezone());

        assertThat(response).hasFieldOrProperty("id");
        assertThat(response).hasFieldOrPropertyWithValue("user_id", region.getUser().getId());
        assertThat(response).hasFieldOrPropertyWithValue("region", request.getRegion());
        assertThat(response).hasFieldOrPropertyWithValue("language", request.getLanguage());
        assertThat(response).hasFieldOrPropertyWithValue("timezone", request.getTimezone());
    }

    @Test
    void shouldGetAllRegions() {
        Region anotherRegion = new Region();
        anotherRegion.setId("another-region-id");
        anotherRegion.setUser(region.getUser());

        anotherRegion.setRegion("PortHarCourt");
        List<Region> regions = List.of(region, anotherRegion);

        when(regionRepository.findAll()).thenReturn(regions);
        GetAllRegion responses = underTest.getAllRegions();

        verify(regionRepository, times(1)).findAll();
        assertThat(responses.message()).isEqualTo("All Regions");
        assertThat(responses.status()).isEqualTo(200);
        assertThat(responses.data()).hasSize(2);
    }

    @Test
    void updateRegion() {
        UpdateRequest request = UpdateRequest.builder()
                .region("Asaba")
                .language("Araba")
                .timezone("Alabs")
                .build();
        when(regionRepository.findById(any())).thenReturn(Optional.of(region));

        GetResponse response = underTest.updateRegion(request, "region-id");
        verify(regionRepository, times(1)).findById("region-id");

        assertThat(response).hasFieldOrProperty("id");
        assertThat(response).hasFieldOrPropertyWithValue("user_id", region.getUser().getId());
        assertThat(response).hasFieldOrPropertyWithValue("region", request.getRegion());
        assertThat(response).hasFieldOrPropertyWithValue("language", request.getLanguage());
        assertThat(response).hasFieldOrPropertyWithValue("timezone", request.getTimezone());
    }

    @Test
    void shouldDeleteRegion() {
        when(regionRepository.findById(region.getId())).thenReturn(Optional.of(region));

        underTest.deleteRegion(region.getId());
        verify(regionRepository, times(1)).delete(region);
    }
}