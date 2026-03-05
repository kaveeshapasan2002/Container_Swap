package com.zpmc.containerswap.domain.port;

import com.zpmc.containerswap.domain.model.Container;
import java.util.Optional;

public interface SwapStore {
    void writeToDisk(String id, Container container);
    Optional<Container> readFromDisk(String id);
    boolean exists(String id);
    void delete(String id);
    int size();
}
