package com.lab.blps.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationDto {
    private Long id;
    private String name;
    private String description;
    private String appFilePath;
    private LocalDateTime createdAt;
}
