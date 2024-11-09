package com.dicoding.asclepius.local.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dicoding.asclepius.helper.DateHelper
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class History(
    @PrimaryKey(autoGenerate = true)
    var nid : Int = 0,

    @ColumnInfo(name = "foto")
    var imageClassifier : String? = null,

    @ColumnInfo(name = "result")
    var results : String? = null,

    @ColumnInfo(name = "date")
    var date : String? = null
) : Parcelable