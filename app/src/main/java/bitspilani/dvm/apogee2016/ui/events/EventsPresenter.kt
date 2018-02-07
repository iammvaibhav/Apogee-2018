package bitspilani.dvm.apogee2016.ui.events

import bitspilani.dvm.apogee2016.data.DataManager
import bitspilani.dvm.apogee2016.data.realmdb.model.Event
import bitspilani.dvm.apogee2016.di.PerFragment
import bitspilani.dvm.apogee2016.ui.base.BasePresenter
import io.realm.Realm
import javax.inject.Inject

/**
 * Created by Vaibhav on 04-02-2018.
 */

@PerFragment
class EventsPresenter<V: EventsMvpView> @Inject constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), EventsMvpPresenter<V> {

    lateinit var realm: Realm

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        realm = Realm.getDefaultInstance()
    }

    fun getQueries() = realm.where(Event::class.java).findAll()
    fun dataManager() = getDataManager()

    override fun onDetach() {
        super.onDetach()
        realm.close()
    }
}