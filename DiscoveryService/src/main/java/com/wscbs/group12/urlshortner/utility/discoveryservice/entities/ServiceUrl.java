package com.wscbs.group12.urlshortner.utility.discoveryservice.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "ServiceUrl")
public class ServiceUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "DISCOVERYID", nullable = false)
    private long discoveryId;

    @Column(name = "URL", nullable = false)
    private String url;
}
