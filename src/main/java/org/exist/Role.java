package org.exist;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Proxy(lazy = false)  // used because lazy fetching for this class throws a proxy exception
@Table(name = "Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleid")
    private int roleid;

    @Column(name = "roledata")
    private String roledata;

    @ManyToMany(mappedBy = "personRoles", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<Person> persons = new HashSet<>();

    public Role(){}

    public Role(String roledata, Set<Person> persons) {
        this.roledata = roledata;
        this.persons = persons;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getRoledata() {
        return roledata;
    }

    public void setRoledata(String roledata) {
        this.roledata = roledata;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleid=" + roleid +
                ", roledata='" + roledata + '\'' +
                ", persons=" + persons +
                '}';
    }
}
