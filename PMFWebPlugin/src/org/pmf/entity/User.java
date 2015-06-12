package org.pmf.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="user_table")
public class User {
	
	public enum Role {
		ADMIN, USER
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int uid;
	
	@Column(nullable=false)
	private String username;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private Role role;
	
	@ManyToMany(mappedBy = "users")
	private Set<Plugin> owned = new HashSet<Plugin>();
	
	public User() {}

	public User(String username, String password, Role role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Set<Plugin> getOwned() {
		return owned;
	}

	public void setOwned(Set<Plugin> owned) {
		this.owned = owned;
	}

	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", password="
				+ password + ", role=" + role + ", owned=" + owned + "]";
	};
	
}
