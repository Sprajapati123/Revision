package com.example.revision.model

import android.os.Parcel
import android.os.Parcelable

data class CategoryModel(
    var categoryId: String = "",
    var categoryImageUrl: String = "",
    var categoryImageName: String = "",
    var categoryName: String = "",
    var categoryDescription: String = ""
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryId)
        parcel.writeString(categoryImageUrl)
        parcel.writeString(categoryImageName)
        parcel.writeString(categoryName)
        parcel.writeString(categoryDescription)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryModel> {
        override fun createFromParcel(parcel: Parcel): CategoryModel {
            return CategoryModel(parcel)
        }

        override fun newArray(size: Int): Array<CategoryModel?> {
            return arrayOfNulls(size)
        }
    }

}