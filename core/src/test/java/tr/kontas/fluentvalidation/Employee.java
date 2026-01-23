package tr.kontas.fluentvalidation;

public class Employee {
    private String level;
    private double salary;
    private double performanceScore;
    private boolean hasDirectReports;
    private int departmentSize;
    private Double bonus;
    private boolean remoteWorkEligible;
    private double yearsWithCompany;
    private boolean hasSecurityClearance;
    private Integer stockOptions;
    private boolean foundingMember;

    // Getters and setters...
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public double getPerformanceScore() { return performanceScore; }
    public void setPerformanceScore(double performanceScore) { this.performanceScore = performanceScore; }
    public boolean hasDirectReports() { return hasDirectReports; }
    public void setHasDirectReports(boolean hasDirectReports) { this.hasDirectReports = hasDirectReports; }
    public int getDepartmentSize() { return departmentSize; }
    public void setDepartmentSize(int departmentSize) { this.departmentSize = departmentSize; }
    public Double getBonus() { return bonus; }
    public void setBonus(Double bonus) { this.bonus = bonus; }
    public boolean isRemoteWorkEligible() { return remoteWorkEligible; }
    public void setRemoteWorkEligible(boolean remoteWorkEligible) { this.remoteWorkEligible = remoteWorkEligible; }
    public double getYearsWithCompany() { return yearsWithCompany; }
    public void setYearsWithCompany(double yearsWithCompany) { this.yearsWithCompany = yearsWithCompany; }
    public boolean hasSecurityClearance() { return hasSecurityClearance; }
    public void setHasSecurityClearance(boolean hasSecurityClearance) { this.hasSecurityClearance = hasSecurityClearance; }
    public Integer getStockOptions() { return stockOptions; }
    public void setStockOptions(Integer stockOptions) { this.stockOptions = stockOptions; }
    public boolean isFoundingMember() { return foundingMember; }
    public void setFoundingMember(boolean foundingMember) { this.foundingMember = foundingMember; }
}
