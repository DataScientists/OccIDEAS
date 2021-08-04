package org.occideas.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.security.dao.UserDao;
import org.occideas.security.filter.WSConstants;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class SecurityConfigTest {

    TestRestTemplate restTemplate;
    URL base;
    @LocalServerPort
    int port;
    @MockBean
    UserDao userDao;
    @MockBean
    TokenManager tokenManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        restTemplate = new TestRestTemplate();
        base = new URL("http://localhost:" + port);
    }

    @Test
    public void givenApplicationIsUp_whenCheckActuator_thenReturnSuccess(){
        ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString()+"/actuator/health", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void givenApplicationIsUp_whenLoggedUserRequestsHomePage_thenReturnSuccess()
            throws IllegalStateException  {
        ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString(), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("OccIDEAS"));
    }

    @Test
    public void givenApplicationIsUp_whenLoggedUserRequestsAdminPage_thenUnauthorized() {
        restTemplate = new TestRestTemplate();
        ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString()+"/web/rest/admin/purgeModule", String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void givenApplicationIsUp_whenLoggedUserLogins_thenAuthorized() {
        String username = "admin";
        String password = "test";
        User user = createUser(username, password);
        when(userDao.findBySSO(username)).thenReturn(user);
        restTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(WSConstants.AUTH_USERNAME_PROP, username);
        headers.add(WSConstants.AUTH_PWD_PROP, password);
        Map<String, Object> map = new HashMap<>();

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Void> response =
                restTemplate.postForEntity(base.toString()+"/web/security/login",
                        entity,
                        Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void givenApplicationIsUp_whenLoggedUserLogins_thenAuthorizedToPurge() {
        String username = "admin";
        String password = "test";
        User user = createUser(username, password);
        when(userDao.findBySSO(username)).thenReturn(user);
        String valid_token = "valid token";
        doNothing().when(tokenManager).validateToken(valid_token);
        when(tokenManager.parseUsernameFromToken(valid_token)).thenReturn(username);
        when(tokenManager.parseAuthFromToken(valid_token)).thenReturn(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        when(tokenManager.parseExpiryFromToken(valid_token)).thenReturn("Valid Expiry");
        restTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(WSConstants.AUTH_TOKEN, valid_token);
        Map<String, Object> map = new HashMap<>();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Void> response =
                restTemplate.exchange(base.toString()+"/web/rest/admin/purgeModule", HttpMethod.GET, entity, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private User createUser(String username, String password) {
        User user = new User();
        user.setSsoId(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName("Sample");
        user.setLastName("Last");
        user.setEmail("sample@email.com");
        return user;
    }

}