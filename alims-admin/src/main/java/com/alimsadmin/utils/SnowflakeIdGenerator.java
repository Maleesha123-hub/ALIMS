package com.alimsadmin.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * ==============================================================
 * Unique entity ID generator implementation for Spring.boot framework
 * ==============================================================
 **/

public class SnowflakeIdGenerator implements IdentifierGenerator {

    private Snowflake snowflake = new Snowflake();

    public SnowflakeIdGenerator() {
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return Long.valueOf(this.snowflake.newId());
    }
}
