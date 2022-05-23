package com.coufie.tugaslistnotechtujuh.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id : Int?,

    @ColumnInfo(name = "title")
    var title : String ,

    @ColumnInfo(name = "content")
    var content : String ,

    @ColumnInfo(name = "time")
    var time : String

    ) : Parcelable
