package org.example;

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

public class TestClass extends TestCase {

    public static Service service;

    @BeforeEach
    public void setUp() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        NotaValidator notaValidator = new NotaValidator();

        StudentXMLRepository studentRepo = new StudentXMLRepository(studentValidator, "studenti.xml");
        TemaXMLRepository temaRepo = new TemaXMLRepository(temaValidator, "teme.xml");
        NotaXMLRepository notaRepo = new NotaXMLRepository(notaValidator, "note.xml");

        TestClass.service = new Service(studentRepo, temaRepo, notaRepo);
    }

    // White box testing
    @Test
    public void testAddTemaValidData() {
        String nrTema = "100";
        String descriere = "test";
        int deadline = 12;
        int primire = 1;
        try {
            service.saveTema(nrTema, descriere, deadline, primire);
            assert(true);
        } catch (ValidationException exception) {
            assert(false);
        }
    }

    @Test
    public void testAddTemaInvalidDataDuplicate() {
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
    public void testAddTemaInvalidDataEmptyStringId() {
        String nrTema = "";
        String descriere = "test";
        int deadline = 12;
        int primire = 2;
        try {
            service.saveTema(nrTema, descriere, deadline, primire);
        } catch (ValidationException exception) {
            assert(true);
        }
    }

    @Test
    public void testAddTemaInvalidDataNullId() {
        String nrTema = null;
        String descriere = "test";
        int deadline = 12;
        int primire = 2;
        try {
            service.saveTema(nrTema, descriere, deadline, primire);
        } catch (ValidationException exception) {
            assert(true);
        }
    }

    @Test
    public void testAddTemaInvalidDataEmptyStringDescriere() {

        String nrTema = "101";
        String descriere = "";
        int deadline = 12;
        int primire = 2;
        try{
            service.saveTema(nrTema, descriere, deadline, primire);
        }catch (ValidationException ve){
            assert(true);
        }
    }

    @Test
    public void testAddTemaInvalidDataNullDescriere() {

        String nrTema = "101";
        String descriere = null;
        int deadline = 12;
        int primire = 2;
        try{
            service.saveTema(nrTema, descriere, deadline, primire);
        }catch (ValidationException ve){
            assert(true);
        }
    }

    @Test
    public void testAddTemaInvalidDataDeadlineSmaller() {

        String nrTema = "102";
        String descriere = "test";
        int deadline = 0;
        int primire = 11;
        try{
            service.saveTema(nrTema, descriere, deadline, primire);
        }catch (ValidationException ve){
            assert(true);
        }
    }

    @Test
    public void testAddTemaInvalidDataDeadlineBigger() {

        String nrTema = "102";
        String descriere = "test";
        int deadline = 15;
        int primire = 11;
        try{
            service.saveTema(nrTema, descriere, deadline, primire);
        }catch (ValidationException ve){
            assert(true);

        }
    }

    @Test
    public void testAddTemaInvalidDataPrimireSmaller() {

        String nrTema = "103";
        String descriere = "test";
        int deadline = 12;
        int primire = 0;
        try{
            service.saveTema(nrTema, descriere, deadline, primire);
        }catch (ValidationException ve){
            assert(true);
        }
    }

    @Test
    public void testAddTemaInvalidDataPrimireBigger() {

        String nrTema = "103";
        String descriere = "test";
        int deadline = 12;
        int primire = 15;
        try{
            service.saveTema(nrTema, descriere, deadline, primire);
        }catch (ValidationException ve){
            assert(true);

        }
    }

    @Test
    public void testAddTemaInvalidPrimireBiggerThanDeadline() {

        String nrTema = "103";
        String descriere = "test";
        int deadline = 12;
        int primire = 14;
        try{
            service.saveTema(nrTema, descriere, deadline, primire);
        }catch (ValidationException ve){
            assert(true);

        }
    }

    //Black Box Testing
    @Test
    public void testAddStudentValidData() {
        Student student = new Student("test", "nume_1", 931);

        int initialLen = 0;

        for(Student s: service.findAllStudents()) {
            initialLen++;
        }

        try {
            service.saveStudent(student.getID(), student.getNume(), student.getGrupa());
        } catch (ValidationException e) {
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
    public void testAddStudentInvalidIDNull() {
        Student student = new Student(null, "nume_1", 931);

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
    public void testAddStudentInvalidNameEmpty() {
        Student student = new Student("test", "", 931);

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
    public void testAddStudentInvalidNameNull() {
        Student student = new Student("test", null, 931);

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
    public void testAddStudentInvalidGroupSmall() {
        Student student = new Student("test", "nume_1", 110);

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
    public void testAddStudentInvalidGroupBig() {
        Student student = new Student("test", "nume_1", 938);

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
}
