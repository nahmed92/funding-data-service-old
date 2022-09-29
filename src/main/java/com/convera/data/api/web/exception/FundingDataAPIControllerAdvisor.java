package com.convera.data.api.web.exception;

import com.convera.common.template.CommonResponse;
import com.convera.common.template.response.error.constants.ResponseErrorCode400;
import com.convera.common.template.response.error.constants.ResponseErrorCode404;
import com.convera.common.template.response.util.CommonResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;

@ControllerAdvice
public class FundingDataAPIControllerAdvisor {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest req)
    {

        return ResponseEntity.badRequest().body(CommonResponseUtil.createResponse400(null,req.getContextPath(),req.getHeader("correlationID"),
                Collections.singletonList(ResponseErrorCode400.ERR_40001.build("funding", ex.getMessage()))));
    }

}
