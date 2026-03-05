package com.flow.service;

import com.flow.dto.ExtensionResponse;
import com.flow.dto.ExtensionResponse.CustomExtensionDto;
import com.flow.dto.ExtensionResponse.FixedExtensionDto;
import com.flow.entity.CustomExtension;
import com.flow.exception.ExtensionException;
import com.flow.repository.CustomExtensionRepository;
import com.flow.repository.FixedExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExtensionService {

    private final FixedExtensionRepository fixedExtensionRepository;
    private final CustomExtensionRepository customExtensionRepository;

    @Transactional(readOnly = true)
    public ExtensionResponse getAll() {
        List<FixedExtensionDto> fixed = fixedExtensionRepository.findAll()
                .stream()
                .map(e -> new FixedExtensionDto(e.getName(), e.isBlocked()))
                .toList();

        List<CustomExtensionDto> custom = customExtensionRepository.findAllByOrderByCreatedAtAsc()
                .stream()
                .map(e -> new CustomExtensionDto(e.getId(), e.getName()))
                .toList();

        return new ExtensionResponse(fixed, custom);
    }

    public void toggleFixed(String name, boolean blocked) {
        fixedExtensionRepository.findById(name)
                .orElseThrow(() -> new ExtensionException("존재하지 않는 고정 확장자입니다: " + name))
                .updateBlocked(blocked);
    }

    public ExtensionResponse addCustom(String rawName) {
        String name = rawName.trim().toLowerCase();

        if (customExtensionRepository.count() >= 200) {
            throw new ExtensionException("커스텀 확장자는 최대 200개까지 추가 가능합니다.");
        }

        if (customExtensionRepository.existsByName(name)) {
            throw new ExtensionException("이미 추가된 확장자입니다: " + name);
        }

        customExtensionRepository.save(new CustomExtension(name));
        return getAll();
    }
}
