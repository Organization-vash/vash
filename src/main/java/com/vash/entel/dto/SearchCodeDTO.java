package com.vash.entel.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchCodeDTO {
    private String ticketCode;
    private LocalDateTime created;
    private Integer moduleName;
    private String advisorName;
    private String customerName;
    private String attentionTime;
    private String attentionStatus;
    private String successStatus;
    private Integer surveyDetails;
}
