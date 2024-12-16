package be.hepl.model.tests;

import be.hepl.model.dao.ClientDAO;
import be.hepl.model.dao.SubjectDAO;
import be.hepl.model.entity.Book;
import be.hepl.model.entity.Client;
import be.hepl.model.entity.Subject;
import be.hepl.model.viewmodel.SubjectSearchVM;

import java.util.ArrayList;

public class TestSubjects {
    public static void main(String[] args) {
        SubjectSearchVM vm = new SubjectSearchVM();
        SubjectDAO dao = new SubjectDAO();
        ArrayList<Subject> subjects;
        Subject subject = new Subject();
        subject.setId(null);
        subject.setName("Science-Fiction");

        dao.save(subject);
        int id = subject.getId();
        System.out.println("Ajout réussi : id = " + id);

        System.out.println("\n--- SELECT * ---");
        subjects = dao.read();
        for (Subject s : subjects) {
            System.out.println(s);
        }

        System.out.println("\n--- SELECT BY ID ---");
        vm.setId(id);
        subjects = dao.read(vm);
        System.out.println(subjects);

        System.out.println("\n--- SELECT BY name ---");
        vm.setId(null);
        vm.setName("Romance");
        System.out.println("\n--- vm ---" + vm);
        subjects = dao.read(vm);
        for (Subject s : subjects) {
            System.out.println(s);
        }

        System.out.println("\nSubject avec id = " + id + " a été supprimé.");
        dao.delete(id);

        System.out.println("\n--- SELECT * ---");
        subjects = dao.read();
        for (Subject s : subjects) {
            System.out.println(s);
        }
    }
}
