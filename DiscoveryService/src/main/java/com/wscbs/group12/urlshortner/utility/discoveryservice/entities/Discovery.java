package com.wscbs.group12.urlshortner.utility.discoveryservice.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "Discovery")
public class Discovery {
    //    https://attacomsian.com/blog/http-requests-resttemplate-spring-boot
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "UUID", nullable = false, unique = true)
    private String uuid;

    @Column(name = "REF_ID", nullable = false, unique = true)
    private String refId;

    @Column(name = "SERVICE_NAME", nullable = false)
    private String serviceName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "PORT", nullable = false)
    private int port;

    @CreationTimestamp
    @Column(name = "CRTN_DATE", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Setter(AccessLevel.NONE)
    private Date crtnDate;

    @UpdateTimestamp
    @Column(name = "LAST_UPD_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Setter(AccessLevel.NONE)
    private Date lastUpdDate;

}
