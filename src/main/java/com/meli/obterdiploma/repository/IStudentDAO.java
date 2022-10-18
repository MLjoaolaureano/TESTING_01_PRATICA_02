package com.meli.obterdiploma.repository;

import com.meli.obterdiploma.model.StudentDTO;

import java.util.List;

public interface IStudentDAO {
    void save(StudentDTO stu);
    boolean delete(Long id);
    boolean exists(StudentDTO stu);
    StudentDTO findById(Long id);

    List<StudentDTO> listAllData();

    void deleteAll();
}