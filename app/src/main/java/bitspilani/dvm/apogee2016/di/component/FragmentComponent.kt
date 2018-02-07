package bitspilani.dvm.apogee2016.di.component

import bitspilani.dvm.apogee2016.di.PerFragment
import bitspilani.dvm.apogee2016.di.module.FragmentModule
import bitspilani.dvm.apogee2016.ui.events.EventsFragment
import dagger.Component

/**
 * Created by Vaibhav on 04-02-2018.
 */

@PerFragment
@Component(dependencies = [ActivityComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(eventsFragment: EventsFragment)
}