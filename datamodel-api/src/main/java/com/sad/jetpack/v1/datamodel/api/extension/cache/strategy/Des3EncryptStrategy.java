package com.sad.jetpack.v1.datamodel.api.extension.cache.strategy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;


import com.sad.jetpack.v1.datamodel.api.extension.cache.util.Des3Util;
import com.sad.jetpack.v1.datamodel.api.extension.cache.util.KeyStoreHelper;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Des3加密策略
 *
 */
public class Des3EncryptStrategy implements IEncryptStrategy {

    private String secretKey;//秘钥
    private String iv = "haohaoha";//移动位置8位
    private Context mContext;

    public Des3EncryptStrategy(Context context, String secretKey, String iv) {
        this.mContext = context;
        this.secretKey = secretKey;
        this.iv = iv;
    }

    @Override
    public String encrypt(String str) {
        try {
            return Des3Util.encode(str, this.secretKey, this.iv);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String decode(String str) {
        try {
            return Des3Util.decode(str, this.secretKey, this.iv);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String createSecretKey() {
        String secretKey;
        String str = getAndroidID();
        if (str.length() > 24) {
            secretKey = str.substring(0, 24);
        } else {
            secretKey = str + getStr(24 - str.length());
        }

        return secretKey;
    }

    private String get24Str(String str) {
        if (TextUtils.isEmpty(str)) return "";
        if (str.length() >= 24) {
            return str.substring(0, 24);
        }
        return str + getStr(24 - str.length());
    }

    private String getStr(int num) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            builder.append("a");
        }
        return builder.toString();
    }

    @SuppressLint("HardwareIds")
    public String getAndroidID() {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void createKeyStoreSecretKey() {
        try {
            KeyStoreHelper.createKeys(mContext, mContext.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
