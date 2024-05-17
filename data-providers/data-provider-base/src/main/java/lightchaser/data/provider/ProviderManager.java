package lightchaser.data.provider;


import lightchaser.core.base.exception.Exceptions;
import lightchaser.core.base.processor.ExtendProcessor;
import lightchaser.core.data.provider.DataProvider;
import lightchaser.core.data.provider.DataProviderInfo;
import lightchaser.core.data.provider.DataProviderManager;
import lightchaser.core.data.provider.DataProviderSource;
import lightchaser.data.provider.optimize.DataProviderExecuteOptimizer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ProviderManager extends DataProviderExecuteOptimizer implements DataProviderManager {


    @Autowired(required = false)
    private List<ExtendProcessor> extendProcessors = new ArrayList<ExtendProcessor>();
    private static final Map<String, DataProvider> cachedDataProviders = new ConcurrentHashMap<>();

    public Map<String, DataProvider> getDataProviders() {
        if (cachedDataProviders.isEmpty()) {
            synchronized (ProviderManager.class) {
                if (cachedDataProviders.isEmpty()) {
                    ServiceLoader<DataProvider> load = ServiceLoader.load(DataProvider.class);
                    for (DataProvider dataProvider : load) {
                        try {
                            cachedDataProviders.put(dataProvider.getType(), dataProvider);
                        } catch (IOException e) {
                            log.error("", e);
                        }
                    }
                }
            }
        }
        return cachedDataProviders;
    }

    @Override
    public List<DataProviderInfo> getSupportedDataProviders() {
        ArrayList<DataProviderInfo> providerInfos = new ArrayList<>();
        for (DataProvider dataProvider : getDataProviders().values()) {
            try {
                providerInfos.add(dataProvider.getBaseInfo());
            } catch (IOException e) {
                log.error("DataProvider init error {" + dataProvider.getClass().getName() + "}", e);
            }
        }
        return providerInfos;
    }

    @Override
    public Object testConnection(DataProviderSource source) throws Exception {
        return getDataProviderService(source.getType()).test(source);
    }

    private DataProvider getDataProviderService(String type){
        DataProvider dataProvider = getDataProviders().get(type);
        if (dataProvider == null) {
            Exceptions.msg("No data provider type " + type);
        }
        return dataProvider;
    }
}
