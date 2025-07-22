package com.student.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.student.entity.Student;
import com.student.repository.StudentRepository;
import com.student.service.StudentService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private StudentRepository studentRepository;


    @GetMapping("/login")
    public String showLoginPage(Model model,
                                @ModelAttribute("success") String success,
                                @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("student", new Student());
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("student", new Student());
        return "register"; // maps to register.html
    }
    
    @PostMapping("/register")
    public String registerStudent(@ModelAttribute("student") Student student, Model model) {
        try {
            studentService.registerStudent(student);
            return "redirect:/login?success=Registered successfully!";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage()); // Pass "Email already exists!" to HTML
            model.addAttribute("student", student); // Preserve form data if needed
            return "register"; // Return to form
        }
    }
    
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String emailId,
                                        @RequestParam String mobileNumber,
                                        Model model) {
        Optional<Student> studentOpt = studentService.findByEmailId(emailId);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();

            if (student.getMobileNumber().equals(mobileNumber)) {
                // Password match found — show the password (or redirect to reset page instead)
                model.addAttribute("success", "Your password is: " + student.getPassword());
            } else {
                model.addAttribute("error", "Mobile number does not match our records.");
            }
        } else {
            model.addAttribute("error", "No student found with that email.");
        }

        return "forgot-password";
    }


    @PostMapping("/login")
    public String login(@RequestParam("emailId") String emailId,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session) {

    	Optional<Student> studentOpt = studentService.findByEmailId(emailId.toLowerCase());

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (student.getPassword().equals(password)) {
                session.setAttribute("loggedInStudent", student);
                return "redirect:/students";
            } else {
                model.addAttribute("error", "Invalid password");
                return "login";
            }
        } else {
            model.addAttribute("error", "Student not found");
            return "login";
        }
    }

    // Show all students
    @GetMapping("/students")
    public String viewStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";  // corresponds to students.html in templates
    }
    @PostMapping("/students/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register"; // or your form page
        }

        studentService.saveStudent(student);
        redirectAttributes.addFlashAttribute("success", "Student registered successfully!");
        return "redirect:/students";
    }

    
 // Show form for editing student
    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "edit-student";
    }

    // Handle form submission for updating student
    @PostMapping("/students/update")
    public String updateStudent(@ModelAttribute("student") Student student, RedirectAttributes redirectAttributes) {
        studentService.updateStudent(student);
        redirectAttributes.addFlashAttribute("success", "Student updated successfully!");
        return "redirect:/students";
    }

    // Delete student by ID
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentService.deleteStudentById(id);
        redirectAttributes.addFlashAttribute("success", "Student deleted successfully!");
        return "redirect:/students";
    }

}
