package com.vash.entel.service.impl;

import com.vash.entel.model.entity.WaitingQueue;
import com.vash.entel.repository.WaitingQueueRepository;
import com.vash.entel.service.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WaitingQueueServiceImpl implements WaitingQueueService {
    private final WaitingQueueRepository waitingQueueRepository;

    @Transactional(readOnly = true)
    @Override
    public List<WaitingQueue> getAllTicketsInQueue() {
        return waitingQueueRepository.findAll();
    }

    @Transactional
    @Override
    public WaitingQueue addTicketToQueue(WaitingQueue waitingQueue) {
        return waitingQueueRepository.save(waitingQueue);
    }
}
