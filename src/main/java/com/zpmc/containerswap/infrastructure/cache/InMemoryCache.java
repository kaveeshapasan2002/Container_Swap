package com.zpmc.containerswap.infrastructure.cache;

import com.zpmc.containerswap.domain.model.Container;
import com.zpmc.containerswap.domain.port.CacheStore;
import com.zpmc.containerswap.domain.port.SwapPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryCache implements CacheStore, SwapPolicy {

    private final int maxCapacity;
    private final LinkedHashMap<String, Container> cache;

    public InMemoryCache(@Value("${cache.max-capacity:5}") int maxCapacity) {
        this.maxCapacity = maxCapacity;
        // accessOrder = true → LRU: every get() moves entry to the end
        // first entry = least recently used
        this.cache = new LinkedHashMap<>(maxCapacity, 0.75f, true);
    }

    @Override
    public void put(String id, Container container) {
        container.touch();
        cache.put(id, container);
    }

    @Override
    public Optional<Container> get(String id) {
        Container c = cache.get(id);
        if (c != null) c.touch();
        return Optional.ofNullable(c);
    }

    @Override
    public Container remove(String id) {
        return cache.remove(id);
    }

    @Override
    public boolean isFull() {
        return cache.size() >= maxCapacity;
    }

    @Override
    public int size() { return cache.size(); }

    @Override
    public int capacity() { return maxCapacity; }

    @Override
    public List<String> getAllIds() {
        return new ArrayList<>(cache.keySet());
    }

    // SwapPolicy: LRU victim = first entry in access-ordered LinkedHashMap
    @Override
    public String selectVictim() {
        Iterator<String> it = cache.keySet().iterator();
        return it.hasNext() ? it.next() : null;
    }
}
