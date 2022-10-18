package com.zonaut.playground.reactive.domain;

import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

@EqualsAndHashCode
public abstract class EntityObject<T> implements Persistable<T>, Serializable {

}
