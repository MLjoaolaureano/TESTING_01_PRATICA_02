package com.meli.obterdiploma.service;

import com.meli.obterdiploma.exception.StudentNotFoundException;
import com.meli.obterdiploma.model.StudentDTO;
import com.meli.obterdiploma.model.SubjectDTO;
import com.meli.obterdiploma.repository.StudentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class ObterDiplomaServiceTest {

    @InjectMocks
    private ObterDiplomaService obterDiplomaService;

    @Mock
    private StudentDAO studentDAO;

    private StudentDTO studentDTO;


    @BeforeEach
    void setup() {
        studentDTO = new StudentDTO(1L, "John Doe", "Foo Message", 10.0, null);
    }

    @Test
    @DisplayName("Test Student Analysis success when StudentDTO is proper")
    void analyzeScores_returnsNewStudentDTO_whenSuccess() {
        final ArrayList<SubjectDTO> subjectList = new ArrayList<>();
        subjectList.add(new SubjectDTO("Matemática", 7.0));
        subjectList.add(new SubjectDTO("Educacao Física", 8.5));
        subjectList.add(new SubjectDTO("Espanhol", 10.0));
        studentDTO.setSubjects(subjectList);

        Mockito.when(studentDAO.findById(ArgumentMatchers.any()))
                .thenReturn(studentDTO);

        StudentDTO studentResponse = obterDiplomaService.analyzeScores(ArgumentMatchers.anyLong());

        assertThat(studentResponse).isNotNull();
        assertThat(studentResponse.getStudentName()).isEqualTo("John Doe");
        assertThat(studentResponse.getAverageScore()).isEqualTo(8.5);
        assertThat(studentResponse.getMessage()).isEqualTo("O aluno John Doe obteve uma média de 8,5. Você pode melhorar.");

    }

    @Test
    @DisplayName("Test Student Analysis success when StudentDTO is Proper and grades are perfect")
    void analyzeScores_returnsNewStudentDTO_whenSuccessAndGradesPerfect() {
        final ArrayList<SubjectDTO> subjectList = new ArrayList<>();
        subjectList.add(new SubjectDTO("Matemática", 10.0));
        subjectList.add(new SubjectDTO("Educacao Física", 10.0));
        subjectList.add(new SubjectDTO("Espanhol", 10.0));
        studentDTO.setSubjects(subjectList);

        Mockito.when(studentDAO.findById(ArgumentMatchers.any()))
                .thenReturn(studentDTO);

        StudentDTO studentResponse = obterDiplomaService.analyzeScores(ArgumentMatchers.anyLong());

        assertThat(studentResponse).isNotNull();
        assertThat(studentResponse.getStudentName()).isEqualTo("John Doe");
        assertThat(studentResponse.getAverageScore()).isEqualTo(10);
        assertThat(studentResponse.getMessage()).isEqualTo("O aluno John Doe obteve uma média de 10. Parabéns!");
    }


    @Test
    @DisplayName("Test Exception Throw when StudentDTO not exists")
    void analyzeScores_throwsStudentNotFoundException_whenStudentNotExists() {
        Long inexistentId = 999L;
        BDDMockito.given(studentDAO.findById(inexistentId))
                .willThrow(new StudentNotFoundException(inexistentId));

        assertThrows(StudentNotFoundException.class, () -> {
            obterDiplomaService.analyzeScores(inexistentId);
        });
    }

    @Test
    @DisplayName("Test Exception Throw when StudentDTO is null")
    void analyzeScores_throwsNullPointerException_whenStudentIsNull() {

        Mockito.when(studentDAO.findById(ArgumentMatchers.any()))
                .thenReturn(null);


        assertThrows(NullPointerException.class, () -> {
            obterDiplomaService.analyzeScores(studentDTO.getId());
        });
    }

    @Test
    @DisplayName("Test Exception Throw when Invalid StudentDTO name")
    void analyzeScores_throwsNullPointerException_whenInvalidStudentDTOName() {
        studentDTO.setStudentName("invalid name");
        Mockito.when(studentDAO.findById(ArgumentMatchers.any()))
                .thenReturn(studentDTO);

        assertThrows(NullPointerException.class, () -> {
            obterDiplomaService.analyzeScores(studentDTO.getId());
        });
    }

    @Test
    @DisplayName("Test Exception Throw when Null Subject List")
    void analyzeScores_throwsNullPointerException_whenNullSubjectList() {
        studentDTO.setStudentName("invalid name");
        Mockito.when(studentDAO.findById(ArgumentMatchers.any()))
                .thenReturn(studentDTO);

        assertThrows(NullPointerException.class, () -> {
            obterDiplomaService.analyzeScores(studentDTO.getId());
        });
    }

}