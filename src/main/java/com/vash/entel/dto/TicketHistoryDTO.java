package com.vash.entel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketHistoryDTO {
    private String ticketCode;
    private String serviceName;
    private String customerFullName;
}
