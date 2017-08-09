package com.hunght.numberlink;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.hunght.data.DataProcess;
import com.hunght.data.GameItem;
import com.hunght.data.LevelItem;
import com.hunght.data.SavedValues;
import com.hunght.data.StaticData;
import com.hunght.inappbillingutils.IabHelper;
import com.hunght.inappbillingutils.IabResult;
import com.hunght.inappbillingutils.Inventory;
import com.hunght.inappbillingutils.Purchase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "numberlink.inappbilling";

    GridView grvLevelItems;
    SavedValues savedValues;

    GridviewLevelItemAdapter gridviewLevelItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* In-app purchase */
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        mHelper = new IabHelper(this, StaticData.getLicenseKey());

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result)
                                       {
                                           if (!result.isSuccess()) {
                                               Log.d(TAG, "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d(TAG, "In-app Billing is set up OK");
                                               mHelper.queryInventoryAsync(mReceivedInventoryListener);
                                           }
                                       }
                                   });
        /* In-app purchase end*/

        savedValues= new SavedValues(this);
        grvLevelItems = (GridView) findViewById(R.id.grvLevelItems);

        ArrayList<LevelItem> levelItems = DataProcess.getLevelItems(this);

        if(StaticData.isStart()) {
            String currentGameId = savedValues.getCurrentGameId();
            if (currentGameId != "") {
                for (LevelItem levelItem : levelItems) {
                    for (GameItem gameItem : levelItem.getGameItems()) {
                        if (gameItem.getId().equalsIgnoreCase(currentGameId)) {
                            StaticData.setCurrentLevel(levelItem);
                            StaticData.setCurrentGame(gameItem);
                            Intent intent = new Intent(this, GameActivity.class);
                            this.startActivity(intent);
                            finish();
                            return;
                        }
                    }
                }
            }
        }
        gridviewLevelItemAdapter = new GridviewLevelItemAdapter(this, levelItems);
        grvLevelItems.setAdapter(gridviewLevelItemAdapter);
    }

    protected void onResume() {
        super.onResume();
        if(gridviewLevelItemAdapter != null) gridviewLevelItemAdapter.notifyDataSetChanged();
        System.gc();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    View vLockLevel = null;
    public void showDialog(View view, String msg){
        final Dialog dialog = new Dialog(MainActivity.this);
        vLockLevel = view;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lock_level_dialog);
        ColorDrawable dialogColor = new ColorDrawable(Color.GRAY);
        dialogColor.setAlpha(0);
        dialog.getWindow().setBackgroundDrawable(dialogColor);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView text = (TextView) dialog.findViewById(R.id.tvLockLevelMessage);
        text.setText(msg);

        Button btBuyOneLevel = (Button) dialog.findViewById(R.id.btBuyOneLevel);
        btBuyOneLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.launchPurchaseFlow(MainActivity.this, ITEM_SKU, 10001,
                        mPurchaseFinishedListener, "mypurchasetoken");
            }
        });

        Button btClose = (Button) dialog.findViewById(R.id.btClose);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();

            }

        }
    };

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        if(vLockLevel != null){
                            vLockLevel.setVisibility(View.GONE);
                        }
                    } else {
                        // handle error
                    }
                }
            };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }
    IInAppBillingService mService;
    IabHelper mHelper;
    static final String ITEM_SKU = "android.test.purchased";

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };
}
