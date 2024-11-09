package com.vash.entel.service.impl;

import com.vash.entel.model.entity.Attention;
import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.repository.AttentionRepository;
import com.vash.entel.repository.TicketCodeRepository;
import com.vash.entel.service.AttentionService;
import com.vash.entel.service.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AttentionServiceImpl implements AttentionService {

}
