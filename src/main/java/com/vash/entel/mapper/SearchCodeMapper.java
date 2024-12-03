package com.vash.entel.mapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.vash.entel.dto.SearchCodeDTO;

@Component
public class SearchCodeMapper {
    public List<SearchCodeDTO> toDtoList(List<Object[]> results) {
        return results.stream().map(this::toDto).collect(Collectors.toList());
    }

    public SearchCodeDTO toDto(Object[] result) {
        return new SearchCodeDTO(
                (String) result[0],                   // ticketCode
                convertTimestampToLocalDateTime(result[1]),            // created
                (Integer) result[2],                  // moduleName
                (String) result[3],                   // advisorName
                (String) result[4],                   // customerName
                result[5] != null ? result[5].toString() : null,  // attentionTime
                (String) result[6],                   // attentionStatus
                (String) result[7],                   // successStatus
                (Integer) result[8]                   // surveyDetails
        );
    }
    private LocalDateTime convertTimestampToLocalDateTime(Object timestamp) {
        if (timestamp instanceof Timestamp) {
            return ((Timestamp) timestamp).toLocalDateTime();
        }
        return null; // Devuelve null si el valor es nulo o no es un Timestamp
    }
}
