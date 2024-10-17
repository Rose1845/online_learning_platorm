package com.rose.online_learning_platform.handlers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EMErrorBuilder {
    private List<String> fields;
    private List<String> errors;
    private List<String> codes;
}
