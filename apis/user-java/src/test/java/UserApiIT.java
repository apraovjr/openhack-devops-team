import io.swagger.Swagger2SpringBoot;
import io.swagger.model.Profile;
import io.swagger.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Swagger2SpringBoot.class)
public class UserApiIT {

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    Profile profile = new Profile();

    @Before
    public void setup() {
        profile.setUserId("Hacker 1");
        profile.setId("aa1d876a-3e37-4a7a-8c9b-769ee6217ec1");
        profile.setTotalTrips(200);

        headers.set("Accept", MediaType.APPLICATION_JSON_UTF8.toString());
        headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString());
    }


    @Test
    public void updateProfile() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(3000);

        HttpEntity<Profile> entity = new HttpEntity<Profile>(profile, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("api/user-java/aa1d876a-3e37-4a7a-8c9b-769ee6217ec1"),
                HttpMethod.PATCH, entity, String.class);

        HttpStatus actual = response.getStatusCode();

        assertTrue(actual.is2xxSuccessful());

    }

    @Test
    public void updateProfile_returnError() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(3000);

        profile.setId("1234");

        HttpEntity<Profile> entity = new HttpEntity<>(profile, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("api/user-java/1234"),
                HttpMethod.PATCH, entity, String.class);

        HttpStatus actual = response.getStatusCode();

        assertTrue(actual.is5xxServerError());

    }

    private String createURLWithPort(String uri) {
        return "http://akstraefikopenhackn0v5.eastus.cloudapp.azure.com/" +uri;
    }
}
