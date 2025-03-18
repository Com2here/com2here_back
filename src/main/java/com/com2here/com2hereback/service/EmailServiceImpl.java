package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.config.redis.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "ck8901ck@gmail.com";
    private final RedisUtil redisUtil;

    @Override
    public CMResponse createCode() {
        BaseResponseStatus status;
        try{

            int leftLimit = 48; // number '0'
            int rightLimit = 122; // alphabet 'z'
            int targetStringLength = 6;
            Random random = new Random();

            String code = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

            status = BaseResponseStatus.SUCCESS;

            return CMResponse.success(status.getCode(), status,code);
        }catch (Exception e){
            status = BaseResponseStatus.FAIL_CODE_CREATE;
            return CMResponse.fail(status.getCode(), status, null);
        }
    }

    @Override
    public CMResponse setEmail(String email, String code) {
        BaseResponseStatus status;

        try {
            Context context = new Context();
            context.setVariable("email", email);
            context.setVariable("code", code);
            // 템플릿 엔진 설정
            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("templates/");
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateResolver.setCacheable(false);

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            // 템플릿 처리
            String setEmail = templateEngine.process("mail", context);

            status = BaseResponseStatus.SUCCESS;
            return CMResponse.success(status.getCode(), status, setEmail);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            status = BaseResponseStatus.FAIL_MAIL_SET;
            return CMResponse.fail(status.getCode(), status, null);
        }
    }


    @Override
    public CMResponse createEmailForm(String email) {
        BaseResponseStatus status;
        try {
            // 인증 코드 생성
            String authCode = (String) createCode().getData();
            System.out.println("authCode : " + authCode);
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipients(RecipientType.TO, email);
            message.setSubject("안녕하세요. 인증번호입니다.");
            message.setFrom(senderEmail);

            // 인증 코드로 이메일 내용 설정
            String emailContent = (String) setEmail(email, authCode).getData();  // authCode를 여기에서 전달해야 합니다.

            message.setContent(emailContent, "text/html; charset=utf-8");

            redisUtil.setDataExpire(email, authCode, 60 * 30L);

            status = BaseResponseStatus.SUCCESS;

            return CMResponse.success(status.getCode(), status, message);
        } catch (Exception e) {
            status = BaseResponseStatus.FAIL_MAIL_CREATE;
            return CMResponse.fail(status.getCode(), status, null);
        }
    }


    @Override
    public CMResponse sendEmail(String email) {
        BaseResponseStatus status;
        try{

            if (redisUtil.existData(email)) {
                redisUtil.deleteData(email);
            }
            // 이메일 폼 생성
            MimeMessage emailForm = (MimeMessage) createEmailForm(email).getData();
            // 이메일 발송
            try {
                javaMailSender.send(emailForm);
                System.out.println("Email sent successfully to: " + email);
            } catch (Exception e) {
                System.err.println("Failed to send email: " + e.getMessage());
                throw e;  // 예외를 다시 던져 호출자에게 알림
            }

            status = BaseResponseStatus.SUCCESS;
            return CMResponse.success(status.getCode(), status, null);
        } catch (Exception e) {
            status = BaseResponseStatus.FAIL_MAIL_SEND;
            return CMResponse.fail(status.getCode(), status, null);
        }
    }

    @Override
    public CMResponse verifyCode(String email, String code) {
        BaseResponseStatus status;
        try {
            log.info("Attempting to verify code for email: " + email); // 이메일 로그 추가
            String codeFoundByEmail = redisUtil.getData(email);
            log.info("Code found by email: " + codeFoundByEmail); // 코드 로그 추가
            log.info("Code: " + code); // 코드 로그 추가

            if (codeFoundByEmail == null) {
                status = BaseResponseStatus.UNMATCHED_EMAIL_CODE;
                return CMResponse.fail(status.getCode(), status, null);
            }

            if (!codeFoundByEmail.equals(code)) {
                status = BaseResponseStatus.INVALID_VERIFICATION_CODE;
                return CMResponse.fail(status.getCode(), status, null);
            }

            status = BaseResponseStatus.SUCCESS;
            return CMResponse.success(status.getCode(), status, null);
        } catch (Exception e) {
            log.error("Error during email verification: ", e);
            status = BaseResponseStatus.FAIL_MAIL_VERIFY;
            return CMResponse.fail(status.getCode(), status, null);
        }
    }

}
