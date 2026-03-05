package com.flow.controller;

import com.flow.dto.ExtensionResponse;
import com.flow.dto.FixedExtensionToggleRequest;
import com.flow.service.ExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/extensions")
@RequiredArgsConstructor
public class ExtensionController {

    private final ExtensionService extensionService;

    @GetMapping
    public ResponseEntity<ExtensionResponse> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(extensionService.getAll());
    }

    @PatchMapping("/fixed/{name}")
    public ResponseEntity<Void> toggleFixed(@PathVariable String name,
                                            @RequestBody FixedExtensionToggleRequest request) {
        extensionService.toggleFixed(name, request.isBlocked());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
