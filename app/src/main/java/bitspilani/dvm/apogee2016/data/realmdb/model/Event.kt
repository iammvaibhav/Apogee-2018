package bitspilani.dvm.apogee2016.data.realmdb.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Event(@PrimaryKey var id: Int = 0,
                 var name: String = "",
                 var category: String = "",
                 var startTime: Date = Date(),
                 var endTime: Date = Date(startTime.time + 7200000),
                 var venue: String = "",
                 var description: String = "",
                 var rules: String? = null) : RealmObject()