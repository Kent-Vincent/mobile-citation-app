package com.example.capstone;

public class TrafficViolation {
    private String Name;
    private String Violation;
    private String TotalPrice;
    private String LicenseNumber;
    private String PlateNumber;
    private String Location;
    private String DateTime;
    private String Officer;
    private String cerImageUrl;
    private String orImageUrl;
    private String signatureImageUrl;
    private String qrCodeImageUrl;

    public TrafficViolation() {

    }

    public TrafficViolation(String name, String violation, String totalPrice, String licenseNumber,
                            String plateNumber, String location, String dateTime, String officer,
                            String cerImageUrl, String orImageUrl, String signatureImageUrl, String qrCodeImageUrl) {
        this.Name = name;
        this.Violation = violation;
        this.TotalPrice = totalPrice;
        this.LicenseNumber = licenseNumber;
        this.PlateNumber = plateNumber;
        this.Location = location;
        this.DateTime = dateTime;
        this.Officer = officer;
        this.cerImageUrl = cerImageUrl;
        this.orImageUrl = orImageUrl;
        this.signatureImageUrl = signatureImageUrl;
        this.qrCodeImageUrl = qrCodeImageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getViolation() {
        return Violation;
    }

    public void setViolation(String violation) {
        this.Violation = violation;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.TotalPrice = totalPrice;
    }

    public String getLicenseNumber(){
        return LicenseNumber;
    }

    public void setLicenseNumber(String licenseNumber){
        this.LicenseNumber = licenseNumber;
    }
    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.PlateNumber = plateNumber;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        this.DateTime = dateTime;
    }

    public String getOfficer() {
        return Officer;
    }

    public void setOfficer(String officer) {
        this.Officer = officer;
    }

    public String getCerImageUrl() {
        return cerImageUrl;
    }

    public void setCerImageUrl(String cerImageUrl) {
        this.cerImageUrl = cerImageUrl;
    }

    public String getOrImageUrl() {
        return orImageUrl;
    }

    public void setOrImageUrl(String orImageUrl) {
        this.orImageUrl = orImageUrl;
    }

    public String getSignatureImageUrl() {
        return signatureImageUrl;
    }

    public void setSignatureImageUrl(String signatureImageUrl) {
        this.signatureImageUrl = signatureImageUrl;
    }

    public String getQrCodeImageUrl() {
        return qrCodeImageUrl;
    }

    public void setQrCodeImageUrl(String qrCodeImageUrl) {
        this.qrCodeImageUrl = qrCodeImageUrl;
    }
}
