package org.exist;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.*;

public class ContactMenu {
    public ContactMenu(){
        initContact();
    }

    protected void initContact(){
        Scanner input = new Scanner(System.in);
        boolean loop = true;
        int choice;

        while(loop){
            System.out.println(" -- Contact Menu -- ");
            System.out.println("[1] Add Contact ");
            System.out.println("[2] Update Contact ");
            System.out.println("[3] Delete Contact ");
            System.out.println("[4] Back");
            System.out.println("Choice: ");
            choice = input.nextInt();

            switch (choice){
                case 1:
                    System.out.println(" -- Add Contact -- ");
                    addContact();
                    break;
                case 2:
                    System.out.println(" -- Update Contact -- ");
                    updateContact();
                    break;
                case 3:
                    System.out.println(" -- Delete Contact -- ");
                    deleteContact();
                    break;
                case 4:
                    loop = false;
                    break;
                default:
                    System.out.println("Invalid Input!");
            }
        }
    }

    // Utility methods
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

    protected List<Object> inputContact(){
        Scanner input = new Scanner(System.in);
        int choice;
        String type = null, data = null;
        boolean check = false;
        List<Object> returnList = new ArrayList<>();  // List of objects to return multiple types of data

        try {
            System.out.println("What type of contact would you like to add? [1]Landline [2]Mobile No. [3]Email: ");
            choice = input.nextInt(); input.nextLine();

            switch (choice){
                case 1:
                    check = true;
                    System.out.println("Enter Landline: ");
                    data = input.nextLine();
                    type = "Landline";
                    break;
                case 2:
                    check = true;
                    System.out.println("Enter Mobile No: ");
                    data = input.nextLine();
                    type = "Mobile No.";
                    break;
                case 3:
                    check = true;
                    System.out.println("Enter Email: ");
                    data = input.nextLine();
                    type = "Email";
                    break;
                default:
                    check = false;
            }
        } catch (InputMismatchException ime){
            System.out.println("Invalid Input!");
        } catch (Exception e){
            e.printStackTrace();
        }
        returnList.add(type);
        returnList.add(data);
        returnList.add(check);
        return returnList;
    }

