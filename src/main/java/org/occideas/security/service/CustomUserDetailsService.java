package org.occideas.security.service;

import org.occideas.security.model.User;
import org.occideas.security.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserService userService;

  public UserDetails loadUserByUsername(String ssoId)
    throws UsernameNotFoundException {
    User user = userService.findBySso(ssoId);
    System.out.println("User : " + user);
    if (user == null) {
      System.out.println("User not found");
      throw new UsernameNotFoundException("Username not found");
    }
    return new org.springframework.security.core.userdetails.User(user.getSsoId(), user.getPassword(),
      user.getState().equals("Active"), true, true, true, getGrantedAuthorities(user));
  }


  private List<GrantedAuthority> getGrantedAuthorities(User user) {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    for (UserProfile userProfile : user.getUserProfiles()) {
      //System.out.println("UserProfile : "+userProfile);
      authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getType()));
    }
    System.out.print("authorities :" + authorities);
    return authorities;
  }

}
