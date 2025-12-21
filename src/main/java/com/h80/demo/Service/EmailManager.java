package com.h80.demo.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.h80.demo.Document.Servers;
import com.h80.demo.Repository.MongoRepo;

@Service
public class EmailManager {

    private final MongoRepo repo;
    private final ManageRequest managerequest;
    private final Set<String> downList = ConcurrentHashMap.newKeySet();
    private JavaMailSender mailSender;

    public EmailManager(JavaMailSender mailSender, ManageRequest managerequest, MongoRepo repo) {
        this.mailSender = mailSender;
        this.managerequest = managerequest;
        this.repo = repo;
    }

    public void ServerIsDown(String url) {
        String domain = managerequest.CheckFormatAndGetDomaine(url);
        if (downList.contains(domain)) {
            return;
        }
        downList.add(domain);
        Servers server = repo.findByDomain(domain)
                .orElseThrow(() -> new IllegalStateException("Server not found: " + domain));

        server.setStatus(false);
        repo.save(server);
        String subject = "⚠️ Alert: Your Website Is Currently Down";
        String to = server.getEmail();
        String body = String.format(
                """
                        This is to inform you that our monitoring system has detected that your website is currently unavailable.

                        Incident details:

                        Website: %s

                        Detected at: %s

                        Current status: ❌ Down

                        The issue may be temporary (server, network, or maintenance related).
                        We recommend checking your hosting service or server as soon as possible.

                        Our system will continue monitoring your website and will notify you immediately once it is back online.

                        Best regards,
                        Website Monitoring Service
                        """,
                server.getDomain(), Instant.now().toString());
        SendMail(to, subject, body);
    }

    public void ServerIsUp(String url) {
        String domain = managerequest.CheckFormatAndGetDomaine(url);
        if (!downList.contains(domain)) {
            return;
        }
        downList.remove(domain);
        Servers servers = repo.findByDomain(domain)
                .orElseThrow(() -> new IllegalStateException("Server not found: " + domain));

        servers.setStatus(true);
        repo.save(servers);
        String to = servers.getEmail();
        String subject = "✅ Good News! Your Website Is Back Online";
        String body = String.format("""
                                Good news 🎉
                                We are pleased to inform you that your website is back online and operating normally.

                                Recovery details:

                                Website: %s

                                Back online at: %s

                                Current status: ✅ Operational

                                Congratulations on resolving the issue successfully.
                                Our monitoring system will continue to track your website 24/7 to ensure stability.

                                If you need any assistance or have further questions, feel free to contact us.

                                Best regards,
                                Website Monitoring Service
                """, servers.getDomain(), Instant.now().toString());
        SendMail(to, subject, body);

    }

    public void SendMail(String to, String Subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(Subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void cleanDownList(List<String> arr) {
        downList.removeAll(arr);  
    }
}
