package com.instagram.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private final Integer OTP_EXPIRATION_TIME = 2;

    private LoadingCache<String, Integer> otpCache;

    public OtpService(){
        super();
        otpCache = CacheBuilder.newBuilder().expireAfterWrite(OTP_EXPIRATION_TIME, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(String key) throws Exception {
                return 0;
            }
        });
    }

    public int generateOtp(String key){

        int otp = (int)(10000 + (Math.random() * 90000));
        otpCache.put(key, otp);
        return otp;
    }

    public int getOtp(String key) throws ExecutionException {
        return otpCache.get(key);
    }

    public void clearOtp(String key) throws ExecutionException {
        otpCache.invalidate(key);
    }

}
