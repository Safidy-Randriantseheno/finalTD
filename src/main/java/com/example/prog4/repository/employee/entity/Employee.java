package com.example.prog4.repository.employee.entity;

import com.example.prog4.repository.cnaps.entity.enums.AgeCalculationOption;
import com.example.prog4.repository.employee.entity.enums.Csp;
import com.example.prog4.repository.employee.entity.enums.Sex;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Formula;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "\"employee\"")
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private String id;
    private String cin;
    private String cnaps;
    private String image;
    private String salary;
    @Formula("CASE " +
            "WHEN age_calculation_option = 'BIRTHDAY' THEN " +
            "CASE " +
            "WHEN EXTRACT(MONTH FROM birth_date) > EXTRACT(MONTH FROM CURRENT_DATE) " +
            "OR (EXTRACT(MONTH FROM birth_date) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(DAY FROM birth_date) > EXTRACT(DAY FROM CURRENT_DATE)) " +
            "THEN EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM birth_date)  " +
            "ELSE " +
            "CASE " +
            "WHEN EXTRACT(MONTH FROM birth_date) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(DAY FROM birth_date) = EXTRACT(DAY FROM CURRENT_DATE) " +
            "THEN EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM birth_date) " +
            "ELSE EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM birth_date) -1" +
            "END " +
            "END " +
            "ELSE " +
            "CASE " +
            "WHEN age_calculation_option = 'YEAR_ONLY' THEN EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM birth_date) " +
            "END " +
            "END")
    private Integer age;


    private String address;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "personal_email")
    private String personalEmail;
    @Column(name = "professional_email")
    private String professionalEmail;
    @Column(name = "registration_number")
    private String registrationNumber;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "entrance_date")
    private LocalDate entranceDate;
    @Column(name = "departure_date")
    private LocalDate departureDate;
    @Column(name = "children_number")
    private Integer childrenNumber;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(read = "CAST(sex AS varchar)", write = "CAST(? AS sex)")
    private Sex sex;
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(read = "CAST(csp AS varchar)", write = "CAST(? AS csp)")
    private Csp csp;

    @ManyToMany
    @JoinTable(
            name = "have_position",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id")
    )
    private List<Position> positions;
    @OneToMany
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private List<Phone> phones;
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(read = "CAST(ageCalculationOption AS varchar)", write = "CAST(? AS ageCalculationOption)")
    private AgeCalculationOption ageCalculationOption;

}