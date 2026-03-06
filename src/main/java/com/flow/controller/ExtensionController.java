package com.flow.controller;

import com.flow.dto.CustomExtensionRequest;
import com.flow.dto.ExtensionResponse;
import com.flow.dto.FixedExtensionToggleRequest;
import com.flow.service.ExtensionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Extension", description = "파일 확장자 차단 API")
@RestController
@RequestMapping("/extensions")
@RequiredArgsConstructor
public class ExtensionController {

    private final ExtensionService extensionService;

    @Operation(summary = "확장자 전체 조회", description = "고정 확장자 및 커스텀 확장자 전체 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<ExtensionResponse> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(extensionService.getAll());
    }

    @Operation(summary = "고정 확장자 토글", description = "고정 확장자의 차단 여부를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토글 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 확장자")
    })
    @PatchMapping("/fixed/{name}")
    public ResponseEntity<ExtensionResponse> toggleFixed(
            @Parameter(description = "확장자명", example = "exe") @PathVariable String name,
            @RequestBody FixedExtensionToggleRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(extensionService.toggleFixed(name, request.isBlocked()));
    }

    @Operation(summary = "커스텀 확장자 추가", description = "커스텀 확장자를 추가합니다. 최대 200개, 최대 20자, 영문+숫자만 허용합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "추가 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패 또는 중복")
    })
    @PostMapping("/custom")
    public ResponseEntity<ExtensionResponse> addCustom(@RequestBody @Valid CustomExtensionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionService.addCustom(request.getName()));
    }

    @Operation(summary = "커스텀 확장자 삭제", description = "커스텀 확장자를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 확장자")
    })
    @DeleteMapping("/custom/{id}")
    public ResponseEntity<ExtensionResponse> deleteCustom(
            @Parameter(description = "확장자 ID") @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(extensionService.deleteCustom(id));
    }
}
