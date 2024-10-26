package com.vash.entel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NextPendingTicketResponseDTO {
    private String ticketCode;
    private String serviceName;
    private Long customerDocNumber;
    private String customerFullName;
}
