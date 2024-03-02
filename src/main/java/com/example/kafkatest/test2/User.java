package com.example.kafkatest.test2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LuoXianchao
 * @since 2024/03/02 14:19
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {

    private String name;

    private Integer age;

}
