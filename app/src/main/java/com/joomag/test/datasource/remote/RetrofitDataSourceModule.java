package com.joomag.test.datasource.remote;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RetrofitDataSourceModule {

    @Binds
    abstract RemoteDataSource bindRegrofitDataSource(RetrofitRemoteDataSource retrofitRemoteDataSource);

}
