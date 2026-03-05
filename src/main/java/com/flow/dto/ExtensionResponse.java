package com.flow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ExtensionResponse {

    private List<FixedExtensionDto> fixed;
    private List<CustomExtensionDto> custom;

    @Getter
    @AllArgsConstructor
    public static class FixedExtensionDto {
        private String name;
        private boolean blocked;
    }

    @Getter
    @AllArgsConstructor
    public static class CustomExtensionDto {
        private Long id;
        private String name;
    }
}
