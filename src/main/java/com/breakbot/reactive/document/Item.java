package com.breakbot.reactive.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document //like @Entity
@Data
@AllArgsConstructor
public class Item {
    @Id
    private String id;
    private String description;
    private Double price;
}
