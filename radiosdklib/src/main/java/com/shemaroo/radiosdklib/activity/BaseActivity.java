package com.shemaroo.radiosdklib.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    /**
     * The Constant ADD_FRAGMENT.
     */
    public static final int ADD_FRAGMENT = 0;

    /**
     * The Constant REPLACE_FRAGMENT.
     */
    public static final int REPLACE_FRAGMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContent();
        initializeComponents();
        setListeners();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Set Content View.
     */
    protected abstract void setContent();

    /**
     * Initialize components.
     */
    protected abstract void initializeComponents();

    /**
     * Sets the listeners.
     */
    protected abstract void setListeners();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Short toast.
     *
     * @param msg the msg
     */
    public void showShortToast(String msg) {
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Long toast.
     *
     * @param msg the msg
     */
    public void showLongToast(String msg) {
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Fragment transaction which can be accessed from everywhere in project.
     *
     * @param container        the container
     * @param transactionType  the transaction type
     * @param fragment         the fragment
     * @param isAddToBackStack the is add to back stack
     * @param tag              the tag
     */
    public void fragmentTransaction(int container, int transactionType,
                                    Fragment fragment, boolean isAddToBackStack, String tag) {

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

        switch (transactionType) {
            case (ADD_FRAGMENT):
                trans.add(container, fragment, tag);
                trans.addToBackStack(tag);
                break;
            case (REPLACE_FRAGMENT):
                trans.replace(container, fragment, tag);
               /* if (isAddToBackStack)
                    trans.addToBackStack(null);
                else*/  trans.addToBackStack(tag);

                break;

        }
        trans.commit();
    }
    public void firstFragmentTransaction(int container,
                                    Fragment fragment, String tag) {

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

                trans.add(container, fragment, tag);
//                trans.addToBackStack(tag);
        trans.commit();
    }


}
