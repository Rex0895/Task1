package com.tsc;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Department {
    private String name;
    private ArrayList<Employer> employees;

    public Department(String nm){
        name=nm;
        employees = new ArrayList<Employer>();
    }
    public Department(Department dp){
        name=dp.name;
        employees = new ArrayList<Employer>(dp.employees);
    }

    public List<Employer> getListEmp()
    {
        return employees;
    }
    public String getName()
    {
        return name;
    }
    public BigDecimal getAvgSalary()
    {
        BigDecimal avgSalary= BigDecimal.ZERO;
        for (Employer emp: employees)
            avgSalary=avgSalary.add(emp.getSalary());
        return avgSalary=avgSalary.divide(BigDecimal.valueOf(employees.size()), RoundingMode.HALF_UP);
    }

    public void setListEmp(List<Employer> emps)
    {
        Collections.copy(employees,emps);
    }
    public void getName(String dn)
    {
        name=dn;
    }


    public void addEmployer(Employer emp)
    {
        if(!employees.contains(emp)&& employees!=null)
            employees.add(emp);
    }
    public void transferEmployer(Department destDep,Employer emp)
    {
        if(!employees.isEmpty())
        {
            destDep.addEmployer(emp);
            employees.remove(emp);
        }
        //System.out.println("Перевод "+emp.getSurname()+" из департамента "+name+" в "+destDep.name);

    }
    public void returnEmployer(Department fromDep,Employer emp)
    {
        if(!fromDep.getListEmp().isEmpty())
        {
            this.addEmployer(emp);
            fromDep.getListEmp().remove(emp);
        }
    }
    //для сравнения департаментов, переопрделеяем методы:
    @Override
    public String toString() {
        return name;
    }
    @Override
    public boolean equals(Object obj) {
        return this.toString().equals((obj.toString()));
    }
}

