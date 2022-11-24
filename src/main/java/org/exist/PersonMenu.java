package org.exist;

import org.hibernate.*;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PersonMenu {
    public PersonMenu() {
        initPerson();
    }

    protected void initPerson() {
        Scanner input = new Scanner(System.in);
        boolean loop = true;
        int choice;
        while (loop) {
            System.out.println(" -- Person Menu -- ");
            System.out.println("[1] Create Person ");
            System.out.println("[2] Delete Person ");
            System.out.println("[3] Update Person ");
            System.out.println("[4] List Person ");
            System.out.println("[5] Add Person Role");
            System.out.println("[6] Delete Person Role");
            System.out.println("[7] Back");
            System.out.println("Choice: ");
            choice = input.nextInt();

            switch (choice) {
                case 1:
                    System.out.println(" -- Create Person -- ");
                    createPerson();
                    break;
                case 2:
                    System.out.println(" -- Delete Person -- ");
                    deletePerson();
                    break;
                case 3:
                    System.out.println(" -- Update Person -- ");
                    updatePerson();
                    break;
                case 4:
                    System.out.println(" -- List Person -- ");
                    listPerson();
                    break;
                case 5:
                    System.out.println(" -- Add Person Role -- ");
                    addPersonRole();
                    break;
                case 6:
                    System.out.println(" -- Delete Person Role -- ");
                    deletePersonRole();
                    break;
                case 7:
                    loop = false;
                    break;
                default:
                    System.out.println("Invalid Input!");
            }
        }
    }

    // Utility methods
    private Date parseDate(String dateStr) throws ParseException {
        final DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date utilDate = formatter.parse(dateStr);
        return new java.sql.Date(utilDate.getTime());
    }

    private boolean validateJavaDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        // makes sure the string follows the date format
        sdf.setLenient(false);
        try {
            Date javaDate = sdf.parse(strDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    protected List<Object> inputPerson(){
        Scanner input = new Scanner(System.in);
        String inputVal;
        boolean check = false;
        Person p = new Person();
        List<Object> returnList = new ArrayList<>(); // list of objects to hold multiple return values

        // asks the user to enter again when the field is empty (applies to all input fields)
        try {
            while (true) {
                System.out.println("Enter Firstname: ");
                inputVal = input.nextLine();
                if (inputVal.isEmpty()) {
                    System.out.println("Field Empty!");
                    check = false;
                } else {
                    p.setFname(inputVal);
                    check = true;
                    break;
                }
            }

            while (true) {
                System.out.println("Enter Middlename: ");
                inputVal = input.nextLine();
                if (inputVal.isEmpty()) {
                    System.out.println("Field Empty!");
                    check = false;
                } else {
                    p.setMname(inputVal);
                    check = true;
                    break;
                }
            }

            while (true) {
                System.out.println("Enter Lastname: ");
                inputVal = input.nextLine();
                if (inputVal.isEmpty()) {
                    System.out.println("Field Empty!");
                    check = false;
                } else {
                    p.setLname(inputVal);
                    check = true;
                    break;
                }
            }

            while (true) {
                System.out.println("Enter Address: ");
                inputVal = input.nextLine();
                if (inputVal.isEmpty()) {
                    System.out.println("Field Empty!");
                    check = false;
                } else {
                    p.setAddress(inputVal);
                    check = true;
                    break;
                }
            }

            while (true) {
                System.out.println("Enter Birthday: ");
                inputVal = input.nextLine();
                if (inputVal.isEmpty()) {
                    System.out.println("Field Empty!");
                    check = false;
                } else {
                    if (!validateJavaDate(inputVal)) {
                        System.out.println("Incorrect Date Format!");
                    } else {
                        p.setBirthday(parseDate(inputVal)); // calls parsedate to convert string to date
                        check = true;
                        break;
                    }
                }
            }

            while (true) {
                System.out.println("Enter GWA: ");
                inputVal = input.nextLine();
                if (inputVal.isEmpty()) {
                    System.out.println("Field Empty!");
                    check = false;
                } else {
                    p.setGwa(Float.parseFloat(inputVal));
                    check = true;
                    break;
                }
            }

            while (true) {
                System.out.println("Enter hire date: ");
                inputVal = input.nextLine();
                if (inputVal.isEmpty()) {
                    System.out.println("Field Empty!");
                    check = false;
                } else {
                    if (!validateJavaDate(inputVal)) {
                        System.out.println("Incorrect Date Format!");
                    } else {
                        p.setHireDate(parseDate(inputVal));
                        check = true;
                        break;
                    }
                }
            }

            while (true) {
                System.out.println("Currently employed? [Y]/[N]: ");
                inputVal = input.nextLine();
                if (inputVal.isEmpty()) {
                    System.out.println("Field Empty!");
                    check = false;
                } else {
                    if (inputVal.equalsIgnoreCase("Y") || inputVal.equalsIgnoreCase("N")) {
                        if (inputVal.equalsIgnoreCase("Y")) {
                            p.setEmployed(true);
                        } else {
                            p.setEmployed(false);
                        }
                        break;
                    } else {
                        System.out.println("Invalid Input!");
                    }
                    check = true;
                }
            }
        } catch (InputMismatchException im) {  // catches inputmismatchexception from gwa field
            System.out.println("Please enter only floating point values for GWA. ");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        returnList.add(p);
        returnList.add(check);
        return returnList;     //returns person p and check for use in other methods
    }

    protected Person getPerson(int id){
        HibernateUtil hbu = new HibernateUtil();
        Session session = hbu.createSession();
        Transaction tx = session.beginTransaction();
        Person person = session.load(Person.class, id);
        System.out.println("Person loaded: " + person.getFname() + " " + person.getMname() + " " + person.getLname());
        tx.commit();
        session.close();
        return person;
    }

    protected Role getRole(int rID){
        HibernateUtil hbu = new HibernateUtil();
        Session session = hbu.createSession();
        Transaction tx = session.beginTransaction();
        Role role = session.load(Role.class, rID);
        System.out.println("Role loaded: " + role);
        tx.commit();
        session.close();
        return role;
    }

    protected void listAllRoles(){
        HibernateUtil hbu = new HibernateUtil();
        Session session = hbu.createSession();
        List<Role> roleList;

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Role order by roleid asc");
            roleList = query.getResultList();
            session.getTransaction().commit();
        } finally {
            session.close();
        }

        System.out.println("Listing all available roles: ");
        roleList.forEach(Role -> {
            System.out.println("Role ID: " + Role.getRoleid());
            System.out.println("Role: " + Role.getRoledata());
            System.out.println();
        });
        System.out.println("---------------------");
    }

    // Actual methods used in the menu
    protected void createPerson() {
        // retrieves input data from inputPerson method and assigns to local variable
        List<Object> inputPersonList = inputPerson();
        Person p = (Person) inputPersonList.get(0);
        boolean check = (boolean) inputPersonList.get(1);

        if (check){
            //insert it to DB
            HibernateUtil hbu = new HibernateUtil();
            Session session = hbu.createSession();
            try {
                session.beginTransaction();
                session.save(p);
                session.getTransaction().commit();
            } finally {
                session.close();
            }
            System.out.println("Inserting data into DB");
        }
        else {
            System.out.println("One or more fields have no input!");
        }
    }

    protected void deletePerson() {
        Scanner input = new Scanner(System.in);
        int keyVal;

        try {
            System.out.println("Enter ID value of person you want to remove: ");
            keyVal = input.nextInt();

            HibernateUtil hbu = new HibernateUtil();
            Session session = hbu.createSession();
            try {
                session.beginTransaction();
                Person p = new Person();       // creats new person entity and sets its ID to current ID
                p.setUserID(keyVal);
                session.createNativeQuery("delete from contact where personid = " + keyVal).executeUpdate();     // deletes all child rows associated with the current person being deleted
                session.createNativeQuery("delete from person_role where personid = " + keyVal).executeUpdate();
                session.delete(p);            // deletes the person entity itself
                session.getTransaction().commit();
            } finally {
                session.close();
            }
        } catch (PersistenceException pe) {  // catches the exception that happens when row does not exist
            System.out.println("Person does not exist!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updatePerson(){
        Scanner input = new Scanner(System.in);
        int keyVal;
        boolean check = false;
        Person p = new Person();

        System.out.println("Enter ID value of person you want to update: ");
        keyVal = input.nextInt();

        HibernateUtil hbu = new HibernateUtil();
        Session session = hbu.createSession();
        try{
            session.beginTransaction();                        // loads existing person tp update
            p = (Person) session.load(Person.class, keyVal);
            System.out.println("Person loaded: " + p);
            session.getTransaction().commit();

            List<Object> inputPersonList = inputPerson();     // retrieves input data from inputPerson method and assigns to local variable
            Person newPerson = (Person) inputPersonList.get(0);
            check = (boolean) inputPersonList.get(1);

            if (check){
                session.beginTransaction();    // replaces existing data with new data
                p.setUserID(keyVal);
                p.setFname(newPerson.getFname());
                p.setMname(newPerson.getMname());
                p.setLname(newPerson.getLname());
                p.setAddress(newPerson.getAddress());
                p.setBirthday(newPerson.getBirthday());
                p.setGwa(newPerson.getGwa());
                p.setHireDate(newPerson.getHireDate());
                p.setEmployed(newPerson.isEmployed());
                session.update(p);
                session.getTransaction().commit();
                System.out.println("Record successfully updated!");
            }
            else {
                System.out.println("One or more fields have no input!");
            }
        } catch (ObjectNotFoundException one){
            System.out.println("Person does not exist!");
        } finally {
            session.close();
        }
    }

    protected void listPerson() {
        Scanner input = new Scanner(System.in);
        int choice;
        boolean loop = true;

        try {
            while (loop){
                System.out.println("List all Person by [1]GWA [2]Date Hired [3] Last Name: ");
                choice = input.nextInt();

                switch (choice){
                    case 1:
                        listByGwa();
                        loop = false;
                        break;
                    case 2:
                        listByDatehired();
                        loop = false;
                        break;
                    case 3:
                        listByLastName();
                        loop = false;
                        break;
                    default:
                        System.out.println("Invalid Input!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void listByGwa() {
        HibernateUtil hbu = new HibernateUtil();
        Session session = hbu.createSession();
        List<Person> personList;
        DateFormat df = new SimpleDateFormat("MM/dd/yy");

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Person");
            personList = query.getResultList();
            session.getTransaction().commit();
        } finally {
            session.close();
        }

        personList.sort(Comparator.comparing(Person::getGwa).reversed()); //sorts personList by GWA

        System.out.println("Listing by GWA(Descending): ");  // prints all contents of personList sorted by GWA
        personList.forEach(Person -> {System.out.println("Person ID: " + Person.getUserID());
            System.out.println("First Name: " + Person.getFname());
            System.out.println("Middle Name: " + Person.getMname());
            System.out.println("Last Name: " + Person.getLname());
            System.out.println("Address: " + Person.getAddress());
            System.out.println("Birthday: " + df.format(Person.getBirthday()));
            System.out.println("GWA: " + Person.getGwa());
            System.out.println("Hire Date: " + df.format(Person.getHireDate()));
            System.out.println("Employed: " + ((Person.isEmployed() == true) ? "Yes" : "No")); // ternary operator to print yes/no instead of true/false
            System.out.println();
        });
        System.out.println("------------------------------");
    }

    protected void listByDatehired(){
        HibernateUtil hbu = new HibernateUtil();
        Session session = hbu.createSession();
        List<Person> personList;
        DateFormat df = new SimpleDateFormat("MM/dd/yy");

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Person order by hiredate asc");
            personList = query.getResultList();
            session.getTransaction().commit();
        } finally {
            session.close();
        }

        System.out.println("Listing by date hired(Ascending): ");
        personList.forEach(Person -> {System.out.println("Person ID: " + Person.getUserID());
            System.out.println("First Name: " + Person.getFname());
            System.out.println("Middle Name: " + Person.getMname());
            System.out.println("Last Name: " + Person.getLname());
            System.out.println("Address: " + Person.getAddress());
            System.out.println("Birthday: " + df.format(Person.getBirthday()));  // retrieves date and make sure it follows correct date format
            System.out.println("GWA: " + Person.getGwa());
            System.out.println("Hire Date: " + df.format(Person.getHireDate()));
            System.out.println("Employed: " + ((Person.isEmployed() == true) ? "Yes" : "No"));
            System.out.println();
        });
        System.out.println("------------------------------");
    }

    protected void listByLastName(){
        HibernateUtil hbu = new HibernateUtil();
        Session session = hbu.createSession();
        List<Person> personList;
        DateFormat df = new SimpleDateFormat("MM/dd/yy");

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Person order by lastname asc");
            personList = query.getResultList();
            session.getTransaction().commit();
        } finally {
            session.close();
        }

        System.out.println("Listing by Last Name(Ascending): ");
        personList.forEach(Person -> {System.out.println("Person ID: " + Person.getUserID());
            System.out.println("First Name: " + Person.getFname());
            System.out.println("Middle Name: " + Person.getMname());
            System.out.println("Last Name: " + Person.getLname());
            System.out.println("Address: " + Person.getAddress());
            System.out.println("Birthday: " + df.format(Person.getBirthday()));
            System.out.println("GWA: " + Person.getGwa());
            System.out.println("Hire Date: " + df.format(Person.getHireDate()));
            System.out.println("Employed: " + ((Person.isEmployed() == true) ? "Yes" : "No"));
            System.out.println();
        });
        System.out.println("------------------------------");
    }

    protected void addPersonRole(){
        Scanner input = new Scanner(System.in);
        int pId;
        int inputRole;

        try{
            System.out.println("Enter Person ID to add Role: ");
            pId = input.nextInt();
            Person p = getPerson(pId);                            // gets existing person to add role to

            HibernateUtil hbu = new HibernateUtil();
            Session session = hbu.createSession();

            System.out.println("Roles: " + p.getPersonRoles());  // prints all roles assigned to this person
            listAllRoles(); // prints all available roles

            try{
                System.out.println("What role would you like to assign for this person? (Use Role ID)");
                inputRole = input.nextInt();

                Role r = getRole(inputRole);  // retrieves role from input

                // adds a relation from role to person p
                p.getPersonRoles().add(r);

                // adds a relation from person to role r
                r.getPersons().add(p);

                session.beginTransaction();    // updates person entity and stores new assigned roles into person_role table
                session.update(p);
                session.save(r);
                session.getTransaction().commit();
                System.out.println("Inserting data into DB");

                // FSR, OBJECTNOTFOUNDException KEEPS APPEARING EVEN AFTER CATCH IS ADDED
            } catch (InputMismatchException ime){
                System.out.println("Invalid Input!");
            } catch (ObjectNotFoundException one){
                System.out.println("Role does not exist!");
            } catch (HibernateException he){
                System.out.println("Role is already assigned!");
            } finally {
                session.close();
            }
        } catch (InputMismatchException ime){
            System.out.println("Invalid Input!");
        } catch (ObjectNotFoundException one) {
            System.out.println("Person does not exist!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void deletePersonRole(){
        Scanner input = new Scanner(System.in);
        int pId;
        int inputRole;

        try{
            System.out.println("Enter person ID to remove role: ");
            pId = input.nextInt();
            Person p = getPerson(pId);

            HibernateUtil hbu = new HibernateUtil();
            Session session = hbu.createSession();

            Set<Role> roleSet = p.getPersonRoles();    // set to store all roles assigned to the current person entity
            List<Role> roleList = new ArrayList<>(roleSet);  // convert to list since set does not keep order of elements.
            roleList.sort(Comparator.comparing(Role::getRoleid)); // sort the list

            System.out.println("Roles Assigned: ");             // prints all roles
            roleList.forEach(Role -> {
                System.out.println("Role ID: " + Role.getRoleid());
                System.out.println("Role: " + Role.getRoledata());
                System.out.println();
            });

            try{
                System.out.println("What role would you like to remove for this person? (Use Role ID)");
                inputRole = input.nextInt();

                session.beginTransaction();
                int query = session.createNativeQuery("delete from person_role where personid = " + p.getUserID() + " and roleid = " +
                        inputRole).executeUpdate();
                session.getTransaction().commit();
                if (query == 0){   // if query returns 0 rows, then role does not exist
                    System.out.println("Role does not exist!");
                }
            } catch (InputMismatchException ime){
                System.out.println("Invalid Input!");
            } catch (ObjectNotFoundException one){
                System.out.println("Role does not exist!");
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                session.close();
            }
        } catch (InputMismatchException ime){
            System.out.println("Invalid Input!");
        } catch (ObjectNotFoundException one) {
            System.out.println("Person does not exist!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
