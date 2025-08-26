package dev.sv.cloud_file_storage.minio;

import dev.sv.cloud_file_storage.config.MinIOContainer;
import dev.sv.cloud_file_storage.config.PostgreSQLContainer;
import dev.sv.cloud_file_storage.config.security.PersistentUserDetails;
import dev.sv.cloud_file_storage.config.security.PersistentUserDetailsService;
import dev.sv.cloud_file_storage.dto.AuthRequestDto;
import dev.sv.cloud_file_storage.dto.ResourceDto;
import dev.sv.cloud_file_storage.exception.ResourceNotFoundException;
import dev.sv.cloud_file_storage.repository.UserRepository;
import dev.sv.cloud_file_storage.service.AuthService;
import dev.sv.cloud_file_storage.service.ResourceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MinioTest implements MinIOContainer, PostgreSQLContainer {

    private static PersistentUserDetails defaultUserDetails;

    @Autowired
    private ResourceService resourceService;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.minio.endpoint", minio::getS3URL);
        registry.add("spring.minio.username", minio::getUserName);
        registry.add("spring.minio.password", minio::getPassword);
    }

    @BeforeAll
    static void setupUser(@Autowired UserRepository userRepository,
                          @Autowired AuthService authService,
                          @Autowired PersistentUserDetailsService persistentUserDetailsService) {
        userRepository.deleteAll();
        authService.register(
                new AuthRequestDto("cakeslayer", "password"),
                mock(HttpServletRequest.class),
                mock(HttpServletResponse.class)
        );
        authService.register(
                new AuthRequestDto("fartsmeller", "password"),
                mock(HttpServletRequest.class),
                mock(HttpServletResponse.class)
        );

        defaultUserDetails = (PersistentUserDetails) persistentUserDetailsService.loadUserByUsername("cakeslayer");
    }

    private static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersistentUserDetails principal = (PersistentUserDetails) authentication.getPrincipal();
        return principal.getId();
    }

    private static @NotNull MockMultipartFile createMockMultipartFile() {
        return new MockMultipartFile(
                "object",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    }

    @Test
    @Order(1)
    @WithUserDetails(value = "cakeslayer", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldUploadFileToUserDirectory() {
        String path = "";
        MockMultipartFile mockFile = createMockMultipartFile();
        Long userId = getCurrentUserId();

        List<ResourceDto> resources = resourceService.uploadResource(path, new MultipartFile[]{mockFile}, userId);

        assertEquals(1, resources.size());
        assertEquals(resources.getFirst().name(), mockFile.getOriginalFilename());
    }

    @Test
    @Order(2)
    @WithUserDetails(value = "cakeslayer", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldRenameUploadedFile() {
        String from = "hello.txt";
        String to = "bye.txt";
        Long userId = getCurrentUserId();

        ResourceDto resourceDto = resourceService.moveResource(from, to, userId);

        assertEquals(to, resourceDto.name());
    }

    @Test
    @Order(3)
    @WithUserDetails(value = "fartsmeller", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldNotAccessOtherUsersFiles() {
        String path = "bye.txt";
        ResourceDto resource = resourceService.getResource(path, defaultUserDetails.getId());
        assertNotNull(resource);

        assertThrows(ResourceNotFoundException.class,
                () -> resourceService.getResource(path, getCurrentUserId()));
    }

    @Test
    @Order(4)
    @WithUserDetails(value = "fartsmeller", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldOnlySearchOwnFiles() {
        String query = "by";
        MockMultipartFile mockMultipartFile = createMockMultipartFile();

        resourceService.uploadResource("by.txt", new MultipartFile[]{mockMultipartFile}, getCurrentUserId());

        assertEquals(1, resourceService.searchResource(query, getCurrentUserId()).size());
    }

}
