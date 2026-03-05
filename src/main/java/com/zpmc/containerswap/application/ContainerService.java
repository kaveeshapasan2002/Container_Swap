package com.zpmc.containerswap.application;

import com.zpmc.containerswap.domain.model.Container;
import com.zpmc.containerswap.domain.port.CacheStore;
import com.zpmc.containerswap.domain.port.SwapPolicy;
import com.zpmc.containerswap.domain.port.SwapStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContainerService {

    private static final Logger log = LoggerFactory.getLogger(ContainerService.class);

    private final CacheStore cache;
    private final SwapStore swap;
    private final SwapPolicy policy;

    public ContainerService(CacheStore cache, SwapStore swap, SwapPolicy policy) {
        this.cache = cache;
        this.swap = swap;
        this.policy = policy;
    }

    public void registerContainer(Container container) {
        if (cache.isFull()) {
            evictToSwap();
        }
        cache.put(container.getContainerId(), container);
        swap.delete(container.getContainerId());
        log.info("REGISTERED in RAM: {}", container);
    }

    public Optional<Container> getContainer(String containerId) {
        // 1. Check RAM
        Optional<Container> fromCache = cache.get(containerId);
        if (fromCache.isPresent()) {
            log.info("HIT [RAM]: {}", containerId);
            return fromCache;
        }

        // 2. Check swap (disk)
        Optional<Container> fromSwap = swap.readFromDisk(containerId);
        if (fromSwap.isPresent()) {
            log.info("HIT [SWAP]: {} — swapping into RAM", containerId);
            Container c = fromSwap.get();
            swap.delete(containerId);

            if (cache.isFull()) {
                evictToSwap();
            }
            cache.put(containerId, c);
            return Optional.of(c);
        }

        log.info("MISS: {} not found anywhere", containerId);
        return Optional.empty();
    }

    public Map<String, Object> getStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("ramUsed", cache.size());
        status.put("ramCapacity", cache.capacity());
        status.put("swapUsed", swap.size());
        status.put("containersInRam", cache.getAllIds());
        return status;
    }

    private void evictToSwap() {
        String victimId = policy.selectVictim();
        if (victimId == null) return;
        Container victim = cache.remove(victimId);
        swap.writeToDisk(victimId, victim);
        log.warn("EVICTED: {} → swap (LRU)", victimId);
    }
}
