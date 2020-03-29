/*
 * Copyright (c) 2019-2020. Ivan Vakhrushev and others.
 * https://github.com/mfvanek/pg-index-health-demo
 *
 * Licensed under the Apache License 2.0
 */

package io.github.mfvanek.pg.index.health.demo;

import io.github.mfvanek.pg.connection.PgConnection;
import io.github.mfvanek.pg.connection.PgConnectionImpl;
import io.github.mfvanek.pg.model.MemoryUnit;
import io.github.mfvanek.pg.settings.ConfigurationMaintenance;
import io.github.mfvanek.pg.settings.ConfigurationMaintenanceImpl;
import io.github.mfvanek.pg.settings.PgParam;
import io.github.mfvanek.pg.settings.ServerSpecification;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;

import javax.annotation.Nonnull;
import java.util.Set;

public class ConfigurationDemoApp {

    public static void main(String[] args) {
        try (EmbeddedPostgres embeddedPostgres = EmbeddedPostgres.start()) {
            checkConfig(embeddedPostgres);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void checkConfig(@Nonnull final EmbeddedPostgres embeddedPostgres) {
        final PgConnection pgConnection = PgConnectionImpl.ofMaster(embeddedPostgres.getPostgresDatabase());
        final ConfigurationMaintenance configurationMaintenance = new ConfigurationMaintenanceImpl(pgConnection);
        final ServerSpecification serverSpecification = ServerSpecification.builder()
                .withCpuCores(Runtime.getRuntime().availableProcessors())
                .withMemoryAmount(16, MemoryUnit.GB)
                .withSSD()
                .build();
        final Set<PgParam> paramsWithDefaultValues = configurationMaintenance.getParamsWithDefaultValues(serverSpecification);
        paramsWithDefaultValues.forEach(System.out::println);
    }
}