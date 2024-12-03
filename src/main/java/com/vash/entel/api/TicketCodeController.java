package com.vash.entel.api;

import com.vash.entel.dto.SearchCodeDTO;
import com.vash.entel.dto.ServiceDTO;
import com.vash.entel.mapper.ServiceMapper;
import com.vash.entel.model.entity.Agency;
import com.vash.entel.model.entity.Service;
import com.vash.entel.service.TicketCodeService;

import com.vash.entel.service.impl.AgencyServiceImpl;
import com.vash.entel.service.impl.ServiceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/code")
public class TicketCodeController {
    private final TicketCodeService ticketCodeService;
    private final ServiceServiceImpl serviceService;
    private final AgencyServiceImpl agencyService;
    private final ServiceMapper serviceMapper;

    @PostMapping("/generateCode")
    private ResponseEntity<Map<String, Object>> generateTicketCode(
            @RequestParam Long document,
            @RequestParam String fullname,
            @RequestParam Integer serviceId,
            @RequestParam Integer agencyId){

        ServiceDTO serviceDTO = serviceService.findById(serviceId);
        Service service = serviceMapper.toEntity(serviceDTO);

        Agency agency = agencyService.findById(agencyId);

        String code = ticketCodeService.generateTicketCode(document, fullname, service, agency);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hola " + fullname + ", serás llamado pronto!");
        response.put("code", code);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchCodeDTO>> searchTicketsByCode(@RequestParam String code) {
        List<SearchCodeDTO> tickets = ticketCodeService.searchTicketsByCode(code);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/derivate")
    public ResponseEntity<String> transferTicketToModule(
            @RequestParam Integer ticketId,
            @RequestParam Integer moduleId) {
        try {
            ticketCodeService.transferTicketToModule(ticketId, moduleId);
            return new ResponseEntity<>("Ticket transferido exitosamente", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
