package com.dagu.lightchaser.service.impl;

import com.dagu.lightchaser.service.BaseService;
import com.dagu.lightchaser.service.DataProviderService;
import lightchaser.core.base.consts.Const;
import lightchaser.core.data.provider.DataProviderInfo;
import lightchaser.core.data.provider.DataProviderManager;
import lightchaser.core.data.provider.DataProviderSource;
import lightchaser.security.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class DataProviderServiceImpl extends BaseService implements DataProviderService {


    private final DataProviderManager dataProviderManager;


    public DataProviderServiceImpl(DataProviderManager dataProviderManager) {
        this.dataProviderManager = dataProviderManager;
    }

    @Override
    public List<DataProviderInfo> getSupportedDataProviders() {
        return dataProviderManager.getSupportedDataProviders();
    }

    @Override
    public Object testConnection(DataProviderSource source) throws Exception {
        Map<String, Object> properties = source.getProperties();
        if (!CollectionUtils.isEmpty(properties)) {
            for (String key : properties.keySet()) {
                Object val = properties.get(key);
                if (val instanceof String) {
                    properties.put(key, decryptValue(val.toString()));
                }
            }
        }
        return dataProviderManager.testConnection(source);
    }

    @Override
    public String decryptValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        if (!value.startsWith(Const.ENCRYPT_FLAG)) {
            return value;
        }
        try {
            return AESUtil.decrypt(value.replaceFirst(Const.ENCRYPT_FLAG, ""));
        } catch (Exception e) {
            return value;
        }
    }


}
