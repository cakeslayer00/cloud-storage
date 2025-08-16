package dev.sv.cloud_file_storage.controller;

import dev.sv.cloud_file_storage.config.security.PersistentUserDetails;
import dev.sv.cloud_file_storage.dto.ResourceDto;
import dev.sv.cloud_file_storage.service.ResourceService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResourceDto get(@RequestParam("path") String path,
                           @AuthenticationPrincipal PersistentUserDetails user) {
        return service.getResource(path, user.getId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("path") String path,
                       @AuthenticationPrincipal PersistentUserDetails user) {
        service.deleteResource(path, user.getId());
    }

    @GetMapping("/download")
    @ResponseStatus(HttpStatus.OK)
    public void download(@RequestParam("path") String path,
                         @AuthenticationPrincipal PersistentUserDetails user,
                         HttpServletResponse response) {
        service.downloadResource(path, user.getId(), response);
    }

    @GetMapping("/move")
    @ResponseStatus(HttpStatus.OK)
    public ResourceDto move(@RequestParam("from") String from,
                            @RequestParam("to") String to,
                            @AuthenticationPrincipal PersistentUserDetails user) {
        return service.moveResource(from, to, user.getId());
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ResourceDto> search(@RequestParam("query") String query,
                                    @AuthenticationPrincipal PersistentUserDetails user) {
        return service.searchResource(query, user.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<ResourceDto> upload(@RequestParam("path") String path,
                                    @RequestParam("object") MultipartFile[] files,
                                    @AuthenticationPrincipal PersistentUserDetails user) {
        return service.uploadResource(path, files, user.getId());
    }

}
