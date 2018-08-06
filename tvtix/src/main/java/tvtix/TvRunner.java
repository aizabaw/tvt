package tvtix;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TvRunner implements Runnable {

	public void run() {
		
		while(true) {

			try {

				Document doc = Jsoup.connect("http://www.tvtickets.com/fmi/shows/browserecord.php?&show=The%20Big%20Bang%20Theory").get();

				Elements eList = doc.getElementsByTag("body");
				Element e = eList.get(0);
				Element c = e.child(0).child(0).child(0).child(2).child(1).child(1).child(0).child(0).child(1).child(0).child(0).child(1);

				boolean contains = StringUtils.contains(c.toString(), "<td colspan=\"3\" align=\"center\" nowrap> <br><br> <font size=\"1\" color=\"#303030\" face=\"verdana\">");

				
				Properties props = System.getProperties();
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", "smtp.gmail.com");
				props.put("mail.smtp.port", "465");
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.auth", "true");
				
				Authenticator auth = new SMTPAuthenticator("fireandaiz@gmail.com", "doordonotthereisnotry");
		        Session session = Session.getDefaultInstance(props, auth);
				
//				Session session = Session.getInstance(props);
				
				Message msg = new MimeMessage(session);
				msg.setSubject("TV TICKETS - TBBT");
				msg.setFrom(new InternetAddress("noreply@tvaiz.com"));
				msg.setRecipients(RecipientType.TO, InternetAddress.parse("alizaanos@gmail.com"));
				
				if (contains) {
					
					System.out.println("NOPE!");
					msg.setText("NOPE!!!");
				
				} else {
				
					System.out.println("YAY!");					
					msg.setText("YAY!!!");
					
				}
				
				Transport.send(msg);
				
				Thread.sleep(30000);

			} catch (IOException ex) {
				System.out.println("Unable to connect to tvtickets.com");
			} catch (InterruptedException ex) {
				System.out.println("ERROR! ERROR! ERROR!");
			} catch (MessagingException ex) {
				System.out.println("FAILED TO SEND EMAIL");
				ex.printStackTrace();
			}

		}

	}
	
	private class SMTPAuthenticator extends javax.mail.Authenticator {
		  
        private String username;
  
        private String password;
  
        public SMTPAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }
  
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

}
