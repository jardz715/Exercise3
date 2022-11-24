package org.exist;

import org.hibernate.Session;

import javax.persistence.Query;
import java.util.*;

public class RoleMenu {
    public RoleMenu(){
        initRole();
    }

    protected void initRole(){
        Scanner input = new Scanner(System.in);
        boolean loop = true;
        int choice;

        while(loop){
            System.out.println(" -- Contact Menu -- ");
            System.out.println("[1] Add Role ");
            System.out.println("[2] Update Role ");
            System.out.println("[3] Delete Role ");
            System.out.println("[4] List Role ");
            System.out.println("[5] Back");
            System.out.println("Choice: ");
            choice = input.nextInt();

            switch (choice){
                case 1:
                    System.out.println(" -- Add Role -- ");
                    addRole();
                    break;
                case 2:
                    System.out.println(" -- Update Role -- ");
                    updateRole();
                    break;
                case 3:
                    System.out.println(" -- Delete Role -- ");
                    deleteRole();
                    break;
                case 4:
                    System.out.println(" -- List Role -- ");
                    listRole();
                    break;
                case 5:
                    loop = false;
                    break;
                default:
                    System.out.println("Invalid Input!");
            }
        }
    }

    protected boolean roleExists(String roleData){
        boolean exists = false;
        HibernateUtil hbu = new HibernateUtil();
        Session session = hbu.createSession();
        session.beginTransaction();
        Query query = session.createNativeQuery("SELECT FROM Role WHERE roledata = :role");
        query.setParameter("role", roleData);
        exists = query.getResultList().size() >= 1;
        session.getTransaction().commit();
        session.close();
        System.out.println("Exist: " + exists);
        return exists;
    }

    protected void addRole(){
        Scanner input = new Scanner(System.in);
        String roleData = null;

        try{
            System.out.println("What role do you want to add? ");
            roleData = input.nextLine();

            // Calls roleExists method to check if role already exists
            if (!roleExists(roleData)){
                //Create new role and assign data to it
                Role r = new Role();
                r.setRoledata(roleData);

                HibernateUtil hbu = new HibernateUtil();
                Session session = hbu.createSession();
                try{
                    session.beginTransaction();
                    session.save(r);
                    session.getTransaction().commit();
                } finally {
                    session.close();
                }
                System.out.println("Inserting data into DB");
            }
            else {
                System.out.println("Role already exists!");
            }
        }   catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void updateRole(){
        Scanner input = new Scanner(System.in);
        int rId;
        String newRole;

        System.out.println("Enter Role ID to update role: ");
        rId = input.nextInt(); input.nextLine();

        HibernateUtil hbu = new HibernateUtil();
        Session session = hbu.createSession();
        try{
            session.beginTransaction();
            Role r = session.get(Role.class, rId);  // retrieves role based on ID input
            session.getTransaction().commit();

            if (r.equals(null)){                    // makes sure to throw a null exception when role does not exist since hibernate does not seem to automatically do it
                throw new NullPointerException();
            }
            else {
                System.out.println("Role Selected: " + r.getRoledata());
                System.out.println("What would you like to update the role with? : ");
                newRole = input.nextLine();

                if (!roleExists(newRole)){   // calls roleExist method to check if new role is already existing
                    session.beginTransaction();
                    r.setRoleid(rId);        // sets role ID to existing one to make sure it stays the same throughout the query process
                    r.setRoledata(newRole);  // sets new role data
                    session.update(r);       // updates role entity
                    session.getTransaction().commit();
                    System.out.println("Role successfully updated!");
                }
                else {
                    System.out.println("Role already exists!");
                }
            }
        } catch (InputMismatchException ime){
            System.out.println("Invalid Input!");
        } catch (NullPointerException npe){
            System.out.println("Role does not exist!");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    protected void deleteRole(){
        Scanner input = new Scanner(System.in);
        int rId;

        try{
            System.out.println("Enter Role ID to delete Role: ");
            rId = input.nextInt();

            HibernateUtil hbu = new HibernateUtil();
            Session session = hbu.createSession();

            session.beginTransaction();
            Role r = session.get(Role.class, rId);   // retrieves role based on ID input
            session.getTransaction().commit();

            if (r.equals(null)){     // makes sure to throw a null exception when role does not exist since hibernate does not seem to automatically do it
                throw new NullPointerException();
            } else {
                try{
                    session.beginTransaction();    // deletes the association between selected role and persons with that role
                    session.createNativeQuery("delete from person_role where roleid = " + rId).executeUpdate();
                    session.createNativeQuery("delete from role where roleid = " + rId).executeUpdate();
                    session.getTransaction().commit();
                    System.out.println("Role " + rId + " successfully deleted.");
                }
                finally {
                    session.close();
                }
            }
        } catch (InputMismatchException ime){
            System.out.println("Invalid Input!");
        } catch (NullPointerException npe){
            System.out.println("Role does not exist!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void listRole(){
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

        System.out.println("Listing by Role ID(Ascending): ");
        roleList.forEach(Role -> {
            System.out.println("Role ID: " + Role.getRoleid());
            System.out.println("Role: " + Role.getRoledata());
            System.out.println();
        });
        System.out.println("--------------------");

    }


}
