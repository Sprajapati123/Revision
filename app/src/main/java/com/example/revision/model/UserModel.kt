package com.example.revision.model

import android.os.Parcel
import android.os.Parcelable

data class UserModel(
    var id : String = "",
    var email : String = "",
    var name : String = "",
    var address : String = "",
    var age : String = "",
    var imageName: String = "",
    var imageUrl : String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(email)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(age)
        parcel.writeString(imageName)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}