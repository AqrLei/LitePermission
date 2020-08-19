package com.aqrlei.litepermission

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * created by AqrLei on 2020/2/29
 */
@Parcelize
class PermissionsNeededModule(
    vararg val permissionNeeded: PermissionNeeded,
    val summaryDesc: String = "",
    val desc: String = ""
) : Parcelable