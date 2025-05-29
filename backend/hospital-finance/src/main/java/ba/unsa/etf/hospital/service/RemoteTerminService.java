package ba.unsa.etf.hospital.service;
import ba.unsa.etf.hospital.client.TerminiClient;
import ba.unsa.etf.hospital.model.Termin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RemoteTerminService {

    private final TerminiClient terminiClient;

    @Autowired
    public RemoteTerminService(TerminiClient terminiClient) {
        this.terminiClient = terminiClient;
    }

    public List<Termin> fetchRemoteTermini() {
        return terminiClient.getAllTermini();
    }
}
