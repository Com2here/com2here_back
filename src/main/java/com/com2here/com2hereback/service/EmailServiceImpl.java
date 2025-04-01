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
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "lysfox01@gmail.com";
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public CMResponse createCode() {
        BaseResponseStatus status;
        try {

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

            return CMResponse.success(status.getCode(), status, code);
        } catch (Exception e) {
            status = BaseResponseStatus.FAIL_CODE_CREATE;
            return CMResponse.fail(status.getCode(), status, null);
        }
    }

    /* mail -> codemail(template수정) */
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
            String setEmail = templateEngine.process("codemail", context);

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
            String emailContent = (String) setEmail(email, authCode).getData(); // authCode를 여기에서 전달해야 합니다.

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
        try {

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
                throw e; // 예외를 다시 던져 호출자에게 알림
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
    public CMResponse sendCodeEmail(String email) {
        BaseResponseStatus status;
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                status = BaseResponseStatus.NO_EXIST_MEMBERS;
                return CMResponse.fail(status.getCode(), status, null);
            }
            // 인증 코드 생성
            String code = (String) createCode().getData();

            // Redis에 인증 코드 저장 (유효기간 30분)
            redisUtil.setDataExpire(email, code, 60 * 30L);

            // 이메일 템플릿 준비
            Context context = new Context();
            context.setVariable("code", code); // 인증 코드를 템플릿에 전달
            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("templates/");
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateResolver.setCacheable(false);

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            // 템플릿 처리
            String emailContent = templateEngine.process("codemail", context);

            // 이메일 발송
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipients(RecipientType.TO, email);
            message.setSubject("인증코드 안내 메일");
            message.setFrom(senderEmail);
            message.setContent(emailContent, "text/html; charset=utf-8");

            // 이메일 발송
            javaMailSender.send(message);

            status = BaseResponseStatus.SUCCESS;
            return CMResponse.success(status.getCode(), status, code);
        } catch (Exception e) {
            status = BaseResponseStatus.FAIL_MAIL_SEND;
            return CMResponse.fail(status.getCode(), status, null);
        }
    }

    /* 동혁's code */
    // @Override
    // public CMResponse verifyCode(String email, String code) {
    // BaseResponseStatus status;
    // try {
    // String codeFoundByEmail = redisUtil.getData(email);

    // if (codeFoundByEmail == null) {
    // status = BaseResponseStatus.UNMATCHED_EMAIL_CODE;
    // return CMResponse.fail(status.getCode(), status, null);
    // }

    // if (!codeFoundByEmail.equals(code)) {
    // status = BaseResponseStatus.INVALID_VERIFICATION_CODE;
    // return CMResponse.fail(status.getCode(), status, null);
    // }
    // User user = userRepository.findByEmail(email);
    // if (user == null) {
    // status = BaseResponseStatus.NO_EXIST_MEMBERS;
    // return CMResponse.fail(status.getCode(), status, null);
    // }
    // user = User.builder()
    // .user_id(user.getUser_id())
    // .username(user.getUsername())
    // .password(user.getPassword())
    // .email(user.getEmail())
    // .uuid(user.getUuid())
    // .isEmailVerified(true)
    // .build();

    // userRepository.save(user);

    // status = BaseResponseStatus.SUCCESS;
    // return CMResponse.success(status.getCode(), status, null);
    // } catch (Exception e) {
    // log.error("Error during email verification: ", e);
    // status = BaseResponseStatus.FAIL_MAIL_VERIFY;
    // return CMResponse.fail(status.getCode(), status, null);
    // }
    // }

    /* 인증 code로 메일 인증하는 형식으로 수정 */
    @Override
    public CMResponse verifyCode(String email, String code) {
        try {
            String codeFoundByEmail = redisUtil.getData(email);

            if (codeFoundByEmail == null) {
                // 인증 코드가 Redis에서 찾을 수 없는 경우
                return CMResponse.fail(BaseResponseStatus.UNMATCHED_EMAIL_CODE.getCode(),
                        BaseResponseStatus.UNMATCHED_EMAIL_CODE, null);
            }

            if (!codeFoundByEmail.equals(code)) {
                // 인증 코드가 일치하지 않는 경우
                return CMResponse.fail(BaseResponseStatus.INVALID_VERIFICATION_CODE.getCode(),
                        BaseResponseStatus.INVALID_VERIFICATION_CODE, null);
            }

            User user = userRepository.findByEmail(email);
            if (user == null) {
                // 사용자 정보가 없을 경우
                return CMResponse.fail(BaseResponseStatus.NO_EXIST_MEMBERS.getCode(),
                        BaseResponseStatus.NO_EXIST_MEMBERS, null);
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

            // 인증 성공 시
            return CMResponse.success(BaseResponseStatus.SUCCESS.getCode(), BaseResponseStatus.SUCCESS, null);
        } catch (Exception e) {
            log.error("Error during email verification: ", e);
            // 예외 발생 시 실패 처리
            return CMResponse.fail(BaseResponseStatus.FAIL_MAIL_VERIFY.getCode(), BaseResponseStatus.FAIL_MAIL_VERIFY,
                    null);
        }
    }

    @Override
    @Transactional
    public CMResponse resetPassword(String email, String code, String newPassword, String confirmPassword) {
        BaseResponseStatus status;

        try {
            // 인증 코드 검증
            CMResponse verificationResponse = verifyCode(email, code);
            if (verificationResponse.getCode() != BaseResponseStatus.SUCCESS.getCode()) {
                status = BaseResponseStatus.INVALID_VERIFICATION_CODE;
                return CMResponse.fail(status.getCode(), status, null);
            }

            if (!newPassword.equals(confirmPassword)) {
                status = BaseResponseStatus.PASSWORD_MISMATCH;
                return CMResponse.fail(status.getCode(), status, null);
            }

            User user = userRepository.findByEmail(email);
            if (user == null) {
                status = BaseResponseStatus.NO_EXIST_MEMBERS;
                return CMResponse.fail(status.getCode(), status, null);
            }

            // 비밀번호 해싱
            String hashedPassword = bCryptPasswordEncoder.encode(newPassword);

            user = User.builder()
                    .user_id(user.getUser_id())
                    .username(user.getUsername())
                    .password(hashedPassword)
                    .email(user.getEmail())
                    .uuid(user.getUuid())
                    .isEmailVerified(user.isEmailVerified())
                    .build();

            userRepository.save(user);

            redisUtil.deleteData(email);

            status = BaseResponseStatus.SUCCESS;
            return CMResponse.success(status.getCode(), status, null);
        } catch (Exception e) {
            log.error("Error during password reset: ", e);
            status = BaseResponseStatus.FAIL_PASSWORD_RESET;
            return CMResponse.fail(status.getCode(), status, null);
        }
    }

}
