package com.dagu.lightchaser.service;

import lightchaser.core.data.provider.DataProviderInfo;
import lightchaser.core.data.provider.DataProviderSource;

import java.util.List;

public interface DataProviderService {


    List<DataProviderInfo> getSupportedDataProviders();


    Object testConnection(DataProviderSource source) throws Exception;

    String decryptValue(String value);

}
