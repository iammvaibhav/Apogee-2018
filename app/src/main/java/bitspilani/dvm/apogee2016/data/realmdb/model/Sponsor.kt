package bitspilani.dvm.apogee2016.data.realmdb.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Vaibhav on 23-01-2018.
 */

open class Sponsor(@PrimaryKey var id: Int = 0,
                   var name: String = "",
                   var type: String = "",
                   var imageURL: String = "") : RealmObject()