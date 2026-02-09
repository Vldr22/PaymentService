package org.resume.paymentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.resume.paymentservice.model.enums.CommonResponseStatus;
import org.springframework.http.ProblemDetail;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T>{

    private T data;
    private CommonResponseStatus status;
    private ProblemDetail problemDetail;
    private LocalDateTime timestamp;

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(
                data,
                CommonResponseStatus.SUCCESS,
                null,
                LocalDateTime.now());
    }

    public static <T> CommonResponse<T> error(ProblemDetail problemDetail) {
        return new CommonResponse<>(
                null,
                CommonResponseStatus.ERROR,
                problemDetail,
                LocalDateTime.now());
    }
}
