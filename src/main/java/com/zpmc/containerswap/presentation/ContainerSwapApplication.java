package com.zpmc.containerswap.presentation;

import com.zpmc.containerswap.application.ContainerService;
import com.zpmc.containerswap.domain.model.Container;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.zpmc.containerswap")
public class ContainerSwapApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContainerSwapApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(ContainerService service) {
        return args -> {
            System.out.println();
            System.out.println("╔═══════════════════════════════════════════╗");
            System.out.println("║   ZPMC Container Cache + Swap Demo       ║");
            System.out.println("║   RAM Capacity: 5 containers             ║");
            System.out.println("╚═══════════════════════════════════════════╝");
            System.out.println();

            String[] types = {"DRY", "REEFER", "TANK", "FLAT", "DRY", "REEFER", "DRY"};
            String[] statuses = {"YARD", "YARD", "GATE_IN", "YARD", "ON_VESSEL", "YARD", "GATE_IN"};

            for (int i = 1; i <= 7; i++) {
                Container c = new Container(
                    "ZPMC-2024-" + String.format("%05d", i),
                    types[i - 1],
                    statuses[i - 1],
                    "Block-" + (char)('A' + i % 4) + ", Row-" + i + ", Tier-1",
                    "MV Pacific Star",
                    12.5 + i * 2.3
                );
                service.registerContainer(c);
            }

            System.out.println("\n--- Status after 7 registrations (capacity=5) ---");
            service.getStatus().forEach((k, v) ->
                System.out.println("  " + k + ": " + v));

            System.out.println("\n--- Requesting ZPMC-2024-00001 (was swapped to disk) ---");
            service.getContainer("ZPMC-2024-00001")
                   .ifPresent(c -> System.out.println("  Retrieved: " + c));

            System.out.println("\n--- Final Status ---");
            service.getStatus().forEach((k, v) ->
                System.out.println("  " + k + ": " + v));

            System.out.println();
            System.out.println("╔═══════════════════════════════════════════════════╗");
            System.out.println("║   Demo Complete! REST API running on port 8080   ║");
            System.out.println("║   Try: curl localhost:8080/api/containers/status  ║");
            System.out.println("╚═══════════════════════════════════════════════════╝");
            System.out.println();
        };
    }
}
