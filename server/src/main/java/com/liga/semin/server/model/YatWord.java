package com.liga.semin.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "yat_words")
public class YatWord {
    @Id
    String word;
}
