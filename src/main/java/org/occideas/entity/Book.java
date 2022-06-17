package org.occideas.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOOK")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private LocalDateTime lastUpdated;
    private String createdBy;

    public Book() {
    }

    public Book(long id, String name, LocalDateTime lastUpdated, String createdBy) {
        this.id = id;
        this.name = name;
        this.lastUpdated = lastUpdated;
        this.createdBy = createdBy;
    }

    public Book(String name, LocalDateTime lastUpdated, String createdBy) {
        this.name = name;
        this.lastUpdated = lastUpdated;
        this.createdBy = createdBy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
