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

    public void setSalary(BigDecimal sal) {
        salary = sal;
    }

    @Override
    public String toString() {
        return surname;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Employer guest = (Employer) obj;
        return (surname == guest.getSurname() || (surname != null && surname.equals(guest.getSurname())))
                && (salary.compareTo(BigDecimal.ZERO) != 0
                && salary.compareTo(guest.getSalary()) == 0);
    }
}
