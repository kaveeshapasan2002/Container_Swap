package com.zpmc.containerswap.infrastructure.swap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zpmc.containerswap.domain.model.Container;
import com.zpmc.containerswap.domain.port.SwapStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@Component
public class DiskSwapStore implements SwapStore {

    private static final Logger log = LoggerFactory.getLogger(DiskSwapStore.class);
    private final ObjectMapper mapper;
    private final Path swapDir;

    public DiskSwapStore(@Value("${swap.directory:./swap-storage}") String swapDirectory) {
        this.swapDir = Paths.get(swapDirectory);
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(swapDir);
        log.info("Swap storage initialized at: {}", swapDir.toAbsolutePath());
    }

    @Override
    public void writeToDisk(String id, Container container) {
        try {
            Path file = swapDir.resolve(sanitizeFilename(id) + ".json");
            mapper.writeValue(file.toFile(), container);
            log.info("SWAP-OUT: {} → disk", id);
        } catch (IOException e) {
            throw new RuntimeException("Failed to swap out container: " + id, e);
        }
    }

    @Override
    public Optional<Container> readFromDisk(String id) {
        try {
            Path file = swapDir.resolve(sanitizeFilename(id) + ".json");
            if (!Files.exists(file)) return Optional.empty();
            Container c = mapper.readValue(file.toFile(), Container.class);
            log.info("SWAP-IN: {} ← disk", id);
            return Optional.of(c);
        } catch (IOException e) {
            throw new RuntimeException("Failed to swap in container: " + id, e);
        }
    }

    @Override
    public boolean exists(String id) {
        return Files.exists(swapDir.resolve(sanitizeFilename(id) + ".json"));
    }

    @Override
    public void delete(String id) {
        try {
            Files.deleteIfExists(swapDir.resolve(sanitizeFilename(id) + ".json"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete swap file: " + id, e);
        }
    }

    @Override
    public int size() {
        try (var stream = Files.list(swapDir)) {
            return (int) stream.filter(p -> p.toString().endsWith(".json")).count();
        } catch (IOException e) {
            return 0;
        }
    }

    private String sanitizeFilename(String id) {
        return id.replaceAll("[^a-zA-Z0-9_-]", "_");
    }
}
