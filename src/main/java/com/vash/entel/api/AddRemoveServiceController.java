package com.vash.entel.api;


import com.vash.entel.service.AddRemoveServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/addRemoveService")
public class AddRemoveServiceController {
    private final AddRemoveServiceService addRemoveServiceService;

    @GetMapping("/{attentionId}/service")
    public ResponseEntity<List<service>> getPublicationByPlaylist(@PathVariable Integer attentionId) {
        List<service> publication = attentionService.getPublicationByPlaylistId(attentionId);
        return ResponseEntity.ok(service);
    }

    @PostMapping("/add/service")
    public ResponseEntity<service> addPublicationToPlaylist(@RequestParam Integer playlistId, @RequestParam Integer publicationId) {
        Service updatedservice = addRemoveServiceService.addService(attentionId, serviceId);
        return ResponseEntity.ok(updatedpublication);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{playlistId}/publications/{publicationsId}")
    public ResponseEntity<Void> removePublicationFromPlaylist(@PathVariable Integer playlistId, @PathVariable Integer publicationId) {
        playlistService.removePublicationFromPlaylist(playlistId, publicationId);
        return ResponseEntity.noContent().build();
    }
}
