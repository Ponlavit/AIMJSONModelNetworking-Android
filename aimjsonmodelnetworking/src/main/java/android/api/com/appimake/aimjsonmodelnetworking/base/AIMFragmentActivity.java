package android.api.com.appimake.aimjsonmodelnetworking.base;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * Created by Nattapongr on 5/4/16 AD.
 */
public class AIMFragmentActivity extends FragmentActivity {
    private ProgressDialog dialogLoading;

    /**
     * AIMUser for show loading dialog
     */
    public void loadingDialog(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialogLoading == null) {
                    dialogLoading = new ProgressDialog(getThis());
                    dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialogLoading.setCanceledOnTouchOutside(false);
                    dialogLoading.setIndeterminate(true);
                    dialogLoading.setCancelable(false);
                }
                dialogLoading.setMessage(msg);
                dialogLoading.show();


            }
        });
    }

    public void changeProgressDialogMSG(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogLoading.setMessage(msg);
            }
        });
    }

    public void hideLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialogLoading != null && dialogLoading.isShowing()) {
                    dialogLoading.dismiss();
                }
            }
        });
    }

    public void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public AIMFragmentActivity getThis() {
        return this;
    }
}
