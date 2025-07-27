package com.dagu.lightchaser.service.impl;

import com.dagu.lightchaser.util.CryptoUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 加密流程测试类
 * 模拟前端完整的加密解密流程
 *
 * @author zhenglin
 * @date 2025/07/27
 */
public class CryptoFlowTest {

    private static final Logger logger = LoggerFactory.getLogger(CryptoFlowTest.class);

    /**
     * 测试完整的前端到后端加密解密流程
     */
    @Test
    void testCompleteEncryptionDecryptionFlow() {
        logger.info("开始测试完整的加密解密流程");
        
        try {
            // 模拟真实密码
            String originalPassword = "root123456";
            logger.info("原始密码: {}", originalPassword);

            // 步骤1: 生成RSA密钥对（模拟服务器启动时生成）
            KeyPair rsaKeyPair = CryptoUtil.generateRSAKeyPair();
            String publicKey = CryptoUtil.getPublicKeyString(rsaKeyPair);
            String privateKey = CryptoUtil.getPrivateKeyString(rsaKeyPair);
            
            logger.info("RSA公钥长度: {}", publicKey.length());
            logger.info("RSA私钥长度: {}", privateKey.length());

            // 步骤2: 生成AES密钥（模拟前端生成）
            String aesKey = CryptoUtil.generateAESKey();
            logger.info("前端AES密钥: {}", aesKey);

            // 步骤3: 前端使用AES加密密码
            String aesEncryptedPassword = CryptoUtil.encryptByAES(originalPassword, aesKey);
            logger.info("AES加密后的密码: {}", aesEncryptedPassword);

            // 步骤4: 前端使用RSA公钥加密AES加密后的密码
            String rsaEncryptedPassword = CryptoUtil.encryptByRSAPublicKey(aesEncryptedPassword, publicKey);
            logger.info("RSA加密后的密码长度: {}", rsaEncryptedPassword.length());

            // === 模拟前端请求数据 ===
            logger.info("\n=== 模拟前端请求 ===");
            logger.info("password: {}", rsaEncryptedPassword);
            logger.info("aesKey: {}", aesKey);

            // === 后端解密流程 ===
            logger.info("\n=== 后端解密流程 ===");

            // 步骤5: 后端使用RSA私钥解密password
            String decryptedAesEncryptedPassword = CryptoUtil.decryptByRSAPrivateKey(rsaEncryptedPassword, privateKey);
            logger.info("RSA解密后得到的AES加密密码: {}", decryptedAesEncryptedPassword);

            // 验证RSA解密结果
            assertEquals(aesEncryptedPassword, decryptedAesEncryptedPassword, "RSA解密结果应与原始AES加密密码一致");

            // 步骤6: 后端使用前端AES密钥解密password
            String finalPassword = CryptoUtil.decryptByAESWithIV(decryptedAesEncryptedPassword, aesKey);
            logger.info("最终解密得到的密码: {}", finalPassword);

            // 验证最终解密结果
            assertEquals(originalPassword, finalPassword, "最终解密密码应与原始密码一致");

            // 步骤7: 后端使用自己的AES密钥重新加密密码（用于存储）
            String backendAesKey = CryptoUtil.generateAESKey();
            String storedPassword = CryptoUtil.encryptByAES(finalPassword, backendAesKey);
            logger.info("后端存储的加密密码: {}", storedPassword);

            logger.info("\n=== 测试成功 ===");
            logger.info("原始密码: {}", originalPassword);
            logger.info("最终解密密码: {}", finalPassword);
            logger.info("密码匹配: {}", originalPassword.equals(finalPassword));

        } catch (Exception e) {
            logger.error("加密解密流程测试失败", e);
            fail("测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试AES加密解密功能
     */
    @Test
    void testAESEncryptionDecryption() {
        logger.info("开始测试AES加密解密");
        
        try {
            String plainText = "test123456";
            String aesKey = CryptoUtil.generateAESKey();
            
            // 加密
            String encrypted = CryptoUtil.encryptByAES(plainText, aesKey);
            logger.info("AES加密结果: {}", encrypted);
            
            // 解密
            String decrypted = CryptoUtil.decryptByAES(encrypted, aesKey);
            logger.info("AES解密结果: {}", decrypted);
            
            assertEquals(plainText, decrypted, "AES解密结果应与原始文本一致");
            
            // 测试WithIV解密方法
            String decryptedWithIV = CryptoUtil.decryptByAESWithIV(encrypted, aesKey);
            logger.info("使用WithIV方法解密: {}", decryptedWithIV);
            
            assertEquals(plainText, decryptedWithIV, "WithIV解密结果应与原始文本一致");
            
            logger.info("AES测试成功");
            
        } catch (Exception e) {
            logger.error("AES测试失败", e);
            fail("AES测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试RSA加密解密功能
     */
    @Test
    void testRSAEncryptionDecryption() {
        logger.info("开始测试RSA加密解密");
        
        try {
            String plainText = "test data for RSA";
            
            // 生成密钥对
            KeyPair keyPair = CryptoUtil.generateRSAKeyPair();
            String publicKey = CryptoUtil.getPublicKeyString(keyPair);
            String privateKey = CryptoUtil.getPrivateKeyString(keyPair);
            
            // 加密
            String encrypted = CryptoUtil.encryptByRSAPublicKey(plainText, publicKey);
            logger.info("RSA加密结果长度: {}", encrypted.length());
            
            // 解密
            String decrypted = CryptoUtil.decryptByRSAPrivateKey(encrypted, privateKey);
            logger.info("RSA解密结果: {}", decrypted);
            
            assertEquals(plainText, decrypted, "RSA解密结果应与原始文本一致");
            
            logger.info("RSA测试成功");
            
        } catch (Exception e) {
            logger.error("RSA测试失败", e);
            fail("RSA测试失败: " + e.getMessage());
        }
    }
}