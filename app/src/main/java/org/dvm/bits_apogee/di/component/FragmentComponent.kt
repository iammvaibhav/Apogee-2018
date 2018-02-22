package org.dvm.bits_apogee.di.component

import org.dvm.bits_apogee.di.PerFragment
import org.dvm.bits_apogee.di.module.FragmentModule
import org.dvm.bits_apogee.ui.events.EventsFragment
import org.dvm.bits_apogee.ui.informatives.*
import org.dvm.bits_apogee.ui.profile.ProfileFragment
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