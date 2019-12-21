package xyz.st.meethere.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

class Auth extends Authenticator {

    private String username = "";
    private String password = "";

    public Auth(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}

class SendMail {

    private Properties props;
    private Session mailSession;
    private MimeMessage mimeMsg;


    public SendMail(String SMTPHost, String Port, String MailUsername, String MailPassword) {
        Auth au = new Auth(MailUsername, MailPassword);
        props = System.getProperties();
        props.put("mail.smtp.host", SMTPHost);
        props.put("mail.smtp.port", Port);
        props.put("mail.smtp.auth", "true");
        mailSession = Session.getInstance(props, au);
    }

    public boolean sendingMimeMail(String MailFrom, String MailTo,
                                   String MailCopyTo, String MailBCopyTo, String MailSubject,
                                   String MailBody) {
        try{
            mimeMsg = new MimeMessage(mailSession);
            mimeMsg.setFrom(new InternetAddress(MailFrom));
            if (MailTo != null) {
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress
                        .parse(MailTo));
            }
            if (MailCopyTo != null) {
                mimeMsg.setRecipients(javax.mail.Message.RecipientType.CC,
                        InternetAddress.parse(MailCopyTo));
            }
            if (MailBCopyTo != null) {
                mimeMsg.setRecipients(javax.mail.Message.RecipientType.BCC,
                        InternetAddress.parse(MailBCopyTo));
            }
            mimeMsg.setSubject(MailSubject, "utf8");
            mimeMsg.setContent(MailBody, "text/html;charset=utf8");
            Transport.send(mimeMsg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

public class MailService {
    public  boolean sendmail(String mailto, String username){
        String MailTo=mailto;
        String MailSubject="MeetHere - Application Received";
        String MailBCopyTo="";
        String MailCopyTo="";
        String MailBody="<h1>Welcome to use Meethere System. Dear "+username+",<br />" +
                "<br />"+"Congratulations,you have registered successfully</h1>";
        String SMTPHost = "smtp.qq.com";
        String Port="25";
        String MailUsername = "690385702@qq.com";
        String MailPassword = "duoohfwsnwxjbfji";
        String MailFrom = "690385702@qq.com";
        if(SMTPHost==null||SMTPHost==""||MailUsername==null||MailUsername==""||MailPassword==null||MailPassword==""||MailFrom==null||MailFrom=="")
        {
            System.out.println("Servlet parameter Wrongs");
        }
        SendMail send=new SendMail(SMTPHost,Port,MailUsername,MailPassword);
        if(send.sendingMimeMail(MailFrom, MailTo, MailCopyTo, MailBCopyTo, MailSubject, MailBody)){
            return true;
        }
        else
        {
            return false;
        }
    }
}
