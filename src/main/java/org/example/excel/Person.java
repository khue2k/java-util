package org.example.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private  Integer id;
    private String name;
    private String address;
    private String numberPhone;
}
