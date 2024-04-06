package com.liga.semin.server.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "phita_words")
public class PhitaWord {
    @Id
    String word;
}
