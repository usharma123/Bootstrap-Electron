package com.example.currency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CurrencyApplication Tests")
class CurrencyApplicationTest {

    @Nested
    @DisplayName("Main Method Tests")
    class MainMethodTests {

        @Test
        @DisplayName("main method should exist")
        void mainMethodShouldExist() throws NoSuchMethodException {
            Method mainMethod = CurrencyApplication.class.getMethod("main", String[].class);
            assertNotNull(mainMethod);
        }

        @Test
        @DisplayName("main method should be static")
        void mainMethodShouldBeStatic() throws NoSuchMethodException {
            Method mainMethod = CurrencyApplication.class.getMethod("main", String[].class);
            assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
        }

        @Test
        @DisplayName("main method should be public")
        void mainMethodShouldBePublic() throws NoSuchMethodException {
            Method mainMethod = CurrencyApplication.class.getMethod("main", String[].class);
            assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
        }
    }

    @Nested
    @DisplayName("Application Class Tests")
    class ApplicationClassTests {

        @Test
        @DisplayName("CurrencyApplication should have SpringBootApplication annotation")
        void shouldHaveSpringBootApplicationAnnotation() {
            assertNotNull(CurrencyApplication.class.getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class));
        }

        @Test
        @DisplayName("CurrencyApplication should be a public class")
        void shouldBePublicClass() {
            assertTrue(java.lang.reflect.Modifier.isPublic(CurrencyApplication.class.getModifiers()));
        }

        @Test
        @DisplayName("CurrencyApplication should not be abstract")
        void shouldNotBeAbstract() {
            assertFalse(java.lang.reflect.Modifier.isAbstract(CurrencyApplication.class.getModifiers()));
        }

        @Test
        @DisplayName("CurrencyApplication should have main method")
        void shouldHaveMainMethod() {
            try {
                Method mainMethod = CurrencyApplication.class.getMethod("main", String[].class);
                assertNotNull(mainMethod);
            } catch (NoSuchMethodException e) {
                fail("main method not found");
            }
        }

        @Test
        @DisplayName("CurrencyApplication should be in correct package")
        void shouldBeInCorrectPackage() {
            assertEquals("com.example.currency", CurrencyApplication.class.getPackageName());
        }
    }

    @Nested
    @DisplayName("Class Structure Tests")
    class ClassStructureTests {

        @Test
        @DisplayName("CurrencyApplication should be a top-level class")
        void shouldBeATopLevelClass() {
            assertFalse(CurrencyApplication.class.isMemberClass());
            assertFalse(CurrencyApplication.class.isLocalClass());
            assertFalse(CurrencyApplication.class.isAnonymousClass());
        }

        @Test
        @DisplayName("CurrencyApplication should not be an interface")
        void shouldNotBeAnInterface() {
            assertFalse(CurrencyApplication.class.isInterface());
        }

        @Test
        @DisplayName("CurrencyApplication should not be an enum")
        void shouldNotBeAnEnum() {
            assertFalse(CurrencyApplication.class.isEnum());
        }

        @Test
        @DisplayName("CurrencyApplication should not be a record")
        void shouldNotBeARecord() {
            assertFalse(CurrencyApplication.class.isRecord());
        }
    }

    @Nested
    @DisplayName("Annotation Tests")
    class AnnotationTests {

        @Test
        @DisplayName("Should have SpringBootApplication annotation")
        void shouldHaveSpringBootApplicationAnnotation() {
            org.springframework.boot.autoconfigure.SpringBootApplication annotation =
                    CurrencyApplication.class.getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class);
            assertNotNull(annotation);
        }
    }
}
