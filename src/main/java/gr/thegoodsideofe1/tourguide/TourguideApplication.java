package gr.thegoodsideofe1.tourguide;
import gr.thegoodsideofe1.tourguide.aes.AES_ENCRYPTION;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
@EnableWebMvc
public class TourguideApplication {
    public static void main(String[] args) {
        SpringApplication.run(TourguideApplication.class, args);
        System.out.println("  /==================================================================\\\n /   ----    ______   _________   _     ___      _   __    _   _____  \\\n {  | || |  | _____/  \\--- ---/  / \\    | |     | | |  \\  | | |  ___/  }\n {  | ___/  | _____      | |    / | \\   | |     | | |   \\ | |  \\ \\__   }\n {  | |     | _____|     | |   / ___ \\  | |     | | | | | | |   \\_  \\  }\n {  | |     | |____      | |  / /   \\ \\ | |____ | | |   / | |  ___/ /  }\n \\  \\_/     \\______|     \\_/  \\/     \\/ |_____/ |_| \\__/  |_|  \\___/  /\n  \\=================================================================/\n \n \n   &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n \n \n       /\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\                  ___________\\\n     <` ΤΡΙΑΙΝΑ ΤΟΥ ΕΠΑΜEΙΝΩΝΤΑ`>             _-\'   ____----/\n      `\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/`            _\'  .  /\n  ______________________________-----------/       \\_________\\\n /_________________________________________         ______----/\n                                           \\  `    /\n                                            \'_   . \\_    \n                                              `_     `\'\'-------\\\n                                                `\'-------------/");
    }
  
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("gr.thegoodsideofe1.tourguide"))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AES_ENCRYPTION aes_encryption() {
        AES_ENCRYPTION encryptionClass = new AES_ENCRYPTION();
        try {
            encryptionClass.init();
        } catch (Exception e){

        }
        return encryptionClass;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");;
            }
        };
    }
}
