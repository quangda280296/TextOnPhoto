package com.vmb.chinh_sua_anh.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.quangda280296.photoeditor.R;
import com.vmb.ads_in_app.GetConfig;
import com.vmb.ads_in_app.Interface.IUpdateNewVersion;
import com.vmb.ads_in_app.LibrayData;
import com.vmb.ads_in_app.handler.AdsHandler;
import com.vmb.ads_in_app.model.AdsConfig;
import com.vmb.ads_in_app.util.CountryCodeUtil;
import com.vmb.ads_in_app.util.OnTouchClickListener;
import com.vmb.ads_in_app.util.PermissionUtil;
import com.vmb.ads_in_app.util.PrintKeyHash;
import com.vmb.ads_in_app.util.SharedPreferencesUtil;
import com.vmb.ads_in_app.util.ToastUtil;
import com.vmb.ads_in_app.util.TokenNotiUtil;
import com.vmb.chinh_sua_anh.Config;
import com.vmb.chinh_sua_anh.base.BaseActivity;
import com.vmb.chinh_sua_anh.handler.Data;
import com.vmb.chinh_sua_anh.service.MyFirebaseInstanceIDService;
import com.vmb.ads_in_app.util.RefreshToken;

public class MenuActivity extends BaseActivity implements IUpdateNewVersion {

    private View root;
    private CallbackManager callbackManager;
    //private View layout_dialog;

    //private ImageView img_close;
    private ImageView img_text_on_photo;
    private ImageView img_gallery;
    private ImageView img_more_apps;
    private ImageView img_setup;

    /*private TextView lbl_title;
    private TextView lbl_content;
    private Button btn_ok;*/

    //private boolean require_update = false;
    private int count = 0;

    @Override
    protected int getResLayout() {
        return R.layout.activity_menu;
    }

    @Override
    protected void initView() {
        GetConfig.callAPI(getApplicationContext(), LibrayData.AdsSize.NATIVE_ADS,
                Config.CODE_CONTROL_APP, Config.VERSION_APP, Config.PACKAGE_NAME);

        root = findViewById(R.id.root);
        /*layout_dialog = findViewById(R.id.layout_dialog);

        lbl_title = findViewById(R.id.lbl_title);
        lbl_content = findViewById(R.id.lbl_content);

        btn_ok = findViewById(R.id.btn_ok);

        img_close = findViewById(R.id.img_close);*/

        img_text_on_photo = findViewById(R.id.img_text_on_photo);
        img_gallery = findViewById(R.id.img_gallery);
        img_more_apps = findViewById(R.id.img_more_apps);
        img_setup = findViewById(R.id.img_setup);
    }

    @Override
    protected void initData() {
        check();

        img_text_on_photo.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        img_gallery.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        img_more_apps.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        img_setup.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PermissionUtil.requestPermission(MenuActivity.this, Config.RequestCode.REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Data.getInstance().scanPhoto(getApplicationContext());
                        }
                    }).start();
                }
            }
        }, 1000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CountryCodeUtil.setCountryCode(getApplicationContext(),
                        Config.CODE_CONTROL_APP, Config.VERSION_APP, Config.PACKAGE_NAME);
                Data.getInstance().setSendTime(SharedPreferencesUtil.getPrefferBool(getApplicationContext(),
                        Config.PREFERENCE_KEY_SEND_TIME, false));
                callbackManager = CallbackManager.Factory.create();
                PrintKeyHash.print(getApplicationContext());

                RefreshToken.getInstance().checkSendToken(getApplicationContext(),
                        Config.CODE_CONTROL_APP, Config.VERSION_APP, Config.PACKAGE_NAME);
            }
        }).start();
    }

    public void check() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action))
                if (action.equals(LibrayData.ACTION_NOTI)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            root.setVisibility(View.VISIBLE);
                            GetConfig.init(MenuActivity.this, MenuActivity.this, (ViewGroup) findViewById(R.id.rectangle));
                        }
                    }, 3000);
                    return;
                }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GetConfig.init(MenuActivity.this, MenuActivity.this, (ViewGroup) findViewById(R.id.rectangle));
                root.setVisibility(View.VISIBLE);
            }
        }, 1500);
    }

    @Override
    protected void onActivity_Result(int requestCode, int resultCode, Intent data) {

    }

    @Override
    protected void onRequestPermissions_Result(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Config.RequestCode.REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE:
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Data.getInstance().setTypeSendTime(1);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Data.getInstance().scanPhoto(getApplicationContext());
                                }
                            }).start();
                        }
                    }, 500);
                } else {
                    ToastUtil.longToast(getApplicationContext(), getString(R.string.accept_to_get_images));
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        count = 0;
        switch (v.getId()) {
            case R.id.img_text_on_photo:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MenuActivity.this, ChooseImageActivity.class));
                    }
                }, 500);
                break;

            case R.id.img_gallery:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MenuActivity.this, SavedImageActivity.class));
                    }
                }, 200);
                break;

            case R.id.img_more_apps:
                String uri = AdsConfig.getInstance().getLink_more_apps();
                if (TextUtils.isEmpty(uri))
                    uri = "https://play.google.com/store/apps/developer?id=Fruits+Studio";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                startActivityForResult(intent, LibrayData.RequestCode.REQUEST_CODE_MORE_APP);
                break;

            case R.id.img_setup:
                startActivity(new Intent(MenuActivity.this, SettingActivity.class));

            /*case R.id.img_close:
                if (require_update)
                    return;
                layout_dialog.setVisibility(View.GONE);
                break;*/

            default:
                break;
        }
    }

    /*public void showUpdate() {
        String content = "";
        String title = "";

        content = AdsConfig.getInstance().getUpdate_message();
        if (TextUtils.isEmpty(content))
            content = "There is a new version, please update soon !";

        title = AdsConfig.getInstance().getUpdate_title();
        if (TextUtils.isEmpty(title))
            title = "Update";

        lbl_title.setText(title);
        lbl_content.setText(content);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = AdsConfig.getInstance().getUpdate_url();
                if (TextUtils.isEmpty(url))
                    url = "https://play.google.com/store/apps/details?id=" + getPackageName();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivityForResult(intent, LibrayData.RequestCode.REQUEST_CODE_UPDATE);
            }
        });

        img_close.setOnTouchListener(new OnTouchClickListener(this, getApplicationContext()));
        layout_dialog.setVisibility(View.VISIBLE);
    }*/

    @Override
    public void onBackPressed() {
        Log.i("onKeyBack", "onKeyBack()");
        count++;
        if (count >= 2)
            finish();
        else
            ToastUtil.shortToast(getApplicationContext(), "Press again to exit");
        /*if (findViewById(R.id.layout_dialog).getVisibility() == View.VISIBLE) {
            if (require_update)
                return;
            findViewById(R.id.layout_dialog).setVisibility(View.GONE);
            return;
        }

        AdsHandler.getInstance().showCofirmDialog(MenuActivity.this);*/
    }

    @Override
    public void onGetConfig(boolean require_update) {
        /*this.require_update = require_update;
        showUpdate();*/
    }
}