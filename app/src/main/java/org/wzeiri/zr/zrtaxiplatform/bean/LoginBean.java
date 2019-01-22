package org.wzeiri.zr.zrtaxiplatform.bean;

/**
 * @author k-lm on 2017/12/11.
 */

public class LoginBean {


    /**
     * userId : 3
     * accessToken : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzIiwibmFtZSI6InVfNWEyZTJkZGM5YWNiMGIyZjhjZTJmYmNiIiwiQXNwTmV0LklkZW50aXR5LlNlY3VyaXR5U3RhbXAiOiI0MTQ5MmUxZC02YzVkLTQxOGYtYTI0Ny0xNTI0YWEzMTI3YTEiLCJqdGkiOiJjYWFmNDNjYy0wNTUyLTQ1MjktYjIyZi0zNTI1MDM0MGY3ZDEiLCJpYXQiOjE1MTI5NzU5NzcsIm5iZiI6MTUxMjk3NTk3NywiZXhwIjoxNTEzMDYyMzc3LCJpc3MiOiJBYnBaZXJvVGVtcGxhdGUiLCJhdWQiOiJBYnBaZXJvVGVtcGxhdGUifQ.0GnxS6cjpabHE6Z3iznORrDaHNVaRwvB4eb7cO8YAho
     * encryptedAccessToken : wNYmO41/48SHNstaLVXxHCCre29BZQl1NhC6NM3R3rwZiL572M4gBaHf6sHsTGZfOEpbGHKUFh7ZhfpUq5S86hSYL8cQr25lR90HLdyAQqC+eMTmvh7mzhOa5Xg//y+uqHuP+eac4ZlBVoukGY25K1T1hcOPhVUdNujFjtl4g7VD7mMzLrVmRP3fKlftZytqY3g2oClrHwBoHDZ2wJ8d9FSIZSNOzWSwo+K4zGD6zjkuMUKpl3Uh3SwaXL7+Qlfl8ymyVkQCAZ3Ds3Qv6xTvWLW9kPRp1eINogLT97DGLHUxLcwYxrF2M5WArIKZ/RwM5szyFn5zxdOyU7MBzWd9I72ERpfmIJpoLkRJs6Zeh2LP3/Z7YNH7WMc5i6KtnECJZ9rS+cwZ3CswOdPtlWLdeNNqernZa64Vk8yanydth3/oihus85mz2HupUEYo1Ur4SAhJta1MLxH6gDOpe5Q0Cv/u+94H+mqbVJ7drkgm456Q7emdlVnJaO+4yciLSXpgQOPLfQSyAI2a20gUXSlayR/+QmNxyhbm9Mt17z5/rXMwUV6mTZacVD6mFyXGCeCl
     * expireInSeconds : 86400
     */

    private int userId;
    private String accessToken;
    private String encryptedAccessToken;
    private int expireInSeconds;
    private int driverStatus;
    private boolean hasBindingCode;
    private int tenantId;
    private int artyStarTimes;
    private TenantBean tenant;

    public int getArtyStarTimes() {
        return artyStarTimes;
    }

    public void setArtyStarTimes(int artyStarTimes) {
        this.artyStarTimes = artyStarTimes;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEncryptedAccessToken() {
        return encryptedAccessToken;
    }

    public void setEncryptedAccessToken(String encryptedAccessToken) {
        this.encryptedAccessToken = encryptedAccessToken;
    }

    public int getExpireInSeconds() {
        return expireInSeconds;
    }

    public void setExpireInSeconds(int expireInSeconds) {
        this.expireInSeconds = expireInSeconds;
    }

    public int getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(int driverStatus) {
        this.driverStatus = driverStatus;
    }

    public TenantBean getTenant() {
        return tenant;
    }

    public void setTenant(TenantBean tenant) {
        this.tenant = tenant;
    }

    public boolean isHasBindingCode() {
        return hasBindingCode;
    }

    public void setHasBindingCode(boolean hasBindingCode) {
        this.hasBindingCode = hasBindingCode;
    }
}
