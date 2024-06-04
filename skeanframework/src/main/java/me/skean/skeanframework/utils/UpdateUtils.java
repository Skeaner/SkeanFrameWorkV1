package me.skean.skeanframework.utils;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.MetaDataUtils;
import com.blankj.utilcode.util.ToastUtils;

import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.skean.skeanframework.component.UpdateDialog;
import me.skean.skeanframework.net.FileIOApi;
import me.skean.skeanframework.net.pgy.PgyerApi;
import me.skean.skeanframework.net.pgy.PgyerAppInfo;
import me.skean.skeanframework.net.pgy.PgyerResult;
import me.skean.skeanframework.rx.DefaultSingleObserver;

/**
 * Created by Skean on 2023/2/28.
 */
public class UpdateUtils {

    public static void checkUpdateByPgyerApi(Context context) {
        String appId = AppUtils.getAppPackageName();
        String apiKey = MetaDataUtils.getMetaDataInApp("PGYER_API_KEY");
        String appKey = MetaDataUtils.getMetaDataInApp("PGYER_APP_KEY");
        String appVersionName = AppUtils.getAppVersionName();
        int appVersionCode = AppUtils.getAppVersionCode();
        NetworkUtil.createService(PgyerApi.class)
                   .getAppInfo(appKey, apiKey)
                   .subscribeOn(Schedulers.io())
                   .map(r -> {
                       if (r.getCode() == 0) {
                           if (r.getData() == null) {
                               throw new RuntimeException("PGYER返回APP信息为空");
                           }
                           else if (!appId.equals(r.getData().getBuildIdentifier())) {
                               throw new RuntimeException("PGYER配置的信息跟APP不一致!");
                           }
                       }
                       else {
                           throw new RuntimeException(r.getMessage());
                       }
                       return r;
                   })
                   .flatMap(r -> NetworkUtil.createService(PgyerApi.class).checkUpdate(appKey, apiKey, appVersionName, appVersionCode))
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(new DefaultSingleObserver<PgyerResult<PgyerAppInfo>>() {
                       @Override
                       public void onSuccess2(PgyerResult<PgyerAppInfo> result) {
                           if (result.getCode() == 0) {
                               if (result.getData() != null) {
                                   PgyerAppInfo info = result.getData();
                                   if (appVersionName.compareTo(info.getBuildVersion()) < 0 || appVersionCode < Integer.parseInt(info.getBuildVersionNo())) {
                                       UpdateDialog.show(context,
                                                         info.getBuildVersion(),
                                                         info.getBuildUpdateDescription(),
                                                         info.getDownloadURL(),
                                                         info.getNeedForceUpdate());
                                   }
                               }
                           }
                           else {
                               ToastUtils.showShort("检查APP更新失败: " + result.getMessage());
                           }
                       }

                       @Override
                       public void onError2(Throwable e) {
                           super.onError2(e);
                           ToastUtils.showShort("检查APP更新失败: " + e.getLocalizedMessage());

                       }
                   });
    }
}
