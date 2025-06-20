package com.xl.common.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Generator {
    // pin码长度
    private static final int PIN_LENGTH = 6;

    // 验证码
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private static final int CODE_LENGTH =4;
    private static final int FONT_SIZE = 24;
    // bumble id生成器
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final String TENANTID= "100";
    private static final String STRIPS= "1000";
    private static final AtomicInteger SEQUENCE = new AtomicInteger(1);

    /**
     * 随机生成6位数
     * @return
     */
    public static String generatorPin(int pinlength){
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pinlength; i++) {
            int randomNum = random.nextInt(10);
            builder.append(randomNum);
        }
        return builder.toString();
    }

    public static String generatorPin(){
        return generatorPin(PIN_LENGTH);
    }

    /**
     * 生成一个包含随机数字的验证码图片，并用Base64编码
     * @return Sting
     * @throws IOException
     */
    public static String[] generateCaptchaBase64Image() throws IOException {
        String base64Image;
        // 创建验证码字符数组
        String[] chars = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        Random random = new Random();
        StringBuilder captchaBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            captchaBuilder.append(chars[random.nextInt(chars.length)]);
        }
        String captchaText = captchaBuilder.toString();
        // 创建BufferedImage对象并绘制验证码
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        g2d.drawString(captchaText, 10, 28);
        // 释放此图形的上下文以及它使用的所有系统资源
        g2d.dispose();
        // 读取图片文件为字节数组
        ByteArrayOutputStream os =new ByteArrayOutputStream();
        ImageIO.write(image,"png",os);
        byte[] imageBytes = os.toByteArray();
        // 将字节数组编码为Base64字符串
        base64Image = Base64.getEncoder().encodeToString(imageBytes);
        // 将验证码图片保存到文件
//        Path path = Paths.get(filePath);
//        try (java.io.OutputStream os = Files.newOutputStream(path)) {
//            javax.imageio.ImageIO.write(image, "png", os);
//        }
        return new String[]{captchaText, base64Image};
    }

    /**
     * 生成相关表No
     * @return
     */
    public static String generateNumberId() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        int sequence = SEQUENCE.getAndIncrement();
        if (SEQUENCE.get() > 9999) {
            SEQUENCE.set(1);
        }
        return TENANTID + STRIPS + timestamp + String.format("%04d", sequence);
    }

    /**
     * 生成10位数的id
     * @return
     */
    public static synchronized String generateIdentityId() {
        // 时间戳占6位（秒级，约64秒循环），随机数占4位
        final int MAX_TIMESTAMP = (1 << 6) - 1;
        final int MAX_RANDOM = (1 << 4) - 1;
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long currentTimestamp = (System.currentTimeMillis() / 1000) % (1 << 6);
        for (;;) {
            int random = ThreadLocalRandom.current().nextInt(MAX_RANDOM + 1);
            long id = (currentTimestamp << 4) | random;

            if (id <= 9999999999L) {  // 确保是10位数
                return TENANTID + STRIPS + timestamp + String.format("%010d", id);
            }
        }
    }


}
