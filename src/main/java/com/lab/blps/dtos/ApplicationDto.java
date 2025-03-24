package com.lab.blps.dtos;

import lombok.Data;

@Data
public class ApplicationDto {
    private Long id;
    private String name;
    private String description;
    private String appFilePath;
}
