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
public class UseCameraFragment extends BaseFragment {



    protected final void startCameraWithPermissionCheck() {
        XXPermissions.with(this).permission(PermissionLists.getCameraPermission()).request((grantedList, deniedList) -> {
            if (deniedList.isEmpty()) startCamera();
            else {
                EasyPermissionDialog.build(this)
                                    .permissions(deniedList)
                                    .show(XXPermissions.isDoNotAskAgainPermissions(requireActivity(), deniedList), allow -> {
                                        if (allow) startCameraWithPermissionCheck();
                                    });
            }
        });
    }

    protected final boolean hasCameraPermission() {
        return XXPermissions.isGrantedPermission(requireContext(), PermissionLists.getCameraPermission());

    }

    public  void startCamera() {
    }


}
