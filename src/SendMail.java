import com.sendgrid.*;
import java.io.IOException;

public class SendMail {
    private final String SENDGRID_API_KEY = Config.getApiKey();

    public void sendMail(String to, String subject, String body) throws IOException {
        Email email = new Email("expify08@gmail.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);

        Mail mail = new Mail(email, subject, toEmail, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch(IOException e) {
            throw new IOException("Failed to send email: " + e.getMessage());
        }
    }
}
