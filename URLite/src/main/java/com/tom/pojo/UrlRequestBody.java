package com.tom.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UrlRequestBody {

    @NotBlank
    @URL
    private String target_url;

    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Alias can only contain letters, numbers, _ and -, 3-30 length.")
    private String alias;

    private String title;
}
