package com.meli.obterdiploma.integrado;


import com.meli.obterdiploma.exception.StudentNotFoundException;
import com.meli.obterdiploma.model.StudentDTO;
import com.meli.obterdiploma.model.SubjectDTO;
import com.meli.obterdiploma.repository.IStudentDAO;
import com.meli.obterdiploma.repository.StudentDAO;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
public class ObterDiplomaControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IStudentDAO studentDAO;

    @BeforeEach
    void setup(){
        this.studentDAO.deleteAll();
    }

    @Test
    void analyzeScores_returnStudentInformation_whenSuccess() throws Exception {
        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        subjectDTOList.add(new SubjectDTO("Math", 10.00));
        subjectDTOList.add(new SubjectDTO("Physical Education", 10.00));
        subjectDTOList.add(new SubjectDTO("Chemistry", 10.00));

        Long newId = 1L;
        StudentDTO studentDTO = new StudentDTO(newId, "John Doe", "Blank", 0.00, subjectDTOList);

        studentDAO.save(studentDTO);

        ResultActions response = mockMvc.perform(get("/analyzeScores/{studentId}", newId).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.studentName", CoreMatchers.is(studentDTO.getStudentName())))
                .andExpect(jsonPath("$.message", CoreMatchers.is("O aluno John Doe obteve uma média de 10. Parabéns!")))
                .andExpect(jsonPath("$.averageScore", CoreMatchers.is(10.0)));
    }

    @Test
    void analyzeScores_throwStudentNotFoundException_whenFailure() throws Exception {

        ResultActions response = mockMvc.perform(get("/analyzeScores/{studentId}",Long.MIN_VALUE).contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof StudentNotFoundException
                ))
                .andExpect(jsonPath("$.description", CoreMatchers.is(String.format("O aluno com Id %d não está registrado.", Long.MIN_VALUE))));
    }

}
