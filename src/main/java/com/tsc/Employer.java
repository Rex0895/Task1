package com.tsc;

import java.math.BigDecimal;

public class Employer {
    private String surname;
    private BigDecimal salary;

    public Employer(String sn, BigDecimal sal) {
        surname = sn;
        salary = sal;
    }

    public String getSurname() {
        return surname;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSurname(String sn) {
        surname = sn;
    }

    public void getSalary(BigDecimal sal) {
        salary = sal;
    }

    @Override
    public String toString() {
        return surname;
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }
}
