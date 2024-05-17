package lightchaser.core.data.provider;

import java.util.List;

public interface DataProviderManager {


    List<DataProviderInfo> getSupportedDataProviders();

    Object testConnection(DataProviderSource source) throws Exception;


}
