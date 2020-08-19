package com.aqrlei.litepermission

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * created by AqrLei on 2020/3/5
 */
@Parcelize
class PermissionWrap (val code:Int, val permission:String):Parcelable