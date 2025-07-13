package br.com.api.infracoes.e2e;

import br.com.api.infracoes.shared.util.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("e2e")
public class ViolationsE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private String baseUrl;
    private String token;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        token = jwtTokenProvider.generateToken("InfracoesService");
    }

    @Test
    @DisplayName("Deveria causar excessão quando token não informado.")
    void shouldFailWhenTokenJWTNotExist() {
        MultiValueMap<String, Object> body = createViolationBody("SERIAL");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body);
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/violations", requestEntity, String.class
                                                                    );

        HttpEntity<MultiValueMap<String, Object>> requestEntityFetch = new HttpEntity<>(body);
        ResponseEntity<String> responseFetch = restTemplate.postForEntity(
                baseUrl + "/violations", requestEntityFetch, String.class
                                                                    );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, responseFetch.getStatusCode());
    }

    @Test
    @DisplayName("Deveria tentar cadastrar infração para equipamento inexistente")
    void shouldFailToCreateViolationWhenEquipmentDoesNotExist() {
        MultiValueMap<String, Object> body = createViolationBody("EQUP_NOT_EXISTS");

        HttpHeaders headers = headers();

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/violations", requestEntity, String.class
                                                                    );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Deveria tentar cadastrar infração para equipamento inativo")
    void shouldFailCreateViolationWhenEquipmentIsInvalid() {
        Map<String, Object> bodyEqp = createEquipmentInactive();
        ResponseEntity<Void> createResponse = createEquipmentHttp(bodyEqp);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        MultiValueMap<String, Object> body = createViolationBody(bodyEqp.get("serial").toString());

        HttpHeaders headers = headers();

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/violations", requestEntity, String.class
                                                                    );

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    @DisplayName("Cadastrar infração válida e receber 201 Created com Location")
    void shouldCreateViolationWhenEquipmentAndDataIsValid() {
        Map<String, Object> bodyEqp = createEquipmentActive();
        ResponseEntity<Void> createResponse = createEquipmentHttp(bodyEqp);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        MultiValueMap<String, Object> body = createViolationBody(bodyEqp.get("serial").toString());

        HttpHeaders headers = headers();

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/violations", requestEntity, String.class
                                                                    );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().get("Location"));
    }

    @Test
    @DisplayName("Listar infrações de um equipamento com filtro from / to retornando apenas o intervalo")
    void shoudListViolationsFromToWhenEquipmentAndFilterIsValid() {
        Map<String, Object> bodyEqp = createEquipmentActive();
        ResponseEntity<Void> createResponse = createEquipmentHttp(bodyEqp);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        MultiValueMap<String, Object> body = createViolationBody(bodyEqp.get("serial").toString());

        HttpHeaders headers = headers();

        for (int i = 0; 2 > i; ++i) {
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    baseUrl + "/violations", requestEntity, String.class
                                                                        );

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getHeaders().get("Location"));
        }

        ResponseEntity<Map> getResponse = restTemplate.exchange(
                baseUrl + "/equipments/" + bodyEqp.get("serial")+"/violations?from=2025-07-09T18:42:00Z&to=2025-07-09T18:42:00Z&page=0&size=5",
                HttpMethod.GET,
                createHttpEntity(null),
                Map.class

                                                               );
        Map<String, Object> page = (Map<String, Object>) getResponse.getBody().get("page");

        assertTrue(Integer.parseInt(page.get("totalElements").toString()) == 2, "Total de infrações deve ser igual a dois");
    }


    private MultiValueMap<String, Object> createViolationBody(String serial) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("serial", serial);
        body.add("occurrenceDateUtc", "2025-07-09T18:42:00Z");
        body.add("measuredSpeed", 100.0);
        body.add("consideredSpeed", 90.0);
        body.add("regulatedSpeed", 60.0);
        body.add("type", "Velocity");

        ByteArrayResource fileAsResource = new ByteArrayResource("test image content".getBytes()) {
            @Override
            public String getFilename() {
                return "test-image.jpg";
            }
        };
        body.add("picture", fileAsResource);

        return body;
    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }

    private ResponseEntity<Void> createEquipmentHttp(Map<String, Object> equipment) {
        Map<String, Object> equipmentData = createEquipmentInactive();
        return restTemplate.exchange(
                baseUrl + "/equipments",
                HttpMethod.POST,
                createHttpEntity(equipment),
                Void.class
                                    );
    }

    private HttpEntity<Object> createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(body, headers);
    }

    private Map<String, Object> createEquipmentInactive() {
        return Map.of(
                "serial", "SERIAL-" + System.currentTimeMillis(),
                "model", "Radar XPTO",
                "address", "Endereço aqui",
                "latitude", 40.0,
                "longitude", 50.0,
                "active", false
                     );
    }

    private Map<String, Object> createEquipmentActive() {
        return Map.of(
                "serial", "SERIAL-" + System.currentTimeMillis(),
                "model", "Radar XPTO",
                "address", "Endereço aqui",
                "latitude", 40.0,
                "longitude", 50.0,
                "active", true
                     );
    }



}
