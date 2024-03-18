package org.example;

import domain.Student;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

public class TestClass extends TestCase {

    public static Service service;

    @BeforeAll
    public static void setup() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        NotaValidator notaValidator = new NotaValidator();

        StudentXMLRepository studentRepo = new StudentXMLRepository(studentValidator, "studenti.xml");
        TemaXMLRepository temaRepo = new TemaXMLRepository(temaValidator, "teme.xml");
        NotaXMLRepository notaRepo = new NotaXMLRepository(notaValidator, "note.xml");

        TestClass.service = new Service(studentRepo, temaRepo, notaRepo);
    }

    @Test
    public void addStudentValidData() {
        Student student = new Student("test", "nume_1", 931);

        int initialLen = 0;

        for(Student s: service.findAllStudents()) {
            initialLen++;
        }

        try {
            service.saveStudent(student.getID(), student.getNume(), student.getGrupa());
        } catch (ValidationException e) {
            System.out.println(e);
            assertFalse(true);
        }

        int len = 0;

        for(Student s: service.findAllStudents()) {
            len++;
        }

        assert(initialLen + 1 == len);

        service.deleteStudent(student.getID());
    }

    @Test
    public void addStudentInvalidID() {
        Student student = new Student("", "nume_1", 931);

        int initialLen = 0;

        for(Student s: service.findAllStudents()) {
            initialLen++;
        }

        try {
            service.saveStudent(student.getID(), student.getNume(), student.getGrupa());
        } catch (Exception e) {
            System.out.println(e);
            assertFalse(true);
        }

        int len = 0;

        for(Student s: service.findAllStudents()) {
            len++;
        }

        assert(initialLen == len);
    }
}
