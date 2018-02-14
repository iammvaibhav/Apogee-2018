package bitspilani.dvm.apogee2016.di.component

import bitspilani.dvm.apogee2016.di.PerFragment
import bitspilani.dvm.apogee2016.di.module.FragmentModule
import bitspilani.dvm.apogee2016.ui.events.EventsFragment
import bitspilani.dvm.apogee2016.ui.informatives.*
import bitspilani.dvm.apogee2016.ui.profile.ProfileFragment
import dagger.Component

/**
 * Created by Vaibhav on 04-02-2018.
 */

@PerFragment
@Component(dependencies = [ActivityComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(eventsFragment: EventsFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(emergencyFragment: EmergencyFragment)
    fun inject(aboutFragment: AboutFragment)
    fun inject(contactFragment: ContactFragment)
    fun inject(developersFragment: DevelopersFragment)
    fun inject(notificationsFragment: NotificationsFragment)
    fun inject(sponsorsFragment: SponsorsFragment)
}