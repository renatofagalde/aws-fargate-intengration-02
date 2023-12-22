package br.com.likwi.awsfargate.model;


import br.com.likwi.awsfargate.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Envelope {
    private EventType eventType;
    private String data;

}
