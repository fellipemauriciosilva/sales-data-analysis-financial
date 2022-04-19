package br.com.agibank.batch.domain;

import java.nio.file.WatchService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString(of = {"watchService","walkAndRegisterDirectories"})
@Builder
public class ServiceWalkAndRegisterWrapper {

    private WatchService watchService;
    private Boolean walkAndRegisterDirectories;
	
}
