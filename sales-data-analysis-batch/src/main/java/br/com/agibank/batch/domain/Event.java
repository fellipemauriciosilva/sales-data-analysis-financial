package br.com.agibank.batch.domain;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString(of = {"kind","path"})
@Builder
public class Event implements Comparable<Event> {

    private final String origin;
    private final WatchEvent.Kind<?> kind;
    private final Path path;
        
	@Override
	public int compareTo(Event event) {
        return this.path.compareTo(event.path) + (this.kind.hashCode() - event.kind.hashCode());		
	}
	
}
