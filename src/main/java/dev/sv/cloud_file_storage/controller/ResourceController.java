package dev.sv.cloud_file_storage.controller;

import dev.sv.cloud_file_storage.dto.ResourceDto;
import dev.sv.cloud_file_storage.service.ResourceService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResourceDto get(@RequestParam("path") String path) {
        return service.getResource(path);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("path") String path) {
        service.deleteResource(path);
    }

    @GetMapping("/download")
    @ResponseStatus(HttpStatus.OK)
    public void download(@RequestParam("path") String path,
                         HttpServletResponse response) {
        service.downloadResource(path, response);
    }

    @GetMapping("/move")
    @ResponseStatus(HttpStatus.OK)
    public ResourceDto move(@RequestParam("from") String from,
                            @RequestParam("to") String to) {
        return service.moveResource(from, to);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResourceDto search(@RequestParam("query") String query) {
        return service.searchResource(query);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceDto upload(@RequestParam("path") String path,
                              @RequestParam("files") MultipartFile[] files) {
        return service.uploadResource(path, files);
    }

}
