package com.rose.online_learning_platform.responses;

import com.rose.online_learning_platform.commons.enums.ResponseStatusEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class GenericResponse<T> {
    private String message;
    private String debugMessage;
    private ResponseStatusEnum status;
    private Page page;
    private T data;
}
