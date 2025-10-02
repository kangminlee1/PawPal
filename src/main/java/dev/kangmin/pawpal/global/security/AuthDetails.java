package dev.kangmin.pawpal.global.security;

import dev.kangmin.pawpal.domain.member.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class AuthDetails implements UserDetails, OAuth2User {

    private final Member member;
    private Map<String, Object> attributes;
    private final Collection<GrantedAuthority> authorities;

    public AuthDetails(Member member, Collection<GrantedAuthority> authorities) {
        this.member = member;
        this.authorities = authorities;
    }

    public AuthDetails(Member member, Map<String, Object> attributes, Collection<GrantedAuthority> authorities) {
        this.member = member;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    //멤버 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorityCollection = new ArrayList<>();
        authorityCollection.add(new SimpleGrantedAuthority(member.getRole().name()));
        return authorityCollection;
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



    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }


    //Oauth
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("email"));
    }

}
