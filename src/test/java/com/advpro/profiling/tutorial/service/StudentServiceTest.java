package com.advpro.profiling.tutorial.service;

import com.advpro.profiling.tutorial.model.Course;
import com.advpro.profiling.tutorial.model.Student;
import com.advpro.profiling.tutorial.model.StudentCourse;
import com.advpro.profiling.tutorial.repository.StudentCourseRepository;
import com.advpro.profiling.tutorial.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentCourseRepository studentCourseRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;
    private StudentCourse sc1;
    private StudentCourse sc2;

    @BeforeEach
    void setUp() {
        student1 = new Student();
        student1.setId(1L);
        student1.setName("Andi");
        student1.setGpa(3.5);

        student2 = new Student();
        student2.setId(2L);
        student2.setName("Budi");
        student2.setGpa(3.9);

        Course courseA = new Course();
        courseA.setId(101L);
        courseA.setName("Pemrograman Lanjut");

        sc1 = new StudentCourse();
        sc1.setStudent(student1);
        sc1.setCourse(courseA);

        sc2 = new StudentCourse();
        sc2.setStudent(student2);
        sc2.setCourse(courseA);
    }

    @Test
    void testGetAllStudentsWithCourses() {

        lenient().when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));
        lenient().when(studentCourseRepository.findByStudentId(1L)).thenReturn(Arrays.asList(sc1));
        lenient().when(studentCourseRepository.findByStudentId(2L)).thenReturn(Arrays.asList(sc2));

        lenient().when(studentCourseRepository.findAll()).thenReturn(Arrays.asList(sc1, sc2));

        List<StudentCourse> result = studentService.getAllStudentsWithCourses();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Andi", result.get(0).getStudent().getName());
        assertEquals("Budi", result.get(1).getStudent().getName());
    }

    @Test
    void testFindStudentWithHighestGpa() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        Optional<Student> result = studentService.findStudentWithHighestGpa();

        assertTrue(result.isPresent());
        assertEquals("Budi", result.get().getName());
        assertEquals(3.9, result.get().getGpa());
    }

    @Test
    void testJoinStudentNames() {
        lenient().when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        String result = studentService.joinStudentNames();

        assertEquals("Andi, Budi", result);
    }
}