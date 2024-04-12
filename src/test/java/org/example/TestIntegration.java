package org.example;

import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

public class TestIntegration extends TestCase {

    public static Service service;

    @BeforeEach
    public void setUp() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        NotaValidator notaValidator = new NotaValidator();

        StudentXMLRepository studentRepo = new StudentXMLRepository(studentValidator, "studenti.xml");
        TemaXMLRepository temaRepo = new TemaXMLRepository(temaValidator, "teme.xml");
        NotaXMLRepository notaRepo = new NotaXMLRepository(notaValidator, "note.xml");

        TestIntegration.service = new Service(studentRepo, temaRepo, notaRepo);
    }

    // In Class
    @Test
    public void testAddAssignmentInvalidDataDuplicate() {
        String nrTema = "100";
        String descriere = "test";
        int deadline = 12;
        int primire = 1;
        try {
            int response = service.saveTema(nrTema, descriere, deadline, primire);
            assert(0 == response);
        } catch (ValidationException exception) {
            assert(true);
        }
    }

    @Test
    public void testAddStudentInvalidIDEmpty() {
        Student student = new Student("", "nume_1", 931);

        int initialLen = 0;

        for(Student s: service.findAllStudents()) {
            initialLen++;
        }

        try {
            service.saveStudent(student.getID(), student.getNume(), student.getGrupa());
        } catch (Exception e) {
            assertFalse(true);
        }

        int len = 0;

        for(Student s: service.findAllStudents()) {
            len++;
        }

        assert(initialLen == len);
    }

    @Test
    public void testAddNotaDuplicateData() {
        int initialLen = 0;

        for(Nota s: service.findAllNote()) {
            initialLen++;
        }

        try {
            service.saveNota("2", "100", 10.0, 12, "Good");
        } catch (ValidationException e) {
            assertFalse(true);
        }

        int len = 0;

        for(Nota s: service.findAllNote()) {
            len++;
        }
        assert(initialLen == len);

    }

    @Test
    public void testIntegrationAddStudentAssignmentGrade() {
        Student student = new Student("2", "Jane Doe", 932);
        Tema tema = new Tema("100", "Advanced Java", 15, 2);
        Nota nota = new Nota(new Pair("2", "101"), 9.5, 13, "Excellent work");

        assertEquals(0, service.saveStudent(student.getID(), student.getNume(), student.getGrupa()));

        assertEquals(1, service.saveTema(tema.getID(), tema.getDescriere(), tema.getDeadline(), tema.getStartline()));

        assertEquals(-1, service.saveNota(nota.getID().getObject1(), nota.getID().getObject2(), nota.getNota(), nota.getSaptamanaPredare(), nota.getFeedback()));
    }

    //Homework
    @Test
    public void testAddStudent() {
        Student student = new Student("101", "Gica Hagi", 1001);
        assertEquals(1, service.saveStudent(student.getID(), student.getNume(), student.getGrupa()));
    }

    @Test
    public void testAddAssignmentWithStudent() {
        Student student = new Student("20", "Beghe Ionut", 931);
        service.saveStudent(student.getID(), student.getNume(), student.getGrupa());

        Tema tema = new Tema("200", "Software Engineering", 15, 1);
        assertEquals(1, service.saveTema(tema.getID(), tema.getDescriere(), tema.getDeadline(), tema.getStartline()));
        service.deleteStudent("20");
    }

    @Test
    public void testAddGradeWithStudentAndAssignment() {
        Student student = new Student("20", "Beghe Ionut", 931);
        service.saveStudent(student.getID(), student.getNume(), student.getGrupa());

        Tema tema = new Tema("300", "Database Systems", 14, 1);
        service.saveTema(tema.getID(), tema.getDescriere(), tema.getDeadline(), tema.getStartline());

        Nota nota = new Nota(new Pair("2", "100"), 9.5, 12, "Very well done");
        assertEquals(0, service.saveNota(nota.getID().getObject1(), nota.getID().getObject2(), nota.getNota(), nota.getSaptamanaPredare(), nota.getFeedback()));
        service.deleteStudent("20");
        service.deleteTema("300");
    }
}
