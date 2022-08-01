package com.wyf;

import com.wyf.popj.mail.Mail;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EmailTest extends TmallApplicationTests {
    @Autowired
    EmailUtils emailUtils;

    @Test
    public void contextLoads() {
        Mail mail = Mail.getMail();
        mail.setSubject("宁就是卷王");
        mail.setText("卷死一个是一个");

        String[] toList = {"1669313568@qq.com"};
        mail.setTo(toList);
        emailUtils.sendMail(mail);
    }


    @Test
    public void sendHTML(){
        Mail mail = Mail.getMail();
        mail.setSubject("宁就是卷王");

        String[] toList = {"1677868627@qq.com"};
        mail.setTo(toList);

        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\t<h3>这是我编辑的黄色网站</h3>\n" +
                "<a href=\"https://cn.pornhub.com/\">这是一个黄色网站</a>"+
                "</body>\n" +
                "</html>";
        mail.setText(content);

        emailUtils.sendMailHtml(mail);
    }

    @Test
    public void sendAttarchment(){
        Mail mail = Mail.getMail();
        mail.setSubject("SpringBoot集成JavaMail实现邮件发送");
        mail.setText("SpringBoot集成JavaMail实现简易邮件发送功能");

        String[] toList = {"wayfreem@163.com"};
        mail.setTo(toList);

        List<FileSystemResource> list = new ArrayList<>();
        list.add(new FileSystemResource(new File("E:\\test\\img\\150A01.png")));
        list.add(new FileSystemResource(new File("E:\\test\\img\\150A01.png")));
        mail.setFile(list);

        emailUtils.sendMailAttachment(mail);
    }

    @Test
    public void sendImg(){
        Mail mail = Mail.getMail();
        mail.setSubject("SpringBoot集成JavaMail实现邮件发送");
        String[] toList = {"wayfreem@163.com"};
        mail.setTo(toList);

        // 设置图片的地址名称
        String contentId = "0001";

        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\t<h3>这是我编辑的黄色网站</h3>\n" +
                "<a href='81.70.181.205:81><h1>PronHub</h1></a>'\n"+
                " <div> <img src=\'cid:"+contentId+"\' /> </div> " +
                "</body>\n" +
                "</html>";
        mail.setText(content);
        mail.setContentId(contentId);

        // 添加附件
        List<FileSystemResource> list = new ArrayList<>();
        list.add(new FileSystemResource(new File("C:\\Users\\Yifei Wang\\122.jpg")));
        mail.setFile(list);

        emailUtils.sendMailInline(mail);
    }

}
