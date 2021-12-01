package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Movement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MovementDTO {
    private Long id;
    private LocalDateTime date;
    private String description;

    public MovementDTO() {}

    public MovementDTO(Movement movement) {
        this.id = movement.getId();
        this.date = movement.getDate();
        this.description = movement.getDescription();
    }

    public Long getId() { return id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getDate() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        String dtStr = date.format(dateFormat);
        return dtStr;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
