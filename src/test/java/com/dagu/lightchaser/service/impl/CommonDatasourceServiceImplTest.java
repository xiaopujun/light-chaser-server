package com.dagu.lightchaser.service.impl;

import com.dagu.lightchaser.config.CryptoConfig;
import com.dagu.lightchaser.model.constants.DataBaseEnum;
import com.dagu.lightchaser.model.dto.DatasourceAddRequest;
import com.dagu.lightchaser.model.po.CommonDatasourcePO;
import com.dagu.lightchaser.mapper.CommonDatasourceMapper;
import com.dagu.lightchaser.util.CryptoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * DatasourceServiceImpl 测试类
 * 测试加密解密流程
 *
 * @author zhenglin
 * @date 2025/07/27
 */
@ExtendWith(MockitoExtension.class)
public class CommonDatasourceServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(CommonDatasourceServiceImplTest.class);

    @Mock
    private CommonDatasourceMapper commonDatasourceMapper;

    @Mock
    private CryptoConfig cryptoConfig;

    @Mock
    private CryptoConfig.Rsa rsa;

    @Mock
    private CryptoConfig.Aes aes;

    @InjectMocks
    private CommonDatasourceServiceImpl datasourceService;

    private String privateKey;
    private String backendAesKey;
    private DatasourceAddRequest testRequest;

    @BeforeEach
    void setUp() throws Exception {
        // 读取实际的私钥文件
        java.io.InputStream privateKeyStream = getClass().getClassLoader().getResourceAsStream("keys/private_key.pem");
        assertNotNull(privateKeyStream, "私钥文件不存在");
        
        String privateKeyContent = new String(privateKeyStream.readAllBytes());
        // 去掉PEM头尾，只保留Base64内容（与CryptoConfig处理方式一致）
        privateKey = privateKeyContent
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s+", "");
        
        // 模拟后端AES密钥
        backendAesKey = "YmVVaTJUNGdvV3huQnZXZzlEUE9kdnBaaS9ZcTZ0UzZtWXl2TWVXTzQ0Qz0=";

        // 创建测试请求（使用前端提供的实际数据）
        testRequest = new DatasourceAddRequest();
        testRequest.setName("mysql-393");
        testRequest.setType(DataBaseEnum.MySQL);
        testRequest.setUsername("root");
        testRequest.setPassword("PymUt8DQQvqLH9vrDPHE/i4+6/PaFqf0P5D8ZoCVOGLqWVhLNgmj8y3HrjxegtJLhbaWaYbV7gr54ItaCevYv4Pv9YT6xExR3Kr6dWu+ABIOvMf9ByQxYKHxAAv5s6V31siVfC4LVUoJx6bMwiryCpq1G6zxHb7UmkLWDGzDX3zTuPCfbqM/U5ezcrmp6InhN7byi4sAUX/utfbJ8pNTLD8ZEBZdiUUNAqri5XA6qobccowaCyO2u274btFwmpO57iUN9Bp/m8QkO0WQPVGUYhQZKqC45eTd2IFWEyapXpwr4+jG8tIcO09HuOHz56NX0gLfVxx+Nx+euUqpPfyNGg==");
        testRequest.setUrl("localhost:3306");
        testRequest.setAesKey("8QlDPRl3/ecevDuXhu7Dir7k+qc3ZS8I5BlP/nDkyrY=");

        // 配置 mock 对象
        when(cryptoConfig.getRsa()).thenReturn(rsa);
        when(rsa.getPrivateKey()).thenReturn(privateKey);
        when(cryptoConfig.getAes()).thenReturn(aes);
        when(aes.getKey()).thenReturn(backendAesKey);
    }

    /**
     * 测试完整的加密解密流程
     */
    @Test
    void testAddDataSourceWithDecryption() {
        logger.info("开始测试 addDataSource 方法的加密解密流程");
        
        try {
            // 模拟数据库插入操作成功
            doAnswer(invocation -> {
                CommonDatasourcePO entity = invocation.getArgument(0);
                entity.setId(1L); // 模拟生成的ID
                return 1; // 返回影响行数
            }).when(commonDatasourceMapper).insert(any(CommonDatasourcePO.class));

            // 执行测试
            Long result = datasourceService.addDataSource(testRequest);

            // 验证结果
            assertNotNull(result, "返回的ID不应为空");
            assertEquals(1L, result, "返回的ID应为1");

            // 验证 mapper 调用
            verify(commonDatasourceMapper, times(1)).insert(any(CommonDatasourcePO.class));

            logger.info("addDataSource 方法测试成功，返回ID: {}", result);

        } catch (Exception e) {
            logger.error("测试过程中发生异常", e);
            fail("测试失败: " + e.getMessage());
        }
    }

    /**
     * 手动测试解密步骤
     */
    @Test
    void testDecryptionSteps() {
        logger.info("开始手动测试解密步骤");
        
        try {
            String privateKeyString = privateKey;
            String frontendAesKey = testRequest.getAesKey();
            String encryptedPassword = testRequest.getPassword();
            
            logger.info("私钥长度: {}", privateKeyString.length());
            logger.info("前端AES密钥: {}", frontendAesKey);
            logger.info("RSA加密的密码长度: {}", encryptedPassword.length());

            // 步骤1: 使用RSA私钥解密password
            logger.info("步骤1: 使用RSA私钥解密password");
            String encryptedPasswordByAES = CryptoUtil.decryptByRSAPrivateKey(encryptedPassword, privateKeyString);
            logger.info("RSA解密后得到的AES加密密码: {}", encryptedPasswordByAES);

            // 步骤2: 使用前端AES密钥解密password
            logger.info("步骤2: 使用前端AES密钥解密password");
            String realPassword = CryptoUtil.decryptByAESWithIV(encryptedPasswordByAES, frontendAesKey);
            logger.info("最终解密得到的真实密码: {}", realPassword);

            // 步骤3: 使用后端AES密钥重新加密
            logger.info("步骤3: 使用后端AES密钥重新加密密码");
            String finalEncryptedPassword = CryptoUtil.encryptByAES(realPassword, backendAesKey);
            logger.info("后端AES加密后的密码: {}", finalEncryptedPassword);

            // 验证整个流程
            assertNotNull(encryptedPasswordByAES, "RSA解密结果不应为空");
            assertNotNull(realPassword, "真实密码不应为空");
            assertNotNull(finalEncryptedPassword, "最终加密密码不应为空");
            assertFalse(realPassword.isEmpty(), "真实密码不应为空字符串");

            logger.info("解密步骤测试完成，真实密码长度: {}", realPassword.length());

        } catch (Exception e) {
            logger.error("解密步骤测试失败", e);
            fail("解密测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试RSA解密功能
     */
    @Test
    void testRSADecryption() {
        logger.info("测试RSA解密功能");
        
        try {
            String encryptedData = testRequest.getPassword();
            String privateKeyString = privateKey;
            
            // 测试RSA解密
            String decrypted = CryptoUtil.decryptByRSAPrivateKey(encryptedData, privateKeyString);
            
            assertNotNull(decrypted, "RSA解密结果不应为空");
            logger.info("RSA解密成功，解密数据: {}", decrypted);
            
        } catch (Exception e) {
            logger.error("RSA解密测试失败", e);
            fail("RSA解密失败: " + e.getMessage());
        }
    }

    /**
     * 测试AES解密功能
     */
    @Test
    void testAESDecryption() {
        logger.info("测试AES解密功能");
        
        try {
            // 先进行RSA解密得到AES加密的密码
            String encryptedPasswordByAES = CryptoUtil.decryptByRSAPrivateKey(testRequest.getPassword(), privateKey);
            String aesKey = testRequest.getAesKey();
            
            // 测试AES解密
            String realPassword = CryptoUtil.decryptByAESWithIV(encryptedPasswordByAES, aesKey);
            
            assertNotNull(realPassword, "AES解密结果不应为空");
            assertFalse(realPassword.isEmpty(), "真实密码不应为空");
            logger.info("AES解密成功，真实密码: {}", realPassword);
            
        } catch (Exception e) {
            logger.error("AES解密测试失败", e);
            fail("AES解密失败: " + e.getMessage());
        }
    }
}