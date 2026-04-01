package com.htc.fastfoodapp.fastfood.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;
    private String message;
    private String error;
    private String path;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
