package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.org.BoilerplateOrganisationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
public class OrgServiceTest {

    @Mock
    private BoilerplateOrganisationService organisationService;

    public void getAllMembers() {
        String organisationId = "";
        var res = organisationService.getAllMembers(organisationId, 0);
        System.out.println("res -- " + res);

    }


}
