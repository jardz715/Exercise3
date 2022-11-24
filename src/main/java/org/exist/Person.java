package org.exist;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personid")
    private int userID;
    @Column(name = "firstname")
    private String fname;
    @Column(name = "middlename")
    private String mname;
    @Column(name = "lastname")
    private String lname;
    @Column(name = "address")
    private String address;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "gwa")
    private float gwa;
    @Column(name = "hiredate")
    private Date hireDate;
    @Column(name = "employed")
    private boolean employed;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Contact> contacts = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "person_role",
            joinColumns = @JoinColumn(name = "personid"),
            inverseJoinColumns = @JoinColumn(name = "roleid")
    )
    private Set<Role> personRoles = new HashSet<>();

    public Person(){}; // default constructor for org.exist.Person class

    public Person(String fname, String mname, String lname, String address, Date birthday, float gwa, Date hireDate, boolean employed) {
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.address = address;
        this.birthday = birthday;
        this.gwa = gwa;
        this.hireDate = hireDate;
        this.employed = employed;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public float getGwa() {
        return gwa;
    }

    public void setGwa(float gwa) {
        this.gwa = gwa;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public boolean isEmployed() {
        return employed;
    }

    public void setEmployed(boolean employed) {
        this.employed = employed;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public Set<Role> getPersonRoles() {
        return personRoles;
    }

    public void setPersonRoles(Set<Role> personRoles) {
        this.personRoles = personRoles;
    }

//    @Override
//    public String toString() {
//        return "Person{" +
//                "userID=" + userID +
//                ", fname='" + fname + '\'' +
//                ", mname='" + mname + '\'' +
//                ", lname='" + lname + '\'' +
//                ", address='" + address + '\'' +
//                ", birthday=" + birthday +
//                ", gwa=" + gwa +
//                ", hireDate=" + hireDate +
//                ", employed=" + employed +
//                ", contacts=" + contacts +
//                ", personRoles=" + personRoles +
//                '}';
//    }
}
