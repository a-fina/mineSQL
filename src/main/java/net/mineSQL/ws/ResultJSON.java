/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.ws;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ax.finamore
 */
public class ResultJSON {
    String success;
    String valid;
    String reason;

    public ResultJSON(String success, String valid, String reason) {
        this.success = success;
        this.valid = valid;
        this.reason = reason;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ResultJSON{" + "success=" + success + ", valid=" + valid + ", reason=" + reason + '}';
    }
    
}
