package com.vash.entel.service;

import com.vash.entel.model.entity.WaitingQueue;
import com.vash.entel.repository.WaitingQueueRepository;

import java.util.List;

public interface WaitingQueueService {
    List<WaitingQueue> getAllTicketsInQueue();
    WaitingQueue addTicketToQueue(WaitingQueue waitingQueue);
}
