package org.exist;

import javax.persistence.*;

@Entity
@Table(name = "Contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contactid")
    private int contactId;
    @Column(name = "type") // could contain landline, mobile no, email
    private String type;
    @Column(name = "data")
    private String data;

    @ManyToOne
    @JoinColumn(name = "personid", nullable = false)
    private Person person;

    public Contact(){};

    public Contact(String type, String data, Person p){
        this.type = type;
        this.data = data;
        this.person = p;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

//    @Override
//    public String toString() {
//        return "Contact{" +
//                "contactId=" + contactId +
//                ", type='" + type + '\'' +
//                ", data='" + data + '\'' +
//                ", person=" + person +
//                '}';
//    }
}
