import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail 
{
    public static void main(String[] args) 
    {
        final String smtpAuthUserName = "raalthor@ad.unc.edu";
        final String smtpAuthPassword = "Areno9!1111111111";
        String emailFrom = "ledigiacomo@unc.edu";
        String emailTo   = "ledigiacomo@unc.edu";
        Authenticator authenticator = new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(smtpAuthUserName, smtpAuthPassword);
            }
        };
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", "smtp.office365.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance( properties, authenticator );
        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailFrom));
            InternetAddress[] to = {new InternetAddress(emailTo)};
            message.setRecipients(Message.RecipientType.TO, to);
            message.setSubject("Test");
            message.setText("Hello Me!");
            Transport.send(message);
        }
        catch (MessagingException exception)
        {
            exception.printStackTrace();
        }
    }
}