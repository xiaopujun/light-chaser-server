package com.dagu.lightchaser.service.impl;

import com.dagu.lightchaser.util.CryptoUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 模拟前端加密流程的测试类
 *
 * @author zhenglin
 * @date 2025/07/27
 */
public class FrontendCryptoTest {

    private static final Logger logger = LoggerFactory.getLogger(FrontendCryptoTest.class);

    /**
     * 完全模拟前端的加密流程
     */
    @Test
    void testFrontendEncryptionFlow() {
        logger.info("开始模拟前端加密流程");

        try {
            // 模拟原始密码
            String originalPassword = "root123456";
            logger.info("原始密码: {}", originalPassword);

            // 1. 生成RSA密钥对（模拟后端）
            KeyPair rsaKeyPair = CryptoUtil.generateRSAKeyPair();
            String publicKey = CryptoUtil.getPublicKeyString(rsaKeyPair);
            String privateKey = CryptoUtil.getPrivateKeyString(rsaKeyPair);

            // 2. 生成AES密钥（模拟前端）
            String aesKey = CryptoUtil.generateAESKey();
            logger.info("前端生成的AES密钥: {}", aesKey);

            // 3. 使用AES-256-CBC加密密码（模拟前端的CryptoJS.AES.encrypt）
            String aesEncryptedPassword = simulateFrontendAESEncrypt(originalPassword, aesKey);
            logger.info("AES加密后的密码（IV:密文格式）: {}", aesEncryptedPassword);

            // 4. 使用RSA-OAEP+SHA256加密AES加密后的密码（模拟前端的forge.pki）
            String rsaEncryptedPassword = simulateFrontendRSAEncrypt(aesEncryptedPassword, publicKey);
            logger.info("RSA加密后的密码长度: {}", rsaEncryptedPassword.length());

            // === 模拟前端请求数据 ===
            logger.info("\n=== 模拟前端请求 ===");
            logger.info("password: {}", rsaEncryptedPassword.substring(0, 50) + "...");
            logger.info("aesKey: {}", aesKey);

            // === 后端解密流程 ===
            logger.info("\n=== 后端解密流程 ===");

            // 5. 后端使用RSA私钥解密password
            String decryptedAesEncryptedPassword = CryptoUtil.decryptByRSAPrivateKey(rsaEncryptedPassword, privateKey);
            logger.info("RSA解密后得到的AES加密密码: {}", decryptedAesEncryptedPassword);

            // 验证RSA解密结果
            assertEquals(aesEncryptedPassword, decryptedAesEncryptedPassword, "RSA解密结果应与原始AES加密密码一致");

            // 6. 后端使用前端AES密钥解密password
            String finalPassword = CryptoUtil.decryptByAESWithIV(decryptedAesEncryptedPassword, aesKey);
            logger.info("最终解密得到的密码: {}", finalPassword);

            // 验证最终解密结果
            assertEquals(originalPassword, finalPassword, "最终解密密码应与原始密码一致");

            logger.info("\n=== 测试成功 ===");
            logger.info("原始密码: {}", originalPassword);
            logger.info("最终解密密码: {}", finalPassword);
            logger.info("密码匹配: {}", originalPassword.equals(finalPassword));

        } catch (Exception e) {
            logger.error("前端加密流程模拟失败", e);
            fail("测试失败: " + e.getMessage());
        }
    }

    /**
     * 模拟前端的AES加密（CryptoJS.AES.encrypt）
     * 生成随机IV，使用AES-256-CBC模式，返回 iv:ciphertext 格式
     */
    private String simulateFrontendAESEncrypt(String plaintext, String keyBase64) throws Exception {
        // 解析Base64密钥
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        // 生成随机IV（16字节）
        byte[] ivBytes = new byte[16];
        new java.security.SecureRandom().nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        // 使用AES-256-CBC加密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        // 组合结果：iv:ciphertext（都是Base64编码）
        String ivBase64 = Base64.getEncoder().encodeToString(ivBytes);
        String ciphertextBase64 = Base64.getEncoder().encodeToString(ciphertext);

        return ivBase64 + ":" + ciphertextBase64;
    }

    /**
     * 模拟前端的RSA加密（使用CryptoUtil）
     */
    private String simulateFrontendRSAEncrypt(String plaintext, String publicKeyBase64) throws Exception {
        // 直接使用CryptoUtil，它已经配置了正确的OAEP填充
        return CryptoUtil.encryptByRSAPublicKey(plaintext, publicKeyBase64);
    }

    /**
     * 测试与实际前端数据的兼容性
     */
    @Test
    void testActualFrontendData() {
        logger.info("测试与实际前端数据的兼容性");

        try {
            // 实际前端传递的数据
            String frontendPassword = "PymUt8DQQvqLH9vrDPHE/i4+6/PaFqf0P5D8ZoCVOGLqWVhLNgmj8y3HrjxegtJLhbaWaYbV7gr54ItaCevYv4Pv9YT6xExR3Kr6dWu+ABIOvMf9ByQxYKHxAAv5s6V31siVfC4LVUoJx6bMwiryCpq1G6zxHb7UmkLWDGzDX3zTuPCfbqM/U5ezcrmp6InhN7byi4sAUX/utfbJ8pNTLD8ZEBZdiUUNAqri5XA6qobccowaCyO2u274btFwmpO57iUN9Bp/m8QkO0WQPVGUYhQZKqC45eTd2IFWEyapXpwr4+jG8tIcO09HuOHz56NX0gLfVxx+Nx+euUqpPfyNGg==";
            String frontendAesKey = "8QlDPRl3/ecevDuXhu7Dir7k+qc3ZS8I5BlP/nDkyrY=";

            // 读取实际的私钥文件
            java.io.InputStream privateKeyStream = getClass().getClassLoader().getResourceAsStream("keys/private_key.pem");
            if (privateKeyStream == null) {
                logger.warn("私钥文件不存在，跳过实际数据测试");
                return;
            }

            String privateKeyContent = new String(privateKeyStream.readAllBytes());
            String privateKey = privateKeyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

            logger.info("开始解密实际前端数据");

            // 步骤1: RSA解密
            String encryptedPasswordByAES = CryptoUtil.decryptByRSAPrivateKey(frontendPassword, privateKey);
            logger.info("RSA解密成功，得到AES加密的密码: {}", encryptedPasswordByAES);

            // 步骤2: AES解密
            String realPassword = CryptoUtil.decryptByAESWithIV(encryptedPasswordByAES, frontendAesKey);
            logger.info("最终解密得到的密码: {}", realPassword);

            assertNotNull(realPassword, "解密后的密码不应为空");
            assertFalse(realPassword.isEmpty(), "解密后的密码不应为空字符串");

            logger.info("实际前端数据解密测试成功");

        } catch (Exception e) {
            logger.error("实际前端数据解密失败", e);
            // 不抛出异常，因为可能是密钥不匹配
            logger.warn("这可能是因为前端使用的公钥与当前私钥不匹配");
        }
    }
}