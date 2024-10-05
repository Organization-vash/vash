package com.vash.entel.api;

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
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/generateCode")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketCodeController {
    private final TicketCodeService ticketCodeService;
    private final ServiceServiceImpl serviceService;
    private final AgencyServiceImpl agencyRepository;
    private final ServiceMapper serviceMapper; // Se añade el mapper para la conversión

    @PostMapping
    private ResponseEntity<Map<String, Object>> generateTicketCode(
            @RequestParam String document,
            @RequestParam String fullname,
            @RequestParam Integer serviceId,
            @RequestParam Integer agencyId){

        ServiceDTO serviceDTO = serviceService.findById(serviceId);
        Service service = serviceMapper.toEntity(serviceDTO);

        Agency agency = agencyRepository.findById(agencyId);

        String code = ticketCodeService.generateTicketCode(document, fullname, service, agency);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hola " + fullname + ", serás llamado pronto!");
        response.put("code", code);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
