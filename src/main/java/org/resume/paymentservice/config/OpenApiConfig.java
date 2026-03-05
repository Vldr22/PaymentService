package org.resume.paymentservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Payment Service API")
                        .description("""
                                Сервис для приёма и управления платежами через Stripe.
                                
                                ---
                                
                                **Аутентификация**
                                * Клиенты - SMS верификация → JWT Bearer токен
                                * Сотрудники и администраторы - email + пароль → JWT Bearer токен
                                * Все защищённые endpoints принимают заголовок `Authorization: Bearer <token>`
                                
                                ---
                                
                                **Уровни доступа**
                                * `ROLE_USER` - клиент, работает со своими картами, платежами и подписками
                                * `ROLE_EMPLOYEE` - сотрудник поддержки, обрабатывает заявки на возврат
                                * `ROLE_ADMIN` - администратор, управляет сотрудниками

                                ---
                                **Формат ответов**
                                
                                Все ошибки возвращаются в едином формате `CommonResponse` с полем `problemDetail`.
                                Успешный ответ:
                                ```json
                                {
                                  "data": {},
                                  "status": "SUCCESS",
                                  "timestamp": "2026-03-04T23:32:00.659547114"
                                }
                                ```
                                Ответ с ошибкой:
                                ```json
                                {
                                  "status": "ERROR",
                                  "problemDetail": {
                                    "type": "about:blank",
                                    "title": "Unauthorized",
                                    "status": 401,
                                    "detail": "Invalid email or password"
                                  },
                                  "timestamp": "2026-03-04T23:32:00.659547114"
                                }
                                ```
                                ---
                                **Ошибки**
                                * `400` — ошибка валидации или бизнес-логики, поле `errors` содержит список проблемных полей
                                * `401` — токен отсутствует, истёк или в blacklist
                                * `403` — недостаточно прав
                                * `404` — ресурс не найден
                                * `409` — конфликт данных (дубликат телефона, активная подписка и т.д.)
                                * `502` — ошибка на стороне Stripe, попробуйте позже
                                * `500` — внутренняя ошибка сервера
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Vladimir — GitHub")
                                .url("https://github.com/Vldr22/PaymentService")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT токен, полученный через SMS верификацию для клиента или через email + пароль для сотрудника")))
                .tags(List.of(
                        new Tag().name("Auth").description("Регистрация и SMS верификация клиентов, вход сотрудников, выход"),
                        new Tag().name("Payments").description("Создание, подтверждение и возврат Stripe платежей"),
                        new Tag().name("Cards").description("Управление сохранёнными платёжными методами"),
                        new Tag().name("Subscriptions").description("Создание и управление подписками, история списаний"),
                        new Tag().name("Staff").description("Управление аккаунтом сотрудников (смена пароля)"),
                        new Tag().name("Support").description("Обработка заявок на возврат средств"),
                        new Tag().name("Admin").description("Регистрация и управление сотрудниками — только для администратора"),
                        new Tag().name("Webhook").description("Обработка входящих событий от Stripe")
                ));
    }

}
