package com.joomag.test.screen.home;

import android.app.Application;

import com.joomag.test.di.AppComponent;
import com.joomag.test.di.FragmentScoped;

import dagger.BindsInstance;
import dagger.Component;

@FragmentScoped
@Component(dependencies = {AppComponent.class}, modules = {HomeModule.class})
public interface HomeComponent {

    void inject(HomeFragment homeFragment);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        Builder appComponent(AppComponent appComponent);

        HomeComponent build();
    }

}
