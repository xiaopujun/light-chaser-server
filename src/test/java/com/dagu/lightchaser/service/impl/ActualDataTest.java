package com.dagu.lightchaser.service.impl;

import com.dagu.lightchaser.util.CryptoUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试实际前端数据的解密
 *
 * @author zhenglin
 * @date 2025/07/27
 */
public class ActualDataTest {

    private static final Logger logger = LoggerFactory.getLogger(ActualDataTest.class);

    /**
     * 使用实际密钥测试前端数据
     */
    @Test
    void testActualFrontendDataWithCurrentKey() {
        logger.info("测试实际前端数据解密");

        try {
            // 实际前端传递的数据（最新的）
            String frontendPassword = "PymUt8DQQvqLH9vrDPHE/i4+6/PaFqf0P5D8ZoCVOGLqWVhLNgmj8y3HrjxegtJLhbaWaYbV7gr54ItaCevYv4Pv9YT6xExR3Kr6dWu+ABIOvMf9ByQxYKHxAAv5s6V31siVfC4LVUoJx6bMwiryCpq1G6zxHb7UmkLWDGzDX3zTuPCfbqM/U5ezcrmp6InhN7byi4sAUX/utfbJ8pNTLD8ZEBZdiUUNAqri5XA6qobccowaCyO2u274btFwmpO57iUN9Bp/m8QkO0WQPVGUYhQZKqC45eTd2IFWEyapXpwr4+jG8tIcO09HuOHz56NX0gLfVxx+Nx+euUqpPfyNGg==";
            String frontendAesKey = "8QlDPRl3/ecevDuXhu7Dir7k+qc3ZS8I5BlP/nDkyrY=";

            // 读取实际私钥
            java.io.InputStream privateKeyStream = getClass().getClassLoader().getResourceAsStream("keys/private_key.pem");
            if (privateKeyStream == null) {
                logger.error("私钥文件不存在");
                return;
            }

            String privateKeyContent = new String(privateKeyStream.readAllBytes());
            String privateKey = privateKeyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

            logger.info("私钥长度: {}", privateKey.length());
            logger.info("前端password长度: {}", frontendPassword.length());
            logger.info("前端aesKey: {}", frontendAesKey);

            // 尝试多种解密方法
            
            // 方法1: 直接解密
            logger.info("\n=== 方法1: 直接RSA解密 ===");
            try {
                String result1 = CryptoUtil.decryptByRSAPrivateKey(frontendPassword, privateKey);
                logger.info("方法1成功: {}", result1);
            } catch (Exception e) {
                logger.error("方法1失败: {}", e.getMessage());
            }

            // 方法2: 尝试不同的填充模式
            logger.info("\n=== 方法2: 尝试PKCS1填充 ===");
            try {
                String result2 = decryptWithPKCS1(frontendPassword, privateKey);
                logger.info("方法2成功: {}", result2);
            } catch (Exception e) {
                logger.error("方法2失败: {}", e.getMessage());
            }

            // 方法3: 检查Base64格式
            logger.info("\n=== 方法3: 检查Base64格式 ===");
            try {
                java.util.Base64.getDecoder().decode(frontendPassword);
                logger.info("Base64格式正确");
            } catch (Exception e) {
                logger.error("Base64格式错误: {}", e.getMessage());
            }

            // 方法4: 尝试URL安全的Base64
            logger.info("\n=== 方法4: 尝试URL安全Base64 ===");
            try {
                String urlSafeDecoded = frontendPassword.replace('-', '+').replace('_', '/');
                // 添加填充
                while (urlSafeDecoded.length() % 4 != 0) {
                    urlSafeDecoded += "=";
                }
                String result4 = CryptoUtil.decryptByRSAPrivateKey(urlSafeDecoded, privateKey);
                logger.info("方法4成功: {}", result4);
            } catch (Exception e) {
                logger.error("方法4失败: {}", e.getMessage());
            }

        } catch (Exception e) {
            logger.error("测试失败", e);
        }
    }

    /**
     * 使用PKCS1填充解密
     */
    private String decryptWithPKCS1(String encryptedData, String privateKeyString) throws Exception {
        // 确保私钥是纯Base64格式
        String cleanPrivateKey = privateKeyString
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");

        byte[] privateKeyBytes = java.util.Base64.getDecoder().decode(cleanPrivateKey);
        java.security.spec.PKCS8EncodedKeySpec keySpec = new java.security.spec.PKCS8EncodedKeySpec(privateKeyBytes);
        java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
        java.security.PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // 使用默认的PKCS1填充
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedBytes = java.util.Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }

    /**
     * 测试简单的RSA加密解密循环
     */
    @Test
    void testSimpleRSALoop() {
        logger.info("测试简单的RSA加密解密循环");

        try {
            // 生成新的密钥对
            java.security.KeyPair keyPair = CryptoUtil.generateRSAKeyPair();
            String publicKey = CryptoUtil.getPublicKeyString(keyPair);
            String privateKey = CryptoUtil.getPrivateKeyString(keyPair);

            String testData = "hello:world:test";
            logger.info("原始数据: {}", testData);

            // 加密
            String encrypted = CryptoUtil.encryptByRSAPublicKey(testData, publicKey);
            logger.info("加密结果长度: {}", encrypted.length());

            // 解密
            String decrypted = CryptoUtil.decryptByRSAPrivateKey(encrypted, privateKey);
            logger.info("解密结果: {}", decrypted);

            if (testData.equals(decrypted)) {
                logger.info("RSA加密解密循环测试成功");
            } else {
                logger.error("RSA加密解密循环测试失败");
            }

        } catch (Exception e) {
            logger.error("RSA循环测试失败", e);
        }
    }
}