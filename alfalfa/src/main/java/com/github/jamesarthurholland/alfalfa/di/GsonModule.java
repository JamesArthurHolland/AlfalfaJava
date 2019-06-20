package com.github.jamesarthurholland.alfalfa.di;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class GsonModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Gson.class)
                .annotatedWith(Names.named("gson"))
                .toInstance(new Gson());
    }

}
