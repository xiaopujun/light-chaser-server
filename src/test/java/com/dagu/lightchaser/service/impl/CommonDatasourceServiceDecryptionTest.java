package com.dagu.lightchaser.service.impl;

import com.dagu.lightchaser.util.CryptoUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简化的解密测试类
 * 专门测试加密解密流程
 *
 * @author zhenglin
 * @date 2025/07/27
 */
public class CommonDatasourceServiceDecryptionTest {

    private static final Logger logger = LoggerFactory.getLogger(CommonDatasourceServiceDecryptionTest.class);

    /**
     * 测试完整的加密解密流程
     * 使用实际的前端数据验证解密逻辑
     */
    @Test
    void testCompleteDecryptionFlow() {
        logger.info("开始测试完整的加密解密流程");
        
        // 前端实际传递的数据
        String frontendPassword = "PymUt8DQQvqLH9vrDPHE/i4+6/PaFqf0P5D8ZoCVOGLqWVhLNgmj8y3HrjxegtJLhbaWaYbV7gr54ItaCevYv4Pv9YT6xExR3Kr6dWu+ABIOvMf9ByQxYKHxAAv5s6V31siVfC4LVUoJx6bMwiryCpq1G6zxHb7UmkLWDGzDX3zTuPCfbqM/U5ezcrmp6InhN7byi4sAUX/utfbJ8pNTLD8ZEBZdiUUNAqri5XA6qobccowaCyO2u274btFwmpO57iUN9Bp/m8QkO0WQPVGUYhQZKqC45eTd2IFWEyapXpwr4+jG8tIcO09HuOHz56NX0gLfVxx+Nx+euUqpPfyNGg==";
        String frontendAesKey = "8QlDPRl3/ecevDuXhu7Dir7k+qc3ZS8I5BlP/nDkyrY=";
        
        try {
            // 读取实际的私钥文件
            java.io.InputStream privateKeyStream = getClass().getClassLoader().getResourceAsStream("keys/private_key.pem");
            if (privateKeyStream == null) {
                logger.error("私钥文件不存在，跳过测试");
                return;
            }
            
            String privateKeyContent = new String(privateKeyStream.readAllBytes());
            String privateKey = privateKeyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
            
            logger.info("私钥长度: {}", privateKey.length());
            logger.info("前端AES密钥: {}", frontendAesKey);
            logger.info("RSA加密的密码长度: {}", frontendPassword.length());

            // 步骤1: 使用RSA私钥解密password
            logger.info("=== 步骤1: 使用RSA私钥解密password ===");
            String encryptedPasswordByAES = CryptoUtil.decryptByRSAPrivateKey(frontendPassword, privateKey);
            logger.info("RSA解密后得到的AES加密密码: {}", encryptedPasswordByAES);

            // 步骤2: 使用前端AES密钥解密password
            logger.info("=== 步骤2: 使用前端AES密钥解密password ===");
            String realPassword = CryptoUtil.decryptByAESWithIV(encryptedPasswordByAES, frontendAesKey);
            logger.info("最终解密得到的真实密码: {}", realPassword);

            // 步骤3: 使用后端AES密钥重新加密（用于存储）
            logger.info("=== 步骤3: 使用后端AES密钥重新加密密码 ===");
            String backendAesKey = "YmVVaTJUNGdvV3huQnZXZzlEUE9kdnBaaS9ZcTZ0UzZtWXl2TWVXTzQ0Qz0=";
            String finalEncryptedPassword = CryptoUtil.encryptByAES(realPassword, backendAesKey);
            logger.info("后端AES加密后的密码: {}", finalEncryptedPassword);

            logger.info("=== 解密流程测试完成 ===");
            logger.info("真实密码: {}", realPassword);
            logger.info("真实密码长度: {}", realPassword.length());

        } catch (Exception e) {
            logger.error("解密流程测试失败", e);
            throw new RuntimeException("解密测试失败: " + e.getMessage(), e);
        }
    }

    /**
     * 测试AES解密（使用已知的测试数据）
     */
    @Test
    void testAESDecryptionWithKnownData() {
        logger.info("开始测试AES解密功能");
        
        try {
            // 使用已知的测试数据
            String testPlainText = "test123";
            String testAesKey = "8QlDPRl3/ecevDuXhu7Dir7k+qc3ZS8I5BlP/nDkyrY=";
            
            // 先加密
            String encrypted = CryptoUtil.encryptByAES(testPlainText, testAesKey);
            logger.info("AES加密结果: {}", encrypted);
            
            // 再解密
            String decrypted = CryptoUtil.decryptByAES(encrypted, testAesKey);
            logger.info("AES解密结果: {}", decrypted);
            
            if (testPlainText.equals(decrypted)) {
                logger.info("AES加密解密测试成功");
            } else {
                logger.error("AES加密解密测试失败，原文: {}, 解密结果: {}", testPlainText, decrypted);
            }
            
        } catch (Exception e) {
            logger.error("AES测试失败", e);
            throw new RuntimeException("AES测试失败: " + e.getMessage(), e);
        }
    }

    /**
     * 测试AES带IV解密功能
     */
    @Test
    void testAESWithIVDecryption() {
        logger.info("开始测试AES带IV解密功能");
        
        try {
            String testPlainText = "test123";
            String testAesKey = "8QlDPRl3/ecevDuXhu7Dir7k+qc3ZS8I5BlP/nDkyrY=";
            
            // 模拟前端加密格式（IV:密文）
            String testEncryptedWithIV = "dGVzdGl2MTIzNDU2Nzg=:dGVzdGVuY3J5cHRlZA=="; // 模拟数据
            
            // 使用标准AES解密测试
            String encrypted = CryptoUtil.encryptByAES(testPlainText, testAesKey);
            String decrypted = CryptoUtil.decryptByAESWithIV(encrypted, testAesKey);
            
            logger.info("标准AES加密: {}", encrypted);
            logger.info("使用WithIV方法解密: {}", decrypted);
            
            if (testPlainText.equals(decrypted)) {
                logger.info("AES WithIV解密测试成功");
            } else {
                logger.error("AES WithIV解密测试失败");
            }
            
        } catch (Exception e) {
            logger.error("AES WithIV测试失败", e);
            throw new RuntimeException("AES WithIV测试失败: " + e.getMessage(), e);
        }
    }
}