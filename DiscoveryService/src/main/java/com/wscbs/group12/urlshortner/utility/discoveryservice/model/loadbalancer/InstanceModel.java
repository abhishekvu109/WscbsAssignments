package com.wscbs.group12.urlshortner.utility.discoveryservice.model.loadbalancer;

import com.wscbs.group12.urlshortner.utility.discoveryservice.entities.Discovery;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class InstanceModel {

    private Long id;

    private Integer requestId;

    private Long hashRingLocation;

    private Discovery allocatedInstance;

}
