package dev.sv.cloud_file_storage.controller;

import dev.sv.cloud_file_storage.config.security.PersistentUserDetails;
import dev.sv.cloud_file_storage.dto.ResourceDto;
import dev.sv.cloud_file_storage.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directory")
@RequiredArgsConstructor
public class DirectoryController {

    private final ResourceService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceDto create(@RequestParam("path") String path,
                              @AuthenticationPrincipal PersistentUserDetails user) {
        return service.createDirectory(path, user.getId());
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResourceDto> read(@RequestParam("path") String path,
                                  @AuthenticationPrincipal PersistentUserDetails user) {
        return service.getDirectory(path, user.getId());
    }

}
