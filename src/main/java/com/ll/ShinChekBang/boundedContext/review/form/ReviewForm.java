package com.ll.ShinChekBang.boundedContext.review.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewForm {
    float rate = 1.0f;
    @NotNull
    @NotBlank
    @NotEmpty
    String content;
}
