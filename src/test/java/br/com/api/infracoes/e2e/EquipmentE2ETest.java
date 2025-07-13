package br.com.api.infracoes.e2e;

import br.com.api.infracoes.shared.util.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("e2e")
public class EquipmentE2ETest {

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

        Map<String, Object> equipmentData = createEquipment();
        ResponseEntity<Void> response =  restTemplate.exchange(
                baseUrl + "/equipments",
                HttpMethod.POST,
                createHttpEntityNoTokenJwt(equipmentData),
                Void.class
                                     );

        ResponseEntity<Map> responseFetch = restTemplate.exchange(
                baseUrl + "/equipments?page=0&size=10",
                HttpMethod.GET,
                createHttpEntityNoTokenJwt(null),
                Map.class
                                                            );


        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED, responseFetch.getStatusCode());
    }

    @Test
    @DisplayName("Criar equipamento válido e recuperá‐lo via GET /equipments/{serial}")
    void shouldCreateEquipmentAndRetrieveItSuccessfully() {

        Map<String, Object> equipmentData = createEquipment();
        ResponseEntity<Void> createResponse = createEquipmentHttp();

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        ResponseEntity<Map> getResponse = restTemplate.exchange(
                baseUrl + "/equipments/" + equipmentData.get("serial"),
                HttpMethod.GET,
                createHttpEntity(null),
                Map.class
                                                               );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());

        Map<String, Object> retrievedEquipment = getResponse.getBody();
        assertEquipmentMatches(equipmentData, retrievedEquipment);
    }

    @Test
    @DisplayName("Listar todos os equipamentos registrando pelo menos dois entries")
    void shouldListEquipmentsAfterSaveTwoAndRetrieveItSuccessfully() {

        ResponseEntity<Void> createResponse = createEquipmentHttp();
        ResponseEntity<Void> createResponse2 = createEquipmentHttp();


        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertEquals(HttpStatus.CREATED, createResponse2.getStatusCode());

        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/equipments?page=0&size=10",
                HttpMethod.GET,
                createHttpEntity(null),
                Map.class
                                                               );

        Map<String, Object> body = response.getBody();
        assertNotNull(body, "Resposta não deve ser nula");

        Map<String, Object> page = (Map<String, Object>) body.get("page");

       assertTrue(Integer.parseInt(page.get("totalElements").toString()) >= 2, "Total de equipamentos deve ser maior ou igual a dois");
    }

    private ResponseEntity<Void> createEquipmentHttp() {
        Map<String, Object> equipmentData = createEquipment();
       return  restTemplate.exchange(
                baseUrl + "/equipments",
                HttpMethod.POST,
                createHttpEntity(equipmentData),
                Void.class
                                                                   );
    }

    private Map<String, Object> createEquipment() {
        return Map.of(
                "serial", "SERIAL-" + System.currentTimeMillis(),
                "model", "Radar XPTO",
                "address", "Endereço aqui",
                "latitude", 40.0,
                "longitude", 50.0,
                "active", true
                     );
    }

    private HttpEntity<Object> createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(body, headers);
    }

    private HttpEntity<Object> createHttpEntityNoTokenJwt(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private void assertEquipmentMatches(Map<String, Object> expected, Map<String, Object> actual) {
        assertEquals(expected.get("serial"), actual.get("serial"));
        assertEquals(expected.get("model"), actual.get("model"));
        assertEquals(expected.get("address"), actual.get("address"));
        assertEquals(expected.get("latitude"), actual.get("latitude"));
        assertEquals(expected.get("longitude"), actual.get("longitude"));
        assertEquals(expected.get("active"), actual.get("active"));
    }

}
