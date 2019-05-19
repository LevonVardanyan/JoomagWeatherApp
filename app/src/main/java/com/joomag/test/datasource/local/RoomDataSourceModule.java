package com.joomag.test.datasource.local;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RoomDataSourceModule {

    @Binds
    abstract LocalDataSource bindRoomDataSource(RoomLocalDataSource roomLocalDataSource);
}
