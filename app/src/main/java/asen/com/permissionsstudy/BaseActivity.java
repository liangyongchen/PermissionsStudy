package asen.com.permissionsstudy;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import asen.com.permissionsstudy.permission.OnPermission;
import asen.com.permissionsstudy.permission.PermissionApply;


/**
 * Created by asus on 2017/12/15.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // region  // android 授权的方法

    private OnPermission onPermission;

    public void setOnPermission(OnPermission mOnPermission) {
        onPermission = mOnPermission;
    }


    private int mRequestCode;

    /**
     * 请求权限
     *
     * @param permissions 需要的权限列表
     * @param requestCode 请求码
     */
    protected void requestPermission(String[] permissions, int requestCode) {
        mRequestCode = requestCode;
        if (checkPermissions(permissions)) {
            permissionSuccess(requestCode);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), requestCode);
        }
    }

    /**
     * 检查所需的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions) {
        // 手机版本 SDK 低于23 ，在Manifest 上注册有效，大于 23 的（android6.0以后的），读取手机的隐私需要在代码动态申请
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取所需权限列表中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            // 检查权限,如果没有授权就添加
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 权限请求成功
     *
     * @param requestCode
     */
    private void permissionSuccess(int requestCode) {
        Log.d("permissionSuccess====== ", "获取权限成功：" + requestCode);
        if (onPermission != null) {
            onPermission.permissionSuccess(requestCode);
        }
    }

    /**
     * 权限请求失败
     *
     * @param requestCode
     */
    private void permissionFail(int requestCode) {
        Log.d("permissionFail====== ", "获取权限失败：" + requestCode);
        if (onPermission != null) {
            onPermission.permissionFail(requestCode);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //系统请求权限回调
        if (requestCode == mRequestCode) {
            if (PermissionApply.verifyPermissions(grantResults)) {
                permissionSuccess(mRequestCode);
            } else {
                permissionFail(mRequestCode);
            }
        }
    }

    // endregion

}
