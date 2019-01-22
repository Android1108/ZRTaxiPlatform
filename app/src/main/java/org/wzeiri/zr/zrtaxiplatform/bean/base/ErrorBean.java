package org.wzeiri.zr.zrtaxiplatform.bean.base;

import java.util.List;

/**
 * @author k-lm on 2017/12/11.
 */

public class ErrorBean {


    /**
     * code : 0
     * message : 当前用户没有登录到系统！
     * details : null
     * validationErrors : null
     */

    private int code;
    private String message;
    private String details;
    private List<ValidationErrors> validationErrors;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<ValidationErrors> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationErrors> validationErrors) {
        this.validationErrors = validationErrors;
    }


    public static class ValidationErrors {

        /**
         * message : The supplied value is invalid.
         * members : ["boardingTime"]
         */

        private String message;
        private List<String> members;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<String> getMembers() {
            return members;
        }

        public void setMembers(List<String> members) {
            this.members = members;
        }
    }
}
