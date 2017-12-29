package asen.com.permissionsstudy;

import android.os.Bundle;

import asen.com.permissionsstudy.permission.OnPermission;
import asen.com.permissionsstudy.permission.PermissionApply;

public class MainActivity extends BaseActivity implements OnPermission {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 授权
        setOnPermission(this);
        requestPermission(PermissionApply.PERMISSION, PermissionApply.PERMISSION_KEY);
    }

    @Override
    public void permissionFail(int requestCode) {
    }

    @Override
    public void permissionSuccess(int requestCode) {
    }

}
