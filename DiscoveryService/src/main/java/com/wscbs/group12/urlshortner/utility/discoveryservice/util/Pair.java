package com.wscbs.group12.urlshortner.utility.discoveryservice.util;

import lombok.*;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Data
public class Pair<K, V> {
    private K key;
    private V value;

}
