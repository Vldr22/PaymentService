package org.resume.paymentservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.model.dto.CommonResponse;
import org.resume.paymentservice.model.dto.request.EmployeeRegistrationRequest;
import org.resume.paymentservice.model.dto.response.EmployeeResponse;
import org.resume.paymentservice.service.facade.AuthFacadeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AuthFacadeService authFacadeService;

    @PostMapping("/register-employee")
    public CommonResponse<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRegistrationRequest request) {

        EmployeeResponse response = authFacadeService.createEmployee(request);
        return CommonResponse.success(response);
    }

}
