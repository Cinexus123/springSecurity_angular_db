package entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

	    @Id
	    @Column(name = "id")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "username")
	    private String username;

	    @JsonIgnore
	    @Column(name = "password")
	    private String password;

	    @Column(name = "firstname")
	    private String firstname;

	    @Column(name = "lastname")
	    private String lastname;
}
