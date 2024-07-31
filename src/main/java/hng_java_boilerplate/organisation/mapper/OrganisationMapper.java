package hng_java_boilerplate.organisation.mapper;

import hng_java_boilerplate.organisation.dto.CreateOrganisationRequestDto;
import hng_java_boilerplate.organisation.dto.DataDto;
import hng_java_boilerplate.organisation.dto.OrgUsersPaginatedResponseDto;
import hng_java_boilerplate.organisation.dto.OrganisationsListDto;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrganisationMapper {
    public OrganisationsListDto mapToOranisationListDto(Organisation organisation) {
        return OrganisationsListDto.builder()
                .orgId(organisation.getId())
                .name(organisation.getName())
                .description(organisation.getDescription())
                .build();
    }

    public DataDto toOrgDataDto(Organisation organisation, User user) {
        return DataDto.builder()
                .id(organisation.getId())
                .name(organisation.getName())
                .description(organisation.getDescription())
                .owner_id(user.getId())
                .slug(organisation.getSlug())
                .email(organisation.getEmail())
                .industry(organisation.getIndustry())
                .type(organisation.getType())
                .country(organisation.getCountry())
                .address(organisation.getAddress())
                .state(organisation.getState())
                .created_at(LocalDateTime.now())
                .updated_at(null)
                .build();
    }

    public void setCreateOrganisationFields (
            Organisation organisation,
            CreateOrganisationRequestDto orgRequest,
            User user
    ) {
        organisation.setName(orgRequest.name());
        organisation.setDescription(orgRequest.description());
        organisation.setEmail(orgRequest.email());
        organisation.setIndustry(orgRequest.industry());
        organisation.setType(orgRequest.type());
        organisation.setCountry(orgRequest.country());
        organisation.setAddress(orgRequest.address());
        organisation.setState(orgRequest.state());
        organisation.setUsers(List.of(user));
        organisation.setCreatedAt(LocalDateTime.now());
        organisation.setUpdatedAt(null);
        organisation.setUsers(List.of(user));
        organisation.setOwner(user);
    }

    public OrgUsersPaginatedResponseDto toOrgUsersPaginatedResponseDto(User user) {
        return OrgUsersPaginatedResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone_number(null)
                .build();
    }
}
