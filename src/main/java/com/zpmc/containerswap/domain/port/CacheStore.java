package com.zpmc.containerswap.domain.port;

import com.zpmc.containerswap.domain.model.Container;
import java.util.List;
import java.util.Optional;

public interface CacheStore {
    void put(String id, Container container);
    Optional<Container> get(String id);
    Container remove(String id);
    boolean isFull();
    int size();
    int capacity();
    List<String> getAllIds();
}
