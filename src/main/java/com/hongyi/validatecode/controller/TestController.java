package com.hongyi.validatecode.controller;

import com.hongyi.validatecode.util.ValidateCode;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author User
 * @date 2020/9/27 9:34
 */
@RestController
public class TestController {


    /**
     * 响应验证码页面
     *
     * @return
     */
    @RequestMapping(value = "/validateCode")
    public String validateCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession();

        ValidateCode vCode = new ValidateCode(120, 40, 5, 100);
        session.setAttribute("code", vCode.getCode());
        Object code = session.getAttribute("code");
        System.out.println(code);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //vCode.write(response.getOutputStream());
        vCode.write(byteArrayOutputStream);
        //return byteArrayOutputStream.toByteArray();
        return null;

    }
    @GetMapping("checkValidate")
    public String checkValidate(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        HttpSession session = request.getSession();
        String sessionCode = (String) session.getAttribute("code");

        //忽略验证码大小写
        if (code != null && !code.equalsIgnoreCase(sessionCode)) {
            throw new RuntimeException("验证码对应不上code=" + code + "  sessionCode=" + sessionCode);
        }
        return "success!";
    }

    public static String test() throws Exception {
        File file = new File("D:\\1601172695072.png");
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] data = new byte[fileInputStream.available()];
        int read = fileInputStream.read(data);
        String s1 = Base64.encodeBase64String(data);
        System.out.println(s1);
        return s1;
    }

    public static void decode(String image,String filename) throws Exception{

        //byte[] bytes = decoder.decode(image);
        byte[] bytes = Base64.decodeBase64(image);
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {// 调整异常数据
                bytes[i] += 256;
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        fileOutputStream.close();
        fileOutputStream.flush();
    }

    public static Map<String,Object> validateCode() {
        try {
            ArrayList<Object> list = new ArrayList<>();
            HashMap<String, Object> map = new HashMap<>();
            ValidateCode vCode = new ValidateCode(120, 40, 5, 100);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //图片写入到输出流
            vCode.write(byteArrayOutputStream);
            //将流中的数据转换成byte数字
            byte[] imageByte = byteArrayOutputStream.toByteArray();
            //获取到随机生产的code码，并存人map
            java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
            String s = encoder.encodeToString(imageByte);
            //String s = Base64.encodeBase64String(imageByte);
            System.out.println(s);
            String code = vCode.getCode();
            map.put("imageByte",imageByte);
            map.put("code",code);

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws Exception{
        //String test = test();
        //decode(test,"D:\\1.jpg");
        validateCode();
    }

}