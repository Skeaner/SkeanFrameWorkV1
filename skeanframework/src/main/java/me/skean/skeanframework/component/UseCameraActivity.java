package me.skean.skeanframework.component;

import android.Manifest;
import android.content.Intent;

import com.hjq.permissions.XXPermissions;
import com.hjq.permissions.permission.PermissionLists;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import skean.yzsm.com.easypermissiondialog.EasyPermissionDialog;

/**
 * 使用相机的基础
 */
public class UseCameraActivity extends BaseActivity {



    protected final void startCameraWithPermissionCheck() {
        XXPermissions.with(this).permission(PermissionLists.getCameraPermission()).request((grantedList, deniedList) -> {
            if (deniedList.isEmpty()) startCamera();
            else {
                EasyPermissionDialog.build(this)
                                    .permissions(deniedList)
                                    .show(XXPermissions.isDoNotAskAgainPermissions(this, deniedList), allow -> {
                                        if (allow) startCameraWithPermissionCheck();
                                    });
            }
        });
    }

    protected final boolean hasCameraPermission() {
        return XXPermissions.isGrantedPermission(this, PermissionLists.getCameraPermission());
    }

    public  void startCamera() {
    }


}
