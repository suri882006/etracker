package in.fourbits.etracker.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbluserlogin")
public class EtrackerUser {
	
	@Id
	@Column(name="userid")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long userId;
	
	@Column(name="username")
	private String userName;
	
	@Column(name="password")
	private String password;
	
	@Column(name="lastlogin")
	private Date lastLogin;
	
	@Column(name="isdisabled")
	private short isDisabled;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public short getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(short isDisabled) {
		this.isDisabled = isDisabled;
	}

}
