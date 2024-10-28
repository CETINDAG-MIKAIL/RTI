package be.hepl.model.tests;

import be.hepl.model.dao.ClientDAO;
import be.hepl.model.dao.SubjectDAO;
import be.hepl.model.entity.Client;
import be.hepl.model.entity.Subject;

public class TestSubjects {
    public static void main(String[] args) {
        SubjectDAO dao = new SubjectDAO();

        Subject subject = new Subject();
        subject.setId(null);
        subject.setName("Romance");

        dao.save(subject);
        int id = subject.getId();
        System.out.println("Ajout réussi : id = " + id);

        System.out.println("--- SELECT BY ID ---");
        dao.load();
        Subject subject2 = dao.getById(id);
        System.out.println(subject2);
    }
}
