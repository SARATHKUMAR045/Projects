package Project.TaskAutomationTool.Controller;

import Project.TaskAutomationTool.service.NotificationService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/sendRemainderEmail")
    public String sendRemainderEmail() {
        try {
            logger.info("Attempting to send Remainder email");
            notificationService.checkAndSendEmail();
            logger.info("Email remainder Send successfully");
            return "Email remainder Send successfully";
        } catch (Exception e) {
            logger.error("Error to send the remainder email{}", e.getMessage());
            return "s";
        }
    }

}
