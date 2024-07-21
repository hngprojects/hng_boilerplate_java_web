package hng_java_boilerplate.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Collections;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

@Service
public class GmailService {
    private static final String APPLICATION_NAME = "Bank-System-API";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Creates and returns a Gmail service instance.
     * 
     * @return Gmail service instance.
     * @throws Exception if an error occurs during service creation.
     */
    public Gmail getGmailService() throws Exception {
        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(getClass().getResourceAsStream("/credentials.json")));

        // Set up authorization flow.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, Collections.singletonList(GmailScopes.GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        // Set up the local server and receive authorization.
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        // Return the Gmail service.
        return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Sends an email using the Gmail service.
     * 
     * @param to The recipient's email address.
     * @param subject The subject of the email.
     * @param bodyText The body text of the email.
     * @throws Exception if an error occurs during email sending.
     */
    public void sendEmail(String to, String name, String bodyText) throws Exception {
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Recipient email address is required");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (bodyText == null || bodyText.isEmpty()) {
            throw new IllegalArgumentException("Body text is required");
        }

        Gmail service = getGmailService();
        Message message = createMessageWithEmail(createEmail(to, "nicanorkyamba98@example.com", name, bodyText));
        service.users().messages().send("me", message).execute();
    }

    /**
     * Creates a MimeMessage email.
     * 
     * @param to The recipient's email address.
     * @param from The sender's email address.
     * @param subject The subject of the email.
     * @param bodyText The body text of the email.
     * @return MimeMessage object.
     * @throws MessagingException if an error occurs during email creation.
     */
    private MimeMessage createEmail(String to, String from, String name, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        try {
            email.setFrom(new InternetAddress(from));
            email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        } catch (AddressException e) {
            throw new MessagingException("Invalid email address", e);
        }
        email.setSubject(name);
        email.setText(bodyText);
        return email;
    }

    /**
     * Encodes the MimeMessage email into a raw base64url string and wraps it into a Message object.
     * 
     * @param email The MimeMessage email to be encoded.
     * @return Message object containing the encoded email.
     * @throws Exception if an error occurs during encoding.
     */
    private Message createMessageWithEmail(MimeMessage email) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}