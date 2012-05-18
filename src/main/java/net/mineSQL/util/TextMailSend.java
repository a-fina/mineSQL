package net.mineSQL.util;

import java.io.PrintStream;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

public class TextMailSend
{

    Message messaggio;

    public TextMailSend(String returnAddress, String from, HttpSession session)
    {
        String SMTPuser = session.getServletContext().getInitParameter("SMTPuser");
        String SMTPserver = session.getServletContext().getInitParameter("SMTPserver");
        int SMTPport = Integer.parseInt(session.getServletContext().getInitParameter("SMTPport"));
        Properties props = new Properties();
        props.put("mail.host", SMTPserver);
        props.put("mail.port", new Integer(SMTPport));
        props.put("mail.from", returnAddress);
        props.put("mail.user", SMTPuser);
        Session mailsession = Session.getDefaultInstance(props, null);
        messaggio = new MimeMessage(mailsession);
        try
        {
            InternetAddress addressFrom = new InternetAddress(from);
            messaggio.setFrom(addressFrom);
        }
        catch(AddressException e)
        {
            System.out.println(e.toString());
        }
        catch(MessagingException e)
        {
            System.out.println(e.toString());
        }
    }

    public boolean sendMessage()
    {
        try
        {
            Transport.send(messaggio);
        }
        catch(MessagingException e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void addDestinatario(String dest)
    {
        try
        {
            InternetAddress addressDest = new InternetAddress(dest);
            messaggio.addRecipient(javax.mail.Message.RecipientType.TO, addressDest);
        }
        catch(AddressException e)
        {
            System.out.println(e.toString());
        }
        catch(MessagingException e)
        {
            System.out.println(e.toString());
        }
    }

    public void addCC(String dest)
    {
        try
        {
            InternetAddress addressDest = new InternetAddress(dest);
            messaggio.addRecipient(javax.mail.Message.RecipientType.CC, addressDest);
        }
        catch(AddressException e)
        {
            System.out.println(e.toString());
        }
        catch(MessagingException e)
        {
            System.out.println(e.toString());
        }
    }

    public void addBCC(String dest)
    {
        try
        {
            InternetAddress addressDest = new InternetAddress(dest);
            messaggio.addRecipient(javax.mail.Message.RecipientType.BCC, addressDest);
        }
        catch(AddressException e)
        {
            System.out.println(e.toString());
        }
        catch(MessagingException e)
        {
            System.out.println(e.toString());
        }
    }

    public void setOggetto(String ogg)
    {
        try
        {
            messaggio.setSubject(ogg);
        }
        catch(MessagingException e)
        {
            System.out.println(e.toString());
        }
    }

    public void setCorpo(String corpo)
    {
        try
        {
            messaggio.setContent(corpo, "text/plain");
        }
        catch(MessagingException e)
        {
            System.out.println(e.toString());
        }
    }

    public void writeMessageToDB()
    {
    }

    public void readMessageFromDB()
    {
    }
}
