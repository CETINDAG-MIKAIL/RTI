package be.hepl.model.tests;

import be.hepl.model.dao.EmployeeDAO;
import be.hepl.model.entity.Employee;
import be.hepl.model.viewmodel.EmployeeSearchVM;

import java.util.ArrayList;

public class TestEmployees {
    public static void main(String[] args) {
        EmployeeDAO dao = new EmployeeDAO();

        Employee employee = new Employee();
        employee.setId(null);
        employee.setLogin("luke");
        employee.setPassword("azerty");

        dao.save(employee);
        int id = employee.getId();
        System.out.println("Ajout réussi : id = " + id);

        System.out.println("\n--- Tous les employees ---");
        ArrayList<Employee> employees = dao.read();
        for(Employee e : employees) {
            System.out.println(e);
        }

        System.out.println("\n--- SELECT BY ID ---");
        dao.read();
        Employee employee2 = dao.getById(id);
        System.out.println(employee2);

        System.out.println("\n--- SELECT sur le login ---");
        EmployeeSearchVM vm = new EmployeeSearchVM();
        vm.setLogin("luke");
        employees = dao.read(vm);
        for(Employee e : employees) {
            System.out.println(e);
        }

        System.out.println("\n--- SELECT sur le login et mdp ---");
        vm.setLogin("luke");
        vm.setPassword("azerty");
        employees = dao.read(vm);
        for(Employee e : employees) {
            System.out.println(e);
        }

        System.out.println("\n--- DELETE sur l'id ---");
        dao.delete(id);
        System.out.println("Employee avec id = " + id + " a été supprimé.");

        System.out.println("\n--- Tous les employees ---");
        employees = dao.read();
        for(Employee e : employees) {
            System.out.println(e);
        }
    }
}
