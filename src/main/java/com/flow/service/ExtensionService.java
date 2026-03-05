package com.flow.service;

import com.flow.dto.ExtensionResponse;
import com.flow.dto.ExtensionResponse.CustomExtensionDto;
import com.flow.dto.ExtensionResponse.FixedExtensionDto;
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
}
