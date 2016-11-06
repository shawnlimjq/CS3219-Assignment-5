package application;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.amazonaws.services.identitymanagement.model.User;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SendNotification implements RequestHandler<String, String> {
    
    // Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
    static final String SMTP_USERNAME = "AKIAI2ZZQ7LDS3QBSPJQ";  // Replace with your SMTP username.
    static final String SMTP_PASSWORD = "AqCkMNlBCjchhZtvyo3bIOqo76UPh7RnI1C/Q+QGhKHY";  // Replace with your SMTP password.
    
    static String FROM;	// Replace with your "From" address. This address must be verified.
    static String TO;	// Replace with a "To" address. If your account is still in the 
    					// sandbox, this address must be verified.
    static String SUBJECT = "[GIT-Guard] Notification for %s";
    static String BODY = "User last ran GIT-Guard for %1s, %2s ago";
    
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    static final String HOST = "email-smtp.us-west-2.amazonaws.com";    
    
    // The port you will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
    // STARTTLS to encrypt the connection.
    static final int PORT = 25;
	
    @Override
    public String handleRequest(String input, Context context) {
    	
    	readInput(input);
    	
        // Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtps");
    	props.put("mail.smtp.port", PORT); 
    	
    	// Set properties indicating that we want to use STARTTLS to encrypt the connection.
    	// The SMTP session will begin on an unencrypted connection, and then the client
        // will issue a STARTTLS command to upgrade to an encrypted connection.
    	props.put("mail.smtp.auth", "true");
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.starttls.required", "true");

        
        try {
        	// Create a Session object to represent a mail session with the specified properties. 
        	Session session = Session.getDefaultInstance(props);
        	
            // Create a message with the specified information. 
            MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(FROM));
	        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TO));
	        msg.setSubject(SUBJECT);
	        msg.setContent(BODY,"text/plain");
	            
	        // Create a transport.        
	        Transport transport = session.getTransport();
	                    
	        // Send the message.
	        try
	        {
	            System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
	            
	            // Connect to Amazon SES using the SMTP username and password you specified above.
	            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
	        	
	            // Send the email.
	            transport.sendMessage(msg, msg.getAllRecipients());
	            String currentTime = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss").format(new Date());
	            System.out.println("Email sent on " + currentTime);
	            return "Email sent on " + currentTime;
	        }
	        catch (Exception ex) {
	            System.out.println("The email was not sent.");
	            System.out.println("Error message: " + ex.getMessage());
	            return "The email was not sent. Error message: " + ex.getMessage();
	        }
	        finally
	        {
	            // Close and terminate the connection.
	            transport.close();        	
	        }
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return null;
    	
    }
    
    private static void readInput(String input) {
    	String[] strArray = input.replace("\"", "").split("\n");
    	FROM = strArray[0];
    	TO = strArray[1];
    	String repo = strArray[2].substring(19);
    	long time = Long.parseLong(strArray[3]);
    	long currentTime = new Date().getTime();
    	
    	
    	SUBJECT = String.format(SUBJECT, repo);
    	BODY = "User last ran GIT-Guard \n" + getDurationBreakdown(currentTime - time);
    	
    }
    
    /**
    * Convert a millisecond duration to a string format
    * 
    * @param millis A duration to convert to a string form
    * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
    */
   private static String getDurationBreakdown(long millis)
   {
       if(millis < 0)
       {
           throw new IllegalArgumentException("Duration must be greater than zero!");
       }

       long days = TimeUnit.MILLISECONDS.toDays(millis);
       millis -= TimeUnit.DAYS.toMillis(days);
       long hours = TimeUnit.MILLISECONDS.toHours(millis);
       millis -= TimeUnit.HOURS.toMillis(hours);
       long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
       millis -= TimeUnit.MINUTES.toMillis(minutes);
       long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

       StringBuilder sb = new StringBuilder(64);
       sb.append(days);
       sb.append(" Days ");
       sb.append(hours);
       sb.append(" Hours ");
       sb.append(minutes);
       sb.append(" Minutes ");
       sb.append(seconds);
       sb.append(" Seconds");

       return(sb.toString());
   }
   
}
