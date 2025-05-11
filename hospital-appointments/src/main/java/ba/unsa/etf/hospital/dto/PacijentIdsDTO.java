package ba.unsa.etf.hospital.dto;

import lombok.Data;
import java.util.List;

@Data
public class PacijentIdsDTO {
    private List<Long> pacijentIds;

    public List<Long> getPacijentIds() {
        return pacijentIds;
    }

    public void setPacijentIds(List<Long> pacijentIds) {
        this.pacijentIds = pacijentIds;
    }
}
