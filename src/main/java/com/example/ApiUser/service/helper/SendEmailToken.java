package com.example.ApiUser.service.helper;

import com.example.ApiUser.entity.authentication.users.EmailVerificationToken;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.repository.authentication.users.EmailVerificationTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SendEmailToken {
    EmailVerificationTokenRepository emailVerificationTokenRepository;
    JavaMailSender mailSender;

    @NonFinal
    @Value("${DOMAIN_APP}")
    String domainApp;


    public void sendVerificationEmail(String email, String token) throws MessagingException {
        String subject = "🎬 Verify your Movie SeeMe App account";
        String verifyUrl = domainApp + "/apiUser/auth/verify-email?token=" + token;
        String content = """
                <html>
                <body style="margin:0; padding:0; background-color:#f5f5f5; font-family: Arial, sans-serif;">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="padding: 20px 0;">
                        <tr>
                            <td align="center">
                                <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                                    <!-- Header -->
                                    <tr>
                                        <td style="background-color: #e50914; color: #ffffff; text-align: center; padding: 25px;">
                                            <h1 style="margin:0; font-size: 24px;">🎬 Movie SeeMe</h1>
                                        </td>
                                    </tr>
                                    <!-- Body -->
                                    <tr>
                                        <td style="padding: 30px;">
                                            <h2 style="color: #333; font-size: 20px; margin-top:0;">Hello!</h2>
                                            <p style="font-size:16px; color:#555;">Thank you for registering an account at <strong>Movie SeeMe App by SonND</strong>.</p>
                                            <p style="font-size:16px; color:#555;">Please click the button below to verify your email and start exploring thousands of exciting movies.</p>
                
                                            <!-- CTA button -->
                                            <div style="text-align:center; margin:30px 0;">
                                                <a href="%s" 
                                                   style="display:inline-block; background-color:#e50914; color:white; text-decoration:none; padding:15px 30px; border-radius:5px; font-weight:bold; font-size:16px;">
                                                   Verify Email
                                                </a>
                                            </div>
                
                                            <p style="font-size:14px; color:#777;">If you did not request this, please ignore this email.</p>
                                            <p style="font-size:14px; color:#777;"><strong>Note:</strong> Unverified accounts cannot recover password if forgotten.</p>
                                            <p style="font-size:14px; color:#777;">The link is valid for <strong>24 hours</strong>.</p>
                                        </td>
                                    </tr>
                                    <!-- Footer -->
                                    <tr>
                                        <td style="background-color: #f0f0f0; text-align:center; padding:15px; font-size:12px; color:#999;">
                                            © 2025 MovieApp. All rights reserved.
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(verifyUrl);

        // Tạo email
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(mimeMessage);
    }

    public void sendPasswordResetEmail(User user) throws MessagingException {
        String token = UUID.randomUUID().toString();

        EmailVerificationToken tokenPassword = EmailVerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();
        emailVerificationTokenRepository.save(tokenPassword);

        String content = String.format("""
                <html>
                <body style="margin:0; padding:0; font-family:Arial, sans-serif; background-color:#f4f4f4;">
                  <table width="100%%%%" cellpadding="0" cellspacing="0" style="padding: 50px 0;">
                    <tr>
                      <td align="center">
                        <table width="500" cellpadding="0" cellspacing="0" style="background:#ffffff; border-radius:10px; box-shadow:0 4px 12px rgba(0,0,0,0.1); overflow:hidden;">
                          <tr>
                            <td style="background:#e50914; color:white; text-align:center; padding:30px 0; font-size:28px; font-weight:bold;">
                              🎬 Movie SeeMe
                            </td>
                          </tr>
                          <tr>
                            <td style="padding: 30px; text-align:center; color:#333;">
                              <p style="font-size:16px; margin-bottom:20px;">
                                You requested to reset your password. Copy and Use the key below in the app must reset your password:
                              </p>
                              <div style="display:inline-block; padding:15px 20px; background:#f0f0f0; border-radius:6px; font-family:monospace; font-size:20px; letter-spacing:1px; word-break:break-all;">
                                %s
                              </div>
                              <p style="margin:20px 0; font-size:14px; color:#555;">
                                This key is valid for <strong>1 hour</strong>.
                              </p>
                            </td>
                          </tr>
                          <tr>
                            <td style="background:#f4f4f4; text-align:center; color:#888; padding:15px; font-size:12px;">
                              © 2025 Movie SeeMe. All rights reserved.
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """, token);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setTo(user.getEmail());
        helper.setSubject("🎬 MovieApp - Key reset password");
        helper.setText(content, true);
        mailSender.send(message);
    }

}
