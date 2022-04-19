package br.com.agibank.batch.domain;

import java.nio.file.WatchEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString(of = {"origin","event"})
@Builder
public class KeyOriginAndEvent {

    private String origin;
    private WatchEvent.Kind<?> event;
    
}
