package dev.sv.cloud_file_storage.controller;

import dev.sv.cloud_file_storage.dto.ResourceDto;
import dev.sv.cloud_file_storage.service.DirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/directory")
@RequiredArgsConstructor
public class DirectoryController {

    private final DirectoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceDto create(@RequestParam("path") String path) {
        return service.createDirectory(path);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResourceDto read(@RequestParam("path") String path) {
        return service.getDirectory(path);
    }

}
