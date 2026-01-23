package tr.kontas.fluentvalidation;

public class User {
    private String country;
    private Integer age;
    private String email;
    private String username;
    private Boolean isPremium;
    private Boolean isVerified;
    private Boolean isPublic;
    private String phone;
    private String middleName;
    private Boolean hasMiddleName;
    private String address;
    private Boolean isDigitalOnly;
    private String notificationPreference;
    private String bio;
    private String role;
    private Integer yearsOfExperience;
    private Double salary;
    private String accountType;
    private Boolean hasBackupEmail;
    private Integer yearsOfMembership;

    // Getters and Setters
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Boolean isPremium() { return isPremium != null && isPremium; }
    public void setPremium(Boolean premium) { isPremium = premium; }

    public Boolean isVerified() { return isVerified != null && isVerified; }
    public void setVerified(Boolean verified) { isVerified = verified; }

    public Boolean isPublic() { return isPublic != null && isPublic; }
    public void setPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public Boolean hasMiddleName() { return hasMiddleName != null && hasMiddleName; }
    public void setHasMiddleName(Boolean hasMiddleName) { this.hasMiddleName = hasMiddleName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Boolean isDigitalOnly() { return isDigitalOnly != null && isDigitalOnly; }
    public void setDigitalOnly(Boolean digitalOnly) { isDigitalOnly = digitalOnly; }

    public String getNotificationPreference() { return notificationPreference; }
    public void setNotificationPreference(String pref) { this.notificationPreference = pref; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer years) { this.yearsOfExperience = years; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public Boolean hasBackupEmail() { return hasBackupEmail != null && hasBackupEmail; }
    public void setHasBackupEmail(Boolean hasBackupEmail) { this.hasBackupEmail = hasBackupEmail; }

    public Integer getYearsOfMembership() { return yearsOfMembership; }
    public void setYearsOfMembership(Integer yearsOfMembership) { this.yearsOfMembership = yearsOfMembership; }
}