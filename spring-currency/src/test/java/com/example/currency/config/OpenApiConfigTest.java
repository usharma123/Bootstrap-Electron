package com.example.currency.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OpenApiConfig Tests")
class OpenApiConfigTest {

    private OpenApiConfig config;
    private OpenAPI openAPI;

    @BeforeEach
    void setUp() {
        config = new OpenApiConfig();
        openAPI = config.currencyOpenApi();
    }

    @Test
    @DisplayName("currencyOpenApi should return non-null OpenAPI object")
    void currencyOpenApiShouldReturnNonNull() {
        assertNotNull(openAPI);
    }

    @Nested
    @DisplayName("Info Tests")
    class InfoTests {

        @Test
        @DisplayName("Info should have correct title")
        void infoShouldHaveCorrectTitle() {
            Info info = openAPI.getInfo();
            assertNotNull(info);
            assertEquals("Currency Converter API", info.getTitle());
        }

        @Test
        @DisplayName("Info should have correct version")
        void infoShouldHaveCorrectVersion() {
            Info info = openAPI.getInfo();
            assertNotNull(info);
            assertEquals("1.0", info.getVersion());
        }

        @Test
        @DisplayName("Info should have correct description")
        void infoShouldHaveCorrectDescription() {
            Info info = openAPI.getInfo();
            assertNotNull(info);
            assertNotNull(info.getDescription());
            assertTrue(info.getDescription().contains("currency converter"));
        }
    }

    @Nested
    @DisplayName("Contact Tests")
    class ContactTests {

        @Test
        @DisplayName("Contact should have correct name")
        void contactShouldHaveCorrectName() {
            Contact contact = openAPI.getInfo().getContact();
            assertNotNull(contact);
            assertEquals("Currency API Support", contact.getName());
        }

        @Test
        @DisplayName("Contact should have correct email")
        void contactShouldHaveCorrectEmail() {
            Contact contact = openAPI.getInfo().getContact();
            assertNotNull(contact);
            assertEquals("support@currency.local", contact.getEmail());
        }
    }

    @Nested
    @DisplayName("License Tests")
    class LicenseTests {

        @Test
        @DisplayName("License should have correct name")
        void licenseShouldHaveCorrectName() {
            License license = openAPI.getInfo().getLicense();
            assertNotNull(license);
            assertEquals("Apache 2.0", license.getName());
        }

        @Test
        @DisplayName("License should have correct URL")
        void licenseShouldHaveCorrectUrl() {
            License license = openAPI.getInfo().getLicense();
            assertNotNull(license);
            assertEquals("https://www.apache.org/licenses/LICENSE-2.0", license.getUrl());
        }
    }

    @Nested
    @DisplayName("Server Tests")
    class ServerTests {

        @Test
        @DisplayName("Should have at least one server configured")
        void shouldHaveAtLeastOneServer() {
            List<Server> servers = openAPI.getServers();
            assertNotNull(servers);
            assertFalse(servers.isEmpty());
        }

        @Test
        @DisplayName("Server should have correct URL")
        void serverShouldHaveCorrectUrl() {
            List<Server> servers = openAPI.getServers();
            assertEquals("http://localhost:8080", servers.get(0).getUrl());
        }

        @Test
        @DisplayName("Server should have correct description")
        void serverShouldHaveCorrectDescription() {
            List<Server> servers = openAPI.getServers();
            assertEquals("Local server", servers.get(0).getDescription());
        }
    }
}