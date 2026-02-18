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
    private OpenAPI openApi;

    @BeforeEach
    void setUp() {
        config = new OpenApiConfig();
        openApi = config.currencyOpenApi();
    }

    @Nested
    @DisplayName("Info Section Tests")
    class InfoSectionTests {

        @Test
        @DisplayName("OpenAPI should not be null")
        void openApiShouldNotBeNull() {
            assertNotNull(openApi);
        }

        @Test
        @DisplayName("Info should not be null")
        void infoShouldNotBeNull() {
            assertNotNull(openApi.getInfo());
        }

        @Test
        @DisplayName("Should have correct title")
        void shouldHaveCorrectTitle() {
            assertEquals("Currency Converter API", openApi.getInfo().getTitle());
        }

        @Test
        @DisplayName("Should have correct version")
        void shouldHaveCorrectVersion() {
            assertEquals("1.0", openApi.getInfo().getVersion());
        }

        @Test
        @DisplayName("Should have description")
        void shouldHaveDescription() {
            String description = openApi.getInfo().getDescription();
            assertNotNull(description);
            assertTrue(description.contains("USD"));
            assertTrue(description.contains("EUR"));
            assertTrue(description.contains("GBP"));
        }
    }

    @Nested
    @DisplayName("Contact Tests")
    class ContactTests {

        @Test
        @DisplayName("Contact should not be null")
        void contactShouldNotBeNull() {
            Contact contact = openApi.getInfo().getContact();
            assertNotNull(contact);
        }

        @Test
        @DisplayName("Contact should have name")
        void contactShouldHaveName() {
            assertEquals("Currency API Support", openApi.getInfo().getContact().getName());
        }

        @Test
        @DisplayName("Contact should have email")
        void contactShouldHaveEmail() {
            assertEquals("support@currency.local", openApi.getInfo().getContact().getEmail());
        }
    }

    @Nested
    @DisplayName("License Tests")
    class LicenseTests {

        @Test
        @DisplayName("License should not be null")
        void licenseShouldNotBeNull() {
            License license = openApi.getInfo().getLicense();
            assertNotNull(license);
        }

        @Test
        @DisplayName("License should have name")
        void licenseShouldHaveName() {
            assertEquals("Apache 2.0", openApi.getInfo().getLicense().getName());
        }

        @Test
        @DisplayName("License should have URL")
        void licenseShouldHaveUrl() {
            assertEquals("https://www.apache.org/licenses/LICENSE-2.0",
                    openApi.getInfo().getLicense().getUrl());
        }
    }

    @Nested
    @DisplayName("Servers Tests")
    class ServersTests {

        @Test
        @DisplayName("Should have one server")
        void shouldHaveOneServer() {
            List<Server> servers = openApi.getServers();
            assertNotNull(servers);
            assertEquals(1, servers.size());
        }

        @Test
        @DisplayName("Server should have correct URL")
        void serverShouldHaveCorrectUrl() {
            Server server = openApi.getServers().get(0);
            assertEquals("http://localhost:8080", server.getUrl());
        }

        @Test
        @DisplayName("Server should have description")
        void serverShouldHaveDescription() {
            Server server = openApi.getServers().get(0);
            assertEquals("Local server", server.getDescription());
        }
    }

    @Nested
    @DisplayName("Full OpenAPI Structure Tests")
    class FullOpenAPIStructureTests {

        @Test
        @DisplayName("OpenAPI should have info")
        void openApiShouldHaveInfo() {
            Info info = openApi.getInfo();
            assertNotNull(info);
            assertNotNull(info.getTitle());
            assertNotNull(info.getVersion());
            assertNotNull(info.getDescription());
        }

        @Test
        @DisplayName("OpenAPI should have servers")
        void openApiShouldHaveServers() {
            assertNotNull(openApi.getServers());
            assertFalse(openApi.getServers().isEmpty());
        }

        @Test
        @DisplayName("All required sections should be present")
        void allRequiredSectionsShouldBePresent() {
            assertNotNull(openApi.getInfo());
            assertNotNull(openApi.getInfo().getContact());
            assertNotNull(openApi.getInfo().getLicense());
            assertNotNull(openApi.getServers());
            assertFalse(openApi.getServers().isEmpty());
        }
    }
}
