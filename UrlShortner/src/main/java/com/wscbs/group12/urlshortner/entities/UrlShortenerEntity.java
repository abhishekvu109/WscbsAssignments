package com.wscbs.group12.urlshortner.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "URLSHORTENERENTITY")
public class UrlShortenerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "UUID", nullable = false, unique = true)
    private String uuid;

    @Column(name = "REF_ID", nullable = false, unique = true)
    private String refId;

    @Column(name = "CLASS_GENERATOR", nullable = false)
    private String classGen;

    @Column(name = "URL")
    private String url;

    @Column(name = "TINY_URL")
    private String tinyUrl;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "EXPIRY_DURATION")
    private int duration;

    @Column(name = "EXPIRY_TIME")
    private LocalDateTime expiryTime;

    //  The generated hash of the URL+UserId+CreateDate
    @Column(name = "URL_HASH")
    private String urlHash;

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
