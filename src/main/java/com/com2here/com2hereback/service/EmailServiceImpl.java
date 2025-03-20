package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.config.redis.RedisUtil;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipients(RecipientType.TO, email);
            message.setSubject("컴히얼 인증 메일");
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
    @Transactional
    public CMResponse sendPasswordEmail(String email) {
        BaseResponseStatus status;
        try {
            String newPassword = (String) createCode().getData();
            User user = userRepository.findByEmail(email);

            // 2700 : 이메일 인증이 되지않은 계정
            if(user.isEmailVerified() == false){
                status = BaseResponseStatus.NOT_EMAIL_VERIFY;
                return CMResponse.fail(status.getCode(),status,null);
            }
            user = User.builder()
                .user_id(user.getUser_id())
                .username(user.getUsername())
                .password(bCryptPasswordEncoder.encode(newPassword))
                .email(user.getEmail())
                .uuid(user.getUuid())
                .isEmailVerified(user.isEmailVerified())
                .build();

            userRepository.save(user);

            // 이메일 템플릿 준비
            Context context = new Context();
            context.setVariable("password", newPassword); // 비밀번호를 템플릿에 전달
            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("templates/");
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateResolver.setCacheable(false);

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            // 템플릿 처리
            String emailContent = templateEngine.process("password", context);

            // 이메일 발송
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipients(RecipientType.TO, email);
            message.setSubject("비밀번호 안내 메일");
            message.setFrom(senderEmail);
            message.setContent(emailContent, "text/html; charset=utf-8");

            // 이메일 발송
            javaMailSender.send(message);

            status = BaseResponseStatus.SUCCESS;
            return CMResponse.success(status.getCode(), status, newPassword);
        } catch (Exception e) {
            status = BaseResponseStatus.FAIL_MAIL_SEND;
            return CMResponse.fail(status.getCode(), status, null);
        }
    }


    @Override
    public CMResponse verifyCode(String email, String code) {
        BaseResponseStatus status;
        try {
            String codeFoundByEmail = redisUtil.getData(email);

            if (codeFoundByEmail == null) {
                status = BaseResponseStatus.UNMATCHED_EMAIL_CODE;
                return CMResponse.fail(status.getCode(), status, null);
            }

            if (!codeFoundByEmail.equals(code)) {
                status = BaseResponseStatus.INVALID_VERIFICATION_CODE;
                return CMResponse.fail(status.getCode(), status, null);
            }
            User user = userRepository.findByEmail(email);
            if (user == null) {
                status = BaseResponseStatus.NO_EXIST_MEMBERS;
                return CMResponse.fail(status.getCode(), status, null);
            }
            user = User.builder()
                .user_id(user.getUser_id())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .uuid(user.getUuid())
                .isEmailVerified(true)
                .build();

            userRepository.save(user);

            status = BaseResponseStatus.SUCCESS;
            return CMResponse.success(status.getCode(), status, null);
        } catch (Exception e) {
            log.error("Error during email verification: ", e);
            status = BaseResponseStatus.FAIL_MAIL_VERIFY;
            return CMResponse.fail(status.getCode(), status, null);
        }
    }

}
