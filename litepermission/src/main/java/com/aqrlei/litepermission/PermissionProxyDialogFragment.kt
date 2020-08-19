package com.aqrlei.litepermission

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.os.bundleOf

/**
 * created by AqrLei on 2020/2/29
 */
abstract class PermissionProxyDialogFragment : BasePermissionDialogFragment() {

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 5001
        const val PERMISSIONS_NEEDED = "permissions_needed"

        fun bundleNeedPermissions(
            vararg permissionsNeeded: PermissionNeeded,
            summaryDesc: String,
            desc: String
        ): Bundle {
            return bundleOf(
                PERMISSIONS_NEEDED to PermissionsNeededModule(
                    *permissionsNeeded,
                    summaryDesc = summaryDesc,
                    desc = desc))
        }
    }

    private var permissionCallback: IPermissionCallback? = null
    protected var permissionsNeededModule: PermissionsNeededModule? = null

    fun setPermissionCallback(permissionCallback: IPermissionCallback) {
        this.permissionCallback = permissionCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionsNeededModule = arguments?.getParcelable(PERMISSIONS_NEEDED)
        filterGrantedPermissions(permissionsNeededModule)
        bindView(view, permissionsNeededModule)
    }

    abstract fun bindView(v: View, permissionsNeededModule: PermissionsNeededModule?)

    abstract fun updatePermissionStatus(permissionsNeededModule: PermissionsNeededModule?)

    private fun filterGrantedPermissions(permissionsNeededModule: PermissionsNeededModule?) {
        permissionsNeededModule ?: return
        for (permissionNeeded in permissionsNeededModule.permissionNeeded) {
            context?.let {
                if (PermissionHelper.checkPermission(it, permissionNeeded.permission)) {
                    permissionNeeded.isGranted = true
                }
            }
        }
    }

    protected fun dispatchPermissions() {
        if (permissionsNeededModule?.permissionNeeded == null) {
            permissionCallback?.onGranted(permissionsNeededModule)
            return
        }
        val permissionsNeeded = permissionsNeededModule!!.permissionNeeded
            .filter { !it.isGranted }
            .map { it.permission }

        if (permissionsNeeded.isEmpty()) {
            permissionCallback?.onGranted(permissionsNeededModule)
        }
        requestPermissions(*permissionsNeeded.toTypedArray())
    }

    private fun requestPermissions(vararg permissions: String) {
        PermissionHelper.requestPermission(
            this,
            *permissions,
            requestCode = REQUEST_CODE_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsNeededModule ?: return
        permissions.forEachIndexed { index, s ->
            permissionsNeededModule!!.permissionNeeded.find {
                it.permission == s
            }?.apply {
                isGranted = isPermissionGranted(grantResults[index])
                shouldShowRationale = isPermissionShouldShowRationale(permission)
            }
        }
        updatePermissionStatus(permissionsNeededModule)
        val permissionsNeededDenied =
            permissionsNeededModule!!.permissionNeeded.filter { !it.isGranted }
        if (permissionsNeededDenied.isNotEmpty()) {
            if (permissionsNeededDenied.find { !it.shouldShowRationale } != null) {
                onDeniedCannotShowPermissionRationale(permissionsNeededModule!!)
            } else {
                onDenied(permissionsNeededModule!!)
            }
        } else {
            onGranted(permissionsNeededModule)
        }
    }

    @CallSuper
    protected open fun onGranted(permissionsNeededModule: PermissionsNeededModule?) {
        permissionCallback?.onGranted(permissionsNeededModule)
    }

    @CallSuper
    protected open fun onDeniedCannotShowPermissionRationale(permissionsNeededModule: PermissionsNeededModule?) {
        permissionCallback?.onDeniedCannotShowPermissionRationale(permissionsNeededModule!!)
    }

    @CallSuper
    protected open fun onDenied(permissionsNeededModule: PermissionsNeededModule?) {
        permissionCallback?.onDenied(permissionsNeededModule!!)
    }

    private fun isPermissionGranted(grantResult: Int): Boolean {
        return grantResult == PackageManager.PERMISSION_GRANTED
    }

    private fun isPermissionShouldShowRationale(permission: String): Boolean {
        return this.shouldShowRequestPermissionRationale(permission)
    }
}