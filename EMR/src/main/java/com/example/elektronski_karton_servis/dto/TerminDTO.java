
package com.example.elektronski_karton_servis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminDTO {
    private Long id;
    private String datum;
    private String vrijemePocetka;
    private String vrijemeKraja;
    private Long pacijentId;
    private Long osobljeId;
}
