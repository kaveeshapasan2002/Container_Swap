package com.zpmc.containerswap.presentation;

import com.zpmc.containerswap.application.ContainerService;
import com.zpmc.containerswap.domain.model.Container;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/containers")
public class ContainerController {

    private final ContainerService service;

    public ContainerController(ContainerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody Container container) {
        service.registerContainer(container);
        return ResponseEntity.ok("Registered: " + container.getContainerId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Container> get(@PathVariable String id) {
        return service.getContainer(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(service.getStatus());
    }
}
