package vinix.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails, Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String email;
	private String password;
	
    private Set<Role> roles = new HashSet<>();
	
	public User() {}

	public User(Long id, String name, String email, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream() // converte para stream 
				.map(x -> new SimpleGrantedAuthority 
				// cada stream é convertido para SimpleGrantedAuthority
				(x.getRoleName()))
				// consegue pegar por ser uma implementação do GrantedAuthority 
				.collect(Collectors.toList());
		
		/*retorna as roles convertidas para o Spring Security*/
	}

	@Override
	public String getUsername() {
		return email; /*retorna o email como identificador do usuário*/
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; /*conta não expirada*/
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; /*conta não bloqueada*/
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; /*senha não expirada*/
	}

	@Override
	public boolean isEnabled() {
		return true; /*conta ativa*/
	}

}
