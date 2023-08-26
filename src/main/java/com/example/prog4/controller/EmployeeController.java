package com.example.prog4.controller;

import com.example.prog4.controller.mapper.EmployeeMapper;
import com.example.prog4.controller.validator.EmployeeValidator;
import com.example.prog4.model.Employee;
import com.example.prog4.model.EmployeeFilter;
import com.example.prog4.service.CSVUtils;
import com.example.prog4.service.EmployeeService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/server/employee")
public class EmployeeController {
    private EmployeeMapper employeeMapper;
    private EmployeeValidator employeeValidator;
    private EmployeeService employeeService;
    private SpringTemplateEngine templateEngine;

    @GetMapping("/list/csv")
    public ResponseEntity<byte[]> getCsv(HttpSession session) {
        EmployeeFilter filters = (EmployeeFilter) session.getAttribute("employeeFiltersSession");
        List<Employee> data = employeeService.getAll(filters);

        String csv = CSVUtils.convertToCSV(data);
        byte[] bytes = csv.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "employees.csv");
        headers.setContentLength(bytes.length);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }


    @GetMapping("/list/filters/clear")
    public String clearFilters(HttpSession session) {
        session.removeAttribute("employeeFilters");
        return "redirect:/employee/list";
    }

    @PostMapping("/createOrUpdate")
    public String saveOne(@ModelAttribute Employee employee) {
        employeeValidator.validate(employee);
        employeeService.saveOne(employee);
        return "redirect:/employee/list";
    }

    @GetMapping("/list/pdf")
    public void exportPdfForEmployeeList(HttpServletResponse response, HttpSession session) throws IOException, DocumentException {
        EmployeeFilter filters = (EmployeeFilter) session.getAttribute("employeeFiltersSession");
        List<Employee> data = employeeService.getAll(filters);

        String html = generateDocument(data); // Generate HTML for the employee list
        generatePdfFromHtml(html, response); // Generate and send the PDF
    }

    public String generateDocument(List<Employee> data) {
        Context context = new Context();
        context.setVariable("employees", data);
        return templateEngine.process("employee_pdf", context);
    }


    public void generatePdfFromHtml(String html, HttpServletResponse response) throws IOException, DocumentException {
        String outputFileName = "thymeleaf.pdf";
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + outputFileName);

        OutputStream outputStream = response.getOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);

        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
    }
}

