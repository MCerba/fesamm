package com.dawson.fesamm.data;

/**
 *  Class represent contact
 * @author Cerba Mihail
 */
public class Contact {
    private String contactImage;
    private String contactName;
    private String contactEmail;

    public String getContactImage() {
        return contactImage;
    }

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactNumber) {
        contactEmail = contactNumber;
    }
}
