package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.service.RemoteTerminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class RemoteTerminController {
    private final RemoteTerminService remoteTerminService;

    @Autowired
    public RemoteTerminController(RemoteTerminService remoteTerminService) {
        this.remoteTerminService = remoteTerminService;
    }

    @GetMapping("/termini")
    public List<Termin> getRemoteTermini() {
        return remoteTerminService.fetchRemoteTermini();
    }
}
