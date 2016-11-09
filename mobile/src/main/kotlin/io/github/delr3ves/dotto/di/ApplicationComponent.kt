package io.github.delr3ves.dotto.di

import dagger.Component
import io.github.delr3ves.dotto.DottoApplication
import io.github.delr3ves.dotto.MorseGeneratorActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: DottoApplication)

    fun inject(activity: MorseGeneratorActivity)
}