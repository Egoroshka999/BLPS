package com.lab.blps.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationPayload implements Serializable {

    private String to;
    private String subject;
    private String body;

}

