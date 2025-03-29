package com.xl.common.utils;

import com.xl.common.dubbo.entity.TradeAccount;
import com.xl.common.dubbo.entity.TradeEntry;
import com.xl.common.dubbo.entity.TradeOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 签名工具类
 */
@Slf4j
public class SignatureUtil {

    /**
     * 签名密钥，实际项目中应该存储在配置文件中
     */
    private static final String SECRET_KEY = "XL_TRANSACTION_SECRET_KEY";

    /**
     * 生成签名
     *
     * @param content 签名内容
     * @return 签名字符串
     */
    public static String generateSignature(String content) {
        String signContent = content + SECRET_KEY;
        return DigestUtils.md5DigestAsHex(signContent.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 验证签名
     *
     * @param content  签名内容
     * @param signature 待验证的签名
     * @return 是否验证通过
     */
    public static boolean verifySignature(String content, String signature) {
        String generatedSignature = generateSignature(content);
        return generatedSignature.equals(signature);
    }

    /**
     * 验证账户数据签名
     *
     * @param tradeAccount 账户对象
     * @return 是否验证通过
     */
    public boolean verifyAccount(TradeAccount tradeAccount) {
        String signContent = tradeAccount.generateSignContent();
        boolean result = SignatureUtil.verifySignature(signContent, tradeAccount.getSignature());
        if (!result) {
            log.warn("账户数据签名验证失败，账户编号：{}", tradeAccount.getAccountNo());
        }
        return result;
    }

} 