package me.skean.skeanframework.component;

import com.hjq.permissions.XXPermissions;
import com.hjq.permissions.permission.PermissionLists;
import com.hjq.permissions.permission.base.IPermission;

import java.util.List;

import skean.yzsm.com.easypermissiondialog.EasyPermissionDialog;

/**
 * 使用OCR功能的基础Activity
 */
public class UseOcrActivity extends BaseActivity {

    private final List<IPermission> permissions = List.of(PermissionLists.getWriteExternalStoragePermission(),
                                                          PermissionLists.getReadExternalStoragePermission(),
                                                          PermissionLists.getCameraPermission(),
                                                          PermissionLists.getReadPhoneStatePermission());

    protected final void userOcrWithPermissionCheck() {
        XXPermissions.with(this).permissions(permissions).request((grantedList, deniedList) -> {
            if (deniedList.isEmpty()) onUseOcr();
            else {
                EasyPermissionDialog.build(this)
                                    .permissions(deniedList)
                                    .show(XXPermissions.isDoNotAskAgainPermissions(this, deniedList), allow -> {
                                        if (allow) userOcrWithPermissionCheck();
                                    });
            }
        });
    }

    protected final boolean hasOcrPermission() {
        return XXPermissions.isGrantedPermissions(this, permissions);
    }

    public void onUseOcr() {
    }

}