    // Actual methods used for menu
    protected void addContact(){
        Scanner input = new Scanner(System.in);
        int pId;
        String type = null, data = null;
        boolean check = false;

        try {
            System.out.println("Enter Person ID to add Contact: ");
            pId = input.nextInt();
            Person p = getPerson(pId);   // gets the person entity from ID input

            List<Object> inputContactList = inputContact();  // calls inputContact to ask user for type of contact to add
            type = (String) inputContactList.get(0);
            data = (String) inputContactList.get(1);
            check = (boolean) inputContactList.get(2);

            if (check){
                Contact c = new Contact(type, data, p);  // creates new contact entity and assigns the input variables to its constructor params
                Set<Contact> contactSet = new HashSet<>(); // creates new set of contacts to store contact entity
                contactSet.add(c);
                p.setContacts(contactSet); // assigns the set of contacts to person entity

                HibernateUtil hbu = new HibernateUtil();
                Session session = hbu.createSession();
                try{
                    session.beginTransaction();
                    session.update(p);          // updates person entity
                    session.save(c);            // saves the contact entity to contact table
                    session.getTransaction().commit();
                } finally {
                    session.close();
                }
                System.out.println("Inserting data into DB");
            }
            else{
                System.out.println("Invalid Input!");
            }
        } catch (InputMismatchException ime){
            System.out.println("Invalid Input!");
        } catch (ObjectNotFoundException one){
            System.out.println("Person does not exist!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void updateContact(){
        Scanner input = new Scanner(System.in);
        int pId, cId;
        String type = null, data = null;
        boolean check = false;

        System.out.println("Enter Person ID to update Contact: ");
        pId = input.nextInt();

        HibernateUtil hbu = new HibernateUtil();
        Session session = hbu.createSession();
        try{
            Person p = getPerson(pId);          // loads the person entity by person ID
            System.out.println();
            listContact(pId);                   // prints all contacts associated with the current person entity

            System.out.println("Enter Contact ID of contact you want to update: ");
            cId = input.nextInt();
            try{
                session.beginTransaction();
                Contact c = session.load(Contact.class, cId);   // loads existing contact selected from the person
                session.getTransaction().commit();

                String fn = c.getPerson().getFname();
                String mn = c.getPerson().getMname();
                String ln = c.getPerson().getLname();

                if (c.getPerson().getUserID() != pId){  // ensures that the contact ID selected is part of the person entity entered earlier
                    System.out.println("This contact does not exist for the person entered!");
                }
                else {
                    System.out.println("Contact loaded: " + c.getContactId());
                    System.out.println("Person: " + fn + " " + mn + " " + ln);


                    List<Object> inputContactList = inputContact(); // calls inputContact to get contact input from user
                    type = (String) inputContactList.get(0);
                    data = (String) inputContactList.get(1);
                    check = (boolean) inputContactList.get(2);

                    if (check){
                        session.beginTransaction();
                        c.setContactId(cId);       // sets contact ID to current to make sure it stays the same throughout update process
                        c.setType(type);           // updates type
                        c.setData(data);           // updates data

                        session.update(c);         // updates contact entity on the DB table
                        session.getTransaction().commit();
                        System.out.println("Contact successfully updated!");
                    }
                    else {
                        System.out.println("Invalid Input!");
                    }
                }
            } catch (InputMismatchException ime){
                System.out.println("Invalid Input!");
            } catch (ObjectNotFoundException one){
                System.out.println("Contact does not exist!");
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (InputMismatchException ime){
            System.out.println("Invalid Input!");
        } catch (ObjectNotFoundException one){
            System.out.println("Person does not exist!");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    protected void listContact(int personId){
        HibernateUtil hbu = new HibernateUtil();
        Session session = hbu.createSession();
        List<Contact> contactList;

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Contact where personid = " + personId + " order by contactid asc");
            contactList = query.getResultList();
            session.getTransaction().commit();
        } finally {
            session.close();
        }

        System.out.println("Listing all Contacts for PersonID: " + personId);
        contactList.forEach(Contact -> {System.out.println("Contact ID: " + Contact.getContactId());
            System.out.println("Contact Type: " + Contact.getType());
            System.out.println("Data: " + Contact.getData());
            System.out.println();
        });
        System.out.println("------------------");
    }

    protected void deleteContact(){
        Scanner input = new Scanner(System.in);
        int pId, cId;

        HibernateUtil hbu = new HibernateUtil();
        Session session = hbu.createSession();
        try{
            System.out.println("Enter Person ID to delete Contact: ");
            pId = input.nextInt();
            System.out.println();
            listContact(pId);          // prints all contacts for entered person ID

            System.out.println("Enter Contact ID of contact you want to delete: ");
            cId = input.nextInt();
            try{
                session.beginTransaction();
                Contact c = session.load(Contact.class, cId);  // loads contact based on ID input
                session.getTransaction().commit();

                if (c.getPerson().getUserID() != pId){  // ensures that the contact ID selected is part of the person entity entered earlier
                    System.out.println("This contact does not exist for the person entered!");
                }
                else {
                    session.beginTransaction();
                    session.createNativeQuery("delete from contact where contactid = " + cId).executeUpdate();
                    session.getTransaction().commit();
                    System.out.println("Contact " + cId + " successfully deleted.");
                }
            } catch (InputMismatchException ime){
                System.out.println("Invalid Input!");
            } catch (ObjectNotFoundException one){
                System.out.println("Contact does not exist!");
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (InputMismatchException ime){
            System.out.println("Invalid Input!");
        } catch (ObjectNotFoundException one){
            System.out.println("Person does not exist!");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
