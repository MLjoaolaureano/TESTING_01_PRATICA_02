package com.meli.obterdiploma.integrado;


import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    void setup() {
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
                .andExpect(jsonPath("$.studentName", CoreMatchers.is(studentDTO.getStudentName())));

        //A partir daqui eu fiz a leitura do response para um objeto do tipo StudentDTO e com este objeto eu consigo comparar
        //o tamanho da lista e os valores de cada objeto da lista.
        ObjectMapper mapper = new ObjectMapper();
        StudentDTO studentDTOAtual = mapper.readValue(response.andReturn().getResponse().getContentAsString(), new TypeReference<StudentDTO>() {
        });

        System.out.println(subjectDTOList);
        assertTrue(subjectDTOList.size() == studentDTOAtual.getSubjects().size()
                && subjectDTOList.get(0).getName().equals(studentDTOAtual.getSubjects().get(0).getName())
                && subjectDTOList.get(1).getName().equals(studentDTOAtual.getSubjects().get(1).getName())
                && subjectDTOList.get(2).getName().equals(studentDTOAtual.getSubjects().get(2).getName()));
        //Aqui você poderia fazer mais comparações, caso ache necessário.

    }

    @Test
    void getStudent_throwStudentNotFoundException_whenUsingInexistentStudentId() throws Exception {

        ResultActions response = mockMvc.perform(get("/student/getStudent/{id}", Long.MAX_VALUE).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.description", CoreMatchers.is(String.format("O aluno com Id %d não está registrado.", Long.MAX_VALUE))))
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof StudentNotFoundException
                ));
    }

    @Test
    void removeStudent_returnOkStatus_whenSuccess() throws Exception {
        Long newId = 1L;

        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        subjectDTOList.add(new SubjectDTO("Math", 10.00));
        subjectDTOList.add(new SubjectDTO("Physical Education", 10.00));
        subjectDTOList.add(new SubjectDTO("Chemistry", 10.00));

        StudentDTO studentDTO = new StudentDTO(newId, "John Doe", "Blank", 0.00, subjectDTOList);
        studentDAO.save(studentDTO);

        ResultActions response = mockMvc.perform(get("/student/removeStudent/{id}", newId).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());

        assertThat(studentDAO.listAllData().size()).isEqualTo(0);

    }

    @Test
    void listStudents_returnOkStatus_whenSuccess() throws Exception {
        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        subjectDTOList.add(new SubjectDTO("Math", 10.00));
        subjectDTOList.add(new SubjectDTO("Physical Education", 10.00));
        subjectDTOList.add(new SubjectDTO("Chemistry", 10.00));

        Long newId = 1L;
        StudentDTO studentDTO = new StudentDTO(newId, "John Doe", "Blank", 0.00, subjectDTOList);

        studentDAO.save(studentDTO);
        studentDAO.save(studentDTO);
        studentDAO.save(studentDTO);

        ResultActions response = mockMvc.perform(get("/student/listStudents").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());

        ObjectMapper mapper = new ObjectMapper();
        Set<StudentDTO> studentDTOSet = mapper.readValue(response.andReturn().getResponse().getContentAsString(), new TypeReference<HashSet<StudentDTO>>() {});

        assertTrue(studentDTOSet.contains(studentDTO));
    }


}
