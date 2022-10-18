package com.zonaut.playground.reactive.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zonaut.playground.reactive.domain.EntityObject;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Table(value = CustomerEntity.CUSTOMERS_TABLE_NAME)
public class CustomerEntity extends EntityObject<UUID> {

    protected static final String CUSTOMERS_TABLE_NAME = "customers";

    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String USERNAME = "username";
    public static final String INFO = "info";

    @Id
    @Column(ID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(CREATED_AT)
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @Column(USERNAME)
    @NonNull
    @Setter(AccessLevel.NONE)
    private String username;

    // https://github.com/pgjdbc/r2dbc-postgresql#jsonjsonb-support
    @Column(INFO)
    private Json info;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return this.createdAt == null;
    }
}
