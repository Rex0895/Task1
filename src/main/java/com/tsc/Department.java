package com.tsc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Department implements Copyable {
    private String name;
    private ArrayList<Employer> employersList;

    public Department() {
    }

    public Department(String nm) {
        name = nm;
    }

    public Department copy() {
        Department d = new Department();
        d.setName(name);
        d.setEmployersList(employersList);
        return d;
    }

    public List<Employer> getEmployersList() {
        return employersList;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getAvgSalary() {
        BigDecimal averageSalary = BigDecimal.ZERO;
        for (Employer emp : employersList)
            averageSalary = averageSalary.add(emp.getSalary());
        return averageSalary = averageSalary.divide(BigDecimal.valueOf(employersList.size()), RoundingMode.HALF_UP);
    }

    public void setEmployersList(List<Employer> emps) {
        //Collections.copy(employersList,emps);
        employersList = new ArrayList<>(emps);
    }

    public void setName(String dn) {
        name = dn;
    }


    public void addEmployer(Employer emp) {
        if (employersList == null)
            employersList = new ArrayList<>();
        if (!employersList.contains(emp))
            employersList.add(emp);
    }

    public void transferEmployer(Department destinationDepartment, Employer emp) {
        if (employersList.contains(emp)) {
            destinationDepartment.addEmployer(emp);
            employersList.remove(emp);
        }
    }

    //для сравнения департаментов, переопрделеяем методы:
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Department guest = (Department) obj;
        return (name == guest.getName() || (name != null && name.equals(guest.getName())))
                && (!employersList.isEmpty() && employersList == guest.getEmployersList());
    }
}

