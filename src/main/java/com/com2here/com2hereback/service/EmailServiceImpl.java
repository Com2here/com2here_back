package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.config.redis.RedisUtil;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.dto.EmailAuthReqDto;
import com.com2here.com2hereback.dto.ResetPasswordReqDto;
import com.com2here.com2hereback.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${mail.from-address}")
    private String fromAddress;

    @Override
    public String createCode() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 6;
        Random random = new Random();

        String code = random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

        return code;
    }

    @Override
    public String setEmail(String email, String code) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        Context context = new Context();
        context.setVariable("code", code);
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        String emailContent = templateEngine.process("codemail", context);

        return emailContent;
    }

    @Override
    public MimeMessage createEmailForm(String email) {
        // 인증 코드 생성
        String authCode = createCode();
        // 인증 코드로 이메일 내용 설정
        String emailContent = setEmail(email, authCode);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("컴히얼 인증코드 안내 메일");
            helper.setText(emailContent, true); 
            helper.setFrom(fromAddress, fromAddress);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 생성 중 오류 발생", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        redisUtil.setDataExpire(email, authCode, 60 * 30L);
        return message;
    }


    @Override
    public void sendEmail(EmailAuthReqDto emailAuthReqDto) {
        if (redisUtil.existData(emailAuthReqDto.getEmail())) {
            redisUtil.deleteData(emailAuthReqDto.getEmail());
        }
        MimeMessage emailForm = createEmailForm(emailAuthReqDto.getEmail());
        try {
            javaMailSender.send(emailForm);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("메일 전송 실패: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.FAIL_MAIL_SEND);
        }
    }

    @Override
    public void verifyCode(String email, String code) {
        String codeFoundByEmail = redisUtil.getData(email);
        if (codeFoundByEmail == null) {
            throw new BaseException(BaseResponseStatus.UNMATCHED_EMAIL_CODE);
        }

        if (!codeFoundByEmail.equals(code)) {
            throw new BaseException(BaseResponseStatus.INVALID_VERIFICATION_CODE);
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }
        user = User.builder()
            .userId(user.getUserId())
            .nickname(user.getNickname())
            .password(user.getPassword())
            .email(user.getEmail())
            .uuid(user.getUuid())
            .role(user.getRole())
            .refreshToken(user.getRefreshToken())
            .profileImageUrl(user.getProfileImageUrl())
            .isEmailVerified(true)
            .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordReqDto resetPasswordRequestDto) {
        verifyCode(resetPasswordRequestDto.getEmail(), resetPasswordRequestDto.getCode());

        if (!resetPasswordRequestDto.getPassword()
            .equals(resetPasswordRequestDto.getConfirmPassword())) {
            throw new BaseException(BaseResponseStatus.UNMATCHED_PASSWORD);
        }

        User user = userRepository.findByEmail(resetPasswordRequestDto.getEmail());
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        String hashedPassword = bCryptPasswordEncoder.encode(resetPasswordRequestDto.getPassword());

        user = User.builder()
            .userId(user.getUserId())
            .nickname(user.getNickname())
            .password(hashedPassword)
            .email(user.getEmail())
            .uuid(user.getUuid())
            .role(user.getRole())
            .refreshToken(user.getRefreshToken())
            .profileImageUrl(user.getProfileImageUrl())
            .isEmailVerified(user.isEmailVerified())
            .build();

        userRepository.save(user);

        redisUtil.deleteData(resetPasswordRequestDto.getEmail());
    }

}
