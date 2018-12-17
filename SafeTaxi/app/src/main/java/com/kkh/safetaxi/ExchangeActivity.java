
package com.kkh.safetaxi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kkh.safetaxi.data.DataPref;
import com.kkh.safetaxi.data.DatabaseManager;
import com.kkh.safetaxi.data.ExchangeData;
import com.kkh.safetaxi.worker.ExchangeWorker;

import java.util.ArrayList;

import static com.kkh.safetaxi.common.Constant.EXCHANGE_LIST;

public class ExchangeActivity extends AppCompatActivity {

    private static final String TAG = "[KKH]ExchangeActivity";
    private Context mContext;
    private Button mMoneyTitle;
    private TextView mMoneyUnit;
    private TextView mMoneyEdit;
    private TextView mUsdEdit;
    private EditText mWonEdit;

    private ArrayList<ExchangeData> mList = new ArrayList<>();
    private ExchangeData mExchangeData;
    private ExchangeData mExchangeDataForUSD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        Log.d(TAG,"onCreate");
        mContext = this;
        mExchangeData = DatabaseManager.getInstance(mContext).getExchangeData();
        ExchangeWorker worker = new ExchangeWorker(mContext);
        worker.setHandler(mHandler);
        worker.execute();
    }

    private void initViews() {
        Log.d(TAG, "initViews");
        mList = DataPref.sExchangeDataList;
        for (int i = 0; i < mList.size(); i++) {
            ExchangeData data = mList.get(i);
            if ("USD".equalsIgnoreCase(data.getmCurUnit())) {
                mExchangeDataForUSD = data;
            }
        }

        mMoneyTitle = findViewById(R.id.money_title);
        mMoneyUnit = findViewById(R.id.money_unit);
        mMoneyEdit = findViewById(R.id.money_edit);
        mUsdEdit = findViewById(R.id.usd_edit);
        mWonEdit = findViewById(R.id.won_edit);

        mMoneyTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListDialog();
            }
        });

        setAddChangeListener();

        if(mExchangeData!=null){
            setExchangeData(mExchangeData);
        }

    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e(TAG, "afterTextChanged");
            try {
                if (s == null || s.length() <= 0 || "".equalsIgnoreCase(s.toString())) {
                    Log.e(TAG, "s return");
                    return;
                }
                int won = Integer.parseInt(s.toString());
                Log.e(TAG, "getUSD(won) : " + getUSD(won));
                // dolor
                mUsdEdit.setText(getUSD(won));
                if (mExchangeData != null) {
                    mMoneyEdit.setText(getMoney(mExchangeData, won));

                }
            } catch (NumberFormatException e) {
                return;
            }
        }
    };

    private void setAddChangeListener() {
        Log.d(TAG, "setAddChangeListener");
        mWonEdit.removeTextChangedListener(mTextWatcher);
        mWonEdit.addTextChangedListener(mTextWatcher);
    }

    private void showListDialog() {
        if (mList != null && mList.size() > 0) {
            final String items[] = new String[mList.size() - 1];
            final int index[] = {0};
            for (int i = 0; i < mList.size(); i++) {
                ExchangeData data = mList.get(i);
                if ("USD".equalsIgnoreCase(data.getmCurUnit())) {
                    continue;
                }
                items[i] = data.getmCurUnit() + "(" + EXCHANGE_LIST.get(data.getmCurUnit()) + ")";
            }
            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
            ab.setTitle(mContext.getString(R.string.exchange_list_dialog_title));
            ab.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    index[0] = i;
                }
            }).setPositiveButton(mContext.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setExchangeData(mList.get(index[0]));

                }
            });
            ab.show();

        }

    }

    private void setExchangeData(final ExchangeData data) {
        Log.d(TAG, "setExchangeData");
        try {
            mExchangeData = data;
            DatabaseManager.getInstance(mContext).insertExchangeData(mExchangeData);
            mMoneyTitle.setText(data.getmCurUnit() + "(" + EXCHANGE_LIST.get(data.getmCurUnit()) + ")");
            mMoneyUnit.setText(data.getmCurUnit());
            mMoneyEdit.setText(getMoney(mExchangeData, Integer.parseInt(mWonEdit.getText().toString())));
            setAddChangeListener();
        } catch (NumberFormatException e) {

        }
    }

    private String getMoney(ExchangeData data, int won) {
        float money = won / Float.parseFloat(data.getmBKPR().replace(",", ""));
        return String.format("%.2f", money);
    }

    private String getUSD(int won) {
        if (mExchangeDataForUSD == null) {
            Log.e(TAG, "mExchangeDataForUSD is null");
            return "";
        }
        float usd = won / Float.parseFloat(mExchangeDataForUSD.getmBKPR().replace(",", ""));
        return String.format("%.2f", usd);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initViews();
        }
    };

}
