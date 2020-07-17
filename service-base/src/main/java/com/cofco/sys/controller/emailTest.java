package com.cofco.sys.controller;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

/**
 * 类的功能描述.
 * 发送指定邮件到邮箱测试类
 * @Auther cofco
 * @Date 2017/4/25
 */

@Controller  //声明为控制器
@RequestMapping("emailTest")
public class emailTest {
    @Resource(name="mailSender")
    JavaMailSenderImpl mailSender;
    @RequestMapping("/sendword")
    public void handle(){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("acccqwezxc@163.com");
        mailMessage.setFrom("507823362@qq.com");//一定要写，501 mail from address must be same as authorization user的错误
        mailMessage.setSubject("spring自带javamail发送的邮件111");
        mailMessage.setText("hi");
        mailSender.send(mailMessage);
    }
    @RequestMapping("/sendhtml")
    public void handle1(){
        MimeMessage mimeMsg = mailSender.createMimeMessage();
        try {
            String html = "<html><head>"+
                    "</head><body>"+
                    "<h1>Hello,this is first html!</h1>"+
                    "<span style='color:red;font-size:36px;'>hello</span>"+
                    "<img src='https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526734280537&di=0f7ac79302207a66fe4db36162486167&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201510%2F07%2F20151007101227_jkevU.jpeg'>"+
                    "</body></html>";
            MimeMessageHelper helper = new MimeMessageHelper(mimeMsg,true);
            helper.setTo("507823362@qq.com");
            helper.setFrom("507823362@qq.com");//一定要写，501 mail from address must be same as authorization user的错误
            helper.setSubject("spring自带javamail发送的邮件");
            helper.setText(html, true);
            mailSender.send(mimeMsg);
        }catch (Exception e){
        }
    }
}

