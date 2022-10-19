package com.meli.obterdiploma.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {

    Long id;

    @NotBlank(message = "O nome do aluno não pode ficar vazio.")
    @Pattern(regexp="([A-Z]|[0-9])[\\s|[0-9]|A-Z|a-z|ñ|ó|í|á|é|ú|Á|Ó|É|Í|Ú]*$", message = "O nome do aluno deve começar com letra maiúscula.")
    @Size(max = 50, message = "O comprimento do nome do aluno não pode exceder 50 caracteres.")
    String studentName;

    String message;
    Double averageScore;

    @NotEmpty(message = "A lista de assuntos não pode ficar vazia.")
    List<@Valid SubjectDTO> subjects;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDTO that = (StudentDTO) o;
        return Objects.equals(studentName, that.studentName) && Objects.equals(message, that.message) && Objects.equals(averageScore, that.averageScore) && Objects.equals(subjects, that.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentName, message, averageScore, subjects);
    }
}
