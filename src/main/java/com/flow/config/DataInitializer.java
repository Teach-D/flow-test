package com.flow.config;

import com.flow.entity.FixedExtension;
import com.flow.repository.FixedExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final List<String> DEFAULT_FIXED_EXTENSIONS =
            List.of("bat", "cmd", "com", "cpl", "exe", "scr", "js");

    private final FixedExtensionRepository fixedExtensionRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (fixedExtensionRepository.count() == 0) {
            DEFAULT_FIXED_EXTENSIONS.forEach(name ->
                    fixedExtensionRepository.save(new FixedExtension(name))
            );
        }
    }
}
