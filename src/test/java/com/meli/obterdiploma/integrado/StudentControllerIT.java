package com.meli.obterdiploma.integrado;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.obterdiploma.exception.StudentNotFoundException;
import com.meli.obterdiploma.model.StudentDTO;
import com.meli.obterdiploma.model.SubjectDTO;
import com.meli.obterdiploma.repository.IStudentDAO;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IStudentDAO studentDAO;

    @BeforeEach
    void setup(){
        this.studentDAO.deleteAll();
    }

    @Test
    void registerStudent_returnOkStatus_whenSuccess() throws Exception {
        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        subjectDTOList.add(new SubjectDTO("Math", 10.00));
        subjectDTOList.add(new SubjectDTO("Physical Education", 10.00));
        subjectDTOList.add(new SubjectDTO("Chemistry", 10.00));

        Long newId = 1L;
        StudentDTO studentDTO = new StudentDTO(newId, "John Doe", "Blank", 0.00, subjectDTOList);

        String studentAsJson = new ObjectMapper().writeValueAsString(studentDTO);

        ResultActions response = mockMvc.perform(post("/student/registerStudent").contentType(MediaType.APPLICATION_JSON).content(studentAsJson));

        response.andExpect(status().isOk());
    }

    @Test
    void registerStudent_throwMethodArgumentNotValidException_whenStudentNameIsEmpty() throws Exception {
        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        subjectDTOList.add(new SubjectDTO("Math", 10.00));
        subjectDTOList.add(new SubjectDTO("Physical Education", 10.00));
        subjectDTOList.add(new SubjectDTO("Chemistry", 10.00));

        Long newId = 1L;
        StudentDTO studentDTO = new StudentDTO(newId, null, "Blank", 0.00, subjectDTOList);

        String studentAsJson = new ObjectMapper().writeValueAsString(studentDTO);


        ResultActions response = mockMvc.perform(post("/student/registerStudent").contentType(MediaType.APPLICATION_JSON).content(studentAsJson));

        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException
                ))
                .andExpect(jsonPath("$.description", CoreMatchers.is(String.format("O nome do aluno não pode ficar vazio."))));
    }

    @Test
    void registerStudent_throwMethodArgumentNotValidException_whenSubjectListIsEmpty() throws Exception {
        List<SubjectDTO> subjectDTOList = new ArrayList<>();

        Long newId = 1L;
        StudentDTO studentDTO = new StudentDTO(newId, "John Doe", "Blank", 0.00, subjectDTOList);

        String studentAsJson = new ObjectMapper().writeValueAsString(studentDTO);


        ResultActions response = mockMvc.perform(post("/student/registerStudent").contentType(MediaType.APPLICATION_JSON).content(studentAsJson));

        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException
                ))
                .andExpect(jsonPath("$.description", CoreMatchers.is(String.format("A lista de assuntos não pode ficar vazia.", Long.MIN_VALUE))));
    }

    @Test
    void registerStudent_throwMethodArgumentNotValidException_whenStudentNameIsLowerCase() throws Exception {
        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        subjectDTOList.add(new SubjectDTO("Math", 10.00));
        subjectDTOList.add(new SubjectDTO("Physical Education", 10.00));
        subjectDTOList.add(new SubjectDTO("Chemistry", 10.00));

        Long newId = 1L;
        StudentDTO studentDTO = new StudentDTO(newId, "juan", "Blank", 0.00, subjectDTOList);

        String studentAsJson = new ObjectMapper().writeValueAsString(studentDTO);


        ResultActions response = mockMvc.perform(post("/student/registerStudent").contentType(MediaType.APPLICATION_JSON).content(studentAsJson));

        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException
                ))
                .andExpect(jsonPath("$.description", CoreMatchers.is("O nome do aluno deve começar com letra maiúscula.")));
    }

    @Test
    void modifyStudent_returnOkStatus_whenSuccess() throws Exception {
        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        subjectDTOList.add(new SubjectDTO("Math", 10.00));
        subjectDTOList.add(new SubjectDTO("Physical Education", 10.00));
        subjectDTOList.add(new SubjectDTO("Chemistry", 10.00));

        Long newId = 1L;
        StudentDTO studentDTO = new StudentDTO(newId, "John Doe", "Blank", 0.00, subjectDTOList);

        studentDAO.save(studentDTO);

        studentDTO.setStudentName("New Name");
        String studentAsJson = new ObjectMapper().writeValueAsString(studentDTO);

        ResultActions response = mockMvc.perform(post("/student/modifyStudent").contentType(MediaType.APPLICATION_JSON).content(studentAsJson));

        response.andExpect(status().isOk());
    }

    @Test
    void modifyStudent_throwMethodArgumentNotValidException_whenStudentNameIsEmpty() throws Exception {
        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        subjectDTOList.add(new SubjectDTO("Math", 10.00));
        subjectDTOList.add(new SubjectDTO("Physical Education", 10.00));
        subjectDTOList.add(new SubjectDTO("Chemistry", 10.00));
        Long newId = 1L;
        StudentDTO studentDTO = new StudentDTO(newId, null, "Blank", 0.00, subjectDTOList);

        studentDAO.save(studentDTO);

        studentDTO.setStudentName(null);
        String studentAsJson = new ObjectMapper().writeValueAsString(studentDTO);

        ResultActions response = mockMvc.perform(post("/student/modifyStudent").contentType(MediaType.APPLICATION_JSON).content(studentAsJson));

        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException
                ))
                .andExpect(jsonPath("$.description", CoreMatchers.is(String.format("O nome do aluno não pode ficar vazio."))));
    }

    @Test
    void modifyStudent_throwMethodArgumentNotValidException_whenSubjectListIsEmpty() throws Exception {
        List<SubjectDTO> subjectDTOList = new ArrayList<>();

        Long newId = 1L;
        StudentDTO studentDTO = new StudentDTO(newId, "John Doe", "Blank", 0.00, subjectDTOList);

        studentDAO.save(studentDTO);

        studentDTO.setSubjects(null);
        String studentAsJson = new ObjectMapper().writeValueAsString(studentDTO);

        ResultActions response = mockMvc.perform(post("/student/modifyStudent").contentType(MediaType.APPLICATION_JSON).content(studentAsJson));

        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException
                ))
                .andExpect(jsonPath("$.description", CoreMatchers.is(String.format("A lista de assuntos não pode ficar vazia.", Long.MIN_VALUE))));
    }

    @Test
    void modifyStudent_throwMethodArgumentNotValidException_whenStudentNameIsLowerCase() throws Exception {
        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        subjectDTOList.add(new SubjectDTO("Math", 10.00));
        subjectDTOList.add(new SubjectDTO("Physical Education", 10.00));
        subjectDTOList.add(new SubjectDTO("Chemistry", 10.00));

        Long newId = 1L;
        StudentDTO studentDTO = new StudentDTO(newId, "juan", "Blank", 0.00, subjectDTOList);

        studentDAO.save(studentDTO);

        studentDTO.setStudentName("juan");
        String studentAsJson = new ObjectMapper().writeValueAsString(studentDTO);


        ResultActions response = mockMvc.perform(post("/student/modifyStudent").contentType(MediaType.APPLICATION_JSON).content(studentAsJson));

        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException
                ))
                .andExpect(jsonPath("$.description", CoreMatchers.is("O nome do aluno deve começar com letra maiúscula.")));
    }

    @Test
    void getStudent_returnStudentEntity_whenSuccess() throws Exception {
        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        subjectDTOList.add(new SubjectDTO("Math", 10.00));
        subjectDTOList.add(new SubjectDTO("Physical Education", 10.00));
        subjectDTOList.add(new SubjectDTO("Chemistry", 10.00));

        Long newId = 1L;
        StudentDTO studentDTO = new StudentDTO(newId, "John Doe", "Blank", 0.00, subjectDTOList);

        studentDAO.save(studentDTO);

        ResultActions response = mockMvc.perform(get("/student/getStudent/{id}", newId).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.studentName", CoreMatchers.is(studentDTO.getStudentName())))
                .andExpect(jsonPath("$.subjects", CoreMatchers.is(new ObjectMapper().writeValueAsString(studentDTO.getSubjects()))));

    }

}
