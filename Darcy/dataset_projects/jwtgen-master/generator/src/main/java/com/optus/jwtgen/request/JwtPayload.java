package com.optus.jwtgen.request;


import javax.validation.constraints.NotNull;

public class JwtPayload {

    @NotNull
    private Long nbf;

    @NotNull
    private Long exp;

    @NotNull
    private Long iat;

    private String jti;

    @NotNull
    private String aud;

    @NotNull
    private String bra;

    @NotNull
    private String abt;

    @NotNull
    private String emp;

    @NotNull
    private String ini;

    public Long getNbf() {
        return nbf;
    }

    public void setNbf(Long nbf) {
        this.nbf = nbf;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getBra() {
        return bra;
    }

    public void setBra(String bra) {
        this.bra = bra;
    }

    public String getAbt() {
        return abt;
    }

    public void setAbt(String abt) {
        this.abt = abt;
    }

    public String getEmp() {
        return emp;
    }

    public void setEmp(String emp) {
        this.emp = emp;
    }

    public String getIni() {
        return ini;
    }

    public void setIni(String ini) {
        this.ini = ini;
    }
}

