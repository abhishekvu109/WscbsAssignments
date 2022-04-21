package com.wscbs.group12.urlshortner.utility.discoveryservice.model.loadbalancer;

import com.wscbs.group12.urlshortner.utility.discoveryservice.entities.Discovery;
import com.wscbs.group12.urlshortner.utility.discoveryservice.util.CommonUtil;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

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
