package com.vash.entel.service.impl;

import com.vash.entel.dto.NextPendingTicketResponseDTO;
import com.vash.entel.model.entity.Attention;
import com.vash.entel.model.entity.Module;
import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.model.entity.WaitingQueue;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.model.enums.ModuleStatus;
import com.vash.entel.repository.AttentionRepository;
import com.vash.entel.repository.ModuleRepository;
import com.vash.entel.repository.TicketCodeRepository;
import com.vash.entel.repository.WaitingQueueRepository;
import com.vash.entel.service.AttentionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttentionServiceImpl implements AttentionService {}
