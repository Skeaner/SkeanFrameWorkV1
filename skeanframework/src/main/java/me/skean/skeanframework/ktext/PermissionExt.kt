@file:JvmName("PermissionExt")

package me.skean.skeanframework.ktext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.permission.base.IPermission
import me.skean.skeanframework.rx.DefaultObserver
import skean.yzsm.com.easypermissiondialog.EasyPermissionDialog

/**
 * Created by Skean on 22/2/27.
 */

inline fun FragmentActivity.reqPermissions(vararg permissions: IPermission, crossinline onGranted: () -> Unit) {
    requestPermissions(this, permissions.toList()) {
        onGranted.invoke()
    }
}

fun requestPermissions(activity: FragmentActivity, permissions: List<IPermission>, onGranted: () -> Unit) {
    XXPermissions.with(activity)
        .permissions(permissions)
        .request { grantedList, deniedList ->
            if (deniedList.isEmpty()) {
                onGranted.invoke()
            } else {
                val isDoNotAskAgain = XXPermissions.isDoNotAskAgainPermissions(activity, deniedList)
                EasyPermissionDialog.build(activity).permissions(permissions)
                    .show(isDoNotAskAgain) { allow ->
                        if (isDoNotAskAgain) onGranted.invoke()
                        else {
                            requestPermissions(activity, permissions, onGranted = onGranted)
                        }
                    }
            }
        }
}


inline fun Fragment.reqPermissions(vararg permissions: IPermission, crossinline onGranted: () -> Unit) {
    requestPermissions(this, permissions.toList()) {
        onGranted.invoke()
    }
}

fun requestPermissions(fragment: Fragment, permissions: List<IPermission>, onGranted: () -> Unit) {
    XXPermissions.with(fragment)
        .permissions(permissions)
        .request { grantedList, deniedList ->
            if (deniedList.isEmpty()) {
                onGranted.invoke()
            } else {
                val isDoNotAskAgain = XXPermissions.isDoNotAskAgainPermissions(fragment.requireActivity(), deniedList)
                EasyPermissionDialog.build(fragment).permissions(permissions)
                    .show(isDoNotAskAgain) { allow ->
                        if (isDoNotAskAgain) onGranted.invoke()
                        else {
                            requestPermissions(fragment, permissions, onGranted = onGranted)
                        }
                    }
            }
        }
}
