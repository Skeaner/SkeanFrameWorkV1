package me.skean.skeanframework.component;

import android.Manifest;
import android.content.Intent;

import com.hjq.permissions.XXPermissions;
import com.hjq.permissions.permission.PermissionLists;
import com.hjq.permissions.permission.base.IPermission;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import skean.yzsm.com.easypermissiondialog.EasyPermissionDialog;

/**
 * 使用相机和读取储存功能的基础Activity
 */
public class UseCameraAndExternalStorageActivity extends BaseActivity {
    private final List<IPermission> permissions = List.of(PermissionLists.getWriteExternalStoragePermission(),
                                                          PermissionLists.getReadExternalStoragePermission(),
                                                          PermissionLists.getCameraPermission());

    ///////////////////////////////////////////////////////////////////////////
    // 2
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // 3
    ///////////////////////////////////////////////////////////////////////////

    protected final void userCameraAndExternalStorageWithPermissionCheck() {
        XXPermissions.with(this).permissions(permissions).request((grantedList, deniedList) -> {
            if (deniedList.isEmpty()) onUseCameraAndExternalStorage();
            else {
                EasyPermissionDialog.build(this)
                                    .permissions(deniedList)
                                    .show(XXPermissions.isDoNotAskAgainPermissions(this, deniedList), allow -> {
                                        if (allow) userCameraAndExternalStorageWithPermissionCheck();
                                    });
            }
        });
    }

    protected final boolean hasCameraPermission() {
        return XXPermissions.isGrantedPermissions(this, permissions);
    }

    public void onUseCameraAndExternalStorage() {
    }

}
