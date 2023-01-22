package com.dnd.wedding.domain.oauth;

import com.dnd.wedding.domain.member.Member;
import com.dnd.wedding.domain.member.Role;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

  private final Long id;
  private final String email;
  private final OAuth2Provider oauth2Provider;
  private final Role role;
  private final Collection<? extends GrantedAuthority> authorities;
  private transient Map<String, Object> attributes;

  public CustomUserDetails(Long id, String email, OAuth2Provider oauth2Provider, Role role,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.email = email;
    this.oauth2Provider = oauth2Provider;
    this.role = role;
    this.authorities = authorities;
  }

  public static CustomUserDetails create(Member member) {
    List<GrantedAuthority> authorities = Collections.singletonList(
        new SimpleGrantedAuthority("ROLE_USER"));

    return new CustomUserDetails(
        member.getId(),
        member.getEmail(),
        member.getOauth2Provider(),
        Role.USER,
        authorities
    );
  }

  public static CustomUserDetails create(Member member, Map<String, Object> attributes) {
    CustomUserDetails userDetails = CustomUserDetails.create(member);
    userDetails.setAttributes(attributes);
    return userDetails;
  }

  // UserDetail Override
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  // OAuth2User Override
  @Override
  public String getName() {
    return String.valueOf(id);
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }
}
