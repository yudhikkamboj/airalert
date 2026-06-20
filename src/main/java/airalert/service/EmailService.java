package airalert.service;

import airalert.model.adsb.AircraftDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class EmailService {

    // This address must be verified with Amazon SES.
    static final String FROM = "yudhik15@gmail.com";
    // This address must be verified with Amazon SES.
    static final String TO = "yudhik15@gmail.com";

    // The configuration set to use for this email. If you do not want to use a
    // configuration set, comment the following variable and the
    // .withConfigurationSetName(CONFIGSET); argument below.
//    static final String CONFIGSET = "ConfigSet";

    // The subject line for the email.
    static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";

    // The HTML body for the email.
    static final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
            + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
            + "AWS SDK for Java</a>";

    // The email body for recipients with non-HTML email clients.
    static final String TEXTBODY = "This email was sent through Amazon SES "
            + "using the AWS SDK for Java.";

    public void sendEmail(List<AircraftDetails> aircraftDetails) {

        try {
            String aircrafs = aircraftDetails.stream().map(a->a.getIcaoType()).reduce((a,b)->a+","+b).orElse("");

            SesClient client = SesClient.builder()
                    .region(Region.AP_SOUTH_1).build();

            SendEmailRequest request = SendEmailRequest.builder()
                    .destination(Destination.builder().toAddresses(TO).build())
                    .message(Message.builder()
                            .body(Body.builder()
                                    .html(Content.builder().charset("UTF-8").data(HTMLBODY).build())
                                    .text(Content.builder().charset("UTF-8").data(aircrafs).build())
                                    .build())
                            .subject(Content.builder().charset("UTF-8").data(SUBJECT).build())
                            .build())
                    .source(FROM)
                    .build();

            client.sendEmail(request);
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
        }
    }
}
