//-----------------------------------------------------------
//  Author: Zachary Perales
//  Course: CSC240-80
//  Due   : 11/12/2019
//  Description: This program calculates the taxes for a user
//               and validates text. It uses many neat features
//               such as document listeners, and document formatters.
//-----------------------------------------------------------

import java.util.ArrayList;

public class Taxes {

    public Taxes() {
        reset();

        fields.add(getSsn());
        fields.add(getFirstName());
        fields.add(getLastName());
        fields.add(getFilingStatus());
        fields.add(getCharitableContributions());
        fields.add(getTotalDeductions());
        fields.add(getGrossIncome());
        fields.add(getNumberOfDependants());
        fields.add(getTaxesPaid());
        setFILING_STATUS();
        setTAX_RATE();
    }

    public void reset() {
        setSsn(null);
        setFirstName(null);
        setLastName(null);
        setFilingStatus(null);
        setCharitableContributions(null);
        setTotalDeductions(null);
        setGrossIncome(null);
        setTaxesPaid(null);
        setTaxesOwed(null);
        setTaxesTotal(null);
        setTaxRate(null);
        setAgi(null);
        setNumberOfDependants(null);
        setRefund(null);
    }

    private ArrayList<String> FILING_STATUS = new ArrayList<>();
    private ArrayList<Double> TAX_RATE = new ArrayList<>();
    private ArrayList<Object> fields = new ArrayList<>();


    private String  ssn;
    private String  firstName;
    private String  lastName;
    private String  filingStatus;
    private Double  charitableContributions;
    private Double  totalDeductions;
    private Double  grossIncome;
    private Double  taxesPaid;
    private Double  taxesOwed;
    private Double  taxesTotal;
    private Double  taxRate;
    private Double  agi;
    private Integer numberOfDependants;
    private Boolean refund;

    public ArrayList<String> getFILING_STATUS() {
        return FILING_STATUS;
    }

    public void setFILING_STATUS() {
        getFILING_STATUS().add("Married Filing Jointly");
        getFILING_STATUS().add("Married Filing Separately");
        getFILING_STATUS().add("Single");
        getFILING_STATUS().add("Head of Household");
    }

    public ArrayList<Double> getTAX_RATE() {
        return TAX_RATE;
    }

    public void setTAX_RATE() {
        getTAX_RATE().add(0.22);
        getTAX_RATE().add(0.18);
        getTAX_RATE().add(0.20);
        getTAX_RATE().add(0.15);
    }

    public ArrayList<Object> getFields() {
        return fields;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFilingStatus() {
        return filingStatus;
    }

    public void setFilingStatus(String filingStatus) {
        this.filingStatus = filingStatus;
    }

    public Double getCharitableContributions() {
        return charitableContributions;
    }

    public void setCharitableContributions(Double charitableContributions) {
        this.charitableContributions = charitableContributions;
    }

    public Double getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(Double totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public Double getGrossIncome() {
        return grossIncome;
    }

    public void setGrossIncome(Double grossIncome) {
        this.grossIncome = grossIncome;
    }

    public Double getTaxesPaid() {
        return taxesPaid;
    }

    public void setTaxesPaid(Double taxesPaid) {
        this.taxesPaid = taxesPaid;
    }

    public Double getTaxesOwed() {
        return taxesOwed;
    }

    public void setTaxesOwed(Double taxesOwed) {
        this.taxesOwed = taxesOwed;
    }

    public Double getTaxesTotal() {
        return taxesTotal;
    }

    public void setTaxesTotal(Double taxesTotal) {
        this.taxesTotal = taxesTotal;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getAgi() {
        return agi;
    }

    public void setAgi(Double agi) {
        this.agi = agi;
    }

    public Integer getNumberOfDependants() {
        return numberOfDependants;
    }

    public void setNumberOfDependants(Integer numberOfDependants) {
        this.numberOfDependants = numberOfDependants;
    }

    public Boolean getRefund() {
        return refund;
    }

    public void setRefund(Boolean refund) {
        this.refund = refund;
    }

    public void calculateAgi() {
        setAgi(((getGrossIncome()) - ((getNumberOfDependants()*4000.00) + getTotalDeductions() + getCharitableContributions())));

        if (getAgi() < 0.00) {
            setAgi(0.00);
        }
    }

    public void calculateTaxRate() {
        setTaxRate(TAX_RATE.get(FILING_STATUS.indexOf(getFilingStatus())));
    }

    public void calculateTaxesTotal() {
        setTaxesTotal(getAgi() * getTaxRate());
    }

    public void calculateTaxesOwed() {
        setTaxesOwed(getTaxesTotal() - getTaxesPaid());
    }
}
