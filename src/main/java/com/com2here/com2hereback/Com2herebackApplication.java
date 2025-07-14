package com.com2here.com2hereback;

//import com.com2here.com2hereback.common.Role;
//import com.com2here.com2hereback.domain.User;
//import com.com2here.com2hereback.repository.UserRepository;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Com2herebackApplication {

	public static void main(String[] args) {
		SpringApplication.run(Com2herebackApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner initAdminUser(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
//		return args -> {
//			if (!userRepository.existsByEmail("admin@example.com")) {
//				userRepository.save(User.builder()
//						.uuid("admin-uuid-001")
//						.email("admin@example.com")
//						.nickname("관리자")
//						.password(bCryptPasswordEncoder.encode("admin123@"))
//						.role(Role.ADMIN)
//						.build());
//
//				System.out.println("관리자 계정 생성 완료 (admin@example.com / admin123@)");
//			}
//		};
//	}
}
