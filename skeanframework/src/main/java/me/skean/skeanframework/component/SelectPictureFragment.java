package me.skean.skeanframework.component;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;

import com.hjq.permissions.XXPermissions;
import com.hjq.permissions.permission.PermissionLists;
import com.hjq.permissions.permission.base.IPermission;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.skean.skeanframework.BuildConfig;
import me.skean.skeanframework.R;
import me.skean.skeanframework.utils.Glide4Engine;
import skean.yzsm.com.easypermissiondialog.EasyPermissionDialog;

import static android.app.Activity.RESULT_OK;

/**
 * 选择图片基础Activity
 */
public class SelectPictureFragment extends BaseFragment {

    private static final int REQUEST_CHOOSE_PICTURE = 99;

    protected List<String> selectedPicturePaths;
    protected List<Uri> selectedPictureUris;
    private int maxSelectCount = 1;
    private boolean rememberSelectedPictures = false;

    ///////////////////////////////////////////////////////////////////////////
    // 1
    ///////////////////////////////////////////////////////////////////////////


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_PICTURE) {
            if (resultCode == RESULT_OK) {
                selectedPictureUris = Matisse.obtainResult(data);
                selectedPicturePaths = Matisse.obtainPathResult(data);
                onSelectPictureResult(selectedPicturePaths);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 3
    ///////////////////////////////////////////////////////////////////////////

    protected final void startSelectPictureWithPermissionCheck() {
        List<IPermission> permissions = List.of(PermissionLists.getWriteExternalStoragePermission(),
                                                PermissionLists.getReadExternalStoragePermission(),
                                                PermissionLists.getCameraPermission());
        XXPermissions.with(this).permissions(permissions).request((grantedList, deniedList) -> {
            if (deniedList.isEmpty()) startSelectPicture();
            else {
                EasyPermissionDialog.build(this)
                                    .permissions(deniedList)
                                    .show(XXPermissions.isDoNotAskAgainPermissions(requireActivity(), deniedList), allow -> {
                                        if (allow) startSelectPictureWithPermissionCheck();
                                    });
            }
        });
    }

    public void onSelectPictureResult(List<String> pathList) {

    }

    protected void setMaxSelectCount(int maxSelectCount) {
        this.maxSelectCount = maxSelectCount;
    }

    protected void setRememberSelectedPictures(boolean rememberSelectedPictures) {
        this.rememberSelectedPictures = rememberSelectedPictures;
    }

    protected void clearSelectedPictures(String path) {
        if (CollectionUtils.isNotEmpty(selectedPicturePaths)) {
            int i = selectedPicturePaths.indexOf(path);
            if (i != -1) {
                selectedPictureUris.remove(i);
                selectedPicturePaths.remove(i);
            }
        }
    }

    public final void startSelectPicture() {
        Matisse.from(this)
               .choose(EnumSet.of(MimeType.JPEG, MimeType.PNG), false)
               .theme(R.style.Matisse_APP)
               .countable(true)
               .capture(true, true)
               .selectedUri(rememberSelectedPictures ? selectedPictureUris : new ArrayList<>())
               .captureStrategy(new CaptureStrategy(true, requireContext().getPackageName() + ".fileprovider", "test"))
               .maxSelectable(maxSelectCount)
               .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
               .thumbnailScale(0.85f)
               .imageEngine(new Glide4Engine())    // for glide-V4
               .originalEnable(false)
               .autoHideToolbarOnSingleTap(true)
               .forResult(REQUEST_CHOOSE_PICTURE);
    }


}
