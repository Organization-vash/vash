package com.vash.entel.api;

import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.repository.TicketCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private TicketCodeRepository ticketRepository;

    @PostMapping("/pregunta")
    public ResponseEntity<Map<String, String>> handleQuestion(@RequestBody Map<String, String> request) {
        String pregunta = request.get("pregunta");

        // Buscar el ticket más reciente en la base de datos
        Ticket_code ticket = ticketRepository.findTopByOrderByCreatedDesc()
                .orElse(null); // Devuelve null si no hay tickets

        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró ningún ticket en el sistema."));
        }

        String responseMessage;

        // Formateador para la fecha y hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy 'a las' HH:mm");

        // Responder según la pregunta
        switch (pregunta.toLowerCase()) {
            case "¿cuál es el código de mi ticket?":
                responseMessage = "Tu código de ticket es: " + ticket.getCode();
                break;
            case "¿cuál es el servicio de mi ticket?":
                responseMessage = "El servicio elegido es " + ticket.getService().getName() + ". El tiempo estimado es 15 minutos.";
                break;
            case "¿cuándo saque mi ticket?":
                // Formatear la fecha de creación
                responseMessage = "Tu ticket fue creado el " + ticket.getCreated().format(formatter) + ".";
                break;
            default:
                responseMessage = "Lo siento, no entiendo tu pregunta.";
        }

        return ResponseEntity.ok(Map.of("message", responseMessage));
    }
}
