package com.meli.obterdiploma.service;

import com.meli.obterdiploma.exception.StudentNotFoundException;
import com.meli.obterdiploma.model.StudentDTO;
import com.meli.obterdiploma.model.SubjectDTO;
import com.meli.obterdiploma.repository.StudentDAO;
import com.meli.obterdiploma.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    StudentService studentService;

    @Mock
    StudentDAO studentDAO;

    @Mock
    StudentRepository studentRepository;

    @BeforeEach
    void setup() {
    }

    @Test
    void create() {
        List<SubjectDTO> listSubject = new ArrayList<>();
        listSubject.add(new SubjectDTO("Matemática", 10.0));
        listSubject.add(new SubjectDTO("Física", 10.0));

        StudentDTO newStudent = new StudentDTO(50L, "John Doe", "Message Doe", 0.0, listSubject);

        Mockito.when(studentDAO.findById(newStudent.getId())).thenReturn(newStudent);

        studentService.create(newStudent);
        StudentDTO studentResponse = studentService.read(newStudent.getId());

        assertEquals(studentResponse.getStudentName(), newStudent.getStudentName());
        assertEquals(studentResponse.getId(), newStudent.getId());
        assertEquals(studentResponse.getAverageScore(), newStudent.getAverageScore());
        assertEquals(studentResponse.getMessage(), newStudent.getMessage());
        assertEquals(studentResponse.getSubjects(), newStudent.getSubjects());

    }

    @Test
    void read_returnsStudentById_whenSuccess() {

        List<SubjectDTO> listSubject = new ArrayList<>();
        listSubject.add(new SubjectDTO("Matemática", 10.0));
        listSubject.add(new SubjectDTO("Física", 10.0));

        StudentDTO newStudent = new StudentDTO(50L, "John Doe", "Message Doe", 0.0, listSubject);

        Mockito.when(studentDAO.findById(newStudent.getId())).thenReturn(newStudent);

        StudentDTO studentResponse = studentService.read(newStudent.getId());

        assertEquals(studentResponse.getStudentName(), newStudent.getStudentName());
        assertEquals(studentResponse.getId(), newStudent.getId());
        assertEquals(studentResponse.getAverageScore(), newStudent.getAverageScore());
        assertEquals(studentResponse.getMessage(), newStudent.getMessage());
        assertEquals(studentResponse.getSubjects(), newStudent.getSubjects());

    }

    @Test
    void read_throwsStudentNotFoundException_whenFails() {
        Long inexistentId = 999L;

        BDDMockito.given(studentDAO.findById(inexistentId))
                .willThrow(new StudentNotFoundException(inexistentId));

        assertThrows(StudentNotFoundException.class, () -> studentService.read(inexistentId));

    }

    @Test
    void update_returnsUpdatedStudent_whenSuccess() {
        List<SubjectDTO> listSubject = new ArrayList<>();
        listSubject.add(new SubjectDTO("Matemática", 10.0));
        listSubject.add(new SubjectDTO("Física", 10.0));
        StudentDTO newStudent = new StudentDTO(50L, "John Doe", "Message Doe", 0.0, listSubject);
        StudentDTO newUpdatedStudent = new StudentDTO(50L, "John Updated", "Updated Message", 0.0, listSubject);

        studentService.create(newStudent);

        Mockito.when(studentDAO.findById(newStudent.getId())).thenReturn(newUpdatedStudent);

        studentService.update(newUpdatedStudent);
        StudentDTO returnNewUpdatedStudent = studentService.read(newUpdatedStudent.getId());

        assertEquals(returnNewUpdatedStudent.getStudentName(), newUpdatedStudent.getStudentName());
        assertEquals(returnNewUpdatedStudent.getId(), newUpdatedStudent.getId());
        assertEquals(returnNewUpdatedStudent.getAverageScore(), newUpdatedStudent.getAverageScore());
        assertEquals(returnNewUpdatedStudent.getMessage(), newUpdatedStudent.getMessage());
        assertEquals(returnNewUpdatedStudent.getSubjects(), newUpdatedStudent.getSubjects());
    }

    @Test
    void update_returnsNewStudent_whenUpdatingInexistentStudent() {
        List<SubjectDTO> listSubject = new ArrayList<>();
        listSubject.add(new SubjectDTO("Matemática", 10.0));
        listSubject.add(new SubjectDTO("Física", 10.0));
        StudentDTO newStudent = new StudentDTO(50L, "John Doe", "Message Doe", 0.0, listSubject);

        Mockito.when(studentDAO.findById(newStudent.getId())).thenReturn(newStudent);

        studentService.update(newStudent);
        StudentDTO returnNewStudent = studentService.read(newStudent.getId());

        assertEquals(returnNewStudent.getStudentName(), newStudent.getStudentName());
        assertEquals(returnNewStudent.getId(), newStudent.getId());
        assertEquals(returnNewStudent.getAverageScore(), newStudent.getAverageScore());
        assertEquals(returnNewStudent.getMessage(), newStudent.getMessage());
        assertEquals(returnNewStudent.getSubjects(), newStudent.getSubjects());
    }

    @Test
    void delete_throwsStudentNotFoundException_whenFetchingRemovedStudent() {
        List<SubjectDTO> listSubject = new ArrayList<>();
        listSubject.add(new SubjectDTO("Matemática", 10.0));
        listSubject.add(new SubjectDTO("Física", 10.0));
        StudentDTO newStudent = new StudentDTO(50L, "John Doe", "Message Doe", 0.0, listSubject);

        studentService.create(newStudent);

        BDDMockito.given(studentDAO.findById(newStudent.getId()))
                .willThrow(new StudentNotFoundException(newStudent.getId()));

        studentService.delete(newStudent.getId());

        assertThrows(StudentNotFoundException.class, () -> studentService.read(newStudent.getId()));
    }

    @Test
    void getAll_returnsSetWithAllStudents_whenSuccess() {
        List<SubjectDTO> listSubject = new ArrayList<>();
        listSubject.add(new SubjectDTO("Matemática", 10.0));
        listSubject.add(new SubjectDTO("Física", 10.0));
        StudentDTO newStudent1 = new StudentDTO(100L, "John Doe", "Message Doe", 0.0, listSubject);
        StudentDTO newStudent2 = new StudentDTO(101L, "Mary Doe", "Message Doe", 0.0, listSubject);
        StudentDTO newStudent3 = new StudentDTO(102L, "Juliet Doe", "Message Doe", 0.0, listSubject);
        Set studentSet = new HashSet();
        studentSet.add(newStudent1);
        studentSet.add(newStudent2);
        studentSet.add(newStudent3);

        Mockito.when(studentRepository.findAll()).thenReturn(studentSet);

        Set returnStudentSet = studentService.getAll();

        assertEquals(returnStudentSet, studentSet);

    }
}