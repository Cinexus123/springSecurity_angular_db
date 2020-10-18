package entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Authority implements GrantedAuthority {

	  @Id
	    @Column(name = "id")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "name")
	    private UserRoleName name;

		@Override
		public String getAuthority() {
			// TODO Auto-generated method stub
			return name.name();
		}

		@JsonIgnore
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		@JsonIgnore
		public UserRoleName getName() {
			return name;
		}

		public void setName(UserRoleName name) {
			this.name = name;
		}
		
		
}
