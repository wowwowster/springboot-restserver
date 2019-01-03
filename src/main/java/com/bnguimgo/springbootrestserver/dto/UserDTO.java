package com.bnguimgo.springbootrestserver.dto;
import java.io.Serializable;

public class UserDTO implements Serializable{

	private static final long serialVersionUID = -443589941665403890L;

    private Long id;

    private String login;
    private String password;
    private String userType;
	

    public UserDTO() {
    }

    public UserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    public UserDTO(String login, String password, String userType) {
        this.login = login;
        this.password = password;
        this.userType = userType;
    }
    
    public UserDTO(Long id, String login) {
    	this.id = id;
        this.login = login;
    }

	public UserDTO(Long id, String login, String password, String userType) {
		this.id = id;
        this.login = login;
        this.password = password;
        this.userType = userType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

   @Override
     public String toString() {
     return String.format("[id=%s, mail=%s, userType=%s]", id, login, userType);
     }

}