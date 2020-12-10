package org.wwu.dma.nosql.data;

import java.util.Date;

public class Relationship {
    private Person[] involvedPersons;
    private RelationshipType type;
    private Date activeSince;

    public Relationship(Person involvedPersonOne, Person involvedPersonTwo, RelationshipType type, Date activeSince) {
        this.involvedPersons = new Person[2];
        this.involvedPersons[0] = involvedPersonOne;
        this.involvedPersons[1] = involvedPersonTwo;
        this.type = type;
        this.activeSince = activeSince;
    }

    public Person[] getInvolvedPersons() {
        return involvedPersons;
    }

    public RelationshipType getType() {
        return type;
    }

    public Date getActiveSince() {
        return activeSince;
    }
}
