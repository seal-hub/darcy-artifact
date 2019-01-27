package org.modules.base.domain;


import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by aleksandra on 05.10.17.
 */
public abstract class Auditable {

    private LocalDate creationDate = LocalDate.now();

    private LocalTime creationTime = LocalTime.now();

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalTime getCreationTime() {
        return creationTime;
    }
}
