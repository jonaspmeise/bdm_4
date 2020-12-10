package org.wwu.dma.nosql.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FriendsNetworkGenerator {
    private List<Person> persons;
    private List<Relationship> relationships;

    public FriendsNetworkGenerator() {
        this.persons = new ArrayList<Person>();
        this.relationships = new ArrayList<Relationship>();
        this.generateFriendsData();
    }

    public void generateFriendsData() {
        Person chandler = new Person(new Date(), "Chandler", "Bing");
        Person monica = new Person(new Date(), "Monica", "Geller");
        Person rachel = new Person(new Date(), "Rachel", "Green");
        Person ross = new Person(new Date(), "Ross", "Geller");
        Person joey = new Person(new Date(), "Joey", "Tribbiani");
        Person phoebe = new Person(new Date(), "Phoebe", "Buffay");

        this.persons.add(chandler);
        this.persons.add(monica);
        this.persons.add(rachel);
        this.persons.add(ross);
        this.persons.add(joey);
        this.persons.add(phoebe);

        this.relationships.add(new Relationship(chandler, monica, RelationshipType.FRIENDS, new Date()));
        this.relationships.add(new Relationship(ross, monica, RelationshipType.FAMILY, new Date()));
        this.relationships.add(new Relationship(ross, rachel, RelationshipType.FRIENDS, new Date()));
        this.relationships.add(new Relationship(phoebe, joey, RelationshipType.FRIENDS, new Date()));
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }
}
